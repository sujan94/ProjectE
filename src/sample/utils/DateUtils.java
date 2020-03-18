package sample.utils;

public class DateUtils {

    public static String getFormattedDOB(String date) {
        String[] dateList = date.split("-");
        return dateList[0] + "-" + getMonthString(dateList[1]) + "-" + dateList[2];
    }

    private static String getMonthString(String monthInInteger) {
        switch (monthInInteger) {
            case "01":
                return "JAN";
            case "02":
                return "FEB";
            case "03":
                return "MAR";
            case "04":
                return "APR";
            case "05":
                return "MAY";
            case "06":
                return "JUN";
            case "07":
                return "JUL";
            case "08":
                return "AUG";
            case "09":
                return "SEP";
            case "10":
                return "OCT";
            case "11":
                return "NOV";
            default:
                return "DEC";
        }
    }

}
