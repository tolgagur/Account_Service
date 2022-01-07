package account.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class PaymentResponse {

    private final String name;
    private final YearMonth period;
    private final String lastname;
    private final String salary;


    public PaymentResponse(String name, String period, String lastname, long salary) {
        this.name = name;
        this.period = YearMonth.parse(period, DateTimeFormatter.ofPattern("MM-yyyy"));
        this.lastname = lastname;

        final long cents = salary % 100;
        final long dollars = salary / 100;

        this.salary = String.format("%d dollar(s) %d cent(s)", dollars, cents);
    }

    public String getName() {
        return name;
    }


    public String getPeriod() {
        return DateTimeFormatter.ofPattern("MMMM-yyyy").format(period);
    }

    @JsonIgnore
    public YearMonth getPeriodAsYearMonth(){
        return period;
    }

    @JsonProperty("lastname")
    public String getLastName() {
        return lastname;
    }

    public String getSalary() {
        return salary;
    }
}