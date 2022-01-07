package account.service.shared.utils.validation;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PeriodValidator {

    public static boolean isValidDate(String date){
        try{
            YearMonth.parse(date, DateTimeFormatter.ofPattern("MM-yyyy"));
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}