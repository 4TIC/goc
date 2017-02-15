Ext.define('goc.view.historicoReunion.HistoricoReunionGrid', {
    extend: 'Ext.ux.uji.grid.Panel',
    alias: 'widget.historicoReunionGrid',
    plugins: [],
    requires: ['goc.view.historicoReunion.HistoricoReunionGridController'],
    controller: 'historicoReunionGridController',
    bind: {
        store: '{reunionesStore}'
    },
    name: 'reunion',
    reference: 'historicoReunionGrid',
    multiSelect: true,
    scrollable: true,
    title: appI18N.historicoReuniones.tituloGridReunionesCompletadas,
    margin: 5,
    border: 0,
    columns: [
        {
            text: 'Id',
            width: 80,
            dataIndex: 'id',
            hidden: true
        },
        {
            text: appI18N.reuniones.asunto,
            dataIndex: 'asunto',
            flex: 1,
            renderer: function (asunto, celda, record) {
                return asunto + '&nbsp;&nbsp;<a class="link" target="_blank" href="/goc/rest/publicacion/reuniones/' + record.get('id') + '"><i class="fa fa-external-link"></i></a>';
            }
        },
        {
            text: appI18N.reuniones.fecha,
            dataIndex: 'fecha',
            width: 150,
            format: 'd/m/Y H:i',
            xtype: 'datecolumn'
        },
        {
            text: appI18N.reuniones.duracion,
            dataIndex: 'duracion',
            renderer: function (value) {
                var duracionTexto =
                {
                    30: '0,5 ' + appI18N.reuniones.horas,
                    60: '1 ' + appI18N.reuniones.hora,
                    90: '1,5 ' + appI18N.reuniones.horas,
                    120: '2 ' + appI18N.reuniones.horas,
                    150: '2,5 ' + appI18N.reuniones.horas,
                    180: '3 ' + appI18N.reuniones.horas
                };

                return duracionTexto[value];
            }
        },
        {
            dataIndex: 'numeroDocumentos',
            text: appI18N.reuniones.documentos,
            align: 'center',
            renderer: function (value) {
                if (value > 0) {
                    return '<span class="fa fa-file-text-o"></span>'
                }
                return ''
            }
        }],

    tbar: [
        {
            xtype: 'textfield',
            emptyText: appI18N.reuniones.buscarReunion,
            flex: 1,
            name: 'searchReunion',
            listeners: {
                change: 'onSearchReunion'
            }
        },
        {
            xtype: 'comboReunionTipoOrgano',
            margin: '0 10 10 0'
        },
        {
            xtype: 'comboOrgano',
            margin: '0 0 10 0'
        }],
    listeners: {
        render: 'onLoad',
        organoSelected: 'organoSelected',
        tipoOrganoSelected: 'tipoOrganoSelected'
    }

})
;
