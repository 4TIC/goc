
Ext.define('goc.view.tipoOrgano.Grid',{
    extend: 'Ext.ux.uji.grid.Panel',

    alias: 'widget.tipoOrganoGrid',
    requires: [
        'goc.store.TipoOrganos'
    ],
    store: {
        type: 'tipoOrganos'
    },
    title: appI18N.tipoOrganos.titulo,
    columns: [{
        text: 'ID',
        dataIndex: 'id',
        hidden: true
    }, {
        text: 'Código',
        dataIndex: 'codigo',
        editor: {
            field: {
                allowBlank: false
            }
        },
    }, {
        text: 'Nombre',
        dataIndex: 'nombre',
        flex: 1,
        editor: {
            field: {
                allowBlank: false
            }
        }
    }]
});
