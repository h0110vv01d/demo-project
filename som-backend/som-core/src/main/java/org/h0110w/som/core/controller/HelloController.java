package org.h0110w.som.core.controller;

import org.h0110w.som.commons.dtos.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String getHello() {
        return "hello";
    }

    @GetMapping("/secured/message")
    public Message getHelloMessage() {
        return new Message("secured hello");
    }

    @GetMapping("/public/message")
    public Message getAnonHelloMessage() {
        return new Message("public hello");
    }


    @GetMapping("/user/message")
    public Message getUserMessage() {
        return new Message("user hello");
    }
}
