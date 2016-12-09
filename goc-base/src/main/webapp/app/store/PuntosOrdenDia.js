Ext.define('goc.store.PuntosOrdenDia', {
    extend: 'Ext.data.Store',
    alias: 'store.puntosOrdenDia',
    fields: [
        { name: 'id', type: 'int', persist: false },
        { name: 'titulo', type: 'string' },
        { name: 'tituloAlternativo', type: 'string' },
        { name: 'descripcion', type: 'string' },
        { name: 'descripcionAlternativa', type: 'string' },
        { name: 'deliberaciones', type: 'string' },
        { name: 'deliberacionesAlternativas', type: 'string' },
        { name: 'acuerdos', type: 'string' },
        { name: 'acuerdosAlternativos', type: 'string' },
        { name: 'orden', type: 'number' },
        { name: 'numeroDocumentos', type: 'number' },
        { name: 'reunionId', type: 'number' },
        { name: 'publico', type: 'boolean' }
    ],

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
