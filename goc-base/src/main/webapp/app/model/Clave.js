Ext.define('goc.model.Clave', {
    extend: 'Ext.data.Model',
    requires : [ 'Ext.ux.uji.data.identifier.None' ],
    identifier :
        {
            type : 'none'
        },
    fields: [
        { name: 'id', type: 'string' },
        { name: 'clave', type: 'string' },
        { name: 'claveAlternativa', type: 'string' },
        { name: 'idDescriptor', type: 'string'}
    ]
});
