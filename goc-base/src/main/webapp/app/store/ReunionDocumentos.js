Ext.define('goc.store.ReunionDocumentos', {
    extend: 'Ext.data.Store',
    alias: 'store.reunionDocumentos',
    model: 'goc.model.ReunionDocumento',

    proxy: {
        type: 'rest',
        url: '/goc/rest/reuniones',
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
