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

        if (win)
        {
            win.destroy();
        }
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
                jsonData: data,
                success : function(response)
                {
                    this.onClose();
                },
                scope: this
            });
        }

    }

});
