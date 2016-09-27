Ext.define('goc.view.reunion.FormOrdenDia', {
    extend: 'Ext.window.Window',
    xtype: 'formOrdenDia',
    width: 640,
    manageHeight: true,
    modal: true,
    bodyPadding: 10,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    requires: [
        'goc.view.reunion.FormOrdenDiaController'
    ],
    controller: 'formOrdenDiaController',

    bbar: [
        '->', {
            xtype: 'button',
            bind: {
                disabled: '{reunionCompletada}'
            },
            text: appI18N.reuniones.guardar,
            handler: 'onSaveRecord'
        }, {
            xtype: 'panel',
            html: '<a style="text-decoration: none; color: #222;" href="#">' + appI18N.common.cancelar + '</a>',
            listeners: {
                render: function (component) {
                    component.getEl().on('click', 'onCancel');
                }
            }
        }],

    bind: {
        title: '{title}'
    },

    items: [{
        xtype: 'form',
        name: 'puntoOrdenDia',
        border: 0,
        layout: 'anchor',
        items: [{
            xtype: 'fieldset',
            title: appI18N.reuniones.informacion,
            defaultType: 'textfield',
            defaults: {
                anchor: '100%'
            },

            items: [
                {name: 'id', xtype: 'hidden', bind: '{puntoOrdenDia.id}'},
                {
                    allowBlank: false,
                    fieldLabel: appI18N.reuniones.titulo,
                    name: 'titulo',
                    emptyText: 'Títol',
                    bind: {
                        value: '{puntoOrdenDia.titulo}',
                        disabled: '{reunionCompletada}'
                    }
                },
                {
                    xtype: 'container',
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'tbfill',
                            flex: 1
                        },
                        {
                            boxLabel: appI18N.reuniones.publico,
                            name: 'publico',
                            bind: {
                                disabled: '{reunionCompletada}',
                                value: '{puntoOrdenDia.publico}'
                            },
                            xtype: 'checkbox',
                            inputValue: '1'
                        }]
                },
                {
                    xtype: 'textareafield',
                    name: 'descripcion',
                    fieldLabel: appI18N.reuniones.descripcion,
                    allowBlank: false,
                    labelAlign: 'top',
                    flex: 1,
                    emptyText: 'descripció',
                    bind: {
                        value: '{puntoOrdenDia.descripcion}',
                        disabled: '{reunionCompletada}'
                    }
                },
                {
                    xtype: 'textareafield',
                    name: 'deliberaciones',
                    fieldLabel: appI18N.reuniones.deliberaciones,
                    labelAlign: 'top',
                    flex: 1,
                    emptyText: 'deliberacions',
                    bind: {
                        value: '{puntoOrdenDia.deliberaciones}',
                        disabled: '{reunionCompletada}'
                    }
                },
                {
                    xtype: 'textareafield',
                    name: 'acuerdos',
                    fieldLabel: appI18N.reuniones.acuerdos,
                    labelAlign: 'top',
                    flex: 1,
                    emptyText: 'acords',
                    bind: {
                        value: '{puntoOrdenDia.acuerdos}',
                        disabled: '{reunionCompletada}'
                    }
                }
            ]
        }]
    }]
});
