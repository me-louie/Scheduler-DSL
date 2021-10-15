package evaluate;

import java.time.LocalDateTime;
import java.util.Objects;

public class ScheduledEvent {

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String title;
    private final String description;

    public ScheduledEvent(LocalDateTime startDate, LocalDateTime endDate, String title, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
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
        return startDate.equals(that.startDate) && endDate.equals(that.endDate) && title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, title);
    }
}
