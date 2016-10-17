Ext.define('goc.view.common.ComboTipoOrgano',
    {
        extend : 'Ext.ux.uji.combo.Combo',
        alias : 'widget.comboReunionTipoOrgano',
        allowBlank: true,
        emptyText: appI18N.reuniones.tipoOrgano,
        matchFieldWidth: false,
        showClearIcon : true,
        width: 200,
        bind: {
            store: '{tipoOrganosStore}'
        },
        listeners: {
            change: function(combo, recordId) {
                if (!recordId) {
                    return combo.up('panel').fireEvent('tipoOrganoSelected', null);
                }
                var record = combo.getStore().getById(recordId);
                combo.up('panel').fireEvent('tipoOrganoSelected', recordId);
            }
        }
    });