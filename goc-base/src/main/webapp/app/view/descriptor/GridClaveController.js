Ext.define('goc.view.descriptor.GridClaveController', {
    extend : 'Ext.ux.uji.grid.PanelController',
    alias : 'controller.gridClaveController',
    descriptor : null,

    claveSelected : function(controller, record)
    {
        var grid = this.getView();
        var toolbar = grid.down("toolbar");
        toolbar.items.each(function(button)
        {
            if (button.name !== 'add')
            {
                button.setDisabled(false);
            }
        });
    },

    descriptorSelected : function(record)
    {
        this.descriptor = record;

        this.getView().getStore().load({
            params : {
                idDescriptor : this.descriptor.get('id')
            }
        });
    },

    onAdd : function()
    {
        var gridDescriptores = this.getView().up('panel[alias=widget.descriptorMainPanel]').down('grid[name=descriptoresGrid]');

        if (!gridDescriptores || !gridDescriptores.getSelectedRow()) return;

        var grid = this.getView();

        var rec = Ext.create(grid.getStore().model.entityName, {
            id : null,
            idDescriptor : gridDescriptores.getSelectedRow().get('id')
        });
        grid.getStore().insert(0, rec);
        var editor = grid.plugins[0];
        editor.cancelEdit();
        editor.startEdit(rec, 0);
    }
});
