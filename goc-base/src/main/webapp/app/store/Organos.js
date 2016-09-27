Ext.define('goc.store.Organos', {
    extend: 'Ext.data.Store',
    alias: 'store.organos',
    model: 'goc.model.Organo',

    proxy: {
        type: 'rest',
        url: '/goc/rest/organos',
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
