Ext.define('goc.view.organo.AutorizadoGridController',
{
    extend : 'Ext.ux.uji.grid.PanelController',
    alias : 'controller.autorizadoGridController',
    organo : null,

    organoSelected : function(organo)
    {
        if (organo === this.organo) {
            return;
        }

        this.organo = organo;

        if (organo.get('inactivo')) {
            return this.getView().setDisabled(true);
        }
        
        var viewModel = this.getViewModel();
        this.getView().setDisabled(false);
        viewModel.getStore('organoAutorizadosStore').load(
        {
            params :
            {
                organoId : organo.get('id'),
                externo : organo.get('externo')
            }
        });
    },

    onAdd : function()
    {
        var organosGrid = this.getView().up('panel').down('grid[name=organosGrid]');
        var record = organosGrid.getView().getSelectionModel().getSelection()[0];
        var vm = this.getViewModel();

        if (!record)
            return;

        var window = Ext.create('goc.view.common.LookupWindowPersonas',
        {
            appPrefix : 'goc',
            title : appI18N.organos.seleccionaSuplente
        });

        window.show();

        var store = vm.getStore('organoAutorizadosStore');
        var self = this;
        window.on('LookoupWindowClickSeleccion', function(res)
        {
            var autorizado = Ext.create('goc.model.OrganoAutorizado',
            {
                personaId : res.get('id'),
                personaNombre : res.get('nombre'),
                organoId : record.get('id'),
                organoExterno : record.get('externo')
            });

            store.add(autorizado);
            store.sync();
        });
    },

    onDelete : function(grid, td, cellindex)
    {
        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];

        if (!record) return;

        var vm = this.getViewModel();
        var store = vm.getStore('organoAutorizadosStore');

        Ext.Msg.confirm(appI18N.common.borrar, appI18N.organos.borrarAutorizacion, function (result) {
            if (result === 'yes') {
                store.remove(record);
                store.sync();
            }
        });
    }
});
