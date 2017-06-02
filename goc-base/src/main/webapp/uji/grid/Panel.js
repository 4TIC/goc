Ext.define('Ext.ux.uji.grid.Panel',
{
    extend : 'Ext.grid.Panel',
    alias : 'widget.ujigridpanel',

    requires : [ 'Ext.grid.plugin.RowEditing', 'Ext.ux.uji.grid.PanelController' ],
    controller : 'gridPanelController',

    plugins : [
    {
        ptype : 'rowediting',
        clicksToEdit : 2
    } ],

    tbar : [
    {
        xtype : 'button',
        name: 'add',
        iconCls : 'fa fa-plus',
        text : appI18N ? appI18N.common.anadir : 'Afegir',
        handler : 'onAdd'
    },
    {
        xtype : 'button',
        name: 'edit',
        iconCls : 'fa fa-edit',
        text : appI18N ? appI18N.common.editar : 'Editar',
        handler : 'onEdit'
    },
    {
        xtype : 'button',
        name: 'remove',
        iconCls : 'fa fa-remove',
        text : appI18N ? appI18N.common.borrar : 'Esborrar',
        handler : 'onDelete'
    } ],

    bbar : [
    {
        xtype : 'button',
        text : appI18N ? appI18N.common.recargar : 'Recarregar',
        action : 'reload',
        iconCls : 'fa fa-refresh',
        handler : 'onReload'
    } ],

    config :
    {
        allowEdit : true,
        showSearchField : false,
        showAddButton : true,
        showRemoveButton : true,
        showReloadButton : true,
        showTopToolbar : true,
        showBottomToolbar : true,
        handlerRemoveButton : function()
        {
            var ref = this.up('grid');
            var records = ref.getSelectionModel().getSelection();

            if (records.length > 0)
            {
                Ext.Msg.confirm(appI18N ? appI18N.common.borrar : 'Esborrar',
                    appI18N ? appI18N.common.confirmarBorrado : 'Esteu segur/a de voler esborrar el registre ?', function(btn, text)
                {
                    if (btn == 'yes')
                    {
                        var rowEditor = ref.getPlugin();

                        rowEditor.cancelEdit();
                        ref.store.remove(records);
                    }
                });
            }
        }
    },

    listeners :
    {
        render : 'onLoad',
        edit : 'onEditComplete',
        canceledit : 'onCancelEdit'
    },

    initComponent : function()
    {
        var ref = this;
        this.callParent(arguments);

        var bbar = this.getDockedItems('toolbar[dock="bottom"]')[0];

        if (this.showSearchField)
        {
            tbar.insert(0, [
            {
                xtype : 'searchfield',
                emptyText : 'Recerca...',
                store : this.store,
                width : 180
            }, ' ', '-' ]);
        }

        if (!this.showTopToolbar)
        {
            tbar.hide();
        }

        if (!this.showBottomToolbar)
        {
            bbar.hide();
        }

        this.store.getProxy().on('exception', function()
        {
            ref.store.rejectChanges();
        });
    },

    getSelectedId : function()
    {
        var selection = this.getSelectionModel().getSelection();

        if (selection.length > 0)
        {
            return selection[selection.length-1].get("id");
        }
    },

    getSelectedRow : function()
    {
        var selection = this.getSelectionModel().getSelection();

        if (selection.length > 0)
        {
            return selection[selection.length-1];
        }
    },

    setUrl : function(url)
    {
        this.getStore().getProxy().url = url
    },

    setParams : function(params)
    {
        this.getStore().getProxy().extraParams = params;
    },

    reloadData : function()
    {
        this.getStore().load();
    },

    clearStore : function()
    {
        var store = this.getStore();

        store.suspendAutoSync();
        store.removeAll();
        store.clearData();
        store.resumeAutoSync();
    },

    addRecord : function(data)
    {
        this.store.insert(0, Ext.create(this.store.model, data));
        this.getView().select(0);
    }
});