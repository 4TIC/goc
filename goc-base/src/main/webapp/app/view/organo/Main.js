Ext.define('goc.view.organo.Main',
{
    extend : 'Ext.panel.Panel',
    alias : 'widget.organoMain',
    requires : [ 'goc.view.organo.Grid', 'goc.view.organo.AutorizadoGrid' ],
    title : appI18N.organos.titulo,
    layout : 'fit',

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
            xtype : 'autorizadoGrid',
            flex : 1,
            width : '100%'
        } ]
    } ]

});
