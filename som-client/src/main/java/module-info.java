module org.h0110w.somclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign;
    requires org.kordamp.bootstrapfx.core;
    requires lombok;
    requires org.slf4j;
    requires org.slf4j.simple;

    opens org.h0110w.somclient to javafx.fxml;
    opens org.h0110w.somclient.scenes.tests to javafx.fxml, org.kordamp.ikonli.core;
    opens org.h0110w.somclient.scenes.main_scene to javafx.fxml;
    opens org.h0110w.somclient.model to com.fasterxml.jackson.databind;
    opens org.h0110w.somclient.config to com.fasterxml.jackson.databind;


    opens org.h0110w.somclient.utils.mapper to com.fasterxml.jackson.databind;
    opens org.h0110w.somclient.service.dto to com.fasterxml.jackson.databind, com.google.gson;
    opens org.h0110w.somclient.appearance to com.fasterxml.jackson.databind;

    exports org.h0110w.somclient;
    exports org.h0110w.somclient.scenes.tests;
    opens org.h0110w.somclient.scenes.main_scene.working_space_nodes to javafx.fxml;
}