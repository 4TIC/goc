Ext.define('Ext.ux.uji.form.model.Lookup',
{
    extend : 'Ext.data.Model',
    fields : [ 'id', 'nombre',
    {
        model : 'Ext.ux.uji.form.model.ExtraParam',
        name : 'extraParam'
    } ]
});
