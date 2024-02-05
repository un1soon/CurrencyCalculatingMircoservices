package ru.mikhalev.projects.CurrencyCalculator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mikhalev.projects.CurrencyCalculator.entity.Currency;

/**
 * @author Ivan Mikhalev
 *
 * Репозиторий для получения валют из базы данных
 *
 */

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {
    Currency getCurrencyByCharCode(String charCode);
}
