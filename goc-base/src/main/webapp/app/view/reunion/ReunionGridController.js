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
        if (cellindex === 5) {
            return this.onAttachmentEdit();
        }

        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];

        if (!record) return;
        this.createModalReunion(record);
    },
    onCompleted: function () {
        var view = this.getView().up('panel');
        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];
        var viewModel = this.getViewModel();

        if (!record) {
            return;
        }

        var asistentesStore = Ext.create('goc.store.OrganoReunionMiembros', {
            proxy: {
                url: '/goc/rest/reuniones/' + record.get('id') + '/miembros'
            },
            autoLoad: true
        });

        this.modal = view.add({
            xtype: 'formReunionAcuerdos',
            viewModel: {
                data: {
                    title: 'Reunió: ' + record.get('asunto'),
                    reunionId: record.get('id'),
                    responsableId: record.get('miembroResponsableActaId'),
                    completada: record.get('completada'),
                    acuerdos: record.get('acuerdos'),
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
        } else {
            grid.getStore().load();
        }
    },

    onAttachmentEdit: function () {
        var view = this.getView().up('panel');
        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];
        var viewModel = this.getViewModel();
        var store = Ext.create('goc.store.ReunionDocumentos');

        if (!record) {
            return;
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

        if (record) {
            organosStore.load({
                params: {
                    reunionId: record ? record.get('id') : null
                },
                callback: function (records) {
                    var modalDefinition = this.getReunionModalDefinition(record, store, organosStore);
                    this.modal = view.add(modalDefinition);
                    this.modal.down('textfield[name=urlGrabacion]').setVisible(true);
                    this.modal.show();
                },
                scope: this
            });
        } else {
            var modalDefinition = this.getReunionModalDefinition(record, store, organosStore);
            this.modal = view.add(modalDefinition);
            //this.modal.down('timefield[name=hora]').setValue('08:00');
            this.modal.down('textfield[name=urlGrabacion]').setVisible(false);
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
