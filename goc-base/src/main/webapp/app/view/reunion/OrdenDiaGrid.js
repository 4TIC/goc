Ext.define('goc.view.reunion.OrdenDiaGrid',
    {
        extend: 'Ext.ux.uji.grid.Panel',
        alias: 'widget.ordenDiaGrid',
        plugins: [],
        disabled: true,
        requires: ['goc.view.reunion.OrdenDiaGridController'],
        controller: 'ordenDiaGridController',
        bind: {
            store: '{puntosOrdenDiaStore}',
            selection: '{selectedReunion}'
        },
        name: 'ordenDia',
        reference: 'ordenDiaGrid',
        scrollable: true,
        title: appI18N.reuniones.tituloOrdenDia,
        multiSelect: true,
        columns: [
            {
                text: 'Id',
                width: 80,
                dataIndex: 'id',
                hidden: true
            },
            {
                xtype: 'actioncolumn',
                title: appI18N.reuniones.posicion,
                align: 'right',
                width: 50,
                items: [
                    {
                        iconCls: 'x-fa fa-arrow-up',
                        tooltip: appI18N.reuniones.subir,
                        isDisabled: function(grid) {
                            var reunionGrid = Ext.ComponentQuery.query("reunionGrid")[0];
                            var record = reunionGrid.getView().getSelectionModel().getSelection()[0];
                            return record.get('completada');
                        },
                        handler: function (grid, index) {
                            var rec = grid.getStore().getAt(index);
                            var puntoOrdenDiaId = rec.get('id');
                            grid.up('panel[name=ordenDia]').fireEvent('subePuntoOrdenDia', puntoOrdenDiaId);
                        }
                    },
                    {
                        iconCls: 'x-fa fa-arrow-down',
                        tooltip: appI18N.reuniones.bajar,
                        isDisabled: function(grid) {
                            var reunionGrid = Ext.ComponentQuery.query("reunionGrid")[0];
                            var record = reunionGrid.getView().getSelectionModel().getSelection()[0];
                            return record.get('completada');
                        },
                        handler: function (grid, index) {
                            var rec = grid.getStore().getAt(index);
                            var puntoOrdenDiaId = rec.get('id');
                            grid.up('panel[name=ordenDia]').fireEvent('bajaPuntoOrdenDia', puntoOrdenDiaId);
                        }
                    }]
            },

            {
                text: appI18N.reuniones.titulo,
                dataIndex: 'titulo',
                flex: 1
            },
            {
                text: appI18N.reuniones.publico,
                dataIndex: 'publico',
                renderer: function(val) {
                    return val ? 'Sí' : 'No';
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
        viewConfig: {
            emptyText: appI18N.reuniones.ordendiaVacio
        },
        listeners: {
            reunionSelected: 'onRefresh',
            celldblclick: 'onEdit',
            onDelete: 'onDelete',
            afterEdit: 'onAfterEdit',
            bajaPuntoOrdenDia: 'bajaPuntoOrdenDia',
            subePuntoOrdenDia: 'subePuntoOrdenDia'
        },

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
                text: appI18N.common.editar,
                handler: 'onEdit',
                isDisabled: function() {
                    return true;
                }
            },
            {
                xtype: 'button',
                iconCls: 'fa fa-remove',
                text: appI18N.common.borrar,
                handler: 'onDelete'
            }, ' | ',
            {
                xtype: 'button',
                iconCls: 'fa fa-check',
                text: appI18N.reuniones.documentacionAdjunta,
                handler: 'onAttachmentEdit'
            }]
    });
