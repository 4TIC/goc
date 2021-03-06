Ext.define('goc.model.ReunionDocumento', {
    extend: 'Ext.data.Model',
    requires : [ 'Ext.ux.uji.data.identifier.None' ],
    identifier :
    {
        type : 'none'
    },
    fields: [
        { name: 'id', type: 'number' },
        { name: 'descripcion', type: 'string' },
        { name: 'descripcionAlternativa', type: 'string' }
    ]
});