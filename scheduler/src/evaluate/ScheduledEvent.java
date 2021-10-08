package evaluate;

import java.util.Date;

public class ScheduledEvent {

    private Date startDate;
    private Date endDate;
    private String title;

    public ScheduledEvent(Date startDate, Date endDate, String title) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getTitle() {
        return title;
    }
}
