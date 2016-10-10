Ext.define('goc.view.common.ComboOrgano',
    {
        extend : 'Ext.ux.uji.combo.Combo',
        alias : 'widget.comboOrgano',
        allowBlank: true,
        emptyText: appI18N.reuniones.organo,
        showClearIcon : true,
        width: 200,

        bind: {
            store: '{organosStore}'
        },
        listeners: {
            change: function(combo, recordId) {
                if (!recordId) {
                    return combo.up('panel').fireEvent('organoSelected', null, null);
                }
                var record = combo.getStore().getById(recordId);
                combo.up('panel').fireEvent('organoSelected', recordId, record.get('externo'));
            }
        }

    });