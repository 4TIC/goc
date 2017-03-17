
var descriptorGridColumns = [
    {
        text: getMultiLangLabel(appI18N.descriptores.descriptorSingular, mainLanguage),
        dataIndex: 'descriptor',
        flex: 1,
        editor: {
            xtype: 'combobox',
            bind: {
                store: '{descriptoresStore}'
            },
            displayField: 'descriptor',
            valueField: 'id',
            allowBlank: false,
            editable: false,
            listeners: {
                expand: function() {
                    this.getStore().reload();
                },
                change: 'onChangeDescriptor'
            }
        }
    }
];

descriptorGridColumns.push({
    text: getMultiLangLabel(appI18N.claves.claveSingular, mainLanguage),
    dataIndex: 'clave',
    flex: 1,
    editor: {
        xtype: 'combobox',
        bind: {
            store: '{clavesStore}'
        },
        displayField: 'clave',
        valueField: 'id',
        allowBlank: false,
        editable: false,
        queryMode: 'local'
    }
});

Ext.define('goc.view.reunion.GridDescriptoresOrdenDia', {
    extend: 'Ext.ux.uji.grid.Panel',
    alias: 'widget.gridDescriptoresOrdenDia',
    requires: [
        'goc.view.reunion.GridDescriptoresOrdenDiaController'
    ],
    controller: 'gridDescriptoresOrdenDiaController',
    reference: 'gridDescriptoresOrdenDia',
    bind: {
        store: '{descriptoresOrdenDiaStore}',
        selection: '{selectedDescriptorClave}'
    },
    minHeight: '370',
    name: 'gridDescriptoresOrdenDia',
    title: appI18N ? appI18N.descriptores.titulo : 'Descriptors',
    scrollable: true,
    tbar: [
        {
            xtype: 'button',
            name: 'add',
            iconCls: 'fa fa-plus',
            text: appI18N ? appI18N.common.anadir : 'Afegir',
            handler: 'onAdd'
        },
        {
            xtype: 'button',
            name: 'edit',
            iconCls: 'fa fa-edit',
            text: appI18N ? appI18N.common.editar : 'Editar',
            handler: 'onEdit'
        },
        {
            xtype: 'button',
            iconCls: 'fa fa-remove',
            text: appI18N ? appI18N.common.borrar : 'Esborrar',
            handler: 'onDelete'
        }],

    columns: descriptorGridColumns,

    listeners: {
        beforeedit: 'onBeforeEdit'
    }
});
