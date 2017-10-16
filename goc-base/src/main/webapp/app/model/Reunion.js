Ext.define('goc.model.Reunion', {
    extend: 'Ext.data.Model',
    requires : [ 'Ext.ux.uji.data.identifier.None' ],
    identifier :
    {
        type : 'none'
    },
    fields: [
        { name: 'id', type: 'number' },
        { name: 'asunto', type: 'string' },
        { name: 'asuntoAlternativo', type: 'string' },
        { name: 'descripcion', type: 'string' },
        { name: 'descripcionAlternativa', type: 'string' },
        { name: 'ubicacion', type: 'string' },
        { name: 'ubicacionAlternativa', type: 'string' },
        { name: 'urlGrabacion', type: 'string' },
        { name: 'numeroDocumentos', type: 'number' },
        { name: 'fecha', type: 'date', dateFormat: 'd/m/Y H:i:s' },
        { name: 'fechaSegundaConvocatoria', type: 'date', dateFormat: 'd/m/Y H:i:s' },
        { name: 'completada', type: 'boolean' },
        { name: 'telematica', type: 'boolean' },
        { name: 'admiteSuplencia', type: 'boolean', defaultValue: true },
        { name: 'admiteDelegacionVoto', type: 'boolean', defaultValue: true },
        { name: 'admiteComentarios', type: 'boolean', defaultValue: true },
        { name: 'telematicaDescripcion', type: 'string' },
        { name: 'telematicaDescripcionAlternativa', type: 'string' },
        { name: 'publica', type: 'boolean' },
        { name: 'avisoPrimeraReunion', type: 'boolean'},
        { name: 'numeroSesion', type: 'string' },
        {
            name: 'duracion',
            type: 'number'
        },
        {
            name: 'hora',
            type: 'string',
            calculate: function(data) {
                return Ext.Date.format(new Date(data.fecha), 'H:i');
            }
        },
        {
            name: 'horaSegundaConvocatoria',
            type: 'string',
            calculate: function(data) {
                return Ext.Date.format(new Date(data.fechaSegundaConvocatoria), 'H:i');
            }
        }
    ]
});