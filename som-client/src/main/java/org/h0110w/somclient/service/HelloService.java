package org.h0110w.somclient.service;

import lombok.extern.slf4j.Slf4j;
import org.h0110w.somclient.exception.ServiceException;
import org.h0110w.somclient.model.Message;

import java.io.IOException;

@Slf4j
public class HelloService extends AbstractService {
    public String getSecuredHello() {
        Message message = null;
        try {
            message = client.get("/secured/message", Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message == null ? "" : message.getMessage();
    }

    public String getPublicMessage() {
        Message message = null;
        try {
            message = client.get("/public/message", Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message == null ? "" : message.getMessage();
    }

    public String getUserMessage() {
        Message message = null;
        try {
            message = client.get("/user/message", Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            log.error("error on post request /user/message, " + e.getMessage());
        }
        return message == null ? "" : message.getMessage();
    }
}
