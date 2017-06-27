Ext.define('goc.view.organo.OrganoInvitadoGridController',
{
    extend : 'Ext.ux.uji.grid.PanelController',
    alias : 'controller.organoInvitadoGridController',

    onLoad : function()
    {
    },

    organoSelected : function(organo)
    {
        this.getViewModel().getStore('organoInvitadosStore').load(
        {
            params : {
                organoId : organo.get('id')
            }
        });

        if (organo.get('inactivo'))
        {
            return this.getView().setDisabled(true);
        }

        this.getView().setDisabled(false);
    },

    onAdd : function()
    {
        var organosGrid = this.getView().up('panel[alias=widget.organoMainPanel]').down('grid[name=organosGrid]');
        var record = organosGrid.getView().getSelectionModel().getSelection()[0];
        var vm = this.getViewModel();

        if (!record)
            return;

        var window = Ext.create('goc.view.common.LookupWindowPersonas',
        {
            appPrefix : 'goc',
            title : appI18N.organos.seleccionaInvitado
        });

        window.show();

        var store = vm.getStore('organoInvitadosStore');
        window.on('LookoupWindowClickSeleccion', function(res)
        {
            var invitado = Ext.create('goc.model.OrganoInvitado',
            {
                personaId : res.get('id'),
                personaNombre : res.get('nombre'),
                personaEmail : res.get('email'),
                organoId : record.get('id')
            });

            var existeInvitado = store.find('personaId', invitado.get('personaId'));
            if (existeInvitado === -1)
            {
                store.add(invitado);
                store.getModel().getField('id').persist = false
                store.sync();
            }
        });
    },

    onDelete : function(grid, td, cellindex)
    {
        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];

        if (!record)
        {
            return Ext.Msg.alert(appI18N.organos.borrarAutorizacion, appI18N.organos.seleccionarParaBorrarInvitado);
        }
        ;

        var vm = this.getViewModel();
        var store = vm.getStore('organoInvitadosStore');

        Ext.Msg.confirm(appI18N.common.borrar, appI18N.organos.borrarInvitado, function(result)
        {
            if (result === 'yes')
            {
                store.remove(record);
                store.sync();
            }
        });
    }
});