Ext.define('goc.store.ReunionInvitados', {
    extend: 'Ext.data.Store',
    alias: 'store.reunionInvitados',
    model: 'goc.model.ReunionInvitado',

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
