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

public class Period {
    private final LocalDate start;
    private final LocalDate end;

    public Period(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public int getValidDays(Period another) {
        if (isInvalid()) {
            return 0;
        }

        if (end.isBefore(another.getStart())) {
            return 0;
        }

        YearMonth anotherYearMonth = YearMonth.from(another.getStart());
        LocalDate overlappingStart = another.getStart();
        LocalDate overlappingEnd = another.getEnd();

        if (isFirstMonth(anotherYearMonth)) {
            overlappingStart = getStart();
        }

        if (isLastMonth(anotherYearMonth)) {
            overlappingEnd = getEnd();
        }

        return overlappingStart.until(overlappingEnd).getDays() + 1;
    }

    private boolean isFirstMonth(YearMonth anotherYearMonth) {
        return anotherYearMonth.equals(YearMonth.from(getStart()));
    }

    private boolean isLastMonth(YearMonth anotherYearMonth) {
        return anotherYearMonth.equals(YearMonth.from(getEnd()));
    }

    private boolean isInvalid() {
        return getStart().isAfter(getEnd());
    }
}
