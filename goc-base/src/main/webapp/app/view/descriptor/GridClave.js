
var clavesGridColumns = [
    {
        text: getMultiLangLabel(appI18N.claves.nombre, mainLanguage),
        dataIndex: 'clave',
        flex: 1,
        editor: {
            field: {
                allowBlank: false
            }
        }
    }
];

if (isMultilanguageApplication()) {
    clavesGridColumns.push({
        text: getMultiLangLabel(appI18N.claves.nombre, alternativeLanguage),
        dataIndex: 'claveAlternativa',
        flex: 1,
        editor: {
            field: {
                allowBlank: false
            }
        }
    });
}

Ext.define('goc.view.descriptor.GridClave', {
    extend: 'Ext.ux.uji.grid.Panel',
    alias: 'widget.claveGrid',
    requires: [
        'goc.view.descriptor.GridClaveController'
    ],
    controller: 'gridClaveController',
    reference: 'claveGrid',
    bind: {
        store: '{clavesStore}',
        selection: '{selectedClave}'
    },
    name: 'claveGrid',
    title: appI18N ? appI18N.claves.titulo : 'Claus',
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
            text: appI18N.common.borrar,
            handler: 'onDelete'
        }],

    columns: clavesGridColumns,

    listeners: {
        render: 'onLoad',
        select: 'claveSelected',
        descriptorSelected: 'descriptorSelected'
    }
});
