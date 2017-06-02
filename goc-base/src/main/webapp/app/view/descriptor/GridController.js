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

        if (!record)
        {
            claveGrid.clearStore();
            claveGrid.disable();
            descriptorTipoOrganoGrid.clearStore();
            descriptorTipoOrganoGrid.disable();
            return;
        }

        if (!record.phantom)
        {
            claveGrid.fireEvent('descriptorSelected', record);
            descriptorTipoOrganoGrid.fireEvent('descriptorSelected', record);
        }
    },

    onEditComplete : function(editor, context)
    {
        this.callParent(arguments);

        var claveGrid = this.getView().up('panel').down('grid[name=claveGrid]');
        var tipoOrganoGrid = this.getView().up('panel').down('grid[name=descriptorTipoOrganoGrid]');

        if (claveGrid)
        {
            claveGrid.setDisabled(false);
        }

        if (tipoOrganoGrid)
        {
            tipoOrganoGrid.setDisabled(false);
        }
    },

    onAdd : function()
    {
        var claveGrid = this.getView().up('panel').down('grid[name=claveGrid]');
        var tipoOrganoGrid = this.getView().up('panel').down('grid[name=descriptorTipoOrganoGrid]');

        if (claveGrid)
        {
            claveGrid.clearStore();
            claveGrid.setDisabled(true);
        }

        if (tipoOrganoGrid)
        {
            tipoOrganoGrid.clearStore();
            tipoOrganoGrid.setDisabled(true);
        }

        this.callParent();
    }
});
