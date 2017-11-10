Ext.define('goc.model.ReunionInvitado', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'id', type: 'number' },
        { name: 'reunionId', type: 'number' },
        { name: 'personaId', type: 'number' },
        { name: 'personaNombre', type: 'string' },
        { name: 'personaEmail', type: 'string' },
        { name: 'motivoInvitacion', type: 'string' }
    ]
});