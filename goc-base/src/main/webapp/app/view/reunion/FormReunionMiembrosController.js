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

        if (!record) return;

        var window = Ext.create('goc.view.common.LookupWindowPersonas', {
            appPrefix: 'goc',
            title: appI18N.reuniones.seleccionaSuplente
        });

        window.show();

        window.on('LookoupWindowClickSeleccion', function (res) {
            console.log(res);
            record.set('suplenteId', res.get('id'));
            record.set('suplenteNombre', res.get('nombre'));
            record.set('suplenteEmail', res.get('email'));
        });
    },

    onRemoveSuplente: function () {
        var grid = this.getView().down('grid');
        var record = grid.getView().getSelectionModel().getSelection()[0];

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
            viewModel.get('store').load({
                url: '/goc/rest/reuniones/' + reunionId + '/miembros',
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
    }
});
