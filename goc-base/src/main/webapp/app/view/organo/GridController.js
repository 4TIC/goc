Ext.define('goc.view.organo.GridController', {
    extend: 'Ext.ux.uji.grid.PanelController',
    alias: 'controller.organoGridController',
    onLoad: function() {
        var viewModel = this.getViewModel();
        viewModel.getStore('organosStore').load();
        viewModel.getStore('tipoOrganosStore').load({
            callback: function() {
                var grid = this.getView();
                grid.getView().refresh();
            },
            scope: this
        });
    },
    decideRowIsEditable: function(editor, context) {
        return context.record.get('externo') !== 'true' || context.record.phantom;
    },

    organoSelected: function(controller, record) {
        var grid = this.getView();
        var toolbar = grid.down("toolbar");
        toolbar.items.each(function(button) {
            if (button.name !== 'add') {
                button.setDisabled(record.get("externo") === "true");
            }
        });

        var record = grid.getView().getSelectionModel().getSelection()[0];
        console.log(record)
        grid.up('panel').down('grid[name=autorizadoGrid]').fireEvent('organoSelected', record.get('id'), record.get('externo'));

    }
});
