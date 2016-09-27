Ext.define('Ext.ux.uji.grid.ComboColumn',
{
    extend : 'Ext.grid.column.Column',
    alias : 'widget.combocolumn',

    config :
    {
        header : '',
        dataIndex : '',
        combo : {}
    },

    initComponent : function()
    {
        this.editor = this.combo;
        this.renderer = this.comboRenderer(this.combo);

        this.callParent(arguments);
    },

    comboRenderer : function(combo)
    {
        return function(value)
        {
            var record = combo.store.findRecord(combo.valueField, value, 0, false, false, true);
            return record ? record.get(combo.displayField) : combo.valueNotFoundText;
        };
    }
});
