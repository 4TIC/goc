Ext.define('goc.view.organo.ComboEstadoOrgano',
    {
        extend: 'Ext.ux.uji.combo.Combo',
        alias: 'widget.comboEstadoOrgano',
        allowBlank: true,
        emptyText: appI18N.organos.seleccionaEstadoOrganos,
        showClearIcon: true,
        width: 180,
        triggerAction : 'all',
        queryMode : 'local',
        displayField : 'text',
        valueField : 'value',
        store: Ext.create('Ext.data.Store', {
            fields: ['value', 'text'],
            data: [
                {
                    value: false,
                    text: 'Actius'
                },
                {
                    value: true,
                    text: 'Inactius'
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