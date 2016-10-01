Ext.define('goc.view.organo.ComboOrgano',
    {
        extend : 'Ext.ux.uji.combo.Combo',
        alias : 'widget.comboOrgano',
        allowBlank: true,
        fieldLabel: appI18N.miembros.organo,
        emptyText: appI18N.miembros.seleccionaOrgano,
        showClearIcon : true,
        width: 380,
        bind: {
            store: '{organosStore}'
        },
        listeners: {
            change: function(combo, recordId) {
                if (!recordId) {
                    return combo.up('reunionGrid').fireEvent('tipoOrganoSelected', null, null);
                }
                var record = combo.getStore().getById(recordId);
                combo.up('panel').fireEvent('tipoOrganoSelected');
            }
        }
    });