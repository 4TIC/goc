Ext.define('goc.view.reunion.FormReunionMiembros', {
    extend : 'Ext.window.Window',
    xtype : 'formReunionMiembros',
    width : 850,
    modal : true,
    bodyPadding : 10,
    layout : {
        type : 'vbox',
        align : 'stretch',
        pack : 'start'
    },
    title : appI18N.reuniones.gestionAsistentes,

    requires : [
        'goc.view.reunion.FormReunionMiembrosController'
    ],
    controller : 'formReunionMiembrosController',
    viewModel : {
        type : 'reunionViewModel'
    },
    tbar : [
        {
            xtype : 'button',
            iconCls : 'fa fa-plus',
            text : appI18N.reuniones.anadirSuplente,
            handler : 'onAddSuplente',
            bind : {
                disabled : '{!admiteSuplencia || selectedMiembro.delegadoVotoId || !selectedMiembro.asistencia}'
            }
        },
        {
            xtype : 'button',
            iconCls : 'fa fa-edit',
            text : appI18N.reuniones.borrarSuplente,
            handler : 'onRemoveSuplente',
            bind : {
                disabled : '{!admiteSuplencia}'
            }
        }
    ],

    items : [
        {
            xtype : 'textfield',
            emptyText : appI18N.reuniones.buscaAsistente,
            name : 'searchMiembro',
            listeners : {
                change : 'onSearchMiembro'
            }
        },
        {
            xtype : 'grid',
            flex : 1,
            height : 300,
            scrollable : true,
            margin : '5 0 5 0',
            viewConfig : {
                emptyText : appI18N.reuniones.sinAsistentes,
                markDirty : false
            },
            bind : {
                store : '{store}',
                selection : '{selectedMiembro}'
            },
            columns : [
                {
                    text : appI18N.common.nombre,
                    dataIndex : 'nombre',
                    flex : 2
                },
                {
                    text : appI18N.miembros.cargo,
                    dataIndex : 'cargoNombre',
                    flex : 1
                },
                {
                    text : appI18N.reuniones.correo,
                    dataIndex : 'email',
                    flex : 1
                },
                {
                    dataIndex : 'suplenteId',
                    hidden : true
                },
                {
                    text : appI18N.reuniones.suplente,
                    dataIndex : 'suplenteNombre',
                    flex : 1
                },
                {
                    text : appI18N.reuniones.suplenteEmail,
                    dataIndex : 'suplenteEmail',
                    flex : 1
                },
                {
                    text : appI18N.reuniones.delegacionVoto,
                    dataIndex : 'delegadoVotoNombre',
                    flex : 1
                },
                {
                    text : appI18N.reuniones.delegacionVotoEmail,
                    dataIndex : 'delegadoVotoEmail',
                    flex : 1
                },
                {
                    text : appI18N.reuniones.asistencia,
                    xtype : 'checkcolumn',
                    dataIndex : 'asistencia',
                    bind : {
                        disabled : '{reunionCompletada}'
                    },
                    listeners : {
                        checkchange : 'onChangeAsistencia',
                        beforecheckchange : function(me, rowIndex, checked, rec)
                        {
                            if (rec.get('delegadoVotoId') || rec.get('suplenteId'))
                            {
                                return false;
                            }

                            return true;
                        }
                    },
                    renderer : function(value, metadata, rec)
                    {
                        if (rec.get('delegadoVotoId') || rec.get('suplenteId'))
                        {
                            metadata.tdCls = 'x-item-disabled';
                        }

                        return this.defaultRenderer(value, metadata);
                    }
                }
            ]
        }
    ],

    bbar : {
        defaultButtonUI : 'default',
        items : [
            '->', {
                xtype : 'button',
                text : appI18N.common.aceptar,
                handler : 'onClose',
                bind : {
                    disabled : '{reunionCompletada}'
                }
            }, {
                xtype : 'panel',
                html : '<a style="text-decoration: none; color: #222;" href="#">' + appI18N.common.cancelar + '</a>',
                listeners : {
                    render : function(component)
                    {
                        component.getEl().on('click', 'onClose');
                    }
                }
            }
        ]
    },

    listeners : {
        show : 'onLoad',
        onSave : 'onSave',
        onAddSuplente : 'onAddSuplente',
        onRemoveSuplente : 'onRemoveSuplente'
    }
});
