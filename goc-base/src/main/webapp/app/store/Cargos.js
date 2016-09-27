Ext.define('goc.store.Cargos', {
    extend: 'Ext.data.Store',
    alias: 'store.cargos',
    model: 'goc.model.Cargo',

    proxy: {
        type: 'rest',
        url: '/goc/rest/cargos',
        reader: {
            type: 'json',
            rootProperty: 'data'
        },
        writer: {
            type: 'json',
            writeAllFields : true
        }
    },

    autoLoad: true
});
