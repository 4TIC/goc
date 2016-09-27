Ext.define('goc.store.PuntosOrdenDiaDocumentos', {
    extend: 'Ext.data.Store',
    alias: 'store.puntosOrdenDiaDocumentos',
    model: 'goc.model.PuntoOrdenDiaDocumento',

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
