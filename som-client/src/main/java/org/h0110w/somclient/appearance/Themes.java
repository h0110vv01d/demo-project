package org.h0110w.somclient.appearance;


import java.util.List;

//https://material.io/design/color/the-color-system.html#tools-for-picking-colors
//https://www.canva.com/colors/color-palettes/
//https://edencoding.com/resources/css_properties/fx-background-color/
//https://www.color-hex.com
//https://www.joshwcomeau.com/gradient-generator/

public class Themes {
    private Themes() {
    }

    public static List<Theme> getThemes() {
        return List.of(DARK_SOLARIZED,
                LIGHT_SOLARIZED,
                DARK,
                LIGHT,
                DARK_METAL,
                DEEP_BLUE,
                RED);
    }

    public static final Theme DARK_SOLARIZED =
            new Theme("themes/dark_solarized.css",
                    "Dark Solarized",
                    "#002b36", "#073642",
                    "#839496", "#93a1a1");

    public static final Theme LIGHT_SOLARIZED =
            new Theme("themes/light_solarized.css",
                    "Light Solarized",
                    "#eee8d5", "#fdf6e3",
                    "#839496", "#93a1a1");

    public static final Theme DARK = new Theme("themes/dark.css",
            "Dark",
            "#252024", "#323232",
            "#eeeeee", "#808080");

    public static final Theme LIGHT =
            new Theme("themes/light.css",
                    "Light",
                    "#fffefc", "#fafafa",
                    "#7c7c7c", "#dedede");

    public static final Theme DEEP_BLUE =
            new Theme("themes/deep_blue.css",
                    "Deep Blue",
                    "#050a30", "#000c66",
                    "#7ec8e3", "#0000ff");

    public static final Theme DARK_METAL =
            new Theme("themes/dark_metal.css",
                    "Dark Metal",
                    "#0b0909", "#44444c",
                    "#8c8c8c", "#d6d6d6");

    public static final Theme RED = new Theme("themes/red.css",
            "RED",
            "#3e0000", "#7c0000",
            "#ba0000", "#f80000");

}
