Ext.define('goc.store.Reuniones', {
    extend: 'Ext.data.Store',
    alias: 'store.reuniones',
    fields: [
        { name: 'id', type: 'int', persist: false },
        { name: 'asunto', type: 'string' },
        { name: 'asuntoAlternativo', type: 'string' },
        { name: 'descripcion', type: 'string' },
        { name: 'descripcionAlternativa', type: 'string' },
        { name: 'ubicacion', type: 'string' },
        { name: 'ubicacionAlternativa', type: 'string' },
        { name: 'urlGrabacion', type: 'string' },
        { name: 'numeroDocumentos', type: 'number' },
        { name: 'completada', type: 'boolean' },
        { name: 'publica', type: 'boolean' },
        { name: 'telematica', type: 'boolean' },
        { name: 'admiteSuplencia', type: 'boolean', defaultValue: true },
        { name: 'admiteDelegacionVoto', type: 'boolean', defaultValue: true },
        { name: 'admiteComentarios', type: 'boolean', defaultValue: true },
        { name: 'telematicaDescripcion', type: 'string' },
        { name: 'telematicaDescripcionAlternativa', type: 'string' },
        { name: 'avisoPrimeraReunion', type: 'boolean'},
        { name: 'avisoPrimeraReunionUser', type: 'string'},
        { name: 'avisoPrimeraReunionFecha', type: 'date', dateFormat: 'd/m/Y H:i:s'},
        { name: 'numeroSesion', type: 'string' },
        {
            type: 'number',
            name: 'duracion'
        },
        { name: 'fecha', type: 'date', dateFormat: 'd/m/Y H:i:s' },
        { name: 'fechaSegundaConvocatoria', type: 'date', dateFormat: 'd/m/Y H:i:s' },
        {
            name: 'hora',
            type: 'string',
            calculate: function(data) {
                return (data.fecha) ? Ext.Date.format(new Date(data.fecha), 'H:i') : '';
            }
        },
        {
            name: 'horaSegundaConvocatoria',
            type: 'string',
            calculate: function(data) {
                return (data.fechaSegundaConvocatoria) ? Ext.Date.format(new Date(data.fechaSegundaConvocatoria), 'H:i') : '';
            }
        },
        { name: 'urlActa', type: 'string'},
        { name: 'urlActaAlternativa', type: 'string'}
    ],
    proxy: {
        type: 'rest',
        url: '/goc/rest/reuniones',
        reader: {
            type: 'json',
            rootProperty: 'data'
        },
        writer: {
            type: 'json',
            writeAllFields : true
        }
    }
});