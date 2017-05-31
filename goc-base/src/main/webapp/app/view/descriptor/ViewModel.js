Ext.define('goc.view.descriptor.ViewModel', {
    extend : 'Ext.app.ViewModel',
    alias : 'viewmodel.descriptorViewModel',

    requires : [
        'goc.store.Descriptores',
        'goc.store.Claves',
        'goc.store.DescriptoresTiposOrgano',
        'goc.store.TipoOrganos'
    ],

    stores : {
        descriptoresStore : {
            type : 'descriptores'
        },
        clavesStore : {
            type : 'claves'
        },
        descriptoresTiposOrganoStore : {
            type : 'descriptoresTiposOrgano'
        },
        tipoOrganosStore : {
            type : 'tipoOrganos'
        }
    }
});
