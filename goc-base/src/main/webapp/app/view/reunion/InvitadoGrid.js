Ext.define('goc.view.reunion.InvitadoGrid',
{
    extend : 'Ext.ux.uji.grid.Panel',
    alias : 'widget.invitadoGrid',
    requires : ['goc.view.reunion.InvitadoGridController'],
    controller : 'invitadoGridController',
    name : 'invitadoGrid',
    title : appI18N.reuniones.invitados,
    multiSelect : false,
    scrollable : true,
    showTopToolbar : false,
    showBottomToolbar : false,
    allowEdit: false,
    bind : {
        store : '{reunionInvitadosStore}'
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
        },
        {
            xtype : 'actioncolumn',
            align : 'right',
            width : 25,
            bind : {
                disabled : '{reunion.completada}'
            },
            items : [
                {
                    iconCls : 'x-fa fa-remove',
                    tooltip : appI18N.common.borrar,
                    isDisabled : function(grid)
                    {
                        return this.disabled;
                    },
                    handler : function(grid, index)
                    {
                        var rec = grid.getStore().getAt(index);
                        this.up('grid').fireEvent('borrarInvitadoReunion', rec);
                    }
                }
            ]
        }
    ],
    viewConfig : {
        emptyText : appI18N.reuniones.sinInvitadosSeleccionados
    },

    addTools : function()
    {
        var me = this;

        me.addTool({
            type : 'plus',
            tooltip : appI18N.reuniones.buscarInvitados,
            callback : 'onShowSearch',
            scope : me
        });
    },

    onShowSearch : function()
    {
        this.fireEvent('addInvitado');
    },

    listeners : {
        addInvitado : 'addInvitado',
        borrarInvitadoReunion : 'onBorrarInvitadoReunion'
    }
});
