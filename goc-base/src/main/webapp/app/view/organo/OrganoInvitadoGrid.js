Ext.define('goc.view.organo.OrganoInvitadoGrid',
{
    extend : 'Ext.ux.uji.grid.Panel',
    alias : 'widget.organoInvitadoGrid',
    plugins : [],
    requires : ['goc.view.organo.OrganoInvitadoGridController'],
    controller : 'organoInvitadoGridController',
    name : 'organoInvitadoGrid',
    title : appI18N.organos.invitados,
    multiSelect : true,
    scrollable : true,
    bind : {
        store : '{organoInvitadosStore}'
    },
    columns : [
        {
            text : 'Id',
            width : 80,
            dataIndex : 'id',
            hidden : true
        },
        {
            text : appI18N.invitados.nombre,
            dataIndex : 'personaNombre',
            flex : 1
        }
    ],
    viewConfig : {
        emptyText : appI18N.organos.sinInvitadosDisponibles
    },
    listeners : {
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
        }
    ]
});
