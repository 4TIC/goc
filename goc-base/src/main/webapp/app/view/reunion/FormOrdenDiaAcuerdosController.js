Ext.define('goc.view.reunion.FormOrdenDiaAcuerdosController',
{
    extend : 'Ext.app.ViewController',
    alias : 'controller.formOrdenDiaAcuerdosController',

    onLoad : function()
    {
        var viewModel = this.getViewModel();
        var reunionId = viewModel.get('reunionId');
        var puntoOrdenDiaId = viewModel.get('puntoOrdenDiaId');

        if (reunionId)
        {
            viewModel.get('store').load({
                url : '/goc/rest/reuniones/' + reunionId + '/puntosOrdenDia/' + puntoOrdenDiaId + '/acuerdos'
            });
        }
    },

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

    borraPuntoOrdenDiaDocumento : function(documentoId)
    {
        var viewModel = this.getViewModel();
        var reunionId = viewModel.get('reunionId');
        var puntoOrdenDiaId = viewModel.get('puntoOrdenDiaId');

        Ext.Msg.confirm(appI18N.common.borrar, appI18N.reuniones.preguntaBorrarAcuerdo, function(result)
        {
            if (result === 'yes')
            {
                Ext.Ajax.request({
                    url : '/goc/rest/reuniones/' + reunionId + '/puntosOrdenDia/' + puntoOrdenDiaId + '/acuerdos/' + documentoId,
                    method : 'DELETE',
                    success : function()
                    {
                        viewModel.get('store').reload();
                    }
                });
            }
        });
    },

    descargaPuntoOrdenDiaDocumento : function(documentoId)
    {
        var viewModel = this.getViewModel();
        var reunionId = viewModel.get('reunionId');
        var puntoOrdenDiaId = viewModel.get('puntoOrdenDiaId');
        var url = '/goc/rest/reuniones/' + reunionId + '/puntosOrdenDia/' + puntoOrdenDiaId + '/acuerdos/' + documentoId + '/descargar';

        var body = Ext.getBody();
        var frame = body.createChild({
            tag : 'iframe',
            cls : 'x-hidden',
            name : 'iframe'
        });

        var form = body.createChild({
            tag : 'form',
            cls : 'x-hidden',
            action : url,
            target : 'iframe'
        });
        form.dom.submit();
    },

    subirPuntoOrdenDiaDocumento : function()
    {
        var viewModel = this.getViewModel();
        var reunionId = viewModel.get('reunionId');
        var puntoOrdenDiaId = viewModel.get('puntoOrdenDiaId');
        var view = this.getView();

        var form = view.down('form[name=subirDocumento]');
        if (form.getForm().isValid() && form.down('filefield[name=documento]').getValue() != "")
        {
            form.submit({
                url : '/goc/rest/reuniones/' + reunionId + '/puntosOrdenDia/' + puntoOrdenDiaId + '/acuerdos/',
                scope : this,
                success : function()
                {
                    viewModel.get('store').reload();

                    view.down('form').reset();
                }
            });
        }
    },

    onFileChangeOrdenDia : function(obj, value)
    {
        var filename = value.split(/(\\|\/)/g).pop(),
            labelNombreDocumento = this.getView().lookupReference('nombreDocumento');
        labelNombreDocumento.setValue(filename);
    }
});
