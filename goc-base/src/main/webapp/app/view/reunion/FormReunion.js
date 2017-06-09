Ext.define("Ext.locale.es.view.MultiSelectorSearch", {
    override : 'Ext.view.MultiSelectorSearch',
    searchText : appI18N.reuniones.multiSearchBusqueda
});

Ext.define("Ext.locale.es.view.MultiSelector", {
    override : 'Ext.view.MultiSelector',
    emptyText : appI18N.reuniones.multiSearchNadaSeleccionado,
    removeRowTip : appI18N.reuniones.multiSearchEliminarOrgano,
    addToolText : appI18N.reuniones.multiSearchBuscarOrgano
});

var formReunionItems = [
    {
        name : 'id',
        xtype : 'hidden',
        bind : '{reunion.id}'
    },
    {
        allowBlank : false,
        fieldLabel : getMultiLangLabel(appI18N.reuniones.asunto, mainLanguage),
        name : 'asunto',
        emptyText : appI18N.reuniones.asunto,
        bind : {
            value : '{reunion.asunto}',
            disabled : '{reunion.completada}'
        }
    }
];

if (isMultilanguageApplication())
{
    formReunionItems.push({
        allowBlank : false,
        fieldLabel : getMultiLangLabel(appI18N.reuniones.asunto, alternativeLanguage),
        name : 'asuntoAlternativo',
        emptyText : appI18N.reuniones.asunto,
        bind : {
            value : '{reunion.asuntoAlternativo}',
            disabled : '{reunion.completada}'
        }
    });
}

formReunionItems.push({
    xtype : 'fieldcontainer',
    fieldLabel : appI18N.reuniones.fecha + "&nbsp;<span style='color:red'>*</span>",
    layout : 'hbox',
    items : [
        {
            allowBlank : false,
            xtype : 'datefield',
            name : 'fecha',
            emptyText : appI18N.reuniones.fecha,
            format : 'd/m/Y',
            altFormats : 'd/m/Y H:i:s',
            bind : {
                value : '{reunion.fecha}',
                disabled : '{reunion.completada}'
            },
            flex : 1,
            padding : '0 10 0 0',
            validator : function(date)
            {
                var now = new Date();
                now.setDate(now.getDate() - 1);
                return Ext.Date.parse(date, 'd/m/Y') > now;
            }
        },
        {
            allowBlank : false,
            xtype : 'timefield',
            minValue : '07:00',
            maxValue : '23:00',
            name : 'hora',
            emptyText : appI18N.reuniones.horaInicio,
            format : 'H:i',
            altFormats : 'd/m/Y H:i:s',
            bind : {
                value : '{reunion.hora}',
                disabled : '{reunion.completada}'
            },
            padding : '0 10 0 0'
        },
        {
            name : 'duracion',
            xtype : 'combo',
            width : 120,
            emptyText : appI18N.reuniones.duracion,
            store : Ext.create('Ext.data.Store',
            {
                fields : ['id', 'value'],
                data : [
                    {
                        value : 30,
                        texto : '0,5 ' + appI18N.reuniones.horas
                    },
                    {
                        value : 60,
                        texto : '1 ' + appI18N.reuniones.hora
                    },
                    {
                        value : 90,
                        texto : '1,5 ' + appI18N.reuniones.horas
                    },
                    {
                        value : 120,
                        texto : '2 ' + appI18N.reuniones.horas
                    },
                    {
                        value : 150,
                        texto : '2,5 ' + appI18N.reuniones.horas
                    },
                    {
                        value : 180,
                        texto : '3 ' + appI18N.reuniones.horas
                    }
                ]
            }),
            triggerAction : 'all',
            queryMode : 'local',
            displayField : 'texto',
            valueField : 'value',
            bind : {
                value : '{reunion.duracion}',
                disabled : '{reunion.completada}'
            }
        }
    ]
});

formReunionItems.push({
    xtype : 'fieldcontainer',
    fieldLabel : appI18N.reuniones.fechaSegundaConvocatoria,
    layout : 'hbox',
    items : [
        {
            xtype : 'timefield',
            minValue : '07:00',
            maxValue : '23:00',
            name : 'horaSegundaConvocatoria',
            emptyText : appI18N.reuniones.horaInicioSegundaConvocatoria,
            format : 'H:i',
            altFormats : 'd/m/Y H:i:s',
            bind : {
                value : '{reunion.horaSegundaConvocatoria}',
                disabled : '{reunion.completada}'
            },
            padding : '0 10 0 0'
        }
    ]
});

formReunionItems.push({
    xtype : 'container',
    fieldLabel : 'Número de sessió',
    layout : 'hbox',
    items : [
        {
            fieldLabel : appI18N.reuniones.numeroSesion,
            name : 'numeroSesion',
            xtype : 'numberfield',
            bind : {
                value : '{reunion.numeroSesion}',
                disabled : '{reunion.completada}'
            },
            width : 180
        },
        {
            xtype : 'tbfill',
            flex : 1
        },
        {
            boxLabel : appI18N.reuniones.admiteSuplencia,
            name : 'admiteSuplencia',
            bind : {
                value : '{reunion.admiteSuplencia}',
                disabled : '{reunion.completada}'
            },
            xtype : 'checkbox',
            checked : true,
            inputValue : '1'
        },
        {
            xtype : 'tbfill',
            flex : 1
        },
        {
            boxLabel : appI18N.reuniones.admiteComentarios,
            name : 'admiteComentarios',
            bind : {
                value : '{reunion.admiteComentarios}',
                disabled : '{reunion.completada}'
            },
            xtype : 'checkbox',
            checked : true,
            inputValue : '1'
        }
    ]
});

formReunionItems.push({
    xtype : 'fieldcontainer',
    fieldLabel : appI18N.reuniones.tipo,
    layout : 'hbox',
    items : [
        {
            boxLabel : appI18N.reuniones.reunionPublica,
            name : 'publica',
            bind : {
                value : '{reunion.publica}',
                disabled : '{reunion.completada}'
            },
            xtype : 'checkbox',
            inputValue : '1'
        },
        {
            boxLabel : appI18N.reuniones.reunionTelematica,
            name : 'telematica',
            padding : '0 0 0 10',
            bind : {
                value : '{reunion.telematica}',
                disabled : '{reunion.completada}'
            },
            xtype : 'checkbox',
            inputValue : '1',
            handler : function(grid, value)
            {
                grid.up('formReunion').down('textareafield[name=telematicaDescripcion]').setVisible(value);
                grid.up('formReunion').down('textareafield[name=telematicaDescripcionAlternativa]').setVisible(value);
            }
        }
    ]
});

formReunionItems.push({
    xtype : 'textareafield',
    padding : '0 0 0 10',
    name : 'telematicaDescripcion',
    columns : 40,
    labelAlign : 'top',
    flex : 1,
    hidden : true,
    emptyText : getMultiLangLabel(appI18N.reuniones.descripcionTelematica, mainLanguage),
    bind : {
        value : '{reunion.telematicaDescripcion}',
        disabled : '{reunion.completada}'
    }
});

if (isMultilanguageApplication())
{
    formReunionItems.push({
        xtype : 'textareafield',
        padding : '0 0 0 10',
        name : 'telematicaDescripcionAlternativa',
        columns : 40,
        labelAlign : 'top',
        flex : 1,
        hidden : true,
        emptyText : getMultiLangLabel(appI18N.reuniones.descripcionTelematica, alternativeLanguage),
        bind : {
            value : '{reunion.telematicaDescripcionAlternativa}',
            disabled : '{reunion.completada}'
        }
    });
}

formReunionItems.push({
    fieldLabel : getMultiLangLabel(appI18N.reuniones.lugar, mainLanguage),
    name : 'ubicacion',
    emptyText : appI18N.reuniones.ubicacionReunion,
    bind : {
        value : '{reunion.ubicacion}',
        disabled : '{reunion.completada}'
    }
});

if (isMultilanguageApplication())
{
    formReunionItems.push({
        fieldLabel : getMultiLangLabel(appI18N.reuniones.lugar, alternativeLanguage),
        name : 'ubicacionAlternativa',
        emptyText : appI18N.reuniones.ubicacionReunion,
        bind : {
            value : '{reunion.ubicacionAlternativa}',
            disabled : '{reunion.completada}'
        }
    });
}

formReunionItems.push({
    fieldLabel : appI18N.reuniones.urlGrabacionShort,
    name : 'urlGrabacion',
    emptyText : appI18N.reuniones.urlGrabacion,
    vtype : 'url',
    bind : {
        value : '{reunion.urlGrabacion}',
        disabled : '{reunion.completada}'
    }
});

formReunionItems.push({
    xtype : 'textareafield',
    name : 'descripcion',
    fieldLabel : getMultiLangLabel(appI18N.reuniones.descripcion, mainLanguage),
    labelAlign : 'top',
    flex : 1,
    emptyText : appI18N.reuniones.descripcion,
    bind : {
        value : '{reunion.descripcion}',
        disabled : '{reunion.completada}'
    }
});

if (isMultilanguageApplication())
{
    formReunionItems.push({
        xtype : 'textareafield',
        name : 'descripcionAlternativa',
        fieldLabel : getMultiLangLabel(appI18N.reuniones.descripcion, alternativeLanguage),
        labelAlign : 'top',
        flex : 1,
        emptyText : appI18N.reuniones.descripcion,
        bind : {
            value : '{reunion.descripcionAlternativa}',
            disabled : '{reunion.completada}'
        }
    });
}

formReunionItems.push({
    xtype : 'multiselector',
    title : appI18N.reuniones.organosAsistentes,
    bind : {
        store : '{organosStore}'
    },

    fieldName : 'nombre',
    viewConfig : {
        deferEmptyText : false,
        emptyText : appI18N.reuniones.asistentesVacio
    },
    columns : {
        items : [
            {
                text : appI18N.common.nombre,
                dataIndex : 'nombre',
                flex : 1
            },
            {
                xtype : 'actioncolumn',
                align : 'right',
                width : 25,
                items : [
                    {
                        iconCls : 'x-fa fa-user',
                        tooltip : appI18N.reuniones.asistentes,
                        handler : function(grid, index)
                        {
                            var organo = grid.getStore().getAt(index);
                            var reunionId = grid.up('form[name=reunion]').down('hidden[name=id]').getValue();
                            grid.up('formReunion').fireEvent('detalleAsistentesReunion', organo, reunionId);
                        }
                    }
                ]
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
                            grid.up('formReunion').fireEvent('borrarAsistenteReunion', rec);
                        }
                    }
                ]
            }
        ]
    },

    search : {
        width : 500,
        field : 'nombre',
        bind : {
            disabled : '{reunion.completada}'
        },
        store : {
            fields : ['id', 'nombre'],
            proxy : {
                type : 'rest',
                url : '/goc/rest/organos/convocables',
                reader : {
                    type : 'json',
                    rootProperty : 'data'
                }
            }
        },
        search : function(text)
        {
            var me = this, filter = me.searchFilter, filters = me.getSearchStore().getFilters();

            if (text)
            {
                filters.beginUpdate();

                if (filter)
                {
                    filter.setValue(text);
                }
                else
                {
                    me.searchFilter = filter = new Ext.util.Filter(
                    {
                        id : 'search',
                        property : me.field,
                        value : text,
                        anyMatch : true
                    });
                }

                filters.add(filter);

                filters.endUpdate();
            }
            else if (filter)
            {
                filters.remove(filter);
            }
        }
    }
});


formReunionItems.push({
    xtype : 'invitadoGrid',
    padding : '10 0 0 0'
});

Ext.define('goc.view.reunion.FormReunion',
{
    extend : 'Ext.window.Window',
    xtype : 'formReunion',
    title : appI18N.reuniones.tituloPrincipal,

    width : 640,
    maxHeight : 1000,
    modal : true,
    bodyPadding : 10,
    autoScroll : true,

    viewConfig : 'fit',

    layout : {
        type : 'vbox',
        align : 'stretch'
    },

    requires : ['goc.view.reunion.FormReunionController'],
    controller : 'formReunionController',

    bbar : [
        '->',
        {
            xtype : 'button',
            text : appI18N.reuniones.guardar,
            bind : {
                disabled : '{reunion.completada}'
            },
            handler : 'onSaveRecord'
        },
        {
            xtype : 'panel',
            html : '<a style="text-decoration: none; color: #222;" href="#">' + appI18N.common.cancelar + '</a>',
            listeners : {
                render : function(component)
                {
                    component.getEl().on('click', 'onClose');
                }
            }
        }
    ],

    bind : {
        title : '{title}'
    },

    items : [
        {
            xtype : 'form',
            name : 'reunion',
            border : 0,
            layout : 'anchor',
            items : [
                {
                    xtype : 'fieldset',
                    title : appI18N.reuniones.informacionBasica,
                    defaultType : 'textfield',
                    defaults : {
                        anchor : '100%'
                    },

                    items : formReunionItems
                }
            ]
        }
    ],

    listeners : {
        borrarAsistenteReunion : 'onBorrarAsistenteReunion',
        detalleAsistentesReunion : 'onDetalleAsistentesReunion',
        afterLayout : 'afterRenderFormReunion'
    }
});
