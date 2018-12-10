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
        tariffService.addPropertyChangeListener((PropertyChangeEvent e)-> tariffChanel.send("updateTariffs"));
    }

    public List<TariffPlanDto> getTariffs(){
        return tariffService.getAllTariffs();
    }
}
