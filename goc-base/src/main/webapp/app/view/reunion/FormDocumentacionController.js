Ext.define('goc.view.reunion.FormDocumentacionController',
{
    extend : 'Ext.app.ViewController',
    alias : 'controller.formDocumentacionController',

    onLoad : function()
    {
        var viewModel = this.getViewModel();
        var reunionId = viewModel.get('id');

        if (reunionId)
        {
            viewModel.get('store').load({
                url : '/goc/rest/reuniones/' + reunionId + '/documentos'
            });
        }

    },

    onClose : function()
    {
        var win = Ext.WindowManager.getActive();
        var grid = Ext.ComponentQuery.query('grid[name=reunion]')[0];
        grid.getStore().reload();

        if (win)
        {
            win.destroy();
        }
    },

    borraDocumento : function(documentoId)
    {
        var viewModel = this.getViewModel();
        var reunionId = viewModel.get('id');

        Ext.Msg.confirm('Esborrar', 'Esteu segur/a de voler esborrar el document?', function(result)
        {
            if (result === 'yes')
            {
                Ext.Ajax.request({
                    url : '/goc/rest/reuniones/' + reunionId + '/documentos/' + documentoId,
                    method : 'DELETE',
                    success : function()
                    {
                        viewModel.get('store').reload();
                    }
                });
            }
        });
    },

    descargaDocumento : function(documentoId)
    {
        var viewModel = this.getViewModel();
        var reunionId = viewModel.get('id');
        var url = '/goc/rest/reuniones/' + reunionId + '/documentos/' + documentoId + '/descargar';

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

    subirDocumento : function()
    {
        var viewModel = this.getViewModel();
        var reunionId = viewModel.get('id');
        var view = this.getView();

        var form = view.down('form[name=subirDocumento]');
        if (form.getForm().isValid() && form.down('filefield[name=documento]').getValue() != "")
        {
            form.submit({
                url : '/goc/rest/reuniones/' + reunionId + '/documentos/',
                scope : this,
                success : function()
                {
                    viewModel.get('store').load({
                        url : '/goc/rest/reuniones/' + reunionId + '/documentos'
                    });

                    view.down('form').reset();
                }
            });
        }
    },

    onFileChange : function(obj, value)
    {
        var filename = value.split(/(\\|\/)/g).pop(),
            labelNombreDocumento = this.getView().lookupReference('nombreDocumento');
        labelNombreDocumento.setValue(filename);
    }
});
