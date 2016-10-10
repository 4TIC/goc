Ext.define('goc.view.historicoReunion.HistoricoReunionGridController', {
    extend: 'Ext.ux.uji.grid.PanelController',
    alias: 'controller.historicoReunionGridController',
    onLoad: function () {
        var viewModel = this.getViewModel();
        console.log('hola')
        viewModel.getStore('reunionesStore').load({
            url: '/goc/rest/reuniones/completadas'
        });
    },
    organoSelected: function (id, externo) {
        var grid = this.getView();

        if (id) {
            grid.getStore().load({
                url: '/goc/rest/reuniones/completadas',
                params: {
                    organoId: id,
                    externo: externo
                }
            });
        } else {
            var comboTipoOrgano = grid.down('comboReunionTipoOrgano');
            grid.getStore().load({
                url: '/goc/rest/reuniones/completadas',
                params: {
                    tipoOrganoId: comboTipoOrgano.getValue()
                }
            });
        }
    },

    filtraComboOrgano: function(tipoOrganoId) {
        var vm = this.getViewModel();
        var store = vm.getStore('organosStore');
        var filter  = new Ext.util.Filter(
            {
                id : 'tipoOrganoId',
                property : 'tipoOrganoId',
                value : tipoOrganoId
            });

        store.addFilter(filter);
    },

    limpiaFiltrosComboOrgano: function() {
        var vm = this.getViewModel();
        var store = vm.getStore('organosStore');
        store.clearFilter();

        var grid = this.getView();

        var comboOrganos = grid.down('comboOrgano');
        comboOrganos.clearValue();
    },

    tipoOrganoSelected: function (id, externo) {
        var grid = this.getView();

        if (id) {
            grid.getStore().load({
                url: '/goc/rest/reuniones/completadas',
                params: {
                    tipoOrganoId: id
                }
            });
            this.filtraComboOrgano(id);
        } else {
            grid.getStore().load();
            this.limpiaFiltrosComboOrgano();
        }
    }

});
