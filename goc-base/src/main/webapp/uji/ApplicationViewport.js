Ext.require('Ext.container.Viewport');
Ext.require('Ext.layout.container.Border');
Ext.require('Ext.ux.uji.TabPanel');

Ext.define('Ext.ux.uji.ApplicationViewport',
{
    extend : 'Ext.Viewport',
    alias : 'widget.applicationViewport',

    layout : 'border',
    treeWidth : 235,
    aplicacionCompleta : true,
    tabPanel : {},

    initComponent : function()
    {
        this.callParent(arguments);

        if (this.aplicacionCompleta)
        {
            this.buildLogoPanel();
            this.buildNavigationTree();
            this.buildWorkTabPanel();
        }

        this.initGlobalAjaxEvents();
        this.buildLoadingIndicator();

        this.i18n();
    },

    buildLogoPanel : function()
    {
        var langSelector = '<select ';

        var htmlLanguage = '<ul class="lang" />';

        if (mainLanguage && alternativeLanguage) {
            htmlLanguage = '<ul class="lang">' +
            '<li><a href="?lang=' + mainLanguage + '">' + mainLanguageDescription + '</a></li><li><a href="?lang=' + alternativeLanguage + '">' + alternativeLanguageDescription + '</a></li></ul>';
        }

        var logoPanel = new Ext.Panel(
            {
                region: 'north',
                layout: 'border',
                height: 70,
                items: [
                    {
                        region: 'center',
                        border: 0,
                        html: '<div class="header-center">' +
                            '<img src="'  + logo + '" class="header-logo" />' +
                            '<div class="header-title">' +
                            '<span class="header-title-def">' + this.tituloAplicacion + '</span></div></div>'
                    },
                    {
                        region: 'east',
                        border: 0,
                        width: 150,
                        html: htmlLanguage
                    },
                    {
                        region: 'east',
                        border: 0,
                        width: 140,
                        html: '<div class="header-right">' +
                            '<span class="header-right-title"><img src="//static.uji.es/js/extjs/uji-commons-extjs/img/lock.png"/>' +
                            '<a style="color:inherit;" href="//ujiapps.uji.es/goc/saml/logout">' + appI18N.common.desconectar + '</a></span></div>'
                    }
                ]
            }
        );

        this.add(logoPanel);
    },

    buildNavigationTree : function()
    {
        var me = this;

        var navigationTree = Ext.create('Ext.tree.Panel',
        {
            title : appI18N.common.conectadoComo + ' ' + login + '@',
            region : 'west',
            lines : false,
            width : this.treeWidth,
            split : true,
            collapsible : true,
            autoScroll : true,
            rootVisible : false,
            bodyStyle : 'padding-bottom:20px;',
            store : Ext.create('Ext.data.TreeStore',
            {
                autoLoad : true,

                root :
                {
                    expanded : true
                },

                proxy :
                {
                    type : 'ajax',
                    url : '/goc/rest/external/config/menus/?lang=' + appLang ,
                    reader :
                    {
                        type : 'json',
                        rootProperty : 'row'
                    }
                }
            }),
            listeners: {
                cellclick: this.addNewTab
            }
        });

        this.add(navigationTree);
    },

    buildWorkTabPanel : function()
    {
        this.tabPanel = Ext.create('Ext.ux.uji.TabPanel',
        {
            deferredRender : false,
            region : 'center'
        });

        if (this.dashboard)
        {
            eval('this.tabPanel.addTab(Ext.create("' + this.codigoAplicacion.toLowerCase() + '.view.dashboard.PanelDashboard", { closable: false }));'); // jshint ignore:line
        }

        this.add(this.tabPanel);
    },

    addNewTab : function(treePanel, td, cellIndex, record, index, config)
    {
        var id = record.id;

        var viewport = this.up("viewport");
        var activeTab = viewport.tabPanel.items.findBy(function(i) {
            return i instanceof eval(id);
        });

        if (activeTab) {
            return viewport.tabPanel.setActiveTab(activeTab);
        }

        var params = Ext.encode(config || {});
        var newPanel = eval("Ext.create('" + id + "', " + params + ")"); // jshint ignore:line
        activeTab = viewport.tabPanel.add(newPanel);
        viewport.tabPanel.setActiveTab(activeTab)
        newPanel.on('newtab', function(config)
        {
            if (config && config.pantalla)
            {
                var params = Ext.util.JSON.encode(config || {});
                var newPanel = eval("Ext.create('" + config.pantalla + "', " + params + ")"); // jshint ignore:line
                viewport.tabPanel.addTab(newPanel);
            }
            else
            {
                alert('[ApplicationViewport.js] ¡Atención!.' +
                      'El parámetro "pantalla" (newtab) con' +
                      'el nombre del componente que se quiere' +
                      'instanciar debe estar definido. Por ejemplo:' +
                      '"pantalla : \'UJI.XX.GestionXXXPanel\'"');
            }
        }, this);
    },

    i18n : function()
    {
        Ext.grid.RowEditor.prototype.cancelBtnText = appI18N ? appI18N.common.cancelar : "Cancel·lar";
        Ext.grid.RowEditor.prototype.saveBtnText = appI18N ? appI18N.common.actualizar : "Actualitzar";

        Ext.MessageBox.buttonText.yes = "Sí";
        Ext.MessageBox.buttonText.ok = appI18N ? appI18N.common.aceptar : "Acceptar";
        Ext.MessageBox.buttonText.cancel = appI18N ? appI18N.common.cancelar : "Cancel·lar";
    },

    initGlobalAjaxEvents : function()
    {
        Ext.Ajax.on('beforerequest', function()
        {
            Ext.getCmp('loadingIndicator').show();
        });

        Ext.Ajax.on('requestcomplete', function(conn, response, options)
        {
            Ext.getCmp('loadingIndicator').hide();

            if (options.isUpload)
            {
                var responseJSON = Ext.decode(response.responseText);
                var msgList = responseJSON.msg || responseJSON.message;

                if (msgList)
                {
                    Ext.MessageBox.show(
                    {
                        title : 'Error',
                        msg : msgList,
                        buttons : Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
            }
        });

        Ext.Ajax.on('requestexception', function(conn, response, options)
        {
            Ext.getCmp('loadingIndicator').hide();

            if (response.responseText)
            {
                var responseJSON = Ext.decode(response.responseText);
                var msgList = responseJSON.msg || responseJSON.message;

                if (msgList)
                {
                    if (msgList.indexOf("appI18N") != -1)
                        alert(eval(msgList));
                    else
                        alert(msgList);
                }
            }
        });
    },

    buildLoadingIndicator : function()
    {
        new Ext.Panel(
        {
            xtype : 'panel',
            id : 'loadingIndicator',
            frame : false,
            border : false,
            html : '<div style="font:normal 11px tahoma,arial,helvetica,sans-serif;border:1px solid gray;padding:8px;background-color:#fff;">' +
                   '<img style="margin-right:4px;" align="left" src="//static.uji.es/img/commons/loading.gif" /> Carregant...</div>',
            hidden : true,
            style : 'z-index: 80000; position:absolute; top:5px; right:5px;',
            renderTo : document.body,
            width : 120
        });

    }
});
