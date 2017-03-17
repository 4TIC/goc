Ext.define('goc.view.descriptor.GridController', {
    extend: 'Ext.ux.uji.grid.PanelController',
    alias: 'controller.descriptorGridController',
    onLoad: function() {
        var viewModel = this.getViewModel();
        viewModel.getStore('descriptoresStore').load();
    },

    descriptorSelected: function(controller, record) {
        var grid = this.getView();
        var toolbar = grid.down("toolbar");
        toolbar.items.each(function(button) {
            if (button.name !== 'add') {
                button.setDisabled(false);
            }
        });

        var record = grid.getView().getSelectionModel().getSelection()[0];
        if(!record.phantom) {
            grid.up('panel').down('grid[name=claveGrid]').fireEvent('descriptorSelected', record);
        }

    },

    initFilters: function() {
        var store = this.getStore('descriptoresStore');
        store.clearFilter();
    },

    onAdd: function() {
        this.initFilters();
        var grid = this.getView();
        var rec = Ext.create(grid.getStore().model.entityName, { id : null });
        grid.getStore().insert(0, rec);
        var editor = grid.plugins[0];
        editor.cancelEdit();
        editor.startEdit(rec, 0);
    },

    onEditComplete: function(editor, context) {
        var grid = this.getView();
        grid.getStore().sync();
        grid.setSelection(null);
    }
});
