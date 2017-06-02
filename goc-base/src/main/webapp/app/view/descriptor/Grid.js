
var descriptoresGridColumns = [
    {
        text: getMultiLangLabel(appI18N.descriptores.nombre, mainLanguage),
        dataIndex: 'descriptor',
        flex: 1,
        editor: {
            field: {
                allowBlank: false
            }
        }
    }
];

if (isMultilanguageApplication()) {
    descriptoresGridColumns.push({
        text: getMultiLangLabel(appI18N.descriptores.nombre, alternativeLanguage),
        dataIndex: 'descriptorAlternativo',
        flex: 1,
        editor: {
            field: {
                allowBlank: false
            }
        }
    });
}

descriptoresGridColumns.push(
    {
        text: getMultiLangLabel(appI18N.descriptores.descripcion, mainLanguage),
        dataIndex: 'descripcion',
        flex: 1,
        editor: {
            field: {
                allowBlank: false
            }
        }
    }
);

if (isMultilanguageApplication()) {
    descriptoresGridColumns.push({
        text: getMultiLangLabel(appI18N.descriptores.descripcion, alternativeLanguage),
        dataIndex: 'descripcionAlternativa',
        flex: 1,
        editor: {
            field: {
                allowBlank: false
            }
        }
    });
}

Ext.define('goc.view.descriptor.Grid', {
    extend: 'Ext.ux.uji.grid.Panel',
    alias: 'widget.descriptorGrid',
    requires: [
        'goc.view.descriptor.GridController'
    ],
    controller: 'descriptorGridController',
    reference: 'descriptorGrid',
    multiSelect: false,
    bind: {
        store: '{descriptoresStore}',
        selection: '{selectedDescriptor}'
    },
    name: 'descriptoresGrid',
    title: appI18N ? appI18N.descriptores.titulo : 'Descriptors',
    scrollable: true,
    multiSelect : false,
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
            text: appI18N.common.borrar,
            handler: 'onDelete'
        }
    ],

    columns: descriptoresGridColumns,

    listeners: {
        render: 'onLoad',
        selectionChange: 'descriptorSelected'
    }
});
