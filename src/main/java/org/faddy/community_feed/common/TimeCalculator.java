package org.faddy.community_feed.common;

import java.time.LocalDate;

public class TimeCalculator {

    /*
    *  util class (singleton)
    * */
    private TimeCalculator() {

    }

    public static LocalDate getDateDaysAgo(int daysAgo) {
        LocalDate currentDate = LocalDate.now();
        return currentDate.minusDays(daysAgo);
    }

}
