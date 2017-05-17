Ext.define('goc.view.miembro.Grid', {
    extend: 'Ext.ux.uji.grid.Panel',
    alias: 'widget.miembroGrid',

    requires: [
        'goc.view.miembro.GridController'
    ],

    controller: 'miembroGridController',
    bind: {
        store: '{miembrosStore}'
    },

    title: appI18N.miembros.tituloGrid,
    scrollable: true,
    flex: 1,

    columns: [
        {
            text: appI18N.miembros.nombre,
            dataIndex: 'nombre',
            flex: 2,
            editor: {
                field: {
                    allowBlank: false
                }
            }
        },
        {
            text: appI18N.miembros.correo,
            dataIndex: 'email',
            flex: 1,
            editor: {
                field: {
                    allowBlank: false
                }
            }
        },
        {
            text: appI18N.miembros.cargo,
            dataIndex: 'cargoId',
            flex: 1,
            renderer: function(value, cell) {
                var viewModel = this.getView().up('miembroMainPanel').getViewModel();
                var cargosStore = viewModel.getStore('cargosStore');

                var record = cargosStore.findRecord('id', value, 0, false, false, true);
                return record ? record.get(appLang === alternativeLanguage ? 'nombreAlternativo' : 'nombre') : '';
            },
            editor: {
                xtype: 'combobox',
                allowBlank: false,
                emptyText: 'Sel·lecciona un càrrec',
                bind: {
                    store: '{cargosStore}'
                },
                triggerAction: 'all',
                displayField: (appLang === alternativeLanguage ? 'nombreAlternativo' : 'nombre'),
                valueField: 'id',
                editable: false,
                listeners: {
                    expand: function() {
                        this.getStore().reload();
                    }
                }
            }
        }
    ],
    listeners: {
        beforeedit: 'decideRowIsEditable'
    }
});
