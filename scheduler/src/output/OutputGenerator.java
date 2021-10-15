package output;

import evaluate.ScheduledEvent;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.FixedUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OutputGenerator {

    private static Calendar createCalendar() {
        Calendar cal = new net.fortuna.ical4j.model.Calendar();
        cal.getProperties().add(new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
        cal.getProperties().add(Version.VERSION_2_0);
        cal.getProperties().add(CalScale.GREGORIAN);
        return cal;
    }

    public void generate(Map<String, Set<ScheduledEvent>> scheduleMap, String outputFileName) throws IOException {
        Calendar cal = createCalendar();
        cal.getComponents().addAll(createEvents(scheduleMap));
        FileOutputStream fout = new FileOutputStream(outputFileName);
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(cal, fout);
    }

    private Set<VEvent> createEvents(Map<String, Set<ScheduledEvent>> scheduleMap) throws SocketException {
        Set<VEvent> events = new HashSet<>();
        UidGenerator ug = new FixedUidGenerator("uidGen");

        for (Map.Entry<String, Set<ScheduledEvent>> entry : scheduleMap.entrySet()) {
            String name = entry.getKey();
            for (ScheduledEvent e : entry.getValue()) {
                VEvent meeting = new VEvent(new DateTime(e.getStartDate().getTime()),
                        new DateTime(e.getEndDate().getTime()),
                        name + " - " + e.getTitle());
                meeting.getProperties().add(ug.generateUid());
                meeting.getProperties().add(new Description(e.getDescription()));
                events.add(meeting);
            }
        }
        return events;
    }


}
