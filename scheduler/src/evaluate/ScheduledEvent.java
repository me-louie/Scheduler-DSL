package evaluate;

import java.time.LocalDateTime;
import java.util.Objects;

public class ScheduledEvent {

    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final String title;
    private final String description;

    public ScheduledEvent(LocalDateTime startDate, LocalDateTime endDate, String title, String description) {
        this.startDateTime = startDate;
        this.endDateTime = endDate;
        this.title = title;
        this.description = description;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledEvent that = (ScheduledEvent) o;
        return startDateTime.equals(that.startDateTime) && endDateTime.equals(that.endDateTime) && title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDateTime, endDateTime, title);
    }
}