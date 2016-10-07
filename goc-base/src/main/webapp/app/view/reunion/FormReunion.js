Ext.define('goc.view.reunion.FormReunion',
{
    extend : 'Ext.window.Window',
    xtype : 'formReunion',
    title : 'Gestió de reunions',
    width : 640,
    manageHeight : true,
    modal : true,
    bodyPadding : 10,
    layout :
    {
        type : 'vbox',
        align : 'stretch'
    },

    requires : [ 'goc.view.reunion.FormReunionController' ],
    controller : 'formReunionController',

    bbar : [ '->',
    {
        xtype : 'button',
        text : 'Guardar',
        bind: {
            disabled : '{reunion.completada}'
        },
        handler : 'onSaveRecord'
    },
    {
        xtype : 'panel',
        html : '<a style="text-decoration: none; color: #222;" href="#">' + appI18N.common.cancelar + '</a>',
        listeners :
        {
            render : function(component)
            {
                component.getEl().on('click', 'onClose');
            }
        }
    } ],

    bind :
    {
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
            defaults :
            {
                anchor : '100%'
            },

            items : [
            {
                name : 'id',
                xtype : 'hidden',
                bind : '{reunion.id}'
            },
            {
                allowBlank : false,
                fieldLabel : appI18N.reuniones.asunto,
                name : 'asunto',
                emptyText : appI18N.reuniones.asunto,
                bind :
                {
                    value : '{reunion.asunto}',
                    disabled : '{reunion.completada}'
                }
            },
            {
                xtype : 'fieldcontainer',
                fieldLabel : appI18N.reuniones.fecha,
                layout : 'hbox',
                items : [

                {
                    allowBlank : false,
                    xtype : 'datefield',
                    name : 'fecha',
                    emptyText : appI18N.reuniones.fecha,
                    format : 'd/m/Y',
                    altFormats : 'd/m/Y H:i:s',
                    bind :
                    {
                        value : '{reunion.fecha}',
                        disabled : '{reunion.completada}'
                    },
                    flex : 1,
                    padding : '0 10 0 0'
                },
                {
                    allowBlank : false,
                    xtype : 'timefield',
                    name : 'hora',
                    emptyText : appI18N.reuniones.horaInicio,
                    format : 'H:i',
                    altFormats : 'd/m/Y H:i:s',
                    bind :
                    {
                        value : '{reunion.hora}',
                        disabled : '{reunion.completada}'
                    },
                    padding : '0 10 0 0'
                },
                {
                    allowBlank : false,
                    name : 'duracion',
                    xtype : 'combo',
                    width : 120,
                    emptyText : appI18N.reuniones.duracion,
                    store : Ext.create('Ext.data.Store',
                    {
                        fields : [ 'id', 'value' ],
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
                        } ]
                    }),
                    triggerAction : 'all',
                    queryMode : 'local',
                    displayField : 'texto',
                    valueField : 'value',
                    bind :
                    {
                        value : '{reunion.duracion}',
                        disabled : '{reunion.completada}'
                    }
                } ]
            },
            {
                xtype : 'container',
                fieldLabel : 'Número de sessió',
                layout : 'hbox',
                items : [
                {
                    fieldLabel : appI18N.reuniones.numeroSesion,
                    name : 'numeroSesion',
                    xtype : 'numberfield',
                    bind :
                    {
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
                    boxLabel : appI18N.reuniones.reunionPublica,
                    name : 'publica',
                    bind :
                    {
                        value : '{reunion.publica}',
                        disabled : '{reunion.completada}'
                    },
                    xtype : 'checkbox',
                    inputValue : '1'
                } ]
            },
            {
                xtype : 'fieldcontainer',
                fieldLabel : appI18N.reuniones.telematica,
                layout : 'hbox',
                items : [

                {
                    name : 'telematica',
                    bind :
                    {
                        value : '{reunion.telematica}',
                        disabled : '{reunion.completada}'
                    },
                    xtype : 'checkbox',
                    inputValue : '1',
                    handler : function(grid, value)
                    {
                        grid.up('formReunion').down('textareafield[name=telematicaDescripcion]').setVisible(value);
                    }
                },
                {
                    xtype : 'textareafield',
                    padding : '0 0 0 10',
                    name : 'telematicaDescripcion',
                    labelAlign : 'top',
                    flex : 1,
                    hidden : true,
                    emptyText : appI18N.reuniones.descripcionTelematica,
                    bind :
                    {
                        value : '{reunion.telematicaDescripcion}',
                        disabled : '{reunion.completada}'
                    }
                } ]
            },

            {
                fieldLabel : appI18N.reuniones.lugar,
                name : 'ubicacion',
                emptyText : appI18N.reuniones.ubicacionReunion,
                bind :
                {
                    value : '{reunion.ubicacion}',
                    disabled : '{reunion.completada}'
                }
            },
            {
                fieldLabel : 'URL gravació',
                name : 'urlGrabacion',
                emptyText : appI18N.reuniones.urlGrabacion,
                bind :
                {
                    value : '{reunion.urlGrabacion}',
                    disabled : '{reunion.completada}'
                }
            },
            {
                xtype : 'textareafield',
                name : 'descripcion',
                fieldLabel : appI18N.reuniones.descripcion,
                labelAlign : 'top',
                flex : 1,
                emptyText : appI18N.reuniones.descripcion,
                bind :
                {
                    value : '{reunion.descripcion}',
                    disabled : '{reunion.completada}'
                }
            },
            {
                xtype : 'multiselector',
                title : appI18N.reuniones.organosAsistentes,
                bind :
                {
                    store : '{organosStore}'
                },

                fieldName : 'nombre',
                viewConfig :
                {
                    deferEmptyText : false,
                    emptyText : appI18N.reuniones.asistentesVacio
                },
                columns :
                {
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
                        } ]
                    },
                    {
                        xtype : 'actioncolumn',
                        align : 'right',
                        width : 25,
                        bind :
                        {
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
                        } ]
                    } ]
                },

                search :
                {
                    field : 'nombre',
                    bind :
                    {
                        disabled : '{reunion.completada}'
                    },
                    store :
                    {
                        fields : [ 'id', 'nombre' ],
                        proxy :
                        {
                            type : 'rest',
                            url : '/goc/rest/organos/convocables',
                            reader :
                            {
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
            } ]
        } ]
    } ],

    listeners :
    {
        borrarAsistenteReunion : 'onBorrarAsistenteReunion',
        detalleAsistentesReunion : 'onDetalleAsistentesReunion'
    }
});
