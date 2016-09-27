Ext.define('goc.store.OrganoAutorizados', {
    extend: 'Ext.data.Store',
    alias: 'store.organoAutorizados',
    model: 'goc.model.OrganoAutorizado',

    proxy: {
        type: 'rest',
        url: '/goc/rest/organos/autorizados',
        reader: {
            type: 'json',
            rootProperty: 'data'
        },
        writer: {
            type: 'json',
            writeAllFields : true
        }
    },

    autoLoad: false
});
