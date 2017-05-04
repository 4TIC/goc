Ext.define('Ext.ux.uji.data.identifier.None',
{
    extend : 'Ext.data.identifier.Generator',
    alias : 'data.identifier.none',

    generate : function()
    {
        return 'none';
    }
});