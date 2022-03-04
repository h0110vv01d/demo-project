package org.h0110w.som.core.service.jmx;

import org.h0110w.som.core.service.UserAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource
public class AppMonitor {
    @Autowired
    private UserAccountsService userService;

    @ManagedOperation
    public long getUserCount() {
        return userService.getUserCount();
    }
}
