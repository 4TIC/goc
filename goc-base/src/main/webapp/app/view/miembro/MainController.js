Ext.define('goc.view.miembro.MainController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.miembroMainController',
    
    onLoad: function() {
        var viewModel = this.getViewModel();
        viewModel.getStore('organosStore').load({
            url: '/goc/rest/organos/activos'
        });
    },

    onOrganoSelected: function(combo, record) {
        var view = this.getView();

        var grid = view.down('grid');
        grid.setHidden(false);
        var store = grid.getStore().load({
            params: {
                organoId: record.get('id'),
                externo: record.get('externo')
            }
        });

        var toolbar = grid.down("toolbar");
        toolbar.items.each(function(button) {
            button.setDisabled(record.get("externo") === "true");
        });
    }
});
