Ext.define('Ext.ux.uji.glyphs.Glyphs',
{
    singleton : true,
    alias : 'Glyphs',

    DEFAULT_FONT_FAMILY : 'FontAwesome',

    ADD : 0xf0c7,
    DELETE : 0xf00d,
    EDIT : 0xf044,
    REFRESH : 0xf021,
    SEARCH : 0xf002,

    SQUARE : 0xf0c8,
    CIRCLE : 0xf111,

    constructor : function()
    {
        Ext.setGlyphFontFamily('FontAwesome');
    }
});