var gridColumns = [
    {
        text : getMultiLangLabel(appI18N.reuniones.descripcion, mainLanguage),
        dataIndex : 'descripcion',
        flex : 1
    }
];

if (isMultilanguageApplication())
{
    gridColumns.push({
        text : getMultiLangLabel(appI18N.reuniones.descripcion, alternativeLanguage),
        dataIndex : 'descripcionAlternativa',
        flex : 1
    });
}

gridColumns.push({
    xtype : 'actioncolumn',
    name : 'delete',
    align : 'right',
    width : 30,
    bind : {
        disabled : '{completada}'
    },
    items : [
        {
            iconCls : 'x-fa fa-remove',
            name : 'delete',
            isDisabled : function(grid)
            {
                return this.disabled;
            },
            tooltip : appI18N.common.borrar,
            handler : function(grid, index)
            {
                var rec = grid.getStore().getAt(index);
                var documentoId = rec.get('id');
                grid.up('formDocumentacion').fireEvent('borraDocumento', documentoId);
            }
        }
    ]
});

gridColumns.push({
    xtype : 'actioncolumn',
    align : 'right',
    width : 30,
    items : [
        {
            iconCls : 'x-fa fa-download',
            tooltip : appI18N.reuniones.descargar,
            handler : function(grid, index)
            {
                var rec = grid.getStore().getAt(index);
                var documentoId = rec.get('id');
                grid.up('formDocumentacion').fireEvent('descargaDocumento', documentoId);
            }
        }
    ]
});

var formItems = [
    {
        xtype : 'textfield',
        fieldLabel : getMultiLangLabel(appI18N.reuniones.descripcion, mainLanguage),
        allowBlank : false,
        emptyText : getMultiLangLabel(appI18N.reuniones.descripcion, mainLanguage),
        width : '100%',
        flex : 1,
        name : 'descripcion'
    }
];

if (isMultilanguageApplication())
{
    formItems.push({
        xtype : 'textfield',
        fieldLabel : getMultiLangLabel(appI18N.reuniones.descripcion, alternativeLanguage),
        allowBlank : false,
        emptyText : getMultiLangLabel(appI18N.reuniones.descripcion, alternativeLanguage),
        width : '100%',
        flex : 1,
        name : 'descripcionAlternativa'
    });
}

formItems.push({
    xtype : 'filefield',
    buttonOnly : true,
    width : 40,
    name : 'documento',
    buttonConfig : {
        text : '',
        iconCls : 'fa fa-file'
    },
    listeners : {
        change : 'onFileChange'
    }
});

formItems.push({
    xtype : 'displayfield',
    width : '100%',
    name : 'nombreDocumento',
    reference : 'nombreDocumento'
});

Ext.define('goc.view.reunion.FormDocumentacion',
{
    extend : 'Ext.window.Window',
    xtype : 'formDocumentacion',
    bind : {
        title : '{title}'
    },
    width : 640,
    minHeight : 540,
    autoScroll : true,
    modal : true,
    bodyPadding : 10,
    layout : {
        type : 'vbox',
        align : 'stretch'
    },

    requires : ['goc.view.reunion.FormDocumentacionController'],
    controller : 'formDocumentacionController',

    bbar : [
        '->',
        {
            xtype : 'panel',
            html : '<a style="text-decoration: none; color: #222;" href="#">' + appI18N.reuniones.cerrar + '</a>',
            listeners : {
                render : function(component)
                {
                    component.getEl().on('click', 'onClose');
                }
            }
        }
    ],

    items : [
        {
            xtype : 'grid',
            flex : 1,
            scrollable : true,
            minHeight : 150,
            margin : '5 0 5 0',
            autoScroll : true,
            viewConfig : {
                emptyText : appI18N.reuniones.documentacionAdjuntaVacia
            },
            bind : {
                store : '{store}'
            },
            hideHeaders : true,
            columns : gridColumns
        },
        {
            xtype : 'form',
            bind : {
                disabled : '{completada}'
            },
            frame : true,
            title : appI18N.reuniones.subirNuevoDocumento,
            layout : 'anchor',
            border : false,
            padding : 5,
            autoScroll : true,
            name : 'subirDocumento',
            items : [
                {
                    xtype : 'fieldcontainer',
                    anchor : '100%',
                    layout : 'vbox',
                    items : formItems
                }
            ],
            buttons : [
                {
                    text : appI18N.reuniones.subirDocumento,
                    handler : 'subirDocumento'

                }
            ]
        }
    ],
    listeners : {
        render : 'onLoad',
        subirDocumento : 'subirDocumento',
        descargaDocumento : 'descargaDocumento',
        borraDocumento : 'borraDocumento'
    }
});
