Ext.define('goc.view.reunion.ReunionGridController', {
    extend: 'Ext.ux.uji.grid.PanelController',
    alias: 'controller.reunionGridController',

    onLoad: function () {
        var viewModel = this.getViewModel();
        viewModel.getStore('reunionesStore').load();
    },

    onAdd: function () {
        this.createModalReunion(null);
    },

    onEdit: function (grid, td, cellindex) {
        if (cellindex === 4) {
            return this.onAttachmentEdit();
        }

        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];

        if (!record) {
            return Ext.Msg.alert(appI18N.common.edicionRegistro, appI18N.common.seleccionarParaEditarRegistro);
        }

        this.createModalReunion(record);
    },

    onEnviarConvocatoria: function() {
        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];

        Ext.Msg.confirm(appI18N.reuniones.confirmacionEnvioTitulo, appI18N.reuniones.confirmacionEnvioMensaje, function(result) {
            if (result === 'yes') {
                Ext.Ajax.request(
                    {
                        url: '/goc/rest/reuniones/' + record.get('id') + '/enviarconvocatoria',
                        method: 'PUT',
                        success: function (response) {
                            var data = Ext.decode(response.responseText);
                            var mensajeRespuesta = (data.message && data.message.indexOf("appI18N") != -1) ? eval(data.message) : data.message;
                            Ext.Msg.alert(appI18N.reuniones.resultadoEnvioConvocatoriaTitle, mensajeRespuesta);
                        },
                        scope: this
                    }
                );
            }
        });
    },

    onCompleted: function () {
        var view = this.getView().up('panel');
        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];
        var viewModel = this.getViewModel();
        var viewport = this.getView().up('viewport');

        if (!record) {
            return Ext.Msg.alert(appI18N.reuniones.cerrarActa, appI18N.reuniones.seleccionarParaCerrarActa);
        }

        var asistentesStore = Ext.create('goc.store.OrganoReunionMiembros', {
            proxy: {
                url: '/goc/rest/reuniones/' + record.get('id') + '/miembros'
            },
            autoLoad: true
        });

        this.modal = viewport.add({
            xtype: 'formReunionAcuerdos',
            viewModel: {
                data: {
                    title: appI18N.reuniones.titleSingular + ': ' + record.get('asunto'),
                    reunionId: record.get('id'),
                    responsableId: record.get('miembroResponsableActaId'),
                    completada: record.get('completada'),
                    acuerdos: record.get('acuerdos'),
                    acuerdosAlternativos: record.get('acuerdosAlternativos'),
                    asistentesStore: asistentesStore
                }
            }
        });

        this.modal.show();
    },

    organoSelected: function(id, externo) {
        var grid = this.getView();

        if (id) {
            grid.getStore().load({
                params: {
                    organoId: id,
                    externo: externo
                }
            });
        }
        else {
            var comboTipoOrgano = grid.down('comboReunionTipoOrgano');
            grid.getStore().load({
                params: {
                    tipoOrganoId: comboTipoOrgano.getValue()
                }
            });
        }
    },

    filtraComboOrgano: function(tipoOrganoId) {
        var vm = this.getViewModel();
        var store = vm.getStore('organosStore');
        var filter  = new Ext.util.Filter(
            {
                id : 'tipoOrganoId',
                property : 'tipoOrganoId',
                value : tipoOrganoId
            });

        store.addFilter(filter);
    },

    limpiaFiltrosComboOrgano: function() {
        var vm = this.getViewModel();
        var store = vm.getStore('organosStore');
        store.clearFilter();

        var grid = this.getView();

        var comboOrganos = grid.down('comboOrgano');
        comboOrganos.clearValue();
    },

    tipoOrganoSelected: function(id, externo) {
        var grid = this.getView();

        if (id) {
            grid.getStore().load({
                params: {
                    tipoOrganoId: id
                }
            });
            var comboOrganos = grid.down('comboOrgano');
            comboOrganos.clearValue();
            this.filtraComboOrgano(id);
        }
        else {
            grid.getStore().load();
            this.limpiaFiltrosComboOrgano();
        }
    },

    onAttachmentEdit: function () {
        var view = this.getView().up('panel');
        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];
        var viewModel = this.getViewModel();
        var store = Ext.create('goc.store.ReunionDocumentos');

        if (!record) {
            return Ext.Msg.alert(appI18N.reuniones.documentacion, appI18N.reuniones.seleccionarParaDocumentacion);
        }

        this.modal = view.add({
            xtype: 'formDocumentacion',
            viewModel: {
                data: {
                    title: appI18N.reuniones.tituloDocumentacion + ': ' + record.get('asunto'),
                    id: record.get('id'),
                    completada: record.get('completada'),
                    store: store
                }
            }
        });

        this.modal.show();

    },

    getReunionModalDefinition: function (reunion, reunionesStore, organosStore) {
        return {
            xtype: 'formReunion',
            viewModel: {
                data: {
                    title: reunion ? appI18N.reuniones.edicion + ': ' + reunion.get('asunto') : appI18N.reuniones.nuevaReunion,
                    id: reunion ? reunion.get('id') : undefined,
                    reunion: reunion || {
                        type: 'goc.model.Reunion',
                        create: true
                    },
                    store: reunionesStore,
                    organosStore: organosStore,
                    organosMiembros: {},
                    miembrosStores: {}
                }
            }
        };
    },

    createModalReunion: function (record) {
        var view = this.getView().up('panel');
        var viewModel = this.getViewModel();
        var store = viewModel.getStore('reunionesStore');
        var organosStore = Ext.create('goc.store.Organos');
        var viewport = this.getView().up('viewport');

        if (record) {
            organosStore.load({
                params: {
                    reunionId: record ? record.get('id') : null
                },
                callback: function (records) {
                    var modalDefinition = this.getReunionModalDefinition(record, store, organosStore);
                    this.modal = viewport.add(modalDefinition);
                    this.modal.down('textfield[name=urlGrabacion]').setVisible(true);
                    this.modal.show();
                },
                scope: this
            });
        } else {
            var modalDefinition = this.getReunionModalDefinition(record, store, organosStore);
            this.modal = viewport.add(modalDefinition);
            this.modal.show();
        }
    },

    reunionSelected: function() {
        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];

        var grid = Ext.ComponentQuery.query('grid[name=ordenDia]')[0];
        grid.fireEvent('reunionSelected', record.get('id'));
    }
});
