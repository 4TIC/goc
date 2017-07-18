$(function()
{
    var appI18N;
    var reunionId = $("div.nuevo-comentario input[name=reunionId]").val();

    function loadComentarios(reunionId, appI18N)
    {
        $.ajax({
            type : "GET",
            url : "/goc/rest/reuniones/" + reunionId + "/comentarios",
            success : function(response)
            {
                if (response.data.length === 0)
                {
                    $('div.comentarios').html('');
                    return;
                }

                var html = '<h3 class="title">' + appI18N.acta.comentarios + '</h3>';

                response.data.map(function(comentario)
                {
                    var creador = comentario.creadorNombre ? comentario.creadorNombre : comentario.creadorId;
                    html +=
                    '<div class="row columns">' +
                    '  <a href="#" class="delete-comentario" data-id="' + comentario.id + '"><i class="fa fa-times"></i></a>' +
                    '  <div class="comentario">' +
                    '    <p class=""autor"><strong>' + appI18N.acta.autor + '</strong>: ' + creador + '</p>' +
                    '    <div class="texto">' + comentario.comentario + '</div>' +
                    '    <p class="fecha"><strong>' + appI18N.acta.fecha + '</strong>: ' + comentario.fecha + '</p>' +
                    '  </div>' +
                    '</div>';
                });

                $('div.comentarios').html(html);
            }
        });
    }

    function loadResultadosBusquedaMiembros(result)
    {
        var items = [];
        $('div.nuevo-delegado-voto .footer button').show();

        for (var i = 0; i < result.data.length; i++)
        {
            var persona = result.data[i];
            items.push('<li><input type="radio" name="persona" value="' + persona.id + '"/> <label style="display: inline; margin-right: 0px;" for="delegadoVotoNombre">' + persona.nombre + '</label> <span style="display: inline">(</span><label style="display: inline" for="delegadoVotoEmail">' + persona.email + '</label><span style="display: inline">)</span></li>')
        }

        $('div.nuevo-delegado-voto ul.resultados').html(items.join('\n'));
    }

    function loadResultadosBusquedaPersonas(result)
    {
        var items = [];
        $('div.nuevo-suplente .footer button').show();

        for (var i = 0; i < result.data.length; i++)
        {
            var persona = result.data[i];
            items.push('<li><input type="radio" name="persona" value="' + persona.id + '"/> <label style="display: inline; margin-right: 0px;" for="suplenteNombre">' + persona.nombre + '</label> <span style="display: inline">(</span><label style="display: inline" for="suplenteEmail">' + persona.email + '</label><span style="display: inline">)</span></li>')
        }

        $('div.nuevo-suplente ul.resultados').html(items.join('\n'));
    }

    function buscaPersona(query)
    {
        $.ajax({
            type : "GET",
            url : "/goc/rest/personas/",
            data : {query : query},
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            success : function(result)
            {
                loadResultadosBusquedaPersonas(result)
            }
        });
    }

    function buscaMiembro(query)
    {
        $.ajax({
            type : "GET",
            url : "/goc/rest/reuniones/" + reunionId + "/miembros",
            data : {query : query},
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            success : function(result)
            {
                loadResultadosBusquedaMiembros(result)
            }
        });
    }

    $('button[name=suplente]').on('click', function(ev)
    {
        $('div.nuevo-suplente input[name=organoMiembroId]').val($(this).data('miembroid'));

        var suplente = $(this).data('suplente');

        if (suplente)
        {
            $('div.nuevo-suplente p.suplente-actual').show();
            $('div.nuevo-suplente p.suplente-actual > strong').html(suplente);
        } else
        {
            $('div.nuevo-suplente p.suplente-actual').hide();
        }

        $('div.nuevo-suplente .header input[name=query-persona]').val('');
        $('div.nuevo-suplente .footer button').hide();
        $('div.nuevo-suplente ul.resultados').html('');
        $('div.nuevo-suplente').modal();
    });

    $('button[name=delegado-voto]').on('click', function(ev)
    {
        $('div.nuevo-delegado-voto input[name=organoMiembroId]').val($(this).data('miembroid'));

        var delegadoVoto = $(this).data('delegadoVoto');

        if (delegadoVoto)
        {
            $('div.nuevo-delegado-voto p.delegado-voto-actual').show();
            $('div.nuevo-delegado-voto p.delegado-voto-actual > strong').html(delegadoVoto);
        } else
        {
            $('div.nuevo-delegado-voto p.delegado-voto-actual').hide();
        }

        buscaMiembro($('div.nuevo-delegado-voto input[name=query-persona]').val());

        $('div.nuevo-delegado-voto .header input[name=query-persona]').val('');
        $('div.nuevo-delegado-voto .footer button').hide();
        $('div.nuevo-delegado-voto ul.resultados').html('');
        $('div.nuevo-delegado-voto').modal();
    });

    $('div.nuevo-suplente input[name=query-persona]').keypress(function(e)
    {
        if (e.which == 13)
        {
            buscaPersona($('div.nuevo-suplente input[name=query-persona]').val());
        }
    });

    $('div.nuevo-suplente button[name=busca-persona]').on('click', function(ev)
    {
        buscaPersona($('div.nuevo-suplente input[name=query-persona]').val());
    });

    $('button[name=borra-suplente]').on('click', function(ev)
    {
        borraSuplente($('div.nuevo-suplente input[name=organoMiembroId]').val());
    });

    $('button[name=borra-suplente-directo]').on('click', function(ev)
    {
        borraSuplente($(this).attr('data-miembroid'));
    });

    $('button[name=borra-delegado-voto]').on('click', function(ev)
    {
        borraDelegadoVoto($('div.nuevo-delegado-voto input[name=organoMiembroId]').val());
    });

    $('button[name=borra-delegado-voto-directo]').on('click', function(ev)
    {
        borraDelegadoVoto($(this).attr('data-miembroid'));
    });

    function borraSuplente(organoMiembroId)
    {
        var data = {
            organoMiembroId : organoMiembroId
        };

        $.ajax({
            type : "DELETE",
            url : "/goc/rest/reuniones/" + reunionId + "/suplente",
            data : JSON.stringify(data),
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            success : function()
            {
                window.location.reload();
            }
        });
    };

    function borraDelegadoVoto(organoMiembroId)
    {
        var data = {
            organoMiembroId : organoMiembroId
        };

        $.ajax({
            type : "DELETE",
            url : "/goc/rest/reuniones/" + reunionId + "/delegadovoto",
            data : JSON.stringify(data),
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            success : function()
            {
                window.location.reload();
            }
        });
    };

    $('button[name=add-suplente]').on('click', function(ev)
    {
        var suplente = $('div.nuevo-suplente ul.resultados li input[type=radio]').filter(':checked').parent('li');
        var organoMiembroId = $('div.nuevo-suplente input[name=organoMiembroId]').val();

        var data = {
            suplenteId : suplente.children('input[type=radio]').val(),
            suplenteNombre : suplente.children('label[for=suplenteNombre]').html(),
            suplenteEmail : suplente.children('label[for=suplenteEmail]').html(),
            organoMiembroId : organoMiembroId
        };

        $.ajax({
            type : "POST",
            url : "/goc/rest/reuniones/" + reunionId + "/suplente",
            data : JSON.stringify(data),
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            success : function()
            {
                window.location.reload();
            }
        });
    });

    $('button[name=add-delegado-voto]').on('click', function(ev)
    {
        var delegacionVoto = $('div.nuevo-delegado-voto ul.resultados li input[type=radio]').filter(':checked').parent('li');
        var organoMiembroId = $('div.nuevo-delegado-voto input[name=organoMiembroId]').val();

        var data = {
            delegadoVotoId : delegacionVoto.children('input[type=radio]').val(),
            delegadoVotoNombre : delegacionVoto.children('label[for=delegadoVotoNombre]').html(),
            delegadoVotoEmail : delegacionVoto.children('label[for=delegadoVotoEmail]').html(),
            organoMiembroId : organoMiembroId
        };

        $.ajax({
            type : "POST",
            url : "/goc/rest/reuniones/" + reunionId + "/delegadovoto",
            data : JSON.stringify(data),
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            success : function()
            {
                window.location.reload();
            }
        });
    });

    $('button.add-comentario').on('click', function()
    {
        $('div.nuevo-comentario').modal();
        $('div.nuevo-comentario textarea').val('');
    });

    $('button.post-comentario').on('click', function()
    {
        var data = {
            comentario : $('div.nuevo-comentario textarea').val(),
            reunionId : reunionId
        }

        $.ajax({
            type : "POST",
            url : "/goc/rest/reuniones/" + reunionId + "/comentarios",
            data : JSON.stringify(data),
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            success : function()
            {
                loadComentarios(reunionId, appI18N);
                $.modal.close();
            }
        });
    });

    $(document).on("click", "a.delete-comentario", function(event)
    {
        event.preventDefault();
        var comentarioId = $(this).attr('data-id');

        if (confirm(appI18N.reuniones.deseaBorrarComentario))
        {
            $.ajax({
                type : "DELETE",
                url : "/goc/rest/reuniones/" + reunionId + "/comentarios/" + comentarioId,
                success : function()
                {
                    loadComentarios(reunionId, appI18N);
                }
            });
        }
    });

    $('div.confirmacion button[name=confirmar], div.confirmacion button[name=denegar]').on('click', function()
    {
        var confirmacion = $(this).attr("name") === 'confirmar' ? true : false;
        var organoMiembroId = $(this).attr('data-miembroid');

        if (!confirmacion)
        {
            $.ajax({
                type : "GET",
                url : "/goc/rest/reuniones/" + reunionId + "/tienesuplente/" + organoMiembroId,
                success : function(response)
                {
                    if (response.message == 'true')
                    {
                        if (confirm(eval('appI18N.reuniones.suplenteNoTendraEfecto')))
                        {
                            updateConfirmacion(false, organoMiembroId)
                            return;
                        } else
                        {
                            return;
                        }
                    }

                    updateConfirmacion(confirmacion, organoMiembroId);
                }
            });

            return;
        }

        updateConfirmacion(confirmacion, organoMiembroId);
    })

    function updateConfirmacion(confirmacion, organoMiembroId)
    {
        $.ajax({
            type : "POST",
            url : "/goc/rest/reuniones/" + reunionId + "/confirmar/" + organoMiembroId,
            data : JSON.stringify({confirmacion : confirmacion}),
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            success : function(response)
            {
                window.location.reload();
            }
        });
    }

    $.getJSON('/goc/app/i18n/' + applang + '.json', function(data)
    {
        appI18N = data;
        loadComentarios(reunionId, appI18N);
    })
});