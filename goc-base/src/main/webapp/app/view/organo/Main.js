Ext.define('goc.view.organo.Main',
{
    extend : 'Ext.panel.Panel',
    alias : 'widget.organoMainPanel',
    title : appI18N.organos.titulo,

    requires : [
        'goc.view.organo.MainController',
        'goc.view.organo.Grid',
        'goc.view.organo.ViewModel',
        'goc.view.organo.AutorizadoGrid',
        'goc.view.organo.OrganoInvitadoGrid',
        'goc.view.organo.ComboEstadoOrgano',
        'goc.view.organo.ComboTipoOrgano'
    ],

    tbar : [
        '->',
        {
            xtype : 'comboEstadoOrgano'
        },
        {
            padding : 10,
            xtype : 'comboTipoOrgano'
        }
    ],

    layout : 'fit',
    border : 0,
    controller : 'organoMainController',

    viewModel : {
        type : 'organoViewModel'
    },

    items : [
        {
            xtype : 'panel',
            layout : 'vbox',
            items : [
                {
                    xtype : 'organoGrid',
                    flex : 1,
                    width : '100%'
                },
                {
                    xtype : 'tabpanel',
                    flex : 1,
                    activeTab : 0,
                    disabled: true,
                    width : '100%',
                    items : [
                        {
                            xtype : 'autorizadoGrid',
                            flex : 1,
                            width : '100%'
                        },
                        {
                            xtype : 'organoInvitadoGrid',
                            flex : 1,
                            width : '100%'
                        }
                    ]
                }
            ]
        }
    ],

    listeners : {
        'tipoOrganoSelected' : 'onTipoOrganoSelected',
        'filtrarOrganos' : 'onFiltrarOrganos'
    }
}
);
