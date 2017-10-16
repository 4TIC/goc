Ext.define('goc.view.reunion.ReunionGridController', {
    extend : 'Ext.ux.uji.grid.PanelController',
    alias : 'controller.reunionGridController',

    onLoad : function()
    {
        var viewModel = this.getViewModel();
        viewModel.getStore('reunionesStore').load();
    },

    onAdd : function()
    {
        this.createModalReunion(null);
    },

    onEdit : function(grid, td, cellindex)
    {
        if (grid && grid.getHeaderCt)
        {
            var cell = grid.getHeaderCt().getHeaderAtIndex(cellindex);

            if (cell.getReference() === 'documentos')
            {
                return this.onAttachmentEdit();
            }
        }

        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];

        if (!record)
        {
            return Ext.Msg.alert(appI18N.common.edicionRegistro, appI18N.common.seleccionarParaEditarRegistro);
        }

        var ref = this;

        Ext.Ajax.request({
            url : '/goc/rest/reuniones/' + record.id,
            method : 'GET',
            success : function(data)
            {
                record.set(Ext.decode(data.responseText).data);

                ref.createModalReunion(record);
            }
        });
    },

    onEnviarConvocatoria : function()
    {
        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];

        if (!record)
        {
            return Ext.Msg.alert(appI18N.reuniones.enviarConvocatoria, appI18N.reuniones.seleccionarParaEnviarConvocatoria);
        }

        var mensaje = appI18N.reuniones.confirmacionEnvioMensaje;

        if (record.get('avisoPrimeraReunion') == 1)
        {
            mensaje = appI18N.reuniones.confirmacionEnvioMensajeYaEnviado;
        }

        Ext.Msg.confirm(appI18N.reuniones.confirmacionEnvioTitulo, mensaje, function(result)
        {
            if (result === 'yes')
            {
                Ext.Ajax.request(
                {
                    url : '/goc/rest/reuniones/' + record.get('id') + '/enviarconvocatoria',
                    method : 'PUT',
                    success : function(response)
                    {
                        var data = Ext.decode(response.responseText);
                        var mensajeRespuesta = (data.message && data.message.indexOf("appI18N") != -1) ? eval(data.message) : data.message;
                        Ext.Msg.alert(appI18N.reuniones.resultadoEnvioConvocatoriaTitle, mensajeRespuesta);
                    },
                    scope : this
                }
                );
            }
        });
    },

    onCompleted : function()
    {
        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];
        var viewport = this.getView().up('viewport');

        if (!record)
        {
            return Ext.Msg.alert(appI18N.reuniones.cerrarActa, appI18N.reuniones.seleccionarParaCerrarActa);
        }

        Ext.Ajax.request(
        {
            url : '/goc/rest/reuniones/' + record.get('id') + '/checktoclose',
            method : 'GET',
            success : function(response)
            {
                var data = Ext.decode(response.responseText);

                if (data.message)
                {
                    var mensajeRespuesta = (data.message && data.message.indexOf("appI18N") != -1) ? eval(data.message) : data.message;
                    Ext.Msg.alert(appI18N.reuniones.resultadoEnvioConvocatoriaTitle, mensajeRespuesta);
                    return;
                }

                var asistentesStore = Ext.create('goc.store.OrganoReunionMiembros', {
                    proxy : {
                        url : '/goc/rest/reuniones/' + record.get('id') + '/miembros'
                    },
                    autoLoad : true
                });

                this.modal = viewport.add({
                    xtype : 'formReunionAcuerdos',
                    viewModel : {
                        data : {
                            title : appI18N.reuniones.titleSingular + ': ' + record.get('asunto'),
                            reunionId : record.get('id'),
                            responsableId : record.get('miembroResponsableActaId'),
                            completada : record.get('completada'),
                            acuerdos : record.get('acuerdos'),
                            acuerdosAlternativos : record.get('acuerdosAlternativos'),
                            asistentesStore : asistentesStore
                        }
                    }
                });
                this.modal.show();
            },
            scope : this
        }
        );
    },

    onHojaFirmas : function()
    {
        var id = this.getView().getSelectedId();

        if (!id)
        {
            return Ext.Msg.alert(appI18N.reuniones.hojaFirmas, appI18N.reuniones.seleccionarParaHojaFirmas);
        }

        window.open('/goc/rest/reuniones/' + id + '/asistentes?lang=' + appLang, '_blank');
    },

    organoSelected : function(id, externo)
    {
        var grid = this.getView();

        if (id)
        {
            grid.getStore().load({
                params : {
                    organoId : id,
                    externo : externo
                }
            });
        }
        else
        {
            var comboTipoOrgano = grid.down('comboReunionTipoOrgano');
            grid.getStore().load({
                params : {
                    tipoOrganoId : comboTipoOrgano.getValue()
                }
            });
        }
    },

    filtraComboOrgano : function(tipoOrganoId)
    {
        var vm = this.getViewModel();
        var store = vm.getStore('organosStore');
        var filter = new Ext.util.Filter(
        {
            id : 'tipoOrganoId',
            property : 'tipoOrganoId',
            value : tipoOrganoId
        });

        store.addFilter(filter);
    },

    limpiaFiltrosComboOrgano : function()
    {
        var vm = this.getViewModel();
        var store = vm.getStore('organosStore');
        store.clearFilter();

        var grid = this.getView();

        var comboOrganos = grid.down('comboOrgano');
        comboOrganos.clearValue();
    },

    tipoOrganoSelected : function(id, externo)
    {
        var grid = this.getView();

        if (id)
        {
            grid.getStore().load({
                params : {
                    tipoOrganoId : id
                }
            });
            var comboOrganos = grid.down('comboOrgano');
            comboOrganos.clearValue();
            this.filtraComboOrgano(id);
        }
        else
        {
            grid.getStore().load();
            this.limpiaFiltrosComboOrgano();
        }
    },

    onAttachmentEdit : function()
    {
        var grid = this.getView();
        var record = grid.getView().getSelectionModel().getSelection()[0];
        var store = Ext.create('goc.store.ReunionDocumentos');
        var viewport = this.getView().up('viewport');

        if (!record)
        {
            return Ext.Msg.alert(appI18N.reuniones.documentacion, appI18N.reuniones.seleccionarParaDocumentacion);
        }

        this.modal = viewport.add({
            xtype : 'formDocumentacion',
            viewModel : {
                data : {
                    title : appI18N.reuniones.tituloDocumentacion + ': ' + record.get('asunto'),
                    id : record.get('id'),
                    completada : record.get('completada'),
                    store : store
                }
            }
        });
        this.modal.show();
    },

    getReunionModalDefinition : function(reunion, reunionesStore, organosStore, invitadosStore)
    {
        return {
            xtype : 'formReunion',
            viewModel : {
                data : {
                    title : reunion ? appI18N.reuniones.edicion + ': ' + reunion.get('asunto') : appI18N.reuniones.nuevaReunion,
                    id : reunion ? reunion.get('id') : undefined,
                    reunion : reunion || {
                        type : 'goc.model.Reunion',
                        create : true
                    },
                    store : reunionesStore,
                    organosStore : organosStore,
                    reunionInvitadosStore : invitadosStore,
                    organosMiembros : {},
                    miembrosStores : {}
                }
            }
        };
    },

    createModalReunion : function(record)
    {
        var viewModel = this.getViewModel();
        var store = viewModel.getStore('reunionesStore');
        var organosStore = Ext.create('goc.store.Organos');
        var invitadosStore = Ext.create('goc.store.ReunionInvitados');
        var viewport = this.getView().up('viewport');
        var ref = this;

        if (record)
        {
            organosStore.load({
                params : {
                    reunionId : record ? record.get('id') : null
                }
            });

            invitadosStore.proxy.url = '/goc/rest/reuniones/' + record.get('id') + '/invitados'

            organosStore.on("load", function()
            {
                invitadosStore.load();
            });

            invitadosStore.on("load", function()
            {
                var modalDefinition = ref.getReunionModalDefinition(record, store, organosStore, invitadosStore);
                ref.modal = viewport.add(modalDefinition);
                ref.modal.down('textfield[name=urlGrabacion]').setVisible(true);
                ref.modal.show();
            });
        } else
        {
            var modalDefinition = this.getReunionModalDefinition(record, store, organosStore, invitadosStore);
            this.modal = viewport.add(modalDefinition);
            this.modal.show();
        }
    },

    reunionSelected : function()
    {
        var grid = this.getView();
        var record = grid.getSelectedRow();

        var gridPuntos = Ext.ComponentQuery.query('grid[name=ordenDia]')[0];

        if (!record)
        {
            gridPuntos.clearStore();
            gridPuntos.disable();
            return;
        }

        gridPuntos.fireEvent('reunionSelected', record.get('id'));
    }
});
