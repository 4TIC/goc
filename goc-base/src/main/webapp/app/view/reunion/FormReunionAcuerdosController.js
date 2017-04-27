Ext.define('goc.view.reunion.FormReunionAcuerdosController',
{
    extend : 'Ext.app.ViewController',
    alias : 'controller.formReunionAcuerdosController',
    onClose : function()
    {
        var win = Ext.WindowManager.getActive();

        var grid = Ext.ComponentQuery.query('grid[name=reunion]')[0];
        grid.getSelectionModel().deselectAll();
        grid.getStore().reload();

        var gridHistorico = Ext.ComponentQuery.query('historicoReunionGrid')[0];
        if (gridHistorico)
        {
            gridHistorico.getStore().reload();
        }

        if (win)
        {
            win.destroy();
        }

        var gridOrdenDia = grid.up('panel').down('grid[name=ordenDia]');
        if (gridOrdenDia) gridOrdenDia.clearStore();
    },

    onSaveRecord : function(button, context)
    {
        var form = Ext.ComponentQuery.query('form[name=formReunionAcuerdos]')[0];

        if (form.isValid())
        {
            var data = form.getValues();
            Ext.Ajax.request(
            {
                url : '/goc/rest/reuniones/' + data.id + '/completada',
                method : 'PUT',
                jsonData : data,
                success : function(response)
                {
                    this.onClose();
                },
                scope : this
            });
        }
    },

    afterRenderFormCerrarActa : function(windowFormCerrarActa)
    {
        var height = Ext.getBody().getViewSize().height;
        if (windowFormCerrarActa.getHeight() > height)
        {
            windowFormCerrarActa.setHeight(height - 30);
            windowFormCerrarActa.setPosition(windowFormCerrarActa.x, 15);
        }
    }
});
