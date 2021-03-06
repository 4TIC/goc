Ext.define('goc.view.reunion.FormOrdenDiaController', {
    extend : 'Ext.app.ViewController',
    alias : 'controller.formOrdenDiaController',

    onClose : function()
    {
        var win = Ext.WindowManager.getActive();
        var grid = Ext.ComponentQuery.query('grid[name=ordenDia]')[0];
        grid.getStore().reload();

        if (win)
        {
            win.destroy();
        }
    },

    onCancel : function()
    {
        this.onClose();
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
            var record = vm.get('puntoOrdenDia');
            var store = vm.get('store');

            if (record.create !== true)
            {
                return record.save({
                    success : function()
                    {
                        this.onClose();
                    },
                    scope : this
                });
            }

            store.add(record);
            store.sync({
                success : function()
                {
                    grid.getStore().reload();
                    this.onClose();
                },
                failure : function()
                {
                    view.setLoading(false);
                },
                scope : this
            });
        }
    },

    afterRenderFormOrdenDia : function(windowFormOrdenDia)
    {
        var height = Ext.getBody().getViewSize().height;
        if (windowFormOrdenDia.getHeight() > height)
        {
            windowFormOrdenDia.setHeight(height - 30);
            windowFormOrdenDia.setPosition(windowFormOrdenDia.x, 15);
        }
    }

});
