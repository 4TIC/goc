
Ext.define('goc.view.cargo.Grid',{
    extend: 'Ext.ux.uji.grid.Panel',

    alias: 'widget.cargoGrid',

    requires: [
        'goc.store.Cargos'
    ],

    store: {
        type: 'cargos'
    },

    title: 'Càrrecs',
    columns: [{
        text: 'ID',
        dataIndex: 'id',
        hidden: true
    }, {
        text: appI18N.cargos.nombre,
        dataIndex: 'nombre',
        flex: 1,
        editor: {
            field: {
                allowBlank: false
            }
        }
    }]
});
