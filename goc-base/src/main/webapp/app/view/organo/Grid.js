Ext.define('goc.view.organo.Grid', {
    extend: 'Ext.ux.uji.grid.Panel',
    alias: 'widget.organoGrid',
    requires: [
        'goc.view.organo.GridController'
    ],
    controller: 'organoGridController',
    reference: 'organoGrid',
    bind: {
        store: '{organosStore}',
        selection: '{selectedOrgano}'
    },
    name: 'organosGrid',
    title: 'Òrgans',
    scrollable: true,

    tbar: [
        {
            xtype: 'button',
            name: 'add',
            iconCls: 'fa fa-plus',
            text: appI18N ? appI18N.common.anadir : 'Afegir',
            handler: 'onAdd'
        },
        {
            xtype: 'button',
            name: 'edit',
            iconCls: 'fa fa-edit',
            text: appI18N ? appI18N.common.editar : 'Editar',
            handler: 'onEdit'
        },
        {
            xtype: 'button',
            iconCls: 'fa fa-remove',
            text: 'Inhabilita',
            handler: 'onToggleEstado',
            hidden: true,
            bind: {
                hidden: '{ocultaBotonInhabilita}'
            }
        },
        {
            xtype: 'button',
            iconCls: 'fa fa-check-circle',
            text: 'Habilita',
            handler: 'onToggleEstado',
            hidden: true,
            bind: {
                hidden: '{ocultaBotonHabilita}'
            }
        }],

    columns: [{
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
