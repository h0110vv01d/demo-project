package org.h0110w.somclient.service;

import org.h0110w.somclient.config.Client;

public abstract class AbstractService {
    protected Client client = Client.getCustomClient();
}
