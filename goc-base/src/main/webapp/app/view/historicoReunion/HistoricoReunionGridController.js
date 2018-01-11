Ext.define('goc.view.historicoReunion.HistoricoReunionGridController', {
    extend: 'Ext.ux.uji.grid.PanelController',
    alias: 'controller.historicoReunionGridController',
    onLoad: function () {
        var viewModel = this.getViewModel();
        var store = viewModel.getStore('reunionesStore');
        store.getProxy().url = '/goc/rest/reuniones/completadas';
        store.load();
    },
    organoSelected: function (id, externo) {
        var grid = this.getView();

        if (id) {
            grid.getStore().load({
                params: {
                    organoId: id,
                    externo: externo
                }
            });
        } else {
            var comboTipoOrgano = grid.down('comboReunionTipoOrgano');
            grid.getStore().load({
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
                params: {
                    tipoOrganoId: id
                }
            });
            var comboOrganos = grid.down('comboOrgano');
            comboOrganos.clearValue();
            this.filtraComboOrgano(id);
        } else {
            grid.getStore().load();
            this.limpiaFiltrosComboOrgano();
        }
    },

    onSearchReunion: function(field, searchString) {
        var viewModel = this.getViewModel();
        var store = viewModel.getStore('reunionesStore');

        if (!searchString) {
            store.clearFilter();
            return;
        }

        var filter  = new Ext.util.Filter(
            {
                id : 'search',
                property : 'asunto',
                value : searchString,
                anyMatch: true
            });

        store.addFilter(filter);
    }
});
