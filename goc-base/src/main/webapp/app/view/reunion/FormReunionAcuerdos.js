Ext.define('goc.view.reunion.FormReunionAcuerdos',
    {
        extend: 'Ext.window.Window',
        xtype: 'formReunionAcuerdos',
        width: 640,
        manageHeight: true,
        modal: true,
        bodyPadding: 10,
        layout: {
            type: 'vbox',
            align: 'stretch'
        },

        requires: ['goc.view.reunion.FormReunionAcuerdosController'],
        controller: 'formReunionAcuerdosController',

        bbar: ['->',
            {
                xtype: 'button',
                text: appI18N.reuniones.firmarYCerrar,
                handler: 'onSaveRecord',
                bind: {
                    disabled: '{completada}'
                }
            },
            {
                xtype: 'panel',
                html: '<a style="text-decoration: none; color: #222;" href="#">' + appI18N.common.cancelar + '</a>',
                listeners: {
                    render: function (component) {
                        component.getEl().on('click', 'onClose');
                    }
                }
            }],

        bind: {
            title: '{title}'
        },

        items: [
            {
                xtype: 'form',
                name: 'formReunionAcuerdos',
                border: 0,
                layout: 'anchor',
                items: [
                    {
                        xtype: 'fieldset',
                        title: appI18N.reuniones.acuerdos,
                        defaultType: 'textfield',
                        defaults: {
                            anchor: '100%'
                        },

                        items: [
                            {
                                name: 'id',
                                xtype: 'hidden',
                                bind: '{reunionId}'
                            },
                            {
                                xtype: 'textareafield',
                                name: 'acuerdos',
                                flex: 1,
                                height: 320,
                                emptyText: appI18N.reuniones.acuerdos,
                                bind: {
                                    value: '{acuerdos}',
                                    disabled: '{completada}'
                                }
                            },
                            {
                                allowBlank: false,
                                name: 'responsable',
                                xtype: 'combo',
                                width: 120,
                                displayField: 'info',
                                valueField: 'id',
                                editable: false,
                                emptyText: appI18N.reuniones.responsableActa,
                                bind: {
                                    disabled: '{completada}',
                                    value: '{responsableId}',
                                    store: '{asistentesStore}'
                                }
                            }
                        ]
                    }]

            }]
    });
