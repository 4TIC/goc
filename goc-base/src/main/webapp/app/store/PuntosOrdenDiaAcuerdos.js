Ext.define('goc.store.PuntosOrdenDiaAcuerdos', {
    extend: 'Ext.data.Store',
    alias: 'store.puntosOrdenDiaAcuerdos',
    model: 'goc.model.PuntoOrdenDiaAcuerdo',

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
