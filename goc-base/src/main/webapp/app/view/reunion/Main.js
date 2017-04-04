Ext.define('goc.view.reunion.Main',
{
    extend : 'Ext.panel.Panel',
    alias : 'widget.reunionMain',
    name : 'reuniones',
    viewModel : {
        type : 'reunionViewModel'
    },
    requires : [
        'goc.view.reunion.ViewModel', 'goc.view.reunion.ReunionGrid', 'goc.view.reunion.OrdenDiaGrid',
        'goc.view.reunion.FormReunion', 'goc.view.reunion.FormOrdenDia',
        'goc.view.reunion.FormReunionAcuerdos', 'goc.view.reunion.FormDocumentacion',
        'goc.view.reunion.FormOrdenDiaDocumentacion', 'goc.view.reunion.FormOrdenDiaAcuerdos',
        'goc.view.reunion.FormReunionMiembros', 'goc.view.common.ComboTipoOrgano', 'goc.view.common.ComboOrgano',
        'goc.view.reunion.GridDescriptoresOrdenDia', 'goc.store.DescriptoresOrdenDia', 'goc.store.Descriptores',
        'goc.store.Claves',
        'goc.view.reunion.FormDescriptoresOrdenDia'
    ],
    title : appI18N.reuniones.tituloPrincipal,
    layout : 'fit',

    items : [
        {
            xtype : 'panel',
            layout : 'vbox',
            items : [
                {
                    xtype : 'reunionGrid',
                    flex : 1,
                    width : '100%'
                },
                {
                    xtype : 'ordenDiaGrid',
                    flex : 1,
                    width : '100%'
                }
            ]
        }
    ]
});
