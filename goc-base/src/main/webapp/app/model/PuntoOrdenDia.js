Ext.define('goc.model.PuntoOrdenDia', {
    extend: 'Ext.data.Model',
    requires : [ 'Ext.ux.uji.data.identifier.None' ],
    identifier :
    {
        type : 'none'
    },
    fields: [
        { name: 'id', type: 'number' },
        { name: 'titulo', type: 'string' },
        { name: 'tituloAlternativo', type: 'string' },
        { name: 'descripcion', type: 'string' },
        { name: 'descripcionAlternativa', type: 'string' },
        { name: 'deliberaciones', type: 'string' },
        { name: 'deliberacionesAlternativas', type: 'string' },
        { name: 'acuerdos', type: 'string' },
        { name: 'acuerdosAlternativos', type: 'string' },
        { name: 'orden', type: 'number' },
        { name: 'reunionId', type: 'number' },
        { name: 'publico', type: 'boolean' }
    ],
});