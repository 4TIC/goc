Ext.define('goc.view.descriptor.MainController', {
    extend : 'Ext.app.ViewController',
    alias : 'controller.descriptorMainController',

    onLoad : function()
    {
        var viewModel = this.getViewModel();
        viewModel.getStore('descriptoresStore').load();
    }
});
