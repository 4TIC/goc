$(function()
{
    $('.collapsible.collapsed').each(function(index)
    {
        $(this).prev().after('<a class="collaptor" href="#"><i class="fa fa-caret-right"></i></a>');
    });

    $(document).on("click", "a.collaptor", function(event)
    {
        event.preventDefault();
        $(this).next().toggleClass('collapsed expanded');
        $(this).children().each(function()
        {
            $(this).toggleClass('fa-caret-right fa-caret-down');
        });
    });

    $('a.nextPage').click(function()
    {
        var form = $('form[name=form-busqueda]'),
            paginaActual = form.find('input[name=pagina]');

        paginaActual.val(parseInt(paginaActual.val()) + 1);

        form.submit();
    });

    $('a.prevPage').click(function()
    {
        var form = $('form[name=form-busqueda]'),
            paginaActual = form.find('input[name=pagina]');

        paginaActual.val(parseInt(paginaActual.val()) - 1);

        form.submit();
    });

    $(document).ready(function()
    {
        $('input[name=fInicio]').datepicker({
            firstDay: 1,
            dateFormat: 'dd/mm/yy',
            onClose: function( selectedDate ) {
                $( "input[name=fFin]" ).datepicker( "option", "minDate", selectedDate );
            }
        });

        $('input[name=fFin]').datepicker({
            firstDay: 1,
            dateFormat: 'dd/mm/yy',
            onClose: function( selectedDate ) {
                $( "input[name=fInicio]" ).datepicker( "option", "maxDate", selectedDate );
            }
        });
    });
});