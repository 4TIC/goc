Ext.define('Ext.ux.uji.data.Store',
{
    extend : 'Ext.data.Store',

    autoLoad : false,
    autoSync : false,

    proxy :
    {
        type : 'rest',

        reader :
        {
            type : 'json',
            successProperty : 'success',
            rootProperty : 'data',
            totalProperty : 'totalCount'
        },

        writer :
        {
            type : 'json',
            writeAllFields : true
        }
    },

    constructor : function()
    {
        this.callParent(arguments);
        var proxy = this.getProxy();

        if (!proxy.url)
        {
            proxy.url = this.url;
        }
    }
});