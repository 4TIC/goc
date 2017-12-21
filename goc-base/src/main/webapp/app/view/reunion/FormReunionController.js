Ext.define('goc.view.reunion.FormReunionController', {
    extend : 'Ext.app.ViewController',
    alias : 'controller.formReunionController',
    onClose : function()
    {
        var win = Ext.WindowManager.getActive();
        var grid = Ext.ComponentQuery.query('grid[name=reunion]')[0];
        grid.getStore().reload();

        if (win)
        {
            win.destroy();
        }
    },

    onBorrarAsistenteReunion : function(record)
    {
        var vm = this.getViewModel();
        var store = vm.get('organosStore');
        store.remove(record);
    },

    getStoreDeMiembros : function(organoId, externo)
    {
        var vm = this.getViewModel();
        var miembrosStores = vm.get('miembrosStores');
        return miembrosStores[organoId + '_' + externo]
    },

    creaStoreDeMiembros : function(organoId, externo)
    {
        var vm = this.getViewModel();
        var miembrosStores = vm.get('miembrosStores');
        miembrosStores[organoId + '_' + externo] = Ext.create('goc.store.OrganoReunionMiembros');
    },

    onDetalleAsistentesReunion : function(organo, reunionId)
    {
        var vm = this.getViewModel();
        var reunion = vm.get('reunion');
        var view = this.getView();
        var remoteLoad = false;
        if (!this.getStoreDeMiembros(organo.get('id'), organo.get('externo')))
        {
            this.creaStoreDeMiembros(organo.get('id'), organo.get('externo'));
            remoteLoad = true;
        }

        var store = this.getStoreDeMiembros(organo.get('id'), organo.get('externo'));

        var formData = view.down('form').getValues();

        var modalDefinition = {
            xtype : 'formReunionMiembros',
            viewModel : {
                data : {
                    reunionId : reunionId,
                    reunionCompletada : reunionId ? reunion.get('completada') : false,
                    admiteSuplencia : formData.admiteSuplencia ? true : false,
                    admiteDelegacionVoto : formData.admiteDelegacionVoto ? true : false,
                    admiteComentarios : formData.admiteComentarios ? true : false,
                    organoId : organo.get('id'),
                    externo : organo.get('externo'),
                    store : store,
                    remoteLoad : remoteLoad
                }
            }
        };
        this.modal = view.add(modalDefinition);
        this.modal.show();
    },

    saveOrganosYInvitados : function(reunionId, organos, invitados, onClose)
    {
        var view = this.getView();

        var datosOrganos = this.buildDatosOrganos(organos);
        var datosInvitados = this.buildDatosInvitados(invitados);

        Ext.Ajax.request({
            url : '/goc/rest/reuniones/' + reunionId + '/organos',
            method : 'PUT',
            jsonData : {organos : datosOrganos},
            success : function()
            {
                Ext.Ajax.request({
                    url : '/goc/rest/reuniones/' + reunionId + '/invitados',
                    method : 'PUT',
                    jsonData : {invitados : datosInvitados},
                    success : function()
                    {
                        onClose();
                    },
                    failure : function()
                    {
                        view.setLoading(false);
                    }
                });
            },
            failure : function()
            {
                view.setLoading(false);
            }
        });
    },

    buildDatosInvitados : function(invitados)
    {
        var datosInvitados = [];

        invitados.each(function(record)
        {
            datosInvitados.push({
                personaId : record.get('personaId'),
                personaNombre : record.get('personaNombre'),
                personaEmail : record.get('personaEmail'),
                motivoInvitacion : record.get('motivoInvitacion')
            });
        });

        return datosInvitados;
    },

    buildDatosOrganos : function(organos)
    {
        var datosOrganos = [];
        var self = this;
        organos.each(function(record)
        {
            var miembros = [];
            var miembrosStore = self.getStoreDeMiembros(record.get('id'), record.get('externo'));
            if (miembrosStore)
            {
                miembrosStore.getData().each(function(record)
                {
                    miembros.push({
                        id : record.get('id'),
                        email : record.get('email'),
                        nombre : record.get('nombre'),
                        cargo : record.get('cargo'),
                        asistencia : record.get('asistencia'),
                        suplenteId : record.get('suplenteId'),
                        suplenteNombre : record.get('suplenteNombre'),
                        suplenteEmail : record.get('suplenteEmail')
                    });
                });
            }

            var data = {
                id : record.get('id'),
                externo : record.get('externo'),
                miembros : miembros
            };

            datosOrganos.push(data);
        });

        return datosOrganos;
    },

    onSaveRecord : function(button, context)
    {
        var form = Ext.ComponentQuery.query('form[name=reunion]')[0];

        if (form.isValid())
        {
            var data = form.getValues();
            var ref = this;

            if (Ext.Date.parse(data.fecha + ' ' + data.hora, 'd/m/Y H:i') < new Date())
            {
                Ext.Msg.confirm(appI18N.reuniones.fechaAnterior, appI18N.reuniones.fechaAnteriorAActual, function(result)
                {
                    if (result === 'yes')
                    {
                        ref.saveReunion();
                    }
                });
            } else
            {
                ref.saveReunion();
            }
        }
    },

    saveReunion : function(record, data, store)
    {
        var view = this.getView();
        var vm = this.getViewModel();
        var form = Ext.ComponentQuery.query('form[name=reunion]')[0];

        var organosStore = vm.get('organosStore');
        var invitadosStore = vm.get('reunionInvitadosStore');

        var record = vm.get('reunion');
        var data = form.getValues();
        var store = vm.get('store');

        view.setLoading(true);

        if (record.create !== true)
        {
            record.set('fecha', Ext.Date.parseDate(data.fecha + ' ' + data.hora, 'd/m/Y H:i'));
            record.set('fechaSegundaConvocatoria', Ext.Date.parseDate(data.fecha + ' ' + data.horaSegundaConvocatoria, 'd/m/Y H:i'));

            return record.save({
                success : function()
                {
                    this.saveOrganosYInvitados(data.id, organosStore.getData(), invitadosStore.getData(), this.onClose);
                },
                failure : function()
                {
                    view.setLoading(false);
                },
                scope : this
            });
        }

        record.fecha = Ext.Date.parseDate(data.fecha + ' ' + data.hora, 'd/m/Y H:i');
        record.fechaSegundaConvocatoria = Ext.Date.parseDate(data.fecha + ' ' + data.horaSegundaConvocatoria, 'd/m/Y H:i');

        store.add(record);
        store.sync({
            success : function()
            {
                this.saveOrganosYInvitados(record.id, organosStore.getData(), invitadosStore.getData(), this.onClose);
            },
            failure : function()
            {
                view.setLoading(false);
            },
            scope : this
        });
    },

    afterRenderFormReunion : function(windowFormReunion)
    {
        var height = Ext.getBody().getViewSize().height;
        if (windowFormReunion.getHeight() > height)
        {
            windowFormReunion.setHeight(height - 30);
            windowFormReunion.setPosition(windowFormReunion.x, 15);
        }
    }
});
