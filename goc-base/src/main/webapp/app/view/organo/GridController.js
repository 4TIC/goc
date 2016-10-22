Ext.define('goc.view.organo.GridController', {
    extend: 'Ext.ux.uji.grid.PanelController',
    alias: 'controller.organoGridController',
    onLoad: function() {
        var comboEstado = this.getView().up('organoMainPanel').down('comboEstadoOrgano');
        comboEstado.setValue(false);

        var viewModel = this.getViewModel();
        viewModel.getStore('organosStore').load();
        viewModel.getStore('tipoOrganosStore').load({
            callback: function() {
                var grid = this.getView();
                grid.getView().refresh();
            },
            scope: this
        });
    },
    decideRowIsEditable: function(editor, context) {
        return context.record.get('externo') !== 'true' || context.record.phantom;
    },

    organoSelected: function(controller, record) {
        var grid = this.getView();
        var toolbar = grid.down("toolbar");
        toolbar.items.each(function(button) {
            if (button.name !== 'add') {
                button.setDisabled(record.get("externo") === "true");
            }
        });

        var record = grid.getView().getSelectionModel().getSelection()[0];
        grid.up('panel').down('grid[name=autorizadoGrid]').fireEvent('organoSelected', record);

    },

    initFilters: function() {
        var grid = this.getView();
        var comboEstado = grid.up('organoMainPanel').down('comboEstadoOrgano');
        var comboTipoOrgano = grid.up('organoMainPanel').down('comboTipoOrgano');
        comboTipoOrgano.clearValue();
        comboEstado.setValue(false);

        var vm = this.getViewModel();
        var store = this.getStore('organosStore');
        store.clearFilter();
    },

    onAdd: function() {
        this.initFilters();
        var grid = this.getView();
        var rec = Ext.create(grid.getStore().model.entityName, { id : null });
        grid.getStore().insert(0, rec);
        var editor = grid.plugins[0];
        editor.cancelEdit();
        editor.startEdit(rec, 0);
    },


    onToggleEstado: function() {
        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];

        if (record.phantom === true) {
            return grid.getStore().remove(records);
        }

        if (record.get('inactivo') === false)
        {
            return Ext.Msg.confirm(appI18N.organos.inhabilitar, appI18N.organos.inhabilitarOrgano, function(btn, text)
            {
                if (btn == 'yes')
                {
                    record.set('inactivo', true);
                    grid.getStore().sync({
                        success: function() {
                            grid.getSelectionModel().clearSelections();
                            grid.getView().refresh();
                        }
                    });
                }
            });
        }

        record.set('inactivo', false);
        grid.getStore().sync({
            success: function() {
                grid.getSelectionModel().clearSelections();
                grid.getView().refresh();
            }
        });

    }
});
