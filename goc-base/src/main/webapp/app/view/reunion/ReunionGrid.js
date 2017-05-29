var reunionesGridColumns = [
    {
        text : 'Id',
        width : 80,
        dataIndex : 'id',
        hidden : true
    },
    {
        text : getMultiLangLabel(appI18N.reuniones.asunto, mainLanguage),
        dataIndex : 'asunto',
        flex : 1,
        renderer : function(asunto, celda, record)
        {
            return asunto + '&nbsp;&nbsp;<a class="link" target="_blank" href="/goc/rest/publicacion/reuniones/' + record.get('id') + '"><i class="fa fa-external-link"></i></a>';
        }
    }
];

if (isMultilanguageApplication())
{
    reunionesGridColumns.push({
        text : getMultiLangLabel(appI18N.reuniones.asunto, alternativeLanguage),
        dataIndex : 'asuntoAlternativo',
        flex : 1,
        renderer : function(asunto, celda, record)
        {
            return asunto + '&nbsp;&nbsp;<a class="link" target="_blank" href="/goc/rest/publicacion/reuniones/' + record.get('id') + '"><i class="fa fa-external-link"></i></a>';
        }
    });
}

reunionesGridColumns.push({
    text : appI18N.reuniones.fecha,
    dataIndex : 'fecha',
    width : 150,
    format : 'd/m/Y H:i',
    xtype : 'datecolumn'
});

reunionesGridColumns.push({
    text : appI18N.reuniones.duracion,
    dataIndex : 'duracion',
    renderer : function(value)
    {
        var duracionTexto =
            {
                30 : '0,5 ' + appI18N.reuniones.horas,
                60 : '1 ' + appI18N.reuniones.hora,
                90 : '1,5 ' + appI18N.reuniones.horas,
                120 : '2 ' + appI18N.reuniones.horas,
                150 : '2,5 ' + appI18N.reuniones.horas,
                180 : '3 ' + appI18N.reuniones.horas
            };

        return duracionTexto[value];
    }
});

reunionesGridColumns.push({
    dataIndex : 'numeroDocumentos',
    text : appI18N.reuniones.documentos,
    align : 'center',
    renderer : function(value)
    {
        if (value > 0)
        {
            return '<span class="fa fa-file-text-o"></span>'
        }
        return ''
    }
});

Ext.define('goc.view.reunion.ReunionGrid',
{
    extend : 'Ext.ux.uji.grid.Panel',
    alias : 'widget.reunionGrid',
    plugins : [],
    requires : ['goc.view.reunion.ReunionGridController'],
    controller : 'reunionGridController',
    bind : {
        store : '{reunionesStore}'
    },
    name : 'reunion',
    reference : 'reunionGrid',
    multiSelect : false,
    scrollable : true,
    title : appI18N.reuniones.tituloGridReuniones,
    margin : 5,
    border : 0,
    columns : reunionesGridColumns,

    tbar : [
        {
            xtype : 'button',
            iconCls : 'fa fa-plus',
            text : appI18N.common.anadir,
            handler : 'onAdd'
        },
        {
            xtype : 'button',
            iconCls : 'fa fa-edit',
            name : 'botonEditar',
            text : appI18N.common.editar,
            handler : 'onEdit'
        },
        {
            xtype : 'button',
            iconCls : 'fa fa-remove',
            name : 'botonBorrar',
            text : appI18N.common.borrar,
            handler : 'onDelete',
            bind : {
                disabled : '{reunionGrid.selection.completada}'
            }
        }, ' | ',
        {
            xtype : 'button',
            iconCls : 'fa fa-check',
            name : 'botonDocumentacion',
            text : appI18N.reuniones.documentacionAdjunta,
            handler : 'onAttachmentEdit'
        }, ' | ', {
            xtype : 'splitbutton',
            text : appI18N.reuniones.acciones,
            iconCls : 'fa fa-gears',
            menu : [
                {
                    iconCls : 'fa fa-envelope-o',
                    name : 'botonEnviarConvocatoria',
                    text : appI18N.reuniones.enviarConvocatoria,
                    handler : 'onEnviarConvocatoria',
                    style : {
                        fontSize : '1.4em'
                    }
                },
                {
                    iconCls : 'fa fa-pencil-square-o',
                    name : 'fullaFirmes',
                    text : appI18N.reuniones.hojaFirmas,
                    handler : 'onHojaFirmas',
                    style : {
                        fontSize : '1.4em'
                    }
                },
                {
                    iconCls : 'fa fa-lock',
                    name : 'botonCerrar',
                    text : appI18N.reuniones.cerrarActa,
                    handler : 'onCompleted',
                    style : {
                        fontSize : '1.4em'
                    }
                }
            ]
        }, '->',
        {
            xtype : 'comboReunionTipoOrgano',
            margin : '0 10 10 0'
        },
        {
            xtype : 'comboOrgano',
            margin : '0 0 10 0'
        }
    ],
    listeners : {
        render : 'onLoad',
        celldblclick : 'onEdit',
        afterEdit : 'onAfterEdit',
        organoSelected : 'organoSelected',
        tipoOrganoSelected : 'tipoOrganoSelected',
        selectionChange : 'reunionSelected'
    }
});
