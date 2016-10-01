Ext.define('goc.view.reunion.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.reunionViewModel',
    requires: [
        'goc.store.Reuniones',
        'goc.store.ReunionDocumentos',
        'goc.store.PuntosOrdenDia',
        'goc.store.PuntosOrdenDiaDocumentos',
        'goc.store.OrganoReunionMiembros',
        'goc.store.TipoOrganos',
        'goc.store.Organos',
        'goc.store.Personas'
    ],
    stores: {
        reunionesStore: {
            type: 'reuniones',
        },
        reunionDocumentosStore: {
            type: 'reunionDocumentos'
        },
        puntosOrdenDiaStore: {
            type: 'puntosOrdenDia'
        },
        puntosOrdenDiaDocumentosStore: {
            type: 'puntosOrdenDiaDocumentos'
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
