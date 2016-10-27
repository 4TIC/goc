Ext.define('goc.model.Miembro', {
    extend: 'Ext.data.Model',
    requires : [ 'Ext.ux.uji.data.identifier.None' ],
    identifier :
    {
        type : 'none'
    },
    fields: [
        { name: 'id', type: 'number' },
        { name: 'personaId', type: 'number' },
        { name: 'nombre', type: 'string' },
        { name: 'email', type: 'string' },
        { name: 'externo', type: 'boolean' },
        { name: 'organoId', type: 'number' },
        { name: 'cargoId', type: 'number' }
    ]
});
