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

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

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

        // same month
        if (isSameMonth(start, end)) {
            Optional<Budget> budgetOptional = budgets.stream()
                .filter(budget -> budget.getCurrentYearMonth().equals(YearMonth.from(start)))
                .findFirst();

            if (!budgetOptional.isPresent()) {
                return 0;
            }

            int validDays = start.until(end).getDays() + 1;
            return budgetOptional.get().getDailyAmount() * validDays;
        } else {
            // diff month
            double totalAmount = 0D;
            YearMonth indexYearMonth = YearMonth.from(start);
            do {
                YearMonth targetYearMonth = indexYearMonth;
                Optional<Budget> budgetOptional = budgets.stream()
                    .filter(budget -> budget.getCurrentYearMonth().equals(targetYearMonth))
                    .findFirst();
                if (budgetOptional.isPresent()) {
                    Budget budget = budgetOptional.get();

                    LocalDate tempStart;
                    LocalDate tempEnd;
                    if (isFirstMonth(start, indexYearMonth)) {
                        tempStart = start;
                        tempEnd = indexYearMonth.atEndOfMonth();
                    } else if (isLastMonth(end, indexYearMonth)) {
                        tempStart = indexYearMonth.atDay(1);
                        tempEnd = end;
                    } else {
                        tempStart = indexYearMonth.atDay(1);
                        tempEnd = indexYearMonth.atEndOfMonth();
                    }

                    int validDays = tempStart.until(tempEnd).getDays() + 1;
                    totalAmount += budget.getDailyAmount() * validDays;
                }

                indexYearMonth = indexYearMonth.plusMonths(1);
            } while (!indexYearMonth.isAfter(YearMonth.from(end)));

            return totalAmount;
        }
    }

    private boolean isLastMonth(LocalDate end, YearMonth indexYearMonth) {
        return indexYearMonth.equals(YearMonth.from(end));
    }

    private boolean isFirstMonth(LocalDate start, YearMonth indexYearMonth) {
        return indexYearMonth.equals(YearMonth.from(start));
    }

    private boolean isSameMonth(LocalDate start, LocalDate end) {
        return YearMonth.from(start).equals(YearMonth.from(end));
    }

}
