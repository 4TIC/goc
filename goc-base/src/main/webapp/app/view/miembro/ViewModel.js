Ext.define('goc.view.miembro.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.miembroViewModel',

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
        organosStore: {
            type: 'organos'
        }
    }
});
