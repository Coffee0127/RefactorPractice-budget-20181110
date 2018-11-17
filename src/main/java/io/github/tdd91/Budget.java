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

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * Budget
 *
 * @author Bo-Xuan Fan
 * @since 2018-11-10
 */
public class Budget {

    public Budget(String yearMonth, int amount) {
        this.yearMonth = yearMonth;
        this.amount = amount;
    }

    private String yearMonth;

    private int amount;

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMonthDays() {
        return getCurrentYearMonth().lengthOfMonth();
    }

    public int getDailyAmount() {
        return getAmount() / getMonthDays();
    }

    public YearMonth getCurrentYearMonth() {
        return YearMonth.parse(getYearMonth(), DateTimeFormatter.ofPattern("yyyyMM"));
    }

    public double getOverlappingAmount(Period period) {
        return ((double) getDailyAmount()) * period.getValidDays(this);
    }
}
