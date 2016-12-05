Ext.define('goc.model.Organo', {
    extend: 'Ext.data.Model',
    requires : [ 'Ext.ux.uji.data.identifier.None' ],
    identifier :
    {
        type : 'none'
    },
    fields: [
        { name: 'id', type: 'string' },
        { name: 'nombre', type: 'string' },
        { name: 'nombreAlternativo', type: 'string' },
        { name: 'inactivo', type: 'boolean' },
        { name: 'tipoOrganoId', type: 'number' }
    ]
});
