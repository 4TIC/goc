Ext.define('goc.store.DescriptoresTiposOrgano', {
    extend: 'Ext.data.Store',
    alias: 'store.descriptoresTiposOrgano',
    model: 'goc.model.DescriptorTipoOrgano',

    proxy: {
        type: 'rest',
        url: '',
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
