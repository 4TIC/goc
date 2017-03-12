Ext.define('goc.view.organo.MainController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.organoMainController',

    onLoad: function () {
        var viewModel = this.getViewModel();
        viewModel.getStore('organosStore').load();
    },

    onTipoOrganoSelected: function (recordId) {
        var vm = this.getViewModel();
        var store = vm.getStore('organosStore');

        if (!recordId) {
            store.removeFilter('tipoOrganoId');
            return;
        }

        var filter = new Ext.util.Filter(
            {
                id: 'tipoOrganoId',
                property: 'tipoOrganoId',
                value: recordId,
                exactMatch: true
            });

        store.addFilter(filter);
    },

    onFiltrarOrganos: function (inactivos) {
        var vm = this.getViewModel();
        var store = vm.getStore('organosStore');

        var filter = new Ext.util.Filter({
            property: 'inactivo',
            value: inactivos
        });

        store.addFilter(filter);

    }
});
