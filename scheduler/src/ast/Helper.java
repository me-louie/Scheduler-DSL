package ast;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Helper {
  public static LocalDate parseDateString(String dateString) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    
    try {
      return LocalDate.parse(dateString, formatter);
    } catch (IllegalArgumentException e) {
      return LocalDate.parse(dateString, formatter2);
    }
  }
}
