Ext.define('goc.view.organo.AutorizadoGrid',
{
    extend : 'Ext.ux.uji.grid.Panel',
    alias : 'widget.autorizadoGrid',
    plugins : [],
    disabled : true,
    requires : [ 'goc.view.organo.AutorizadoGridController' ],
    controller : 'autorizadoGridController',
    name : 'autorizadoGrid',
    title : appI18N.organos.autorizados,
    multiSelect : true,
    scrollable: true,
    bind: {
        store: '{organoAutorizadosStore}'
    },
    columns : [
    {
        text : 'Id',
        width : 80,
        dataIndex : 'id',
        hidden : true
    },
    {
        text : 'Nom',
        dataIndex : 'personaNombre',
        flex : 1
    } ],
    viewConfig :
    {
        emptyText : appI18N.organos.sinAutorizadosDisponibles
    },
    listeners :
    {
        organoSelected : 'organoSelected',
        onDelete : 'onDelete'
    },

    tbar : [
    {
        xtype : 'button',
        iconCls : 'fa fa-plus',
        text : appI18N.common.anadir,
        handler : 'onAdd'
    },
    {
        xtype : 'button',
        iconCls : 'fa fa-remove',
        text : appI18N.common.borrar,
        handler : 'onDelete'
    } ]
});
