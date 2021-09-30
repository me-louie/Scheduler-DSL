package ast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
  public static Date parseDateString(String dateString) throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat format2 = new SimpleDateFormat("MM-dd-yyyy");
    
    try {
      return format.parse(dateString);
    } catch (ParseException e) {
      return format2.parse(dateString);
    }
  }
}
