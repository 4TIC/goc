Ext.define('goc.view.miembro.GridController',
{
    extend : 'Ext.ux.uji.grid.PanelController',
    alias : 'controller.miembroGridController',
    onAdd : function()
    {
        var grid = this.getView();
        var mainPanel = grid.up('miembroMainPanel');
        var combo = mainPanel.down("combobox");
        var organo = combo.getSelection();
        var rec = Ext.create('goc.model.Miembro', {
            organoId: organo.get('id')
        });

        var window = Ext.create('goc.view.common.LookupWindowPersonas',
        {
            appPrefix : 'goc',
            title : appI18N.miembros.anyadirMiembro
        });

        window.show();

        window.on('LookoupWindowClickSeleccion', function(res)
        {
            rec.set('personaId', res.get('id'));
            rec.set('nombre', res.get('nombre'));
            rec.set('email', res.get('email'));

            grid.getStore().insert(0, rec);
            var editor = grid.plugins[0];
            editor.cancelEdit();
            editor.startEdit(rec, 0);
        });
    },

    decideRowIsEditable : function(editor, context)
    {
        var grid = this.getView();
        var mainPanel = grid.up('miembroMainPanel');
        var combo = mainPanel.down("combobox");
        var organo = combo.getSelection();

        if (organo.get('externo') === 'true')
        {
            return false;
        }

        return true;
    }
});
