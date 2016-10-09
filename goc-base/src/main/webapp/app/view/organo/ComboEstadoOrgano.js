Ext.define('goc.view.organo.ComboEstadoOrgano',
    {
        extend: 'Ext.ux.uji.combo.Combo',
        alias: 'widget.comboEstadoOrgano',
        allowBlank: true,
        fieldLabel: appI18N.organos.estado,
        labelWidth: 100,
        labelAlign: 'right',
        width: 280,
        triggerAction : 'all',
        queryMode : 'local',
        displayField : 'text',
        valueField : 'value',
        store: Ext.create('Ext.data.Store', {
            fields: ['value', 'text'],
            data: [
                {
                    value: false,
                    text: appI18N.organos.activos
                },
                {
                    value: true,
                    text: appI18N.organos.inactivos
                }
            ]
        }),
        listeners: {
            change: function (combo, inactivo) {
                return combo.up('organoMainPanel').fireEvent('filtrarOrganos', inactivo);
            }
        }
    })
;