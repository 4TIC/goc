
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
    scrollable: true,
    columns: [{
        text: 'ID',
        dataIndex: 'id',
        hidden: true
    }, {
        text: appI18N.tipoOrganos.codigo,
        dataIndex: 'codigo',
        editor: {
            field: {
                allowBlank: false
            }
        },
    }, {
        text: appI18N.tipoOrganos.nombre,
        dataIndex: 'nombre',
        flex: 1,
        editor: {
            field: {
                allowBlank: false
            }
        }
    }]
});
