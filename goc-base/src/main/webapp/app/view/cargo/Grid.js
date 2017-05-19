var cargoGridColumns = [
    {
        text : 'ID',
        dataIndex : 'id',
        hidden : true
    },
    {
        text : getMultiLangLabel(appI18N.cargos.nombre, mainLanguage),
        dataIndex : 'nombre',
        flex : 4,
        editor : {
            field : {
                allowBlank : false
            }
        }
    }
];

if (isMultilanguageApplication())
{
    cargoGridColumns.push({
        text : getMultiLangLabel(appI18N.cargos.nombre, alternativeLanguage),
        dataIndex : 'nombreAlternativo',
        flex : 4,
        editor : {
            field : {
                allowBlank : false
            }
        }
    });
}

cargoGridColumns.push({
    text : getMultiLangLabel(appI18N.cargos.firma),
    dataIndex : 'firma',
    flex : 1,
    inputValue : true,
    align: 'center',
    editor : {
        xtype : 'checkboxfield'
    },
    renderer : function(v)
    {
        if (v) return 'Sí';
        return 'No';
    }
});

Ext.define('goc.view.cargo.Grid', {
    extend : 'Ext.ux.uji.grid.Panel',

    alias : 'widget.cargoGrid',

    requires : [
        'goc.store.Cargos'
    ],

    store : {
        type : 'cargos'
    },

    title : appI18N.cargos.titulo,
    scrollable : true,

    columns : cargoGridColumns
});
