Ext.define('goc.view.cargo.Main', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.cargoMain',
    title: appI18N.cargos.titulo,
    requires : [ 'goc.view.cargo.Grid' ],
    layout: 'fit',
    
    items: [{
        xtype: 'cargoGrid'
    }]
});
