Ext.define('goc.model.DescriptorTipoOrgano', {
    extend: 'Ext.data.Model',
    requires : [ 'Ext.ux.uji.data.identifier.None' ],
    identifier :
        {
            type : 'none'
        },
    fields: [
        { name: 'id', type: 'string' },
        { name: 'idDescriptor', type: 'int' },
        { name: 'idTipoOrgano', type: 'int' }
    ]
});
