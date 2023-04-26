import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Time {

    public static String passed(String from, String to){
        StringBuilder result = new StringBuilder();

        try{
            if(!isDate(from) && !isDateTime(from)){
                LocalDate localDate = LocalDate.parse(from);
            }
            if(!isDate(to) && !isDateTime(to)){
                LocalDate localDate = LocalDate.parse(to);
            }

            result.append("Od ");

            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));

            if(isDateTime(from) && isDateTime(to)){
                ZonedDateTime zonedDateTimeFrom = ZonedDateTime.of(LocalDateTime.parse(from), ZoneId.of("Europe/Warsaw"));
                ZonedDateTime zonedDateTimeto = ZonedDateTime.of(LocalDateTime.parse(to), ZoneId.of("Europe/Warsaw"));

                Period period = Period.between(LocalDate.from(zonedDateTimeFrom), LocalDate.from(zonedDateTimeto));

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy (EEEE) 'godz.' HH:mm").withLocale(new Locale("pl"));

                result.append(zonedDateTimeFrom.format(dateTimeFormatter)).append(" do ").append(zonedDateTimeto.format(dateTimeFormatter)).append("\n");
                result.append(" - mija: ").append(ChronoUnit.DAYS.between(zonedDateTimeFrom, zonedDateTimeto)).append(ChronoUnit.DAYS.between(zonedDateTimeFrom, zonedDateTimeto) == 1 ? " dzień, tygodni " : " dni, tygodni ").append(decimalFormat.format(ChronoUnit.DAYS.between(zonedDateTimeFrom, zonedDateTimeto) / 7.0)).append("\n");
                result.append(" - godzin: ").append(ChronoUnit.HOURS.between(zonedDateTimeFrom, zonedDateTimeto)).append(", minut: ").append(ChronoUnit.MINUTES.between(zonedDateTimeFrom, zonedDateTimeto)).append("\n");

                String years = getPeriodValues(period).get(2);
                String months = getPeriodValues(period).get(1);
                String days = getPeriodValues(period).get(0);

                result.append(" - kalendarzowo: ").append(years).append(months).append(days);
            }else if(isDate(from) && isDate(to)){
                LocalDate localDateFrom = LocalDate.parse(from);
                LocalDate localDateTo = LocalDate.parse(to);

                Period period = Period.between(localDateFrom, localDateTo);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy (EEEE)").withLocale(new Locale("pl"));

                result.append(localDateFrom.format(dateTimeFormatter)).append(" do ").append(localDateTo.format(dateTimeFormatter)).append("\n");

                double chronoDays = ChronoUnit.DAYS.between(localDateFrom, localDateTo);
                double chronoWeeks = chronoDays/7.0;

                result.append(" - mija: ").append(chronoDays).append(chronoDays == 1 ? " dzień, tygodni " : " dni, tygodni ").append(decimalFormat.format(chronoWeeks));

                String years = getPeriodValues(period).get(2);
                String months = getPeriodValues(period).get(1);
                String days = getPeriodValues(period).get(0);

                result.append("\n - kalendarzowo: ").append(years).append(months).append(days);
            }
        }catch(Exception e){
            return("***"+e.getClass().toString().substring(5)+": "+e.getMessage());
        }

        return result.toString();
    }

    public static boolean isDateTime(String date){
        try{
            LocalDateTime localDateTime = LocalDateTime.parse(date);
        } catch (Exception e){
            return false;}
        return true;
    }

    public static boolean isDate(String date) {
        try{
            LocalDate localDate = LocalDate.parse(date);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public static List<String> getPeriodValues(Period period){
        List<String> resList = new ArrayList<>();
        String days = switch (period.getDays()) {
            case 0 -> "";
            case 1 -> "1 dzień";
            default -> period.getDays() + " dni";
        };

        String months = switch (period.getMonths()) {
            case 0 -> "";
            case 1 -> "1 miesiąc" + (days.equals("") ? "" : ", ");
            default -> period.getMonths() + " miesiące" + (days.equals("") ? "" : ", ");
        };
        int monthsAsInt = period.getMonths();

        if(monthsAsInt>=5 && monthsAsInt <=21 || (""+monthsAsInt).endsWith("\\d1") || (""+monthsAsInt).endsWith("5") || (""+monthsAsInt).endsWith("6") || (""+monthsAsInt).endsWith("7") || (""+monthsAsInt).endsWith("8") || (""+monthsAsInt).endsWith("9"))
            months=period.getMonths()+" miesięcy"+(days.equals("")?"":", ");


        String years = switch (period.getYears()) {
            case 0 -> "";
            case 1 -> "1 rok" + (months.equals("") ? "" : ", ");
            default -> period.getYears() + " lata" + (months.equals("") ? "" : ", ");
        };

        int yearsAsInt = period.getYears();

        if(yearsAsInt>=5 && yearsAsInt <=21 || (""+yearsAsInt).endsWith("\\d1") || (""+yearsAsInt).endsWith("5") || (""+yearsAsInt).endsWith("6") || (""+yearsAsInt).endsWith("7") || (""+yearsAsInt).endsWith("8") || (""+yearsAsInt).endsWith("9"))
            years=period.getYears()+" lat"+(months.equals("")?"":", ");

        resList.add(days);
        resList.add(months);
        resList.add(years);
        return resList;
    }
}
