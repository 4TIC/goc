Ext.define('goc.view.descriptor.GridController', {
    extend : 'Ext.ux.uji.grid.PanelController',
    alias : 'controller.descriptorGridController',
    onLoad : function()
    {
        var viewModel = this.getViewModel();
        viewModel.getStore('descriptoresStore').load();
    },

    descriptorSelected : function(controller, record)
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

        var selection = grid.getView().getSelectionModel().getSelection();
        
        var record = selection[selection.length - 1];
        if (!record.phantom)
        {
            grid.up('panel').down('grid[name=claveGrid]').fireEvent('descriptorSelected', record);
        }

    },

    onEditComplete : function(editor, context)
    {
        this.callParent(arguments);

        var claveGrid = this.getView().up('panel').down('grid[name=claveGrid]');

        if (claveGrid)
        {
            claveGrid.setDisabled(false);
        }
    },

    onAdd : function()
    {
        var claveGrid = this.getView().up('panel').down('grid[name=claveGrid]');

        if (claveGrid)
        {
            claveGrid.clearStore();
            claveGrid.setDisabled(true);
        }

        this.callParent();
    }
});
