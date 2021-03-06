Ext.define('goc.view.organo.ComboTipoOrgano',
    {
        extend : 'Ext.ux.uji.combo.Combo',
        alias : 'widget.comboTipoOrgano',
        allowBlank: true,
        emptyText: appI18N.organos.seleccionaTipoOrgano,
        showClearIcon : true,
        width: 380,
        bind: {
            store: '{tipoOrganosStore}'
        },
        listeners: {
            expand: function() {
                this.getStore().reload();
            },
            change: function(combo, recordId) {
                if (!recordId) {
                    return combo.up('organoMainPanel').fireEvent('tipoOrganoSelected', null, null);
                }
                var record = combo.getStore().getById(recordId);
                combo.up('organoMainPanel').fireEvent('tipoOrganoSelected', recordId);
            }
        }
    });