Ext.define('Ext.ux.uji.form.LookupComboBox',
{
    extend : 'Ext.form.ComboBox',
    alias : 'widget.lookupcombobox',

    mode : 'local',
    triggerAction : 'all',
    editable : false,
    valueField : 'id',
    displayField : 'nombre',
    store : {},

    extraFields : [],
    appPrefix : '',
    bean : 'base',
    windowTitle : 'Cercar registres',
    windowLayout : 'border',
    windowModal : true,
    windowHidden : true,
    windowWidth : 400,
    windowHeight : 400,
    windowCloseAction : 'hide',
    windowClearAfterSearch : false,

    listConfig : 
    {
        maxHeight: 0
    },

    initComponent : function()
    {
        this.store = Ext.create('Ext.data.ArrayStore',
        {
            fields : [ 'id', 'nombre' ].concat(this.extraFields),
            data : [ [ '', '' ] ]
        });

        this.on('expand', function(combo)
        {
            if (!this.lookupWindow)
            {
                this.lookupWindow = this.getLookupWindow(combo);
            }

            this.lookupWindow.show();
        });

        this.oldWidth = this.width;

        this.callParent(arguments);
    },

    executeSearch : function(grid, query)
    {
        grid.store.load(
        {
            query : query,
            bean : this.bean
        });
    },

    removeStore : function()
    {
        this.store.removeAll();
        this.clearValue();
        this.store.loadData([ [ '', '' ] ]);
    },

    loadRecord : function(id, nombre)
    {
        var Record = Ext.data.Record.create([ 'id', 'nombre' ].concat(this.extraFields));

        var record = new Record(
        {
            id : id,
            nombre : nombre

        }, id);

        this.store.removeAll();
        this.clearValue();

        this.store.add([ record ]);
        this.store.commitChanges();

        this.setValue(record.data.id);
    },

    getLookupWindow : function(combo)
    {
        var ref = this;
        var window = Ext.create('Ext.ux.uji.form.LookupWindow',
        {
            bean : ref.bean,
            appPrefix : ref.appPrefix,
            extraFields : ref.extraFields,
            width : ref.windowWidth,
            height : ref.windowHeight,
            modal : ref.windowModal,
            title : ref.windowTitle,
            layout : ref.windowLayout,
            hidden : ref.windowHidden,
            closeAction : ref.windowCloseAction,
            clearAfterSearch : ref.windowClearAfterSearch
        });

        window.on('LookoupWindowClickSeleccion', function(record)
        {
            combo.store.removeAll();
            combo.clearValue();

            combo.store.add([ record.data ]);
            combo.store.commitChanges();

            combo.setValue(record.data.id);
        });

        this.relayEvents(window, [ 'LookoupWindowClickSeleccion' ]);

        window.show();

        return window;
    }
});
