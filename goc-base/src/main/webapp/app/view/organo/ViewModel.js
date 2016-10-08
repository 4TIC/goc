Ext.define('goc.view.organo.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.organoViewModel',

    requires: [
        'goc.store.Organos',
        'goc.store.TipoOrganos',
        'goc.store.OrganoAutorizados'
    ],

    formulas: {
        ocultaBotonHabilita: {
            get: function(getter) {
                var selected = getter('selectedOrgano')
                if (!selected || selected.get('externo') === "true") return true;
                return selected.get('inactivo') ? false : true;
            }
        },
        ocultaBotonInhabilita: {
            get: function(getter) {
                var selected = getter('selectedOrgano')
                if (!selected || selected.get('externo') === "true") return true;
                return selected.get('inactivo') ? true : false;
            }
        }
    },
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
