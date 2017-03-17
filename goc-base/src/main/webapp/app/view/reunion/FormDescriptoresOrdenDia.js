Ext.define('goc.view.reunion.FormDescriptoresOrdenDia', {
    extend: 'Ext.window.Window',
    xtype: 'formDescriptoresOrdenDia',

    width: 640,
    height: 800,
    modal: true,
    bodyPadding: 10,
    autoScroll: true,
    y: -60,

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    requires: [
        'goc.view.reunion.FormDescriptoresOrdenDiaController'
    ],
    controller: 'formDescriptoresOrdenDiaController',

    bbar: [
        '->', {
            xtype: 'panel',
            html: '<a style="text-decoration: none; color: #222;" href="#">' + appI18N.common.cerrar + '</a>',
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
                {
                    xtype: 'gridDescriptoresOrdenDia'
                }
            ]
        }],
        listeners: {
            render: 'onLoad'
        }
    }]
});
