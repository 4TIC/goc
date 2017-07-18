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
        { name: 'asistencia', type: 'bool', defaultValue: true },
        { name: 'organoReunionId', type: 'number' },
        { name: 'reunionId', type: 'number' },
        { name: 'miembroId', type: 'number' },
        { name: 'organoId', type: 'string' },
        { name: 'suplenteId', type: 'number' },
        { name: 'suplenteNombre', type: 'string' },
        { name: 'suplenteEmail', type: 'string' },
        { name: 'delegadoVotoId', type: 'number' },
        { name: 'delegadoVotoNombre', type: 'string' },
        { name: 'delegadoVotoEmail', type: 'string' }
    ]
});
