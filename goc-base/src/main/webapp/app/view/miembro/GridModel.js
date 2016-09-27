Ext.define('goc.view.miembro.GridModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.miembroGridModel',

    requires: [
        'goc.model.Miembro',
        'goc.model.Organo',
        'goc.store.Miembros',
        'goc.store.Organos',
        'goc.store.Cargos'
    ],

    stores: {
        miembrosStore: {
            type: 'miembros'
        },
        cargosStore: {
            type: 'cargos'
        },
        organoStore: {
            type: 'organos',
            filters: [{
                property: 'externo',
                value: 'false'
            }]
        }
    }
});
