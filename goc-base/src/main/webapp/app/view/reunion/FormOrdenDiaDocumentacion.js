Ext.define('goc.view.reunion.FormOrdenDiaDocumentacion',
    {
        extend: 'Ext.window.Window',
        xtype: 'formOrdenDiaDocumentacion',
        bind: {
            title: '{title}'
        },
        width: 640,
        manageHeight: true,
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
            }],

        items: [
            {
                xtype: 'grid',
                scrollable: true,
                margin: '5 0 5 0',
                layout: 'anchor',
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
                            }]
                    }]
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
