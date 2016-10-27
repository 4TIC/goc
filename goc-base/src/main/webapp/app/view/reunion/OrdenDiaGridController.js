Ext.define('goc.view.reunion.OrdenDiaGridController', {
    extend: 'Ext.ux.uji.grid.PanelController',
    alias: 'controller.ordenDiaGridController',
    reunionId: null,
    onRefresh: function (reunionId) {
        if (reunionId === this.reunionId) return;
        this.reunionId = reunionId;

        var viewModel = this.getViewModel();
        this.getView().setDisabled(false);
        viewModel.getStore('puntosOrdenDiaStore').load({
            url: '/goc/rest/reuniones/' + reunionId + '/puntosOrdenDia'
        });

        this.actualizaEstadoBotones();
    },

    actualizaEstadoBotones: function() {
        var reunionGrid = Ext.ComponentQuery.query("reunionGrid")[0];
        var record = reunionGrid.getView().getSelectionModel().getSelection()[0];

        var grid = this.getView();
        var botonAdd = grid.getDockedItems()[1].items.items[0];
        var botonBorrar = grid.getDockedItems()[1].items.items[2];
        botonBorrar.setDisabled(record.get('completada'));
        botonAdd.setDisabled(record.get('completada'));
    },

    onDelete: function() {
        var grid = this.getView();
        var records = grid.getView().getSelectionModel().getSelection();
        var reunionId = this.reunionId;

        if (records.length === 0) {
            return Ext.Msg.alert(appI18N.reuniones.cerrarActa, appI18N.reuniones.seleccionarParaBorrarPuntoOrdenDia);
        }

        if (records.length === 1 && records[0].phantom === true) {
            return grid.getStore().remove(records);
        }

        if (records.length > 0)
        {
            Ext.Msg.confirm(appI18N.common.borrar, appI18N.reuniones.preguntaBorrarRegistro, function(btn, text)
            {
                if (btn == 'yes')
                {
                    var store = grid.getStore();
                    store.getProxy().url = '/goc/rest/reuniones/' + reunionId + '/puntosOrdenDia/';
                    grid.getStore().remove(records);
                    grid.getStore().sync();
                }
            });
        }
    },

    bajaPuntoOrdenDia: function(puntoOrdenDiaId) {
        var grid = this.getView();
        Ext.Ajax.request({
            url: '/goc/rest/reuniones/' + this.reunionId + '/puntosOrdenDia/' + puntoOrdenDiaId + '/bajar',
            method: 'PUT',
            success: function () {
                grid.getStore().reload();
            }
        });
    },

    subePuntoOrdenDia: function(puntoOrdenDiaId) {
        var grid = this.getView();
        Ext.Ajax.request({
            url: '/goc/rest/reuniones/' + this.reunionId + '/puntosOrdenDia/' + puntoOrdenDiaId + '/subir',
            method: 'PUT',
            success: function () {
                grid.getStore().reload();
            }
        });
    },

    onAdd: function () {
        this.createModalPuntoOrdenDia(null);
    },

    onEdit: function (grid, td, cellindex) {
        if (cellindex === 5) {
            return this.onAttachmentEdit();
        }
        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];

        if (!record) {
            return Ext.Msg.alert(appI18N.reuniones.cerrarActa, appI18N.reuniones.seleccionarParaEditarPuntoOrdenDia);
        }

        this.createModalPuntoOrdenDia(record);
    },

    onAttachmentEdit: function () {
        var view = this.getView().up('panel');
        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];
        var viewModel = this.getViewModel();
        var store = Ext.create('goc.store.PuntosOrdenDiaDocumentos');

        var reunionGrid = Ext.ComponentQuery.query("reunionGrid")[0];
        var reunion = reunionGrid.getView().getSelectionModel().getSelection()[0];

        if (!record) {
            return Ext.Msg.alert(appI18N.reuniones.cerrarActa, appI18N.reuniones.seleccionarParaDocumentacionPuntoOrdenDia);
        }

        this.modal = view.add({
            xtype: 'formOrdenDiaDocumentacion',
            viewModel: {
                data: {
                    title: appI18N.reuniones.documentacionDelPunto + ': ' + record.get('titulo'),
                    puntoOrdenDiaId: record.get('id'),
                    reunionId: this.reunionId,
                    reunionCompletada: reunion.get('completada'),
                    store: store
                }
            }
        });

        this.modal.show();

    },

    getPuntoOrdenDiaModalDefinition: function (puntoOrdenDia, store) {
        store.getProxy().url = '/goc/rest/reuniones/' + this.reunionId + '/puntosOrdenDia';

        var reunionGrid = Ext.ComponentQuery.query("reunionGrid")[0];
        var reunion = reunionGrid.getView().getSelectionModel().getSelection()[0];

        return {
            xtype: 'formOrdenDia',
            viewModel: {
                data: {
                    title: puntoOrdenDia ? appI18N.reuniones.edicion + ': ' + puntoOrdenDia.get('titulo') : appI18N.reuniones.nuevoPunto,
                    id: puntoOrdenDia ? puntoOrdenDia.get('id') : undefined,
                    reunionId: this.reunionId,
                    puntoOrdenDia: puntoOrdenDia || {
                        type: 'goc.model.PuntoOrdenDia',
                        reunionId: this.reunionId,
                        create: true
                    },
                    reunionCompletada: reunion.get('completada'),
                    store: store,
                }
            }
        };
    },

    createModalPuntoOrdenDia: function (record) {
        var view = this.getView().up('panel');
        var viewModel = this.getViewModel();
        var store = viewModel.getStore('puntosOrdenDiaStore');

        if (record) {
            var modalDefinition = this.getPuntoOrdenDiaModalDefinition(record, store);
            this.modal = view.add(modalDefinition);
            this.modal.down('textareafield[name=deliberaciones]').setVisible(true);
            this.modal.down('textareafield[name=acuerdos]').setVisible(true);
            this.modal.show();
        } else {
            var modalDefinition = this.getPuntoOrdenDiaModalDefinition(record, store);
            this.modal = view.add(modalDefinition);
            this.modal.down('textareafield[name=deliberaciones]').setVisible(false);
            this.modal.down('textareafield[name=acuerdos]').setVisible(false);
            this.modal.show();
        }
    }
});
