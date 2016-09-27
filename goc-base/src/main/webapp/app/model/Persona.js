Ext.define('goc.model.Persona', {
    extend: 'Ext.data.Model',
    requires : [ 'Ext.ux.uji.data.identifier.None' ],
    identifier :
    {
        type : 'none'
    },
    fields: [
        { name: 'id', type: 'number' },
        { name: 'nombre', type: 'string' },
        { name: 'email', type: 'string' }
    ]
});
