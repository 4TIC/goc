Ext.define('goc.view.descriptor.GridTipoOrgano', {
    extend : 'Ext.ux.uji.grid.Panel',
    alias : 'widget.descriptorTipoOrganoGrid',
    requires : [
        'goc.view.descriptor.GridTipoOrganoController'
    ],
    controller : 'gridTipoOrganoController',
    reference : 'descriptorTipoOrganoGrid',
    bind : {
        store : '{descriptoresTiposOrganoStore}',
        selection : '{selectedDescriptorTipoOrgano}'
    },
    name : 'descriptorTipoOrganoGrid',
    title : appI18N ? appI18N.tipoOrganos.restringirATipoOrganos : "Restringir a aquests tipus d'òrgans",
    scrollable : true,
    disabled : true,
    tbar : [
        {
            xtype : 'button',
            name : 'add',
            iconCls : 'fa fa-plus',
            text : appI18N ? appI18N.common.anadir : 'Afegir',
            handler : 'onAdd'
        },
        {
            xtype : 'button',
            iconCls : 'fa fa-remove',
            text : appI18N.common.borrar,
            handler : 'onDelete'
        }
    ],

    columns : [
        {
            text : appI18N.tipoOrganos.titulo,
            dataIndex : 'idTipoOrgano',
            flex : 1,
            renderer : function(value, cell)
            {
                var viewModel = this.getView().up('descriptorMainPanel').getViewModel();
                var tiposOrganoStore = viewModel.getStore('tipoOrganosStore');

                var record = tiposOrganoStore.findRecord('id', value, 0, false, false, true);
                return record ? record.get(appLang === alternativeLanguage ? 'nombreAlternativo' : 'nombre') : '';

            },
            editor : {
                xtype : 'combobox',
                allowBlank : false,
                emptyText : appI18N.cargos.seleccionaUnTipoDeOrgano,
                bind : {
                    store : '{tipoOrganosStore}'
                },
                triggerAction : 'all',
                displayField : (appLang === alternativeLanguage ? 'nombreAlternativo' : 'nombre'),
                valueField : 'id',
                editable : false
            }
        }
    ],

    listeners : {
        descriptorSelected : 'descriptorSelected'
    }
})
;
