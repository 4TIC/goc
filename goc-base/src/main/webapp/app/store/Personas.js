Ext.define('goc.store.Personas', {
    extend: 'Ext.data.Store',
    alias: 'store.personas',
    model: 'goc.model.Persona',

    proxy: {
        type: 'rest',
        url: '/goc/rest/personas',
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
