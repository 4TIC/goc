$(function () {
    var appI18N;
    var reunionId = $("div.nuevo-comentario input[name=reunionId]").val();

    function loadComentarios(reunionId, appI18N) {
        $.ajax({
            type: "GET",
            url: "/goc/rest/reuniones/" + reunionId + "/comentarios",
            success: function (response) {
                if (response.data.length ===0) return;
                var html = '<h1>' + appI18N.acta.comentarios + '</h1>';

                response.data.map(function (comentario) {
                    var creador = comentario.creadorNombre ? comentario.creadorNombre : comentario.creadorId;
                    html += '<div class="comentario">' +
                        '  <p class=""autor"><strong>' + appI18N.acta.autor + '</strong>: ' + creador + '</p>' +
                        '  <div class="texto">' + comentario.comentario + '</div>' +
                        '  <p class="fecha"><strong>' + appI18N.acta.fecha + '</strong>: ' + comentario.fecha + '</p>' +
                        '</div>';
                });

                $('div.comentarios').html(html);
            }
        });
    }

    function loadResultadosBusquedaPersonas(result) {
        var items = [];
        $('div.nuevo-suplente .footer button').show();

        for (var i = 0; i < result.data.length; i++) {
            var persona = result.data[i];
            items.push('<li><input type="radio" value="' + persona.id + '"/> <label for="suplenteNombre">' + persona.nombre + '</label> (<label for="suplenteEmail">' + persona.email + '</label>)</li>')
        }

        $('div.nuevo-suplente ul.resultados').html(items.join('\n'));
    }

    function buscaPersona(query) {
        $.ajax({
            type: "GET",
            url: "/goc/rest/personas/",
            data: {query: query},
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (result) {
                loadResultadosBusquedaPersonas(result)
            }
        });
    }

    $('button[name=suplente]').on('click', function (ev) {
        $('div.nuevo-suplente input[name=organoMiembroId]').val($(this).data('miembroid'));

        var suplente = $(this).data('suplente');

        if (suplente) {
            $('div.nuevo-suplente p.suplente-actual').show();
            $('div.nuevo-suplente p.suplente-actual > strong').html(suplente);
        } else {
            $('div.nuevo-suplente p.suplente-actual').hide();
        }

        $('div.nuevo-suplente .header input[name=query-persona]').val('');
        $('div.nuevo-suplente .footer button').hide();
        $('div.nuevo-suplente ul.resultados').html('');
        $('div.nuevo-suplente').modal();
    });

    $('input[name=query-persona]').keypress(function (e) {
        if (e.which == 13) {
            buscaPersona($('input[name=query-persona]').val());
        }
    });

    $('button[name=busca-persona]').on('click', function (ev) {
        buscaPersona($('input[name=query-persona]').val());
    });

    $('button[name=borra-suplente]').on('click', function (ev) {
        var organoMiembroId = $('div.nuevo-suplente input[name=organoMiembroId]').val();
        var data = {
            organoMiembroId: organoMiembroId
        };

        $.ajax({
            type: "DELETE",
            url: "/goc/rest/reuniones/" + reunionId + "/suplente",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function () {
                window.location.reload();
            }
        });
    });

    $('button[name=add-suplente]').on('click', function (ev) {
        var suplente = $('div.nuevo-suplente ul.resultados li input[type=radio]').filter(':checked').parent('li');
        var organoMiembroId = $('div.nuevo-suplente input[name=organoMiembroId]').val();

        var data = {
            suplenteId: suplente.children('input[type=radio]').val(),
            suplenteNombre: suplente.children('label[for=suplenteNombre]').html(),
            suplenteEmail: suplente.children('label').next('label[for=suplenteEmail]').html(),
            organoMiembroId: organoMiembroId
        };

        $.ajax({
            type: "POST",
            url: "/goc/rest/reuniones/" + reunionId + "/suplente",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function () {
                window.location.reload();
            }
        });
    });

    $('button.add-comentario').on('click', function () {
        $('div.nuevo-comentario').modal();
        $('div.nuevo-comentario textarea').val('');
    });

    $('button.post-comentario').on('click', function () {
        var data = {
            comentario: $('div.nuevo-comentario textarea').val(),
            reunionId: reunionId
        }

        $.ajax({
            type: "POST",
            url: "/goc/rest/reuniones/" + reunionId + "/comentarios",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function () {
                loadComentarios(reunionId, appI18N);
                $.modal.close();
            }
        });
    });

    $('div.confirmacion button[name=confirmar], div.confirmacion button[name=denegar]').on('click', function () {
        var confirmacion = $(this).attr("name") === 'confirmar' ? true : false;
        $.ajax({
            type: "POST",
            url: "/goc/rest/reuniones/" + reunionId + "/confirmar",
            data: JSON.stringify({confirmacion: confirmacion}),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function () {
                window.location.reload();
            }
        });

    })

    $.getJSON('/goc/app/i18n/' + applang + '.json', function(data) {
        appI18N = data;
        loadComentarios(reunionId, appI18N);
    })
});