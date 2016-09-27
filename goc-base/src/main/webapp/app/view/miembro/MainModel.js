Ext.define('goc.view.miembro.MainModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.miembroMainModel',
    stores: {
        organosStore: {
            type: 'organos'
        }
    }
});
