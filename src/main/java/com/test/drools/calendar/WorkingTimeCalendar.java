package com.test.drools.calendar;

import org.kie.api.time.Calendar;

import java.time.*;

public class WorkingTimeCalendar implements Calendar {

    @Override
    public boolean isTimeIncluded(long timestamp) {
        LocalDateTime time =
                Instant.ofEpochMilli(timestamp)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
        return time.getDayOfWeek() == DayOfWeek.SATURDAY;
    }
}
