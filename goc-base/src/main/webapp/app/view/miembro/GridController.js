Ext.define('goc.view.miembro.GridController', {
    extend: 'Ext.ux.uji.grid.PanelController',
    alias: 'controller.miembroGridController',
    onAdd: function() {
        var mainPanel = Ext.ComponentQuery.query("miembroMain")[0];
        var combo = mainPanel.down("combobox");
        var organo = combo.getSelection();
        var rec = Ext.create('goc.model.Miembro', {
            organoId: organo.get('id')
        });

        var grid = this.getView();
        grid.getStore().insert(0, rec);
        var editor = grid.plugins[0];
        editor.cancelEdit();
        editor.startEdit(rec, 0);
    },
    
    decideRowIsEditable: function(editor, context) {
        var grid = this.getView();
        var mainPanel = grid.up('miembroMainPanel');
        var combo = mainPanel.down("combobox");
        var organo = combo.getSelection();
        
        if (organo.get('externo') === 'true') {
            return false;
        }
        
        return true;
    }
});
