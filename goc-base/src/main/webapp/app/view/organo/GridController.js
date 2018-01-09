Ext.define('goc.view.organo.GridController', {
    extend : 'Ext.ux.uji.grid.PanelController',
    alias : 'controller.organoGridController',
    onLoad : function()
    {
        var viewModel = this.getViewModel();
        viewModel.getStore('organosStore').load();
        viewModel.getStore('tipoOrganosStore').load({
            callback : function()
            {
                var grid = this.getView();
                grid.getView().refresh();
            },
            scope : this
        });
    },

    afterLoad : function()
    {
        var comboEstado = this.getView().up('organoMainPanel').down('comboEstadoOrgano');
        comboEstado.setValue(false);
    },

    decideRowIsEditable : function(editor, context)
    {
        return context.record.get('externo') !== 'true' || context.record.phantom;
    },

    organoSelected : function(controller, record)
    {
        var grid = this.getView();
        var toolbar = grid.down("toolbar");

        var recordModel = grid.getSelectedRow();
        var gridAutorizados = grid.up('panel').down('grid[name=autorizadoGrid]');
        var gridInvitados = grid.up('panel').down('grid[name=organoInvitadoGrid]');
        var tab = this.getView().up('panel').down('tabpanel');

        if (!recordModel)
        {
            gridAutorizados.clearStore();
            gridInvitados.clearStore();
            tab.disable();

            toolbar.items.each(function(button)
            {
                if (button.name !== 'add')
                {
                    button.setDisabled();
                }
            });

            return;
        }

        toolbar.items.each(function(button)
        {
            if (button.name !== 'add')
            {
                button.setDisabled(record[0].get("externo") === "true");
            }
        });

        var selection = grid.getView().getSelectionModel().getSelection();

        var record = selection[selection.length - 1];
        gridAutorizados.fireEvent('organoSelected', record);
        gridInvitados.fireEvent('organoSelected', record);

        if (record.get('inactivo'))
        {
            return tab.disable();
        }

        tab.enable();
    },

    initFilters : function()
    {
        var grid = this.getView();
        var comboEstado = grid.up('organoMainPanel').down('comboEstadoOrgano');
        var comboTipoOrgano = grid.up('organoMainPanel').down('comboTipoOrgano');
        comboTipoOrgano.clearValue();
        comboEstado.setValue(false);

        var vm = this.getViewModel();
        var store = this.getStore('organosStore');
        store.clearFilter();

        grid.up('organoMainPanel').fireEvent('filtrarOrganos', false);
    },

    onAdd : function()
    {
        this.initFilters();
        this.callParent();
    },

    cancelEdit : function()
    {
        var editor = this.getView().plugins[0];
        editor.cancelEdit();
    },

    onToggleEstado : function()
    {
        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];

        if (record.phantom === true)
        {
            return grid.getStore().remove(records);
        }

        this.cancelEdit();

        if (record.get('inactivo') === false)
        {
            return Ext.Msg.confirm(appI18N.organos.inhabilitar, appI18N.organos.inhabilitarOrgano, function(btn, text)
            {
                if (btn == 'yes')
                {
                    record.set('inactivo', true);
                    grid.getStore().sync({
                        success : function()
                        {
                            grid.getSelectionModel().clearSelections();
                            grid.getView().refresh();
                            grid.up('panel').down('grid[name=autorizadoGrid]').clearStore();
                            grid.up('panel').down('grid[name=organoInvitadoGrid]').clearStore();
                        }
                    });
                }
            });
        }

        record.set('inactivo', false);
        grid.getStore().sync({
            success : function()
            {
                grid.getSelectionModel().clearSelections();
                grid.getView().refresh();
                grid.up('panel').down('grid[name=autorizadoGrid]').clearStore();
                grid.up('panel').down('grid[name=organoInvitadoGrid]').clearStore();
            }
        });
    }
})
;
