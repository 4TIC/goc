Ext.define('Ext.ux.uji.grid.PanelController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.gridPanelController',

    onLoad: function() {
        var grid = this.getView();
        grid.getStore().load();
    },

    onAdd: function() {
        var grid = this.getView();
        var rec = Ext.create(grid.getStore().model.entityName, { id : null });
        grid.getStore().insert(0, rec);
        var editor = grid.plugins[0];
        editor.cancelEdit();
        editor.startEdit(rec, 0);
    },

    onEdit: function() {
        var grid = this.getView();
        var selection = grid.getView().getSelectionModel().getSelection()[0];
        if (selection) {
            var editor = grid.plugins[0];
            editor.cancelEdit();
            editor.startEdit(selection);
        }
    },

    onDelete: function() {
        var grid = this.getView();
        var records = grid.getView().getSelectionModel().getSelection();

        if (records.length === 1 && records[0].phantom === true) {
            return grid.getStore().remove(records);
        }

        if (records.length > 0)
        {
            Ext.Msg.confirm('Esborrar', 'Esteu segur/a de voler esborrar el registre ?', function(btn, text)
            {
                if (btn == 'yes')
                {
                    grid.getStore().remove(records);
                    grid.getStore().sync();
                }
            });
        }
    },

    onEditComplete: function(editor, context) {
        var grid = this.getView();
        grid.getStore().sync();
    },

    onCancelEdit: function(editor, context) {
        var grid = this.getView();
        var record = context.record;
        if (record.phantom) {
            grid.getStore().remove(context.record);
            grid.getStore().sync();
        }
    },

    onReload: function() {
        var grid = this.getView();
        grid.getStore().reload();
    }

});
