Ext.define('goc.view.historicoReunion.Main',
    {
        extend: 'Ext.panel.Panel',
        alias: 'widget.historicoReunionMain',
        name: 'reuniones',
        viewModel: {
            type: 'historicoReunionViewModel'
        },
        requires: [
            'goc.view.historicoReunion.ViewModel',
            'goc.view.historicoReunion.HistoricoReunionGrid',
            'goc.view.historicoReunion.HistoricoReunionGridController',
            'goc.view.common.ComboOrgano',
            'goc.view.common.ComboTipoOrgano'],
        title: appI18N.reuniones.tituloPrincipal,
        layout: 'fit',

        items: [
            {
                xtype: 'panel',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [
                    {
                        xtype: 'historicoReunionGrid',
                        flex: 1,
                        width: '100%'
                    }]
            }]
    });
