Ext.define('goc.store.DescriptoresOrdenDia', {
    extend: 'Ext.data.Store',
    alias: 'store.descriptoresOrdenDia',
    model: 'goc.model.DescriptorOrdenDia',

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
    }
});
