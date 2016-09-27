Ext.define('Ext.ux.uji.form.TwinComboBox',
{
    extend : 'Ext.form.ComboBox',
    requires : [ 'Ext.form.field.Trigger' ],

    getTrigger : function()
    {
        var fieldTrigger = Ext.create('Ext.form.TwinTriggerField');
        return fieldTrigger.getTrigger();
    },

    initTrigger : function()
    {
        var fieldTrigger = Ext.create('Ext.form.TwinTriggerField');
        fieldTrigger.initTrigger();
    },

    trigger1Class : 'x-form-clear-trigger',
    hideTrigger1 : true,

    initComponent : function()
    {
        this.triggerConfig =
        {
            tag : 'span',
            cls : 'x-form-twin-triggers',
            cn : [
            {
                tag : 'img',
                src : Ext.BLANK_IMAGE_URL,
                cls : 'x-form-trigger ' + this.trigger1Class
            },
            {
                tag : 'img',
                src : Ext.BLANK_IMAGE_URL,
                cls : 'x-form-trigger ' + this.trigger2Class
            } ]
        };

        this.callParent(arguments);
    },

    onTrigger2Click : function()
    {
        this.onTriggerClick();
    },

    onTrigger1Click : function()
    {
        this.clearValue();
    }
});
