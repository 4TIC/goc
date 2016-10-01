Ext.define('goc.view.reunion.ComboTipoOrgano',
    {
        extend : 'Ext.ux.uji.combo.Combo',
        alias : 'widget.comboReunionTipoOrgano',
        allowBlank: true,
        emptyText: appI18N.reuniones.tipoOrgano,
        showClearIcon : true,
        width: 200,
        bind: {
            store: '{tipoOrganosStore}'
        },
        listeners: {
            change: function(combo, recordId) {
                if (!recordId) {
                    return combo.up('reunionGrid').fireEvent('tipoOrganoSelected', null);
                }
                var record = combo.getStore().getById(recordId);
                combo.up('reunionGrid').fireEvent('tipoOrganoSelected', recordId);
            }
        }
    });