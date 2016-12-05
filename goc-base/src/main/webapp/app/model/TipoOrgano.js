Ext.define('goc.model.TipoOrgano', {
    extend: 'Ext.data.Model',
    requires : [ 'Ext.ux.uji.data.identifier.None' ],
    identifier :
    {
        type : 'none'
    },
    fields: [
        { name: 'id', type: 'number' },
        { name: 'codigo', type: 'string' },
        { name: 'nombre', type: 'string' },
        { name: 'nombreAlternativo', type: 'string' }
    ]
});
