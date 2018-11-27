package ru.javaschool.tariffads.service;

import ru.javaschool.tariffads.dto.TariffPlanDto;

import java.beans.PropertyChangeListener;
import java.util.List;

public interface TariffService {
    List<TariffPlanDto> getAllTariffs();
    void updateTariffs();
    void addPropertyChangeListener(PropertyChangeListener listener);
}
