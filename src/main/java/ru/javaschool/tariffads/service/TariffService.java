package ru.javaschool.tariffads.service;

import ru.javaschool.tariffads.dto.TariffPlanDto;

import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Interface to work with tariff plans
 */
public interface TariffService {
    /**
     * Method to get tariff plans
     * @return list with tariff plans dtp
     */
    List<TariffPlanDto> getAllTariffs();

    /**
     * Method to update tariff plans
     */
    void updateTariffs();

    /**
     * Method to add property change listener to tariff plans list
     * @param listener property change listener
     */
    void addPropertyChangeListener(PropertyChangeListener listener);
}
