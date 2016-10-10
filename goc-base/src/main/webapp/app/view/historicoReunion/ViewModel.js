Ext.define('goc.view.historicoReunion.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.historicoReunionViewModel',
    requires: [
        'goc.store.Reuniones',
        'goc.store.TipoOrganos',
        'goc.store.Organos'
    ],
    stores: {
        reunionesStore: {
            type: 'reuniones'
        },
        tipoOrganosStore: {
            type: 'tipoOrganos',
            autoLoad: true
        },
        organosStore: {
            type: 'organos',
            autoLoad: true
        }
    }
});
