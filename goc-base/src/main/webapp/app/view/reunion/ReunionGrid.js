Ext.define('goc.view.reunion.ReunionGrid',
    {
        extend: 'Ext.ux.uji.grid.Panel',
        alias: 'widget.reunionGrid',
        plugins: [],
        requires: ['goc.view.reunion.ReunionGridController'],
        controller: 'reunionGridController',
        bind: {
            store: '{reunionesStore}'
        },
        name: 'reunion',
        reference: 'reunionGrid',
        multiSelect: true,
        scrollable: true,
        title: appI18N.reuniones.tituloGridReuniones,
        margin: 5,
        border: 0,
        columns: [
            {
                text: 'Id',
                width: 80,
                dataIndex: 'id',
                hidden: true
            },
            {
                text: appI18N.reuniones.asunto,
                dataIndex: 'asunto',
                flex: 1,
                renderer: function (asunto, celda, record) {
                    return asunto + '&nbsp;&nbsp;<a class="link" target="_blank" href="/goc/rest/publicacion/reuniones/' + record.get('id') + '"><i class="fa fa-external-link"></i></a>';
                }
            },
            {
                text: appI18N.reuniones.fecha,
                dataIndex: 'fecha',
                width: 150,
                format: 'd/m/Y H:i',
                xtype: 'datecolumn'
            },
            {
                text: appI18N.reuniones.duracion,
                dataIndex: 'duracion',
                renderer: function (value) {
                    var duracionTexto =
                    {
                        30: '0,5 hores',
                        60: '1 hora',
                        90: '1,5 hores',
                        120: '2 hores',
                        150: '2,5 hores'
                    };

                    return duracionTexto[value];
                }
            },
            {
                dataIndex: 'numeroDocumentos',
                text: appI18N.reuniones.documentos,
                align: 'center',
                renderer: function (value) {
                    if (value > 0) {
                        return '<span class="fa fa-file-text-o"></span>'
                    }
                    return ''
                }
            }],

        tbar: [
            {
                xtype: 'button',
                iconCls: 'fa fa-plus',
                text: appI18N.common.anadir,
                handler: 'onAdd'
            },
            {
                xtype: 'button',
                iconCls: 'fa fa-edit',
                name: 'botonEditar',
                text: appI18N.common.editar,
                handler: 'onEdit'
            },
            {
                xtype: 'button',
                iconCls: 'fa fa-remove',
                name: 'botonBorrar',
                text: appI18N.common.borrar,
                handler: 'onDelete',
                bind: {
                    disabled: '{reunionGrid.selection.completada}'
                }
            }, ' | ',
            {
                xtype: 'button',
                iconCls: 'fa fa-check',
                name: 'botonDocumentacion',
                text: appI18N.reuniones.documentacionAdjunta,
                handler: 'onAttachmentEdit'
            }, ' | ',
            {
                xtype: 'button',
                iconCls: 'fa fa-check',
                name: 'botonEnviarConvocatoria',
                text: appI18N.reuniones.enviarConvocatoria,
                handler: 'onEnviarConvocatoria'
            }, ' | ',
            {
                xtype: 'button',
                iconCls: 'fa fa-check',
                name: 'botonCerrar',
                text: appI18N.reuniones.cerrarActa,
                handler: 'onCompleted'
            }, '->',
            {
                xtype: 'comboReunionTipoOrgano',
                margin: '0 10 10 0'
            },
            {
                xtype: 'comboOrgano',
                margin: '0 0 10 0'
            }],
        listeners: {
            render: 'onLoad',
            select: 'onSelect',
            celldblclick: 'onEdit',
            select: 'reunionSelected',
            afterEdit: 'onAfterEdit',
            organoSelected: 'organoSelected',
            tipoOrganoSelected: 'tipoOrganoSelected'
        }
    });
