Ext.define('goc.view.descriptor.GridClaveController', {
    extend : 'Ext.ux.uji.grid.PanelController',
    alias : 'controller.gridClaveController',
    descriptor : null,
    onLoad : function()
    {
    },

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
        this.getView().setDisabled(false);
        this.descriptor = record;
        var vm = this.getViewModel();
        
        vm.getStore('clavesStore').load({
            params : {
                idDescriptor : this.descriptor.get('id')
            }
        });
    },

    initFilters : function()
    {
        var store = this.getStore('clavesStore');
        store.clearFilter();
    },

    onAdd : function()
    {
        this.initFilters();
        var grid = this.getView();
        var rec = Ext.create(grid.getStore().model.entityName, {
            id : null,
            idDescriptor : this.descriptor.get('id')
        });
        grid.getStore().insert(0, rec);
        var editor = grid.plugins[0];
        editor.cancelEdit();
        editor.startEdit(rec, 0);
    }
});
