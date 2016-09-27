Ext.define('goc.store.Miembros', {
    extend: 'Ext.data.Store',
    alias: 'store.miembros',
    model: 'goc.model.Miembro',

    proxy: {
        type: 'rest',
        url: '/goc/rest/miembros',
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
