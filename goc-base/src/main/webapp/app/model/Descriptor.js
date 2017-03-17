Ext.define('goc.model.Descriptor', {
    extend: 'Ext.data.Model',
    requires : [ 'Ext.ux.uji.data.identifier.None' ],
    identifier :
        {
            type : 'none'
        },
    fields: [
        { name: 'id', type: 'string' },
        { name: 'descriptor', type: 'string' },
        { name: 'descriptorAlternativo', type: 'string' },
        { name: 'descripcion', type: 'string' },
        { name: 'descripcionAlternativa', type: 'string' },
        { name: 'estado', type: 'int' }
    ]
});
