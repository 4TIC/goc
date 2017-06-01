Ext.define('goc.view.reunion.FormDescriptoresOrdenDiaController', {
    extend : 'Ext.app.ViewController',
    alias : 'controller.formDescriptoresOrdenDiaController',
    ruta : '/goc/rest/reuniones/:idReunion/puntosOrdenDia/:idOrdenDia/descriptores',

    onLoad : function()
    {
        var vm              = this.getViewModel(),
            idReunion       = vm.get('reunionId'),
            idPuntoOrdenDia = vm.get('puntoOrdenDiaId'),
            grid            = Ext.ComponentQuery.query('grid[name=gridDescriptoresOrdenDia]')[0];
        store = vm.getStore('descriptoresOrdenDiaStore'),
        storeDescriptores = vm.getStore('descriptoresStore');
        var rutaEspecifica = this.ruta.replace(':idReunion', idReunion);
        rutaEspecifica = rutaEspecifica.replace(':idOrdenDia', idPuntoOrdenDia);
        store.proxy.url = rutaEspecifica;
        storeDescriptores.load({
            callback : function()
            {
                store.load({
                    callback : function()
                    {
                        grid.getView().refresh();
                    }
                });
            }
        });
    },

    onClose : function()
    {
    },

    onCancel : function()
    {
        var win = Ext.WindowManager.getActive();
        if (win)
        {
            win.destroy();
        }
    },

    onSaveRecord : function(button, context)
    {
        var vm   = this.getViewModel(),
            view = this.getView(),
            form = Ext.ComponentQuery.query('form[name=puntoOrdenDia]')[0];

        var reunionId = vm.get('reunionId');
        var grid = Ext.ComponentQuery.query('grid[name=ordenDia]')[0];

        if (form.isValid())
        {
            view.setLoading(true);
        }
    }

});
