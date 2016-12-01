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

        var htmlLanguage = '<ul style="background: url(http://e-ujier.uji.es/img/portal2/imagenes/cabecera_1px.png) repeat-x scroll left top transparent; height: 70px; padding-top: 35px;" class="lang" />';

        debugger;

        if (mainLanguage && alternativeLanguage) {
            htmlLanguage = '<ul style="background: url(http://e-ujier.uji.es/img/portal2/imagenes/cabecera_1px.png) repeat-x scroll left top transparent; height: 70px; padding-top: 35px;" class="lang">' +
            '<li><a href="?lang=' + mainLanguage + '">' + mainLanguageDescription + '</a></li><li><a href="?lang=' + alternativeLanguage + '">' + alternativeLanguageDescription + '</a></li></ul>';
        }

        console.log(mainLanguage, alternativeLanguage, htmlLanguage);

        var logoPanel = new Ext.Panel(
            {
                region: 'north',
                layout: 'border',
                height: 70,
                items: [
                    {
                        region: 'center',
                        border: 0,
                        html: '<div style="background: url(http://e-ujier.uji.es/img/portal2/imagenes/cabecera_1px.png) repeat-x scroll left top transparent; height: 70px;">' +
                            '<img src="'  + logo + '" style="float: left;margin: 10px 16px;" />' +
                            '<div style="float:left; margin-top:24px;">' +
                            '<span style="color: #CDCCE5; font-family: Helvetica,Arial,sans-serif; font-size:2em;">' + this.tituloAplicacion + '</span></div></div>'
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
                        html: '<div style="background: url(http://e-ujier.uji.es/img/portal2/imagenes/cabecera_1px.png) repeat-x scroll left top transparent; height: 70px; padding-top: 35px">' +
                            '<span style="color: #CDCCE5; font-family: Helvetica,Arial,sans-serif;"><img src="http://static.uji.es/js/extjs/uji-commons-extjs/img/lock.png"/>' + 
                            '<a style="color:inherit;" href="http://xmlrpc.uji.es/lsm/logout_sso.php">' + appI18N.common.desconectar + '</a></span></div>'
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
        Ext.MessageBox.buttonText.ok = "Acceptar";
        Ext.MessageBox.buttonText.cancel = "Cancel·lar";
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
                   '<img style="margin-right:4px;" align="left" src="http://static.uji.es/img/commons/loading.gif" /> Carregant...</div>',
            hidden : true,
            style : 'z-index: 80000; position:absolute; top:5px; right:5px;',
            renderTo : document.body,
            width : 120
        });

    }
});
