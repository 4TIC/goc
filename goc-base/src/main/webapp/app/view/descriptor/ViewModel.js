Ext.define('goc.view.descriptor.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.descriptorViewModel',

    requires: [
        'goc.store.Descriptores',
        'goc.store.Claves'/*,
        'goc.store.TipoOrganos',
        'goc.store.OrganoAutorizados'*/
    ],

    stores: {
        descriptoresStore: {
            type: 'descriptores'
        },
        clavesStore: {
            type: 'claves'
        }
    }
});
