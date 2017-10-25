Ext.define('goc.model.OrganoInvitado', {
    extend: 'Ext.data.Model',
    requires : [ 'Ext.ux.uji.data.identifier.None' ],
    identifier :
    {
        type : 'none'
    },
    fields: [
        { name: 'id', type: 'number' },
        { name: 'personaId', type: 'number' },
        { name: 'personaNombre', type: 'string' },
        { name: 'organoId', type: 'string' },
        { name: 'soloConsulta', type: 'boolean' }
    ]
});
