Ext.define('Ext.ux.uji.grid.ForeignColumn',
    {
        extend : 'Ext.grid.column.Column',
        alias : 'widget.foreigncolumn',

        config :
        {
            header : '',
            property : null,
            dataIndex : null,
            model : null,
            modelWithNamespace : null,
            store : null,
            editable : false,
            allowBlank : true,
            valueField : 'id',
            displayField : 'nombre',
            valueNotFoundText : 'No se encuentra la propiedad'
        },

        initComponent : function()
        {
            this.initializeConfigValuesByConvention();

            if (this.editable)
            {
                this.setupEditor();
            }

            this.setupRenderer();

            this.callParent(arguments);
        },

        initializeConfigValuesByConvention : function()
        {
            if (!this.property)
            {
                this.property = '_' + this.dataIndex.slice(0, -2);
            }

            if (!this.model)
            {
                this.model = Ext.String.capitalize(this.dataIndex.slice(0, -2));
            }

            this.modelWithNamespace = this.getModelName(Ext.ux.uji.AppConfig.appName, this.model);
        },

        getModelName : function(appName, modelBaseName)
        {
            return appName + '.model.' + modelBaseName;
        },

        setupEditor : function()
        {
            var store = this.getStore();

            this.editor =
            {
                xtype : 'combobox',
                store : store,
                displayField : this.displayField,
                valueField : this.valueField,
                allowBlank : this.allowBlank,
                editable : true,
                mode : 'local'
            };
        },

        getStore : function()
        {
            var store = this.store || Ext.create('Ext.data.Store',
                    {
                        model : this.modelWithNamespace,
                        autoLoad : true,
                        proxy :
                        {
                            type : 'rest',
                            url : this.getProxyUrl(),

                            reader :
                            {
                                type : 'json',
                                successProperty : 'success',
                                rootProperty : 'data'
                            },

                            writer :
                            {
                                type : 'json'
                            }
                        }
                    });
            return store;
        },

        getProxyUrl : function()
        {
            return '/' + Ext.ux.uji.AppConfig.appName.toLowerCase() + '/rest/' + this.pluralize(this.model.toLowerCase());
        },

        setupRenderer : function()
        {
            var me = this;
            this.renderer = function(value, metadata, record)
            {
                var relatedModel = record.get(me.property);
                if (!relatedModel)
                {
                    return null;
                }
                if (typeof relatedModel[me.displayField] === 'undefined')
                {
                    return me.valueNotFoundText;
                }
                return relatedModel[me.displayField];
            };
        },

        pluralize : function(string)
        {
            if (this.esVocal(string.slice(-1)))
            {
                return string + 's';
            }
            return string + 'es';
        },

        esVocal : function(char)
        {
            return (/^[aeiou]$/i).test(char);
        }
    });