Ext.define('goc.view.reunion.GridDescriptoresOrdenDiaController', {
    extend: 'Ext.ux.uji.grid.PanelController',
    alias: 'controller.gridDescriptoresOrdenDiaController',
    descriptor: null,
    onLoad: function() {
    },

    onChangeDescriptor: function (combo, newValue, oldValue) {
        var value = parseInt(newValue);
        var vm = this.getViewModel(),
            clavesStore = vm.getStore('clavesStore');
        if(isNaN(value)){
            var record = this.getView().getSelectionModel().getSelection()[0];
            clavesStore.load({
                params: {
                    idDescriptor: record.get('idDescriptor')
                }
            });
        } else {
            clavesStore.load({
                params: {
                    idDescriptor: value
                }
            });
        }
    },

    initFilters: function() {
        var store = this.getStore('clavesStore');
        store.clearFilter();
    },

    onAdd: function() {
        this.initFilters();
        var grid = this.getView();
        var rec = Ext.create(grid.getStore().model.entityName, {id: null});
        grid.getStore().insert(0, rec);
        var editor = grid.plugins[0];
        editor.cancelEdit();
        editor.startEdit(rec, 0);
    },

    onEditComplete: function(editor, context) {
        var grid = this.getView(),
            vm = this.getViewModel(),
            descriptoresStore = vm.get('descriptoresStore'),
            clavesStore = vm.get('clavesStore'),
            record = grid.getView().getSelectionModel().getSelection()[0],
            idDescriptor = null,
            idClave = null;
        if(record.phantom) {
            idDescriptor = record.get('descriptor');
            idClave = record.get('clave');
        } else {
            var newIdDescriptor = parseInt(record.get('descriptor'));
            if(!isNaN(newIdDescriptor)){
                idDescriptor = newIdDescriptor;
            } else {
                idDescriptor = record.get('idDescriptor');
            }
            var newIdClave = parseInt(record.get('clave'));
            if(!isNaN(newIdClave)){
                idClave = newIdClave;
            } else {
                idClave = record.get('idClave');
            }

        }
        var descriptor = descriptoresStore.getById(idDescriptor),
            clave = clavesStore.getById(idClave);
        record.set('idDescriptor', idDescriptor);
        record.set('idClave', idClave);
        record.set('descriptor', descriptor.get('descriptor'));
        record.set('clave', clave.get('clave'));

        var model = grid.getStore().getModel();

        if (record.id === 'none')
        {
            model.getField('id').persist = false;
        }

        grid.getStore().sync();
        //grid.setSelection(null);

        model.getField('id').persist = true;
    },

    onBeforeEdit: function () {
        var vm = this.getViewModel(),
            clavesStore = vm.getStore('clavesStore'),
            record = this.getView().getSelectionModel().getSelection()[0];
        if(record != null) {
            clavesStore.load({
                params: {
                    idDescriptor: record.get('idDescriptor')
                }
            });
        }
    }
});
