var formItems = [
    {
        name : 'id',
        xtype : 'hidden',
        bind : '{puntoOrdenDia.id}'
    },
    {
        allowBlank : false,
        fieldLabel : getMultiLangLabel(appI18N.reuniones.titulo, mainLanguage),
        name : 'titulo',
        emptyText : getMultiLangLabel(appI18N.reuniones.titulo, mainLanguage),
        bind : {
            value : '{puntoOrdenDia.titulo}',
            disabled : '{reunionCompletada}'
        }
    }
];

if (isMultilanguageApplication())
{
    formItems.push({
        allowBlank : false,
        fieldLabel : getMultiLangLabel(appI18N.reuniones.titulo, alternativeLanguage),
        name : 'tituloAlternativo',
        emptyText : getMultiLangLabel(appI18N.reuniones.titulo, alternativeLanguage),
        bind : {
            value : '{puntoOrdenDia.tituloAlternativo}',
            disabled : '{reunionCompletada}'
        }
    });
}

formItems.push({
    xtype : 'container',
    layout : 'hbox',
    items : [
        {
            xtype : 'tbfill',
            flex : 1
        },
        {
            boxLabel : appI18N.reuniones.publicarAcuerdos,
            name : 'publico',
            bind : {
                disabled : '{reunionCompletada}',
                value : '{puntoOrdenDia.publico}'
            },
            xtype : 'checkbox',
            inputValue : '1'
        }
    ]
});

formItems.push({
    xtype : 'textareafield',
    name : 'descripcion',
    fieldLabel : getMultiLangLabel(appI18N.reuniones.descripcion, mainLanguage),
    labelAlign : 'top',
    flex : 1,
    emptyText : getMultiLangLabel(appI18N.reuniones.descripcion, mainLanguage),
    bind : {
        value : '{puntoOrdenDia.descripcion}',
        disabled : '{reunionCompletada}'
    }
});

if (isMultilanguageApplication())
{
    formItems.push({
        xtype : 'textareafield',
        name : 'descripcionAlternativa',
        fieldLabel : getMultiLangLabel(appI18N.reuniones.descripcion, alternativeLanguage),
        labelAlign : 'top',
        flex : 1,
        emptyText : getMultiLangLabel(appI18N.reuniones.descripcion, alternativeLanguage),
        bind : {
            value : '{puntoOrdenDia.descripcionAlternativa}',
            disabled : '{reunionCompletada}'
        }
    });
}

formItems.push({
    xtype : 'textareafield',
    name : 'deliberaciones',
    fieldLabel : getMultiLangLabel(appI18N.reuniones.deliberaciones, mainLanguage),
    labelAlign : 'top',
    flex : 1,
    emptyText : getMultiLangLabel(appI18N.reuniones.deliberaciones, mainLanguage),
    bind : {
        value : '{puntoOrdenDia.deliberaciones}',
        disabled : '{reunionCompletada}'
    }
});

if (isMultilanguageApplication())
{
    formItems.push({
        xtype : 'textareafield',
        name : 'deliberacionesAlternativas',
        fieldLabel : getMultiLangLabel(appI18N.reuniones.deliberaciones, alternativeLanguage),
        labelAlign : 'top',
        flex : 1,
        emptyText : getMultiLangLabel(appI18N.reuniones.deliberaciones, alternativeLanguage),
        bind : {
            value : '{puntoOrdenDia.deliberacionesAlternativas}',
            disabled : '{reunionCompletada}'
        }
    });
}

formItems.push({
    xtype : 'textareafield',
    name : 'acuerdos',
    fieldLabel : getMultiLangLabel(appI18N.reuniones.acuerdos, mainLanguage),
    labelAlign : 'top',
    flex : 1,
    emptyText : getMultiLangLabel(appI18N.reuniones.acuerdos, mainLanguage),
    bind : {
        value : '{puntoOrdenDia.acuerdos}',
        disabled : '{reunionCompletada}'
    }
});

if (isMultilanguageApplication())
{
    formItems.push({
        xtype : 'textareafield',
        name : 'acuerdosAlternativos',
        fieldLabel : getMultiLangLabel(appI18N.reuniones.acuerdos, alternativeLanguage),
        labelAlign : 'top',
        flex : 1,
        emptyText : getMultiLangLabel(appI18N.reuniones.acuerdos, alternativeLanguage),
        bind : {
            value : '{puntoOrdenDia.acuerdosAlternativos}',
            disabled : '{reunionCompletada}'
        }
    });
}

Ext.define('goc.view.reunion.FormOrdenDia', {
    extend : 'Ext.window.Window',
    xtype : 'formOrdenDia',

    width : 640,
    maxHeight : 1000,
    modal : true,
    bodyPadding : 10,
    autoScroll : true,

    layout : {
        type : 'vbox',
        align : 'stretch'
    },

    requires : [
        'goc.view.reunion.FormOrdenDiaController'
    ],
    controller : 'formOrdenDiaController',

    bbar : {
        defaultButtonUI : 'default',
        items : [
            '->', {
                xtype : 'button',
                bind : {
                    disabled : '{reunionCompletada}'
                },
                text : appI18N.reuniones.guardar,
                handler : 'onSaveRecord'
            }, {
                xtype : 'panel',
                html : '<a style="text-decoration: none; color: #222;" href="#">' + appI18N.common.cancelar + '</a>',
                listeners : {
                    render : function(component)
                    {
                        component.getEl().on('click', 'onCancel');
                    }
                }
            }
        ]
    },

    bind : {
        title : '{title}'
    },

    items : [
        {
            xtype : 'form',
            name : 'puntoOrdenDia',
            border : 0,
            layout : 'anchor',
            items : [
                {
                    xtype : 'fieldset',
                    title : appI18N.reuniones.informacion,
                    defaultType : 'textfield',
                    defaults : {
                        anchor : '100%'
                    },

                    items : formItems
                }
            ]
        }
    ],

    listeners : {
        afterLayout : 'afterRenderFormOrdenDia',
        close : 'onClose'
    }
});
