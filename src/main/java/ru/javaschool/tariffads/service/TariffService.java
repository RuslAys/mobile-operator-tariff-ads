package ru.javaschool.tariffads.service;

import ru.javaschool.tariffads.dto.TariffPlanDto;

import java.util.List;

public interface TariffService {
    List<TariffPlanDto> getAllTariffs();
}
