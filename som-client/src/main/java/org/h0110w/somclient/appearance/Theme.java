package org.h0110w.somclient.appearance;

import lombok.Getter;
import org.h0110w.somclient.SomClientApplication;

import java.util.List;

@Getter
public class Theme {
    public static final String CSS_BG1_NAME = "backgroundColor1";
    public static final String CSS_BG2_NAME = "backgroundColor2";
    public static final String CSS_CT1_NAME = "contentColor1";
    public static final String CSS_CT2_NAME = "contentColor2";
    private final String name;
    private final String themePath;
    private final String backgroundColor1;
    private final String backgroundColor2;
    private final String contentColor1;
    private final String contentColor2;

    public Theme(String themePath, String name,
                 String backgroundColor1, String backgroundColor2,
                 String contentColor1, String contentColor2) {
        this.themePath = themePath;
        this.name = name;
        this.backgroundColor1 = backgroundColor1;
        this.backgroundColor2 = backgroundColor2;
        this.contentColor1 = contentColor1;
        this.contentColor2 = contentColor2;
    }

    public List<String> getColors() {
        return List.of(backgroundColor1, backgroundColor2,
                contentColor1, contentColor2);
    }

    public String getCSSPath() {
        return SomClientApplication.class
                .getResource(themePath).toExternalForm();
    }

    @Override
    public String toString() {
        return "Theme{" +
                "name='" + name + '\'' +
                '}';
    }
}
