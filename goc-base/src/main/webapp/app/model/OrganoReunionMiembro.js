Ext.define('goc.model.OrganoReunionMiembro', {
    extend: 'Ext.data.Model',
    requires : [ 'Ext.ux.uji.data.identifier.None' ],
    identifier :
    {
        type : 'none'
    },
    fields: [
        { name: 'id', type: 'number' },
        { name: 'nombre', type: 'string' },
        { name: 'email', type: 'string' },
        { name: 'asistencia', type: 'bool' },
        { name: 'organoReunionId', type: 'number' },
        { name: 'reunionId', type: 'number' },
        { name: 'miembroId', type: 'number' },
        { name: 'organoId', type: 'number' },
        { name: 'suplenteId', type: 'number' },
        { name: 'suplenteNombre', type: 'string' }
    ]
});
