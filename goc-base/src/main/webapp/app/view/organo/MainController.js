Ext.define('goc.view.organo.MainController', {
    extend : 'Ext.app.ViewController',
    alias : 'controller.organoMainController',

    onLoad : function()
    {
        var viewModel = this.getViewModel();
        viewModel.getStore('organosStore').load();
    },

    onTipoOrganoSelected : function(recordId)
    {
        var vm = this.getViewModel();
        var store = vm.getStore('organosStore');

        var grid = this.getView().down('grid[name=organosGrid]');
        grid.getSelectionModel().deselectAll();

        if (!recordId)
        {
            store.removeFilter('tipoOrganoId');
            return;
        }

        var filter = new Ext.util.Filter(
        {
            id : 'tipoOrganoId',
            property : 'tipoOrganoId',
            value : recordId,
            exactMatch : true
        });

        store.addFilter(filter);
    },

    onFiltrarOrganos : function(inactivos)
    {
        var vm = this.getViewModel();
        var store = vm.getStore('organosStore');

        var grid = this.getView().down('grid[name=organosGrid]');
        grid.getSelectionModel().deselectAll();

        var filter = new Ext.util.Filter({
            property : 'inactivo',
            value : inactivos
        });

        store.addFilter(filter);
    },

    onSearchOrgano: function(searchString)
    {
        var viewModel = this.getViewModel();
        var store = viewModel.getStore('organosStore');

        var filter  = new Ext.util.Filter(
        {
            id : 'search',
            property : 'nombre',
            value : searchString,
            anyMatch: true
        });

        store.addFilter(filter);
    }
});

