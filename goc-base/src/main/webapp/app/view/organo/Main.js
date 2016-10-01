Ext.define('goc.view.organo.Main',
    {
        extend: 'Ext.panel.Panel',
        alias: 'widget.organoMainPanel',
        requires: ['goc.view.organo.MainController', 'goc.view.organo.Grid', 'goc.view.organo.ViewModel', 'goc.view.organo.AutorizadoGrid', 'goc.view.organo.ComboTipoOrgano'],
        title: appI18N.organos.titulo,
        tbar: [
            '->',
            {
                padding: 10,
                xtype: 'comboTipoOrgano'
            },
        ],
        layout: 'fit',
        border: 0,
        controller: 'organoMainController',

        viewModel: {
            type: 'organoViewModel'
        },

        items: [
            {
                xtype: 'panel',
                layout: 'vbox',
                items: [
                    {
                        xtype: 'organoGrid',
                        flex: 1,
                        width: '100%'
                    },
                    {
                        xtype: 'autorizadoGrid',
                        flex: 1,
                        width: '100%'
                    }]
            }],
        listeners: {
            'tipoOrganoSelected': 'onTipoOrganoSelected'
        }

    });
