Ext.define('Ext.ux.uji.combo.Combo',
{
    extend : 'Ext.form.field.ComboBox',
    alias : 'widget.ujicombo',

    valueField : 'id',
    displayField : 'nombre',
    triggerAction : 'all',
    queryMode : 'local',

    editable : false,
    allowBlank : false,
    showClearIcon : false,

    triggers :
    {
        clear :
        {
            cls : 'x-form-clear-trigger',
            handler : function(cmp)
            {
                cmp.clearValue();
            },
            scope : 'this',
            weight : -1
        }
    },

    initEvents : function()
    {
        this.callParent(arguments);
        var trigger = this.getTrigger("clear");
        trigger.hide();

        this.addManagedListener(this.bodyEl, 'mouseover', function()
        {
            if (this.showClearIcon)
            {
                trigger.show();
            }
        }, this);

        this.addManagedListener(this.bodyEl, 'mouseout', function()
        {
            trigger.hide();
        });
    }
});