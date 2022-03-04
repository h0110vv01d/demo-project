package org.h0110w.som.test_service;

import org.h0110w.som.commons.dtos.Message;
import org.springframework.stereotype.Service;

@Service
public class HelloService {
    public Message getHello() {
        return new Message("hello");
    }
}
