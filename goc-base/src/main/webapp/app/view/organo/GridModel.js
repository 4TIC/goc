Ext.define('goc.view.organo.GridModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.organoGridModel',
    requires: [
        'goc.store.Organos',
        'goc.store.TipoOrganos',
        'goc.store.OrganoAutorizados',
        'goc.store.Personas'
    ],
    stores: {
        organosStore: {
            type: 'organos'
        },
        tipoOrganosStore: {
            type: 'tipoOrganos'
        },
        organoAutorizadosStore: {
            type: 'organoAutorizados'
        }

    }
});
