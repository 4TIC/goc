Ext.define('goc.view.reunion.FormReunionMiembrosController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.formReunionMiembrosController',
    onClose: function () {
        var win = Ext.WindowManager.getActive();
        if (win) {
            win.destroy();
        }
    },

    onAddSuplente: function () {
        var grid = this.getView().down('grid');
        var record = grid.getView().getSelectionModel().getSelection()[0];

        if (!record) {
            return Ext.Msg.alert(appI18N.reuniones.addSuplente, appI18N.reuniones.seleccionarParaAnyadirSuplente);
        }

        var window = Ext.create('goc.view.common.LookupWindowPersonas', {
            appPrefix: 'goc',
            title: appI18N.reuniones.seleccionaSuplente
        });

        window.show();

        window.on('LookoupWindowClickSeleccion', function (res) {
            record.set('suplenteId', res.get('id'));
            record.set('suplenteNombre', res.get('nombre'));
            record.set('suplenteEmail', res.get('email'));
        });
    },

    onRemoveSuplente: function () {
        var grid = this.getView().down('grid');
        var record = grid.getView().getSelectionModel().getSelection()[0];

        if (!record) {
            return Ext.Msg.alert(appI18N.reuniones.borrarSuplente, appI18N.reuniones.seleccionarParaBorrarSuplente);
        }
        record.set('suplenteId', null);
        record.set('suplenteNombre', null);
        record.set('suplenteEmail', null);
    },

    onLoad: function () {
        var viewModel = this.getViewModel();
        var reunionId = viewModel.get('reunionId');
        var organoId = viewModel.get('organoId');
        var externo = viewModel.get('externo');
        var remoteLoad = viewModel.get('remoteLoad')
        var grid = this.getView().down('grid');

        if (remoteLoad) {

            if (reunionId) {
                return viewModel.get('store').load({
                    url: '/goc/rest/reuniones/' + reunionId + '/miembros',
                    params: {
                        organoId: organoId,
                        externo: externo
                    }
                });
            }

            return viewModel.get('store').load({
                url: '/goc/rest/miembros',
                params: {
                    organoId: organoId,
                    externo: externo
                }
            });
        }
    },

    onSave: function () {
        var viewModel = this.getViewModel();
        var store = viewModel.get('store')
        var reunionId = viewModel.get('reunionId');
        this.onClose();
    },

    onSearchMiembro: function(field, searchString) {
        var viewModel = this.getViewModel();
        var store = viewModel.get('store')

        if (!searchString) {
            store.clearFilter();
            return;
        }

        var filter  = new Ext.util.Filter(
            {
                id : 'tipoOrganoId',
                property : 'nombre',
                value : searchString,
                anyMatch: true
            });

        store.addFilter(filter);

    }
});
