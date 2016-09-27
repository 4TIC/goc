Ext.define('goc.view.tipoOrgano.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.tipoOrganoMain',
    title: 'Tipus d\'Òrgans',
    requires : [ 'goc.view.tipoOrgano.Grid' ],
    layout: 'fit',

    items: [{
        xtype: 'tipoOrganoGrid'
    }]
});
