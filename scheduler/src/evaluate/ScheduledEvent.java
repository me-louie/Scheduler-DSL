package evaluate;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Objects;

public class ScheduledEvent {

    private final Calendar startDate;
    private final Calendar endDate;
    private final String title;

    public ScheduledEvent(LocalDateTime startDate, LocalDateTime endDate, String title) {
        this.startDate = Calendar.getInstance();
        this.startDate.clear();
        // we subtract 1 from month because LocalDateTime uses one-based indexing, Calendar zero-based
        this.startDate.set(startDate.getYear(), startDate.getMonthValue() - 1, startDate.getDayOfMonth(),
                startDate.getHour(), startDate.getMinute(), startDate.getSecond());
        this.endDate = Calendar.getInstance();
        this.endDate.clear();
        this.endDate.set(endDate.getYear(), endDate.getMonthValue() - 1, endDate.getDayOfMonth(),
                endDate.getHour(), endDate.getMinute(), endDate.getSecond());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledEvent that = (ScheduledEvent) o;
        return startDate.equals(that.startDate) && endDate.equals(that.endDate) && title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, title);
    }
}
