Ext.define('goc.store.Claves', {
    extend: 'Ext.data.Store',
    alias: 'store.claves',
    model: 'goc.model.Clave',

    proxy: {
        type: 'rest',
        url: '/goc/rest/claves',
        reader: {
            type: 'json',
            rootProperty: 'data'
        },
        writer: {
            type: 'json',
            writeAllFields : true
        }
    }
});
