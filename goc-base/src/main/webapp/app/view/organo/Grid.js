Ext.define('goc.view.organo.Grid', {
    extend: 'Ext.ux.uji.grid.Panel',
    alias: 'widget.organoGrid',
    requires: [
        'goc.view.organo.GridController'
    ],
    controller: 'organoGridController',
    bind: {
        store: '{organosStore}'
    },
    name: 'organosGrid',
    title: 'Òrgans',
    scrollable: true,
    columns: [ {
        text: appI18N.organos.nombre,
        dataIndex: 'nombre',
        flex: 1,
        editor: {
            field: {
                allowBlank: false
            }
        }
    }, {
        text: appI18N.organos.tipoOrgano,
        dataIndex: 'tipoOrganoId',
        flex: 1,
        renderer: function (id, meta, rec) {
            var store = this.up('organoMainPanel').getViewModel().getStore('tipoOrganosStore');
            var tipoOrganoRecord = store.getById(id);
            return tipoOrganoRecord ? tipoOrganoRecord.get('nombre') : '';
        },
        editor: {
            xtype: 'combobox',
            bind: {
                store: '{tipoOrganosStore}'
            },
            displayField: 'nombre',
            valueField: 'id',
            allowBlank: false,
            editable: false,
            queryMode: 'local'
        }
    }, {
        text: appI18N.organos.externo,
        width: 80,
        renderer: function (id, meta, rec) {
            return rec.get('externo') === 'true' ? 'Sí' : 'No';
        }
    }],

    listeners: {
        render: 'onLoad',
        beforeedit: 'decideRowIsEditable',
        select: 'organoSelected',
    }
});
