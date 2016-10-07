Ext.define('goc.view.reunion.FormDocumentacion',
    {
        extend: 'Ext.window.Window',
        xtype: 'formDocumentacion',
        bind: {
            title: '{title}'
        },
        width: 640,
        height: 440,
        modal: true,
        bodyPadding: 10,
        layout: {
            type: 'vbox',
            align: 'stretch'
        },

        requires: ['goc.view.reunion.FormDocumentacionController'],
        controller: 'formDocumentacionController',

        bbar: ['->',
            {
                xtype: 'button',
                text: appI18N.reuniones.cerrar,
                handler: 'onClose'
            }],

        items: [
            {
                xtype: 'grid',
                flex: 1,
                scrollable: true,
                maxHeight: 180,
                margin: '5 0 5 0',
                viewConfig: {
                    emptyText: appI18N.reuniones.documentacionAdjuntaVacia
                },
                bind: {
                    store: '{store}'
                },
                hideHeaders: true,
                columns: [
                    {
                        text: appI18N.reuniones.descripcion,
                        dataIndex: 'descripcion',
                        flex: 1
                    },
                    {
                        xtype: 'actioncolumn',
                        name: 'delete',
                        align: 'right',
                        width: 25,
                        bind: {
                            disabled: '{completada}'
                        },
                        items: [
                            {
                                iconCls: 'x-fa fa-remove',
                                name: 'delete',
                                isDisabled: function(grid) {
                                    return this.disabled;
                                },
                                tooltip: appI18N.common.borrar,
                                handler: function (grid, index) {
                                    var rec = grid.getStore().getAt(index);
                                    var documentoId = rec.get('id');
                                    grid.up('formDocumentacion').fireEvent('borraDocumento', documentoId);
                                }
                            }]
                    },
                    {
                        xtype: 'actioncolumn',
                        align: 'right',
                        width: 25,
                        items: [
                            {
                                iconCls: 'x-fa fa-download',
                                tooltip: appI18N.reuniones.descargar,
                                handler: function (grid, index) {
                                    var rec = grid.getStore().getAt(index);
                                    var documentoId = rec.get('id');
                                    grid.up('formDocumentacion').fireEvent('descargaDocumento', documentoId);
                                }
                            }]
                    }]
            },
            {
                xtype: 'form',
                bind: {
                    disabled: '{completada}'
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
                        layout: 'hbox',
                        items: [

                            {
                                xtype: 'textfield',
                                fieldLabel: appI18N.reuniones.descripcion,
                                allowBlank: false,
                                emptyText: appI18N.reuniones.descripcion,
                                width: '100%',
                                flex: 1,
                                name: 'descripcion'
                            },
                            {
                                xtype: 'filefield',
                                buttonOnly: true,
                                width: 40,
                                name: 'documento',
                                buttonConfig: {
                                    text: '',
                                    iconCls: 'fa fa-file'
                                }
                            }
                        ]
                    },
                ],
                buttons: [
                    {
                        text: appI18N.reuniones.subirDocumento,
                        handler: 'subirDocumento'

                    }
                ]
            }
        ],
        listeners: {
            render: 'onLoad',
            subirDocumento: 'subirDocumento',
            descargaDocumento: 'descargaDocumento',
            borraDocumento: 'borraDocumento'
        }
    });
