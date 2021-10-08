package evaluate;

import java.util.Calendar;

public class ScheduledEvent {

    private Calendar startDate;
    private Calendar endDate;
    private String title;

    public ScheduledEvent(Calendar startDate, Calendar endDate, String title) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public String getTitle() {
        return title;
    }
}
