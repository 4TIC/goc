Ext.define('goc.view.reunion.FormDescriptoresOrdenDiaController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.formDescriptoresOrdenDiaController',
    ruta: '/goc/rest/reuniones/:idReunion/puntosOrdenDia/:idOrdenDia/descriptores',

    onLoad: function () {
        var vm = this.getViewModel(),
            idReunion = vm.get('reunionId'),
            idPuntoOrdenDia = vm.get('puntoOrdenDiaId'),
            grid = Ext.ComponentQuery.query('grid[name=gridDescriptoresOrdenDia]')[0];
            store = vm.getStore('descriptoresOrdenDiaStore'),
            storeDescriptores = vm.getStore('descriptoresStore');
        var rutaEspecifica = this.ruta.replace(':idReunion', idReunion);
        rutaEspecifica = rutaEspecifica.replace(':idOrdenDia', idPuntoOrdenDia);
        store.proxy.url = rutaEspecifica;
        storeDescriptores.load({
            callback: function () {
                store.load({
                    callback: function () {
                        grid.getView().refresh();
                    }
                });
            }
        });
    },

    onClose: function () {
        // var win = Ext.WindowManager.getActive();
        // var grid = Ext.ComponentQuery.query('grid[name=ordenDia]')[0];
        // grid.getStore().reload();
        //
        // if (win) {
        //     win.destroy();
        // }
    },

    onCancel: function () {
        var win = Ext.WindowManager.getActive();
        if (win) {
            win.destroy();
        }
    },

    onSaveRecord: function (button, context) {
        var vm = this.getViewModel(),
            view = this.getView(),
            form = Ext.ComponentQuery.query('form[name=puntoOrdenDia]')[0];

        var reunionId = vm.get('reunionId');
        var grid = Ext.ComponentQuery.query('grid[name=ordenDia]')[0];

        if (form.isValid()) {
            console.log("guardando.... " + reunionId + " - " + vm.get('puntoOrdenDiaId'));
            view.setLoading(true);
            // var record = vm.get('puntoOrdenDia');
            // var data = form.getValues();
            // var store = vm.get('store');
            //
            // if (record.create !== true) {
            //     return record.save({
            //         success: function () {
            //             this.onClose();
            //         },
            //         scope: this
            //     });
            // }
            //
            // store.add(record);
            // store.sync({
            //     success: function () {
            //         grid.getStore().reload();
            //         this.onClose();
            //     },
            //     failure: function () {
            //         view.setLoading(false);
            //     },
            //     scope: this
            // });
        }
    }

});
