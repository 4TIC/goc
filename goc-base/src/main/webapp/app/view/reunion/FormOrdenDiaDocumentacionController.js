Ext.define('goc.view.reunion.FormOrdenDiaDocumentacionController',
{
    extend : 'Ext.app.ViewController',
    alias : 'controller.formOrdenDiaDocumentacionController',

    onLoad: function () {
        var viewModel = this.getViewModel();
        var reunionId = viewModel.get('reunionId');
        var puntoOrdenDiaId = viewModel.get('puntoOrdenDiaId');

        if (reunionId) {
            viewModel.get('store').load({
                url: '/goc/rest/reuniones/' + reunionId + '/puntosOrdenDia/' + puntoOrdenDiaId + '/documentos'
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

    borraPuntoOrdenDiaDocumento: function (documentoId) {
        var viewModel = this.getViewModel();
        var reunionId = viewModel.get('reunionId');
        var puntoOrdenDiaId = viewModel.get('puntoOrdenDiaId');

        Ext.Msg.confirm(appI18N.common.borrar, appI18N.reuniones.preguntaBorrarDocumento, function (result) {
            if (result === 'yes') {
                Ext.Ajax.request({
                    url: '/goc/rest/reuniones/' + reunionId + '/puntosOrdenDia/' + puntoOrdenDiaId + '/documentos/' + documentoId,
                    method: 'DELETE',
                    success: function () {
                        viewModel.get('store').reload();
                    }
                });
            }
        });
    },

    descargaPuntoOrdenDiaDocumento: function (documentoId) {
        var viewModel = this.getViewModel();
        var reunionId = viewModel.get('reunionId');
        var puntoOrdenDiaId = viewModel.get('puntoOrdenDiaId');
        var url = '/goc/rest/reuniones/' + reunionId + '/puntosOrdenDia/' + puntoOrdenDiaId + '/documentos/' + documentoId + '/descargar';

        var body = Ext.getBody();
        var frame = body.createChild({
            tag: 'iframe',
            cls: 'x-hidden',
            name: 'iframe'
        });

        var form = body.createChild({
            tag: 'form',
            cls: 'x-hidden',
            action: url,
            target: 'iframe'
        });
        form.dom.submit();
    },

    subirPuntoOrdenDiaDocumento: function () {
        var viewModel = this.getViewModel();
        var reunionId = viewModel.get('reunionId');
        var puntoOrdenDiaId = viewModel.get('puntoOrdenDiaId');
        var view = this.getView();

        var form = view.down('form[name=subirDocumento]');
        if (form.getForm().isValid() && form.down('filefield[name=documento]').getValue() != "") {
            form.submit({
                url: '/goc/rest/reuniones/' + reunionId + '/puntosOrdenDia/' + puntoOrdenDiaId + '/documentos/',
                scope: this,
                success: function () {
                    viewModel.get('store').reload();
                }
            });
        }
    },

    onFileChangeOrdenDia: function (obj, value) {
        var filename = value.substring(12, value.length),
            labelNombreDocumento = this.getView().lookupReference('nombreDocumento');
        labelNombreDocumento.setValue(filename);
    }
});
