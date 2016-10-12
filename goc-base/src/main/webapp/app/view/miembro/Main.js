Ext.define('goc.view.miembro.Main', {
    extend: 'Ext.panel.Panel',

    alias: 'widget.miembroMainPanel',
    controller: 'miembroMainController',

    viewModel: {
        type: 'miembroViewModel'
    },

    requires: ['goc.view.miembro.MainController', 'goc.view.miembro.ViewModel', 'goc.view.miembro.Grid'],

    title: appI18N.miembros.titulo,
    padding: 10,
    autoHeight: true,
    layout: {
        type: 'vbox',
        align : 'stretch',
        pack  : 'start'
    },
    items: [
        {
            xtype: 'combobox',
            emptyText: appI18N.miembros.seleccionaOrgano,
            bind: {
                store: '{organosStore}'
            },
            allowBlank: false,
            fieldLabel: appI18N.miembros.organo,
            triggerAction: 'all',
            queryMode: 'local',
            displayField: 'nombre',
            valueField: 'id',
            editable: false,
            listeners: {
                select: 'onOrganoSelected'
            }
        },
        {
            xtype: 'miembroGrid',
            flex: 1,
            hidden: true
        }
    ],
    listeners: {
        render: 'onLoad'
    }
});
