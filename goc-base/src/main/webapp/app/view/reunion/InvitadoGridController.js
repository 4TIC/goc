Ext.define('goc.view.reunion.InvitadoGridController', {
    extend : 'Ext.ux.uji.grid.PanelController',
    alias : 'controller.invitadoGridController',

    addInvitado : function()
    {
        var window = Ext.create('goc.view.common.LookupWindowPersonas',
        {
            appPrefix : 'goc',
            title : appI18N.reuniones.seleccionaInvitado
        });

        window.show();

        var vm = this.getViewModel();
        var store = vm.get('reunionInvitadosStore');

        window.on('LookoupWindowClickSeleccion', function(res)
        {
            var invitado = Ext.create('goc.model.ReunionInvitado',
            {
                personaId : res.get('id'),
                personaNombre : res.get('nombre')
            });

            var existeInvitado = store.find('personaId', invitado.get('personaId'));
            if (existeInvitado === -1)
            {
                store.add(invitado);
            }
        });
    },

    onBorrarInvitadoReunion : function(record)
    {
        var vm = this.getViewModel();
        var store = vm.get('reunionInvitadosStore');
        store.remove(record);
    }
});
