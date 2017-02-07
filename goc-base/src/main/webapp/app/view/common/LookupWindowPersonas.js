Ext.define('goc.view.common.LookupWindowPersonas',
    {
        extend: 'Ext.Window',

        alias: 'widget.lookupWindow',
        appPrefix: '',
        bean: 'base',
        lastQuery: '',
        queryField: '',
        formularioBusqueda: '',
        gridBusqueda: '',
        botonBuscar: '',
        botonCancelar: '',
        title : appI18N ? appI18N.common.buscarRegistros : 'Cercar registres',
        layout: 'border',
        modal: true,
        hidden: true,
        width: 400,
        height: 400,
        closeAction: 'hide',
        clearAfterSearch: true,

        initComponent: function () {
            this.callParent(arguments);
            this.initUI();
            this.add(this.formularioBusqueda);
            this.add(this.gridBusqueda);
        },

        initUI: function () {
            this.buildQueryField();
            this.buildBotonBuscar();
            this.buildFormularioBusqueda();
            this.buildBotonCancelar();
            this.buildBotonSeleccionar();
            this.buildGridBusqueda();
        },

        executeSearch: function (query) {
            this.gridBusqueda.store.load(
                {
                    params: {
                        query: query,
                        bean: this.bean
                    }
                });
        },

        buildQueryField: function () {
            var ref = this;
            this.queryField = Ext.create('Ext.form.TextField',
                {
                    name: 'query',
                    margin: 10,
                    text : (appI18N ? appI18N.common.expresion : 'Expressió') + ': ',
                    value: '',
                    listeners: {
                        specialkey: function (field, e) {
                            if (e.getKey() == e.ENTER) {
                                ref.botonBuscar.handler.call(ref.botonBuscar.scope);
                            }
                        }
                    }
                });
        },

        buildBotonBuscar: function () {
            var ref = this;
            this.botonBuscar = Ext.create('Ext.Button',
                {
                    text: (appI18N ? appI18N.common.buscarRegistros : 'Cerca'),
                    margin: 10,
                    handler: function (boton, event) {
                        if (ref.queryField.getValue().length < 3) {
                            Ext.Msg.alert("Error", appI18N ? appI18N.common.errorBusqueda : 'Per fer una cerca cal introduïr al menys 3 caracters.');
                        }
                        else {
                            ref.lastQuery = ref.queryField.getValue();
                            ref.executeSearch(ref.queryField.getValue());
                        }
                    }
                });
        },

        buildFormularioBusqueda: function () {
            this.formularioBusqueda = Ext.create('Ext.Panel',
                {
                    layout: 'hbox',
                    region: 'north',
                    items: [this.queryField, this.botonBuscar]
                });
        },

        buildBotonCancelar: function () {
            var ref = this;

            this.botonCancelar = Ext.create('Ext.Button',
                {
                    text : appI18N ? appI18N.common.cancelar : 'Cancel.lar',
                    handler: function (e) {
                        ref.hide();
                    }
                });
        },

        buildBotonSeleccionar: function () {
            var ref = this;

            this.botonSeleccionar = Ext.create('Ext.Button',
                {
                    text : appI18N ? appI18N.common.seleccionar : 'Seleccionar',
                    handler: function (e) {
                        var record = ref.gridBusqueda.getSelectionModel().getSelection()[0];

                        if (record) {
                            ref.fireEvent('LookoupWindowClickSeleccion', record);

                            if (ref.clearAfterSearch) {
                                var query = ref.queryField;
                                query.setValue('');
                                ref.gridBusqueda.store.removeAll(true);
                                ref.gridBusqueda.getView().refresh();
                            }
                            ref.hide();
                        }
                    }
                });
        },

        buildGridBusqueda: function () {
            var ref = this;

            var resultColumnList = [
                {
                    header: 'Codi',
                    width: 50,
                    dataIndex: 'id',
                    hidden: true
                },
                {
                    header : appI18N ? appI18N.common.nombre : 'Nom',
                    width: 200,
                    dataIndex: 'nombre'
                }];

            this.gridBusqueda = Ext.create('Ext.grid.Panel',
                {
                    region: 'center',
                    flex: 1,
                    frame: true,
                    loadMask: true,
                    store: Ext.create('Ext.data.Store',
                        {
                            model: 'goc.model.Persona',
                            autoSync: false,

                            proxy: {
                                type: 'ajax',
                                url: '/goc/rest/external/personas',

                                reader: {
                                    type: 'json',
                                    rootProperty: 'data'
                                }
                            },
                            listeners: {
                                load: function (store, records, successful, eOpts) {
                                    if (ref.gridBusqueda.store.data.length === 0) {
                                        Ext.Msg.alert(appI18N ? appI18N.common.aviso : "Aviso",
                                            appI18N ? appI18N.common.sinResultados : "La búsqueda realitzada no ha produït cap resultat");
                                    }
                                }
                            }
                        }),

                    columns: resultColumnList,
                    forceFit: true,
                    listeners: {
                        celldblclick: function (grid, td, cellindex, record) {
                            ref.botonSeleccionar.handler.call(ref.botonSeleccionar.scope);
                        }
                    },
                    buttons: [this.botonSeleccionar, this.botonCancelar]

                });
        },

        onEsc: function () {
            this.botonCancelar.handler.call(this.botonCancelar.scope);
        },

        listeners: {
            'show': function (window) {
                window.queryField.focus(true, 200);
            }
        }
    });