Ext.define('goc.view.reunion.FormReunionController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.formReunionController',
    onClose: function () {
        var win = Ext.WindowManager.getActive();
        var grid = Ext.ComponentQuery.query('grid[name=reunion]')[0];
        grid.getStore().reload();

        if (win) {
            win.destroy();
        }
    },

    onBorrarAsistenteReunion: function (record) {
        var vm = this.getViewModel();
        var store = vm.get('organosStore');
        store.remove(record);
    },

    getStoreDeMiembros: function (organoId, externo) {
        var vm = this.getViewModel();
        var miembrosStores = vm.get('miembrosStores');
        return miembrosStores[organoId + '_' + externo]
    },

    creaStoreDeMiembros: function (organoId, externo) {
        var vm = this.getViewModel();
        var miembrosStores = vm.get('miembrosStores');
        miembrosStores[organoId + '_' + externo] = Ext.create('goc.store.OrganoReunionMiembros');
    },

    onDetalleAsistentesReunion: function (organo, reunionId) {
        var vm = this.getViewModel();
        var reunion = vm.get('reunion');
        var view = this.getView();
        var remoteLoad = false;
        if (!this.getStoreDeMiembros(organo.get('id'), organo.get('externo'))) {
            this.creaStoreDeMiembros(organo.get('id'), organo.get('externo'));
            remoteLoad = true;
        }

        var store = this.getStoreDeMiembros(organo.get('id'), organo.get('externo'));

        var formData = view.down('form').getValues();

        var modalDefinition = {
            xtype: 'formReunionMiembros',
            viewModel: {
                data: {
                    reunionId: reunionId,
                    reunionCompletada: reunionId ? reunion.get('completada') : false,
                    admiteSuplencia: formData.admiteSuplencia ? true: false,
                    admiteComentarios: formData.admiteComentarios ? true : false,
                    organoId: organo.get('id'),
                    externo: organo.get('externo'),
                    store: store,
                    remoteLoad: remoteLoad
                }
            }
        };
        this.modal = view.add(modalDefinition);
        this.modal.show();
    },

    saveOrganos: function (reunionId, organos, onClose) {
        var view = this.getView();

        var datosOrganos = [];
        var self = this;
        organos.each(function (record) {
            var miembros = [];
            var miembrosStore = self.getStoreDeMiembros(record.get('id'), record.get('externo'));
            if (miembrosStore) {
                miembrosStore.getData().each(function (record) {
                    miembros.push({
                        id: record.get('id'),
                        email: record.get('email'),
                        asistencia: record.get('asistencia'),
                        suplenteId: record.get('suplenteId'),
                        suplenteNombre: record.get('suplenteNombre'),
                        suplenteEmail: record.get('suplenteEmail')
                    });
                });
            }

            var data = {
                id: record.get('id'),
                externo: record.get('externo'),
                miembros: miembros
            };

            datosOrganos.push(data);
        });

        Ext.Ajax.request({
            url: '/goc/rest/reuniones/' + reunionId + '/organos',
            method: 'PUT',
            jsonData: {organos: datosOrganos},
            success: function () {
                onClose();
            }, failure: function () {
                view.setLoading(false);
            }
        });
    },

    onSaveRecord: function (button, context) {
        var vm = this.getViewModel(),
            view = this.getView(),
            form = Ext.ComponentQuery.query('form[name=reunion]')[0],
            multiselector = Ext.ComponentQuery.query('form[name=reunion] multiselector')[0]

        var organosStore = vm.get('organosStore');

        if (form.isValid()) {
            view.setLoading(true);
            var record = vm.get('reunion');
            var data = form.getValues();
            var store = vm.get('store');

            if (record.create !== true) {
                record.set('fecha', Ext.Date.parseDate(data.fecha + ' ' + data.hora, 'd/m/Y H:i'));
                record.set('fechaSegundaConvocatoria', Ext.Date.parseDate(data.fecha + ' ' + data.horaSegundaConvocatoria, 'd/m/Y H:i'));

                return record.save({
                    success: function () {
                        this.saveOrganos(data.id, organosStore.getData(), this.onClose);
                    }, failure: function () {
                        view.setLoading(false);
                    },
                    scope: this
                });
            }

            record.fecha = Ext.Date.parseDate(data.fecha + ' ' + data.hora, 'd/m/Y H:i');
            record.fechaSegundaConvocatoria = Ext.Date.parseDate(data.fecha + ' ' + data.hora, 'd/m/Y H:i');

            store.add(record);
            store.sync({
                success: function () {
                    this.saveOrganos(record.id, organosStore.getData(), this.onClose);
                },
                failure: function () {
                    view.setLoading(false);
                },
                scope: this
            });
        }
    },

    afterRenderFormReunion: function(windowFormReunion) {
        var height = Ext.getBody().getViewSize().height;
        if (windowFormReunion.getHeight() > height) {
            windowFormReunion.setHeight(height-30);
            windowFormReunion.setPosition(windowFormReunion.x, 15);
        }
    }
});
