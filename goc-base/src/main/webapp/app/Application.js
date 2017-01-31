Ext.Loader.setPath('Ext.ux', '//static.uji.es/js/extjs/ext-6.0.3.64/packages/ux/classic/src');
Ext.Loader.setPath('Ext.ux.uji', 'uji');
Ext.Loader.setPath('goc', 'app');

var appI18N;
Ext.Ajax.request(
{
    url : '//static.uji.es/js/extjs/ext-6.0.3.64/build/classic/locale/locale-' + appLang + '.js',
    success : function(response)
    {
        eval(response.responseText);
        Ext.Ajax.request(
        {
            url : 'app/i18n/' + appLang + '.json',
            success : function(response)
            {

                appI18N = Ext.decode(response.responseText);

                Ext.require('Ext.ux.uji.data.Store');
                Ext.require('Ext.ux.TabCloseMenu');
                Ext.require('Ext.ux.form.SearchField');
                Ext.require('Ext.ux.uji.ApplicationViewport');
                Ext.require('goc.view.dashboard.PanelDashboard');
                Ext.require('goc.view.tipoOrgano.Main');
                Ext.require('goc.view.organo.Main');
                Ext.require('goc.view.cargo.Main');
                Ext.require('goc.view.miembro.Main');
                Ext.require('goc.view.reunion.Main');
                Ext.require('goc.view.historicoReunion.Main');
                Ext.require('goc.view.common.LookupWindowPersonas');

                Ext.ariaWarn = Ext.emptyFn;

                Ext.define('Overrides.form.field.Base',
                {
                    override : 'Ext.form.field.Base',

                    getLabelableRenderData : function()
                    {
                        var me = this, data = me.callParent(), labelSeparator = me.labelSeparator;

                        if (!me.allowBlank)
                        {
                            data.labelSeparator = labelSeparator + ' <span style="color:red">*</span>';
                        }

                        return data;
                    }
                });

                Ext.application(
                {
                    extend : 'Ext.app.Application',

                    name : 'goc',
                    title : 'goc',

                    requires : [ 'goc.model.Organo' ],

                    launch : function()
                    {
                        var viewport = Ext.create('Ext.ux.uji.ApplicationViewport',
                        {
                            codigoAplicacion : 'GOC',
                            tituloAplicacion : appI18N.header.titulo,
                            dashboard : true
                        });
                    }
                });
            }
        });
    }
});
