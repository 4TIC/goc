Ext.define('Ext.ux.uji.grid.PanelController', {
    extend : 'Ext.app.ViewController',
    alias : 'controller.gridPanelController',

    onLoad : function()
    {
        var grid = this.getView();
        grid.getStore().load();
    },

    onAdd : function()
    {
        var grid = this.getView();
        var rec = Ext.create(grid.getStore().model.entityName, {id : null});
        grid.getStore().insert(0, rec);
        var editor = grid.plugins[0];
        editor.cancelEdit();
        editor.startEdit(rec, 0);
    },

    onEdit : function()
    {
        var grid = this.getView();
        var selection = grid.getView().getSelectionModel().getSelection()[0];
        if (!selection)
        {
            return Ext.Msg.alert(appI18N.common.edicionRegistro || "Edició de registre", appI18N.common.seleccionarParaEditarRegistro || "Cal sel·leccionar un registre per a poder fer l'edició");
        }

        var editor = grid.plugins[0];
        editor.cancelEdit();
        editor.startEdit(selection);
    },

    onDelete : function()
    {
        var grid = this.getView();
        var records = grid.getView().getSelectionModel().getSelection();

        if (records.length === 0)
        {
            return Ext.Msg.alert(appI18N.common.borradoRegistro || "Esborrar registre", appI18N.common.seleccionarParaBorrarRegistro || "Cal sel·leccionar un registre per a poder esborrar-lo");
        }

        if (records.length === 1 && records[0].phantom === true)
        {
            return grid.getStore().remove(records);
        }

        if (records.length > 0)
        {
            Ext.Msg.confirm(appI18N.common.borradoRegistro || "Esborrar registre", appI18N.common.confirmarBorrado || 'Esteu segur/a de voler' +
            ' esborrar el registre ?', function(btn, text)
            {
                if (btn == 'yes')
                {
                    grid.getStore().remove(records);
                    grid.getStore().sync();
                }
            });
        }
    },

    onEditComplete : function(editor, context)
    {
        var grid = this.getView();
        var model = grid.getStore().getModel();

        if (context.record.id === 'none')
        {
            model.getField('id').persist = false;
        }

        grid.getStore().sync();

        model.getField('id').persist = true;
    },

    onCancelEdit : function(editor, context)
    {
        var grid = this.getView();
        var record = context.record;
        if (record.phantom)
        {
            grid.getStore().remove(context.record);
            grid.getStore().sync();
        }
    },

    onReload : function()
    {
        var grid = this.getView();
        grid.getStore().reload();
    }
});
