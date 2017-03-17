Ext.define('goc.view.descriptor.Main', {
    extend: 'Ext.panel.Panel',

    alias: 'widget.descriptorMainPanel',
    controller: 'descriptorMainController',

    viewModel: {
        type: 'descriptorViewModel'
    },

    requires: ['goc.view.descriptor.MainController', 'goc.view.descriptor.ViewModel', 'goc.view.descriptor.Grid', 'goc.view.descriptor.GridClave'],

    title: appI18N.descriptores.titulo,
    padding: 10,
    autoHeight: true,
    layout: {
        type: 'vbox',
        align : 'stretch',
        pack  : 'start'
    },
    items: [
       /* {
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
        },*/
        {
            xtype: 'descriptorGrid',
            flex: 1
        },
        {
            xtype: 'claveGrid',
            flex: 1
        }
    ],
    listeners: {
        render: 'onLoad'
    }
});
