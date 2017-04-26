var organoGridColumns = [
    {
        text : getMultiLangLabel(appI18N.organos.nombre, mainLanguage),
        dataIndex : 'nombre',
        flex : 1,
        editor : {
            field : {
                allowBlank : false
            }
        }
    }
];

if (isMultilanguageApplication())
{
    organoGridColumns.push({
        text : getMultiLangLabel(appI18N.organos.nombre, alternativeLanguage),
        dataIndex : 'nombreAlternativo',
        flex : 1,
        editor : {
            field : {
                allowBlank : false
            }
        }
    });
}

organoGridColumns.push({
    text : appI18N.organos.tipoOrgano,
    dataIndex : 'tipoOrganoId',
    flex : 1,
    renderer : function(id, meta, rec)
    {
        var store = this.up('organoMainPanel').getViewModel().getStore('tipoOrganosStore');
        var tipoOrganoRecord = store.getById(id);
        return tipoOrganoRecord ? tipoOrganoRecord.get('nombre') : '';
    },
    editor : {
        xtype : 'combobox',
        bind : {
            store : '{tipoOrganosStore}'
        },
        displayField : 'nombre',
        valueField : 'id',
        allowBlank : false,
        editable : false,
        listeners : {
            expand : function()
            {
                this.getStore().reload();
            }
        }
    }
});

organoGridColumns.push({
    text : appI18N.organos.externo,
    width : 80,
    renderer : function(id, meta, rec)
    {
        return rec.get('externo') === 'true' ? 'Sí' : 'No';
    }
});

organoGridColumns.push({
    text : appI18N.organos.activo,
    width : 80,
    renderer : function(id, meta, rec)
    {
        return rec.get('inactivo') ? 'No' : 'Sí';
    }
});

Ext.define('goc.view.organo.Grid', {
    extend : 'Ext.ux.uji.grid.Panel',
    alias : 'widget.organoGrid',
    requires : [
        'goc.view.organo.GridController'
    ],
    controller : 'organoGridController',
    reference : 'organoGrid',
    bind : {
        store : '{organosStore}',
        selection : '{selectedOrgano}'
    },
    name : 'organosGrid',
    title : appI18N ? appI18N.organos.titulo : 'Òrgans',
    scrollable : true,

    tbar : [
        {
            xtype : 'button',
            name : 'add',
            iconCls : 'fa fa-plus',
            text : appI18N ? appI18N.common.anadir : 'Afegir',
            handler : 'onAdd'
        },
        {
            xtype : 'button',
            name : 'edit',
            iconCls : 'fa fa-edit',
            text : appI18N ? appI18N.common.editar : 'Editar',
            handler : 'onEdit'
        },
        {
            xtype : 'button',
            iconCls : 'fa fa-remove',
            text : 'Inhabilita',
            handler : 'onToggleEstado',
            hidden : true,
            bind : {
                hidden : '{ocultaBotonInhabilita}'
            }
        },
        {
            xtype : 'button',
            iconCls : 'fa fa-check-circle',
            text : 'Habilita',
            handler : 'onToggleEstado',
            hidden : true,
            bind : {
                hidden : '{ocultaBotonHabilita}'
            }
        }
    ],

    columns : organoGridColumns,

    listeners : {
        render : 'onLoad',
        beforeedit : 'decideRowIsEditable',
        selectionChange : 'organoSelected'
    }
});
