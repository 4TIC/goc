Ext.define('goc.model.Cargo', {
    extend: 'Ext.data.Model',
    requires : [ 'Ext.ux.uji.data.identifier.None' ],
    identifier :
    {
        type : 'none'
    },
    fields: [
        { name: 'id', type: 'number' },
        { name: 'firma', type: 'boolean' },
        { name: 'nombre', type: 'string' },
        { name: 'nombreAlternativo', type: 'string' }
    ]
});
