Ext.define('goc.view.miembro.Main', {
    extend: 'Ext.panel.Panel',

    alias: 'widget.miembroMain',
    controller: 'miembroMainController',

    viewModel: {
        type: 'miembroMainModel'
    },

    requires: ['goc.view.miembro.MainController', 'goc.view.miembro.MainModel', 'goc.view.miembro.Grid'],

    title: appI18N.miembros.titulo,
    padding: 10,

    items: [
        {
            xtype: 'combobox',
            width: 320,
            emptyText: appI18N.miembros.seleccionarColectivo,
            bind: {
                store: '{organosStore}'
            },
            allowBlank: false,
            fieldLabel: 'Col·lectiu',
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
