package ru.mikhalev.projects.CurrencyCalculator.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.mikhalev.projects.CurrencyCalculator.entity.Currency;
import ru.mikhalev.projects.CurrencyCalculator.jsonClasses.Valute;

/**
 * @author Ivan Mikhalev
 *
 * Маппер объектов класса Valute в объекты класса Currency
 *
 */

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "charCode", target = "charCode")
    @Mapping(source = "value", target = "value")
    Currency toCurrency(Valute valute);
}
