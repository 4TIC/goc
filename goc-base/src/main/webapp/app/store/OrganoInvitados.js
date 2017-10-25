Ext.define('goc.store.OrganoInvitados', {
    extend: 'Ext.data.Store',
    alias: 'store.organoInvitados',
    model: 'goc.model.OrganoInvitado',

    proxy: {
        type: 'rest',
        url: '/goc/rest/organos/invitados',
        reader: {
            type: 'json',
            rootProperty: 'data'
        },
        writer: {
            type: 'json',
            writeAllFields : true
        }
    },

    autoLoad: false,
    autoSync: false
});
