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

    items: [
        {
            xtype: 'combobox',
            width: 320,
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
            minHeight: 300,
            hidden: true
        }
    ],
    listeners: {
        render: 'onLoad'
    }
});
