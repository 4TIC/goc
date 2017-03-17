Ext.define('goc.view.descriptor.MainController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.descriptorMainController',

    onLoad: function () {
        var viewModel = this.getViewModel();
        viewModel.getStore('descriptoresStore').load();
    }/*,

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
                value: recordId
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

    }*/
});
