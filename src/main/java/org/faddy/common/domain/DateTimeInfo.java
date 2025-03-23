package org.faddy.common.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateTimeInfo {
    private boolean isEdited;
    private LocalDateTime dateTime;

    private static final boolean INITIAL_EDITED_STATE = false;
    private static final boolean UPDATED_EDITED_STATE = true;

    public DateTimeInfo() {
        this.isEdited = INITIAL_EDITED_STATE;
        this.dateTime = LocalDateTime.now();
    }

    public void updateDateTime() {
        this.isEdited = UPDATED_EDITED_STATE;
        this.dateTime = LocalDateTime.now();
    }


    public boolean isEdited() {
        return this.isEdited;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }
}
