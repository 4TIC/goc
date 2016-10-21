Ext.define('goc.store.TipoOrganos', {
    extend: 'Ext.data.Store',
    alias: 'store.tipoOrganos',
    model: 'goc.model.TipoOrgano',

    proxy: {
        type: 'rest',
        url: '/goc/rest/tipoOrganos',
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
