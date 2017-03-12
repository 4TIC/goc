
var gridColumns = [
    {
        text: getMultiLangLabel(appI18N.reuniones.descripcion, mainLanguage),
        dataIndex: 'descripcion',
        flex: 1
    }
];

if (isMultilanguageApplication()) {
    gridColumns.push({
        text: getMultiLangLabel(appI18N.reuniones.descripcion, alternativeLanguage),
        dataIndex: 'descripcionAlternativa',
        flex: 1
    });
}

gridColumns.push({
    xtype: 'actioncolumn',
    bind: {
        disabled: '{reunionCompletada}'
    },
    align: 'right',
    width: 50,
    items: [
        {
            iconCls: 'x-fa fa-remove',
            tooltip: appI18N.common.borrar,
            isDisabled: function(grid) {
                return this.disabled;
            },
            handler: function (grid, index) {
                var rec = grid.getStore().getAt(index);
                var documentoId = rec.get('id');
                grid.up('formOrdenDiaDocumentacion').fireEvent('borraPuntoOrdenDiaDocumento', documentoId);
            }
        },
        {
            iconCls: 'x-fa fa-download',
            tooltip: appI18N.reuniones.descargar,
            handler: function (grid, index) {
                var rec = grid.getStore().getAt(index);
                var documentoId = rec.get('id');
                grid.up('formOrdenDiaDocumentacion').fireEvent('descargaPuntoOrdenDiaDocumento', documentoId);
            }
        }
    ]
});

var formItems = [
    {
        xtype: 'textfield',
        fieldLabel: getMultiLangLabel(appI18N.reuniones.descripcion, mainLanguage),
        allowBlank: false,
        emptyText: appI18N.reuniones.descripcion,
        width: '100%',
        flex: 1,
        name: 'descripcion'
    }
];

if (isMultilanguageApplication()) {
    formItems.push({
        xtype: 'textfield',
        fieldLabel: getMultiLangLabel(appI18N.reuniones.descripcion, alternativeLanguage),
        allowBlank: false,
        emptyText: appI18N.reuniones.descripcion,
        width: '100%',
        flex: 1,
        name: 'descripcionAlternativa'
    });
}

formItems.push({
    xtype: 'filefield',
    buttonOnly: true,
    width: 40,
    name: 'documento',
    buttonConfig: {
        text: '',
        iconCls: 'fa fa-file'
    },
    listeners: {
        change: 'onFileChangeOrdenDia'
    }
});

formItems.push({
    xtype: 'displayfield',
    width: 200,
    name: 'nombreDocumento',
    reference: 'nombreDocumento'
});


Ext.define('goc.view.reunion.FormOrdenDiaDocumentacion',
    {
        extend: 'Ext.window.Window',
        xtype: 'formOrdenDiaDocumentacion',
        bind: {
            title: '{title}'
        },
        width: 640,
        height: '90%',
        modal: true,
        bodyPadding: 10,
        layout: {
            type: 'vbox',
            align: 'stretch'
        },

        requires: ['goc.view.reunion.FormOrdenDiaDocumentacionController'],
        controller: 'formOrdenDiaDocumentacionController',

        bbar: ['->',
            {
                xtype: 'button',
                text: appI18N.reuniones.cerrar,
                handler: 'onClose'
            }
        ],

        items: [
            {
                xtype: 'grid',
                scrollable: true,
                flex: 1,
                margin: '5 0 5 0',
                viewConfig: {
                    emptyText: appI18N.reuniones.documentacionAdjuntaVacia
                },
                bind: {
                    store: '{store}'
                },
                hideHeaders: true,
                columns: gridColumns
            },
            {
                xtype: 'form',
                bind: {
                    disabled: '{reunionCompletada}'
                },
                frame: true,
                title: appI18N.reuniones.subirNuevoDocumento,
                layout: 'anchor',
                border: false,
                padding: 5,
                name: 'subirDocumento',
                items: [
                    {
                        xtype: 'fieldcontainer',
                        anchor: '100%',
                        layout: 'vbox',
                        items: formItems
                    }
                ],
                buttons: [
                    {
                        text: appI18N.reuniones.subirDocumento,
                        handler: 'subirPuntoOrdenDiaDocumento'

                    }
                ]
            }
        ],
        listeners: {
            render: 'onLoad',
            subirPuntoOrdenDiaDocumento: 'subirPuntoOrdenDiaDocumento',
            descargaPuntoOrdenDiaDocumento: 'descargaPuntoOrdenDiaDocumento',
            borraPuntoOrdenDiaDocumento: 'borraPuntoOrdenDiaDocumento'
        }
    });
