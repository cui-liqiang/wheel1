package testpackage.controllers;

import core.annotation.Component;
import core.annotation.Qualified;
import testpackage.services.Service;

import javax.inject.Inject;

@Component("controllerA")
public class ControllerA implements Controller {
    private Service service;

    @Inject public ControllerA(@Qualified(id="serviceA")Service service) {
        this.service = service;
    }

    @Override
    public void handle() {
    }

    public Service getService() {
        return service;
    }
}
