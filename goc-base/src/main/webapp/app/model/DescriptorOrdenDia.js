Ext.define('goc.model.DescriptorOrdenDia', {
    extend: 'Ext.data.Model',
    requires : [ 'Ext.ux.uji.data.identifier.None' ],
    identifier :
        {
            type : 'none'
        },
    fields: [
        { name: 'id', type: 'string' },
        { name: 'idDescriptor', type: 'int' },
        { name: 'idClave', type: 'int' },
        { name: 'descriptor', type: 'string'},
        { name: 'clave', type: 'string'}
    ]
});
