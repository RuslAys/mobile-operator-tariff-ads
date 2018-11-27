package ru.javaschool.tariffads.controller;

import ru.javaschool.tariffads.dto.TariffPlanDto;
import ru.javaschool.tariffads.service.TariffService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class TariffController implements Serializable {
    @Inject
    private TariffService tariffService;

    @Inject
    @Push
    private PushContext tariffChanel;

    @PostConstruct
    public void init(){
        tariffService.addPropertyChangeListener((PropertyChangeEvent e)->{
            System.out.println("Old value " + e.getOldValue());
            System.out.println("New value " + e.getNewValue());
            tariffChanel.send("updateTariffs");
            System.out.println("Pushed");
            System.out.println("New tariffs " + tariffService.getAllTariffs());
        });
    }

    public List<TariffPlanDto> getTariffs(){
        return tariffService.getAllTariffs();
    }

    public void update(){
        tariffChanel.send("updateTariffs");
    }
}
