/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Bo-Xuan Fan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.tdd91;

import static java.util.stream.Collectors.toMap;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

/**
 * BudgetService
 *
 * @author Bo-Xuan Fan
 * @since 2018-11-10
 */
public class BudgetService {

    private IBudgetRepo repo;

    public double totalAmount(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return 0.0;
        }

        List<Budget> budgets = repo.getAll();

        Map<String, Integer> budgetPerDayMap = findBudgetPerDayMap(budgets);


        // YearMonth startYearMonth = YearMonth.from(start);
        // YearMonth endYearMonth = YearMonth.from(end);
        //
        // Map<YearMonth, Integer> durationDays = new HashMap<>();
        // while (startYearMonth.isBefore(endYearMonth)) {
        //     durationDays.put(startYearMonth, startYearMonth.lengthOfMonth());
        //     startYearMonth = startYearMonth.plusMonths(1);
        // }
        //
        // double budget = 0.0;
        //
        // for (Entry<YearMonth, Integer> entry : durationDays.entrySet()) {
        //     String key = entry.getKey().toString();
        //     if (budgetPerDayMap.containsKey(key)) {
        //         budget += entry.getValue() * budgetPerDayMap.get(key);
        //     }
        // }
        //
        // return budget;

        // Budget budget = budgets.get(0);
        // int monthDays = YearMonth.parse(budget.getYearMonth(), DateTimeFormatter.ofPattern("yyyyMM")).lengthOfMonth();
        // int budgetPerDay = budget.getAmount() / monthDays;

        double result = 0.0;

        long durationDays;
        if (YearMonth.from(start).equals(YearMonth.from(end))) {
            int budgetPerDay = budgetPerDayMap.get(YearMonth.from(start).format(DateTimeFormatter.ofPattern("yyyyMM")));
            durationDays = start.until(end, ChronoUnit.DAYS) + 1;
            result += budgetPerDay * durationDays;
        } else {
            Integer budgetPerDay = budgetPerDayMap.get(YearMonth.from(start).format(DateTimeFormatter.ofPattern("yyyyMM")));
            if (budgetPerDay == null) {
                budgetPerDay = 0;
            }
            LocalDate newEnd = start.with(TemporalAdjusters.lastDayOfMonth());
            durationDays = start.until(newEnd, ChronoUnit.DAYS) + 1;
            result += budgetPerDay * durationDays;

            budgetPerDay = budgetPerDayMap.get(YearMonth.from(end).format(DateTimeFormatter.ofPattern("yyyyMM")));
            if (budgetPerDay == null) {
                budgetPerDay = 0;
            }
            LocalDate newStart = end.with(TemporalAdjusters.firstDayOfMonth());
            durationDays = newStart.until(end, ChronoUnit.DAYS) + 1;
            result += budgetPerDay * durationDays;
        }

        return result;
    }

    private Map<String, Integer> findBudgetPerDayMap(List<Budget> budgets) {
        return budgets.stream().peek(budget -> {
            int monthDays = YearMonth.parse(budget.getYearMonth(), DateTimeFormatter.ofPattern("yyyyMM")).lengthOfMonth();
            budget.setAmount(budget.getAmount() / monthDays);
        })
            .collect(toMap(Budget::getYearMonth, Budget::getAmount));
    }

}
