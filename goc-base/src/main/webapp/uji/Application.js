Ext.define('Ext.ux.uji.Application',
{
    extend : 'Ext.app.Application',

    init : function()
    {
        this.addForeignColumnsToModels();

        var me = this;
        Ext.define('Ext.ux.uji.AppConfig',
        {
            alias : 'widget.AppConfig',
            singleton : true,
            appName : me.name
        });
    },

    addForeignColumnsToModels : function()
    {
        var me = this;
        Ext.each(this.models, function(modelName)
        {
            var model = Ext.ModelManager.getModel(me.name + '.model.' + modelName), fields = [];
            if (model)
            {
                Ext.each(model.getFields(), function(field)
                {
                    if (field.foreign)
                    {
                        fields.push(me.createMappedField(field));
                        field.defaulValue = field.defaulValue || {};
                    }
                    fields.push(field);
                });
                model.setFields(fields);
            }
        });
    },

    createMappedField : function(field)
    {
        return Ext.create('Ext.data.Field',
        {
            name : field.name + 'Id',
            type : 'int',
            mapping : field.name + '.id',
            useNull : field.useNull
        });
    }

});