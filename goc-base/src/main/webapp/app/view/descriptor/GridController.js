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
        var record = grid.getSelectedRow();
        var claveGrid = grid.up('panel').down('grid[name=claveGrid]');
        var descriptorTipoOrganoGrid = grid.up('panel').down('grid[name=descriptorTipoOrganoGrid]');
        var tab = this.getView().up('panel').down('tabpanel');

        if (!record)
        {
            claveGrid.clearStore();
            descriptorTipoOrganoGrid.clearStore();
            tab.disable();
            return;
        }

        if (!record.phantom)
        {
            claveGrid.fireEvent('descriptorSelected', record);
            descriptorTipoOrganoGrid.fireEvent('descriptorSelected', record);
            tab.enable();
        }
    },

    onEditComplete : function(editor, context)
    {
        this.callParent(arguments);

        var tab = this.getView().up('panel').down('tabpanel');

        if (tab)
        {
            tab.setDisabled(false);
        }
    },

    onAdd : function()
    {
        var claveGrid = this.getView().up('panel').down('grid[name=claveGrid]');
        var tipoOrganoGrid = this.getView().up('panel').down('grid[name=descriptorTipoOrganoGrid]');
        var tab = this.getView().up('panel').down('tabpanel');

        if (claveGrid)
        {
            claveGrid.clearStore();
        }

        if (tipoOrganoGrid)
        {
            tipoOrganoGrid.clearStore();
        }

        if (tab)
        {
            tab.setDisabled(true);
        }

        this.callParent();
    }
});
