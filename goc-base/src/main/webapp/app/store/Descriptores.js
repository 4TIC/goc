Ext.define('goc.store.Descriptores', {
    extend: 'Ext.data.Store',
    alias: 'store.descriptores',
    model: 'goc.model.Descriptor',

    proxy: {
        type: 'rest',
        url: '/goc/rest/descriptores',
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
