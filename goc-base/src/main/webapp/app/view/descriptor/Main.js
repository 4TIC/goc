Ext.define('goc.view.descriptor.Main', {
    extend : 'Ext.panel.Panel',

    alias : 'widget.descriptorMainPanel',
    controller : 'descriptorMainController',

    viewModel : {
        type : 'descriptorViewModel'
    },

    requires : [
        'goc.view.descriptor.MainController', 'goc.view.descriptor.ViewModel', 'goc.view.descriptor.Grid',
        'goc.view.descriptor.GridClave', 'goc.view.descriptor.GridTipoOrgano'
    ],

    title : appI18N.descriptores.titulo,
    autoHeight : true,
    layout : {
        type : 'vbox',
        align : 'stretch',
        pack : 'start'
    },
    items : [
        {
            xtype : 'descriptorGrid',
            flex : 1
        },
        {
            xtype : 'tabpanel',
            flex : 1,
            activeTab : 0,
            disabled: true,
            split: true,
            items : [
                {
                    xtype : 'claveGrid'
                },
                {
                    xtype : 'descriptorTipoOrganoGrid'
                }
            ]
        },

    ],
    listeners : {
        render : 'onLoad'
    }
});
