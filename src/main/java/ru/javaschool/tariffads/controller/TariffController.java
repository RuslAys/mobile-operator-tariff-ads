package ru.javaschool.tariffads.controller;

import org.apache.log4j.Logger;
import ru.javaschool.tariffads.dto.TariffPlanDto;
import ru.javaschool.tariffads.service.TariffService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class TariffController implements Serializable {

    Logger logger = Logger.getLogger(TariffController.class);

    @Inject
    private TariffService tariffService;

    @Inject
    @Push
    private PushContext tariffChanel;

    @PostConstruct
    public void init(){
        logger.debug("init tariffController");
        tariffService.addPropertyChangeListener(e -> tariffChanel.send("updateTariffs"));
    }

    public List<TariffPlanDto> getTariffs(){
        logger.debug("call tariffController.getTariffs()");
        return tariffService.getAllTariffs();
    }
}
