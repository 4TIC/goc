Ext.define('goc.store.OrganoReunionMiembros', {
    extend: 'Ext.data.Store',
    alias: 'store.organoReunionMiembros',
    fields: [
        { name: 'id', type: 'number' },
        { name: 'nombre', type: 'string' },
        { name: 'email', type: 'string' },
        { name: 'cargoNombre', type: 'string' },
        { name: 'asistencia', type: 'bool', defaultValue: true },
        { name: 'organoReunionId', type: 'number' },
        { name: 'reunionId', type: 'number' },
        { name: 'miembroId', type: 'number' },
        { name: 'organoId', type: 'string' },
        { name: 'suplenteId', type: 'number' },
        { name: 'suplenteNombre', type: 'string' },
        { name: 'suplenteEmail', type: 'string' },
        { name: 'info', mapping: 'nombre', convert: function(v, record) {
            return v + ' (' + record.get('cargoNombre') + ')';
        }}
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
