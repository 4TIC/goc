Ext.define('goc.view.descriptor.GridTipoOrganoController', {
    extend : 'Ext.ux.uji.grid.PanelController',
    alias : 'controller.gridTipoOrganoController',
    descriptor : null,

    descriptorSelected : function(record)
    {
        this.getView().setDisabled(false);
        this.descriptor = record;
        var store = this.getView().getStore();

        store.getProxy().url = '/goc/rest/descriptores/' + this.descriptor.get('id') + '/tiposorgano';

        store.load();
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
