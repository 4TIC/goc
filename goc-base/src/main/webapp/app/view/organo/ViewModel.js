Ext.define('goc.view.organo.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.organoViewModel',

    requires: [
        'goc.store.Organos',
        'goc.store.TipoOrganos',
        'goc.store.OrganoAutorizados'
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
