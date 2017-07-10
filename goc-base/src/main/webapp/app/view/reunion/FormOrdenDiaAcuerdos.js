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
    bind : {
        disabled : '{reunionCompletada}'
    },
    align : 'right',
    width : 50,
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
                var documentoId = rec.get('id');
                grid.up('formOrdenDiaAcuerdos').fireEvent('borraPuntoOrdenDiaDocumento', documentoId);
            }
        },
        {
            iconCls : 'x-fa fa-download',
            tooltip : appI18N.reuniones.descargar,
            handler : function(grid, index)
            {
                var rec = grid.getStore().getAt(index);
                var documentoId = rec.get('id');
                grid.up('formOrdenDiaAcuerdos').fireEvent('descargaPuntoOrdenDiaDocumento', documentoId);
            }
        }
    ]
});

var formItems = [
    {
        xtype : 'textfield',
        fieldLabel : getMultiLangLabel(appI18N.reuniones.descripcion, mainLanguage),
        allowBlank : false,
        emptyText : appI18N.reuniones.descripcion,
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
        emptyText : appI18N.reuniones.descripcion,
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
        change : 'onFileChangeOrdenDia'
    }
});

formItems.push({
    xtype : 'displayfield',
    width : '100%',
    name : 'nombreDocumento',
    reference : 'nombreDocumento'
});

Ext.define('goc.view.reunion.FormOrdenDiaAcuerdos',
{
    extend : 'Ext.window.Window',
    xtype : 'formOrdenDiaAcuerdos',
    autoScroll : true,
    bind : {
        title : '{title}'
    },
    width : 640,
    minHeight : 540,
    modal : true,
    bodyPadding : 10,
    layout : {
        type : 'vbox',
        align : 'stretch'
    },

    requires : ['goc.view.reunion.FormOrdenDiaAcuerdosController'],
    controller : 'formOrdenDiaAcuerdosController',

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
            scrollable : true,
            flex : 1,
            margin : '5 0 5 0',
            viewConfig : {
                emptyText : appI18N.reuniones.acuerdosAdjuntosVacia
            },
            bind : {
                store : '{store}'
            },
            hideHeaders : true,
            columns : gridColumns,
            autoScroll : true,
            minHeight : 150
        },
        {
            xtype : 'form',
            autoScroll : true,
            bind : {
                disabled : '{reunionCompletada}'
            },
            frame : true,
            title : appI18N.reuniones.subirNuevoAcuerdo,
            layout : 'anchor',
            border : false,
            padding : 5,
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
                    text : appI18N.reuniones.subirAcuerdo,
                    handler : 'subirPuntoOrdenDiaDocumento'

                }
            ]
        }
    ],
    listeners : {
        render : 'onLoad',
        subirPuntoOrdenDiaDocumento : 'subirPuntoOrdenDiaDocumento',
        descargaPuntoOrdenDiaDocumento : 'descargaPuntoOrdenDiaDocumento',
        borraPuntoOrdenDiaDocumento : 'borraPuntoOrdenDiaDocumento'
    }
});
