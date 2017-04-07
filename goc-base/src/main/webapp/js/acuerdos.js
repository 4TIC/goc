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
});