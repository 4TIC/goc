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
        grid.up('panel').down('grid[name=autorizadoGrid]').fireEvent('organoSelected', record.get('id'), record.get('externo'));

    },

    onToggleEstado: function() {
        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];

        if (record.phantom === true) {
            return grid.getStore().remove(records);
        }

        if (record.get('inactivo') === false)
        {
            return Ext.Msg.confirm('Esborrar', 'Esteu segur/a de voler inhabilitar el òrgan ?', function(btn, text)
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
