Ext.define('goc.model.ReunionInvitado', {
    extend: 'Ext.data.Model',
    /*requires : [ 'Ext.ux.uji.data.identifier.None' ],
    identifier :
    {
        type : 'none'
    },*/
    fields: [
        { name: 'id', type: 'number' },
        { name: 'reunionId', type: 'number' },
        { name: 'personaId', type: 'number' },
        { name: 'nombre', type: 'string' }
    ]
});