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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * io.github.tdd91.BudgetServiceTest
 *
 * @author Bo-Xuan Fan
 * @since 2018-11-10
 */
@RunWith(MockitoJUnitRunner.class)
public class BudgetServiceTest {

    @InjectMocks
    private BudgetService service = new BudgetService();

    @Mock
    private IBudgetRepo repo;

    @Before
    public void setUp() {
    }

    @Test
    public void sameMonthOneDay() {
        givenBudgets(new Budget("201801", 31));
        LocalDate start = givenDate(2018, 1, 1);
        LocalDate end = givenDate(2018, 1, 1);
        budgetShouldBe(1.0, start, end);
    }

    @Test
    public void startDateIsAfterEndDate() {
        givenBudgets(new Budget("201801", 31));
        LocalDate start = givenDate(2018, 2, 1);
        LocalDate end = givenDate(2018, 1, 31);
        budgetShouldBe(0.0, start, end);
    }

    @Test
    public void sameMontNotOneDay() {
        givenBudgets(new Budget("201801", 31));
        LocalDate start = givenDate(2018, 1, 1);
        LocalDate end = givenDate(2018, 1, 2);
        budgetShouldBe(2.0, start, end);
    }

    @Test
    public void differentMonth_EndDateHasNoBudget() {
        givenBudgets(new Budget("201801", 31));
        LocalDate start = givenDate(2018, 1, 31);
        LocalDate end = givenDate(2018, 2, 2);
        budgetShouldBe(1.0, start, end);
    }

    @Test
    public void differentMonth_StartDateHasNoBudget() {
        givenBudgets(new Budget("201802", 280));
        LocalDate start = givenDate(2018, 1, 31);
        LocalDate end = givenDate(2018, 2, 2);
        budgetShouldBe(20.0, start, end);
    }

    @Test
    public void differentMonth_AllHaveBudget() {
        givenBudgets(
            new Budget("201801", 31),
            new Budget("201802", 280),
            new Budget("201803", 3100)
        );
        LocalDate start = givenDate(2018, 1, 31);
        LocalDate end = givenDate(2018, 3, 10);
        budgetShouldBe(1281.0, start, end);
    }

    @Test
    public void period_no_overlap_before_budget() {
        givenBudgets(
            new Budget("201802", 280)
        );
        LocalDate start = givenDate(2018, 1, 31);
        LocalDate end = givenDate(2018, 1, 31);
        budgetShouldBe(0.0, start, end);
    }

    @Test
    public void period_no_overlap_after_budget() {
        givenBudgets(
            new Budget("201802", 280)
        );
        LocalDate start = givenDate(2018, 3, 1);
        LocalDate end = givenDate(2018, 3, 1);
        budgetShouldBe(0.0, start, end);
    }

    private void givenBudgets(Budget... budgets) {
        when(repo.getAll()).thenReturn(Arrays.asList(budgets));
    }

    private void budgetShouldBe(double expected, LocalDate start, LocalDate end) {
        assertEquals(expected, service.totalAmount(start, end), 0.00);
    }

    private LocalDate givenDate(int year, int month, int dayOfMonth) {
        return LocalDate.of(year, month, dayOfMonth);
    }
}
