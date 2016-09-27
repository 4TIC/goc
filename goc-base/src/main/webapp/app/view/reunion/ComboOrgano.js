Ext.define('goc.view.reunion.ComboOrgano',
    {
        extend : 'Ext.ux.uji.combo.Combo',
        alias : 'widget.comboOrgano',
        allowBlank: true,
        fieldLabel: appI18N.reuniones.organo,
        showClearIcon : true,
        width: 380,

        bind: {
            store: '{organosStore}'
        },
        listeners: {
            change: function(combo, recordId) {
                if (!recordId) {
                    return combo.up('reunionGrid').fireEvent('organoSelected', null, null);
                }
                var record = combo.getStore().getById(recordId);
                combo.up('reunionGrid').fireEvent('organoSelected', recordId, record.get('externo'));
            }
        }

    });