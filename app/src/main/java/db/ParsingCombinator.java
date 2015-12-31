package db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Егор on 22.04.2015.
 */

public class ParsingCombinator {
    public static final String[] zzz = new String[] {
            "^(\\d{2}\\.\\d{2}(\\,\\s)?)+",
            "(по\\s)?((пн|вт|ср|чт|пт|сб|вс)\\,?\\s?)+",
            "^(по выходным)",
            "^(по будням)",
            "^(по нечетным)",
            "^(по четным)"};
    public static final String []ttt = new String[] {
            "(с)\\s\\d{1,2}\\s(###)",
            "(по)\\s\\d{1,2}\\s(###)",
            "(кроме)(\\s\\d{2}\\.\\d{2}\\,?)+",
            "(кроме)\\s((пн|вт|ср|чт|пт|сб|вс)\\W*)+"};
    private static final String dateCol = "'NOW'";//"date('NOW' ,'localtime')";
    private static String currentMonthName;
    private static String currentMonthNum;
    private HashMap<String, String> map;
    private String inputString;
    private String queryString;
    private int currentYear;
    private ArrayList<String[]> listv;
    public ParsingCombinator() {
        inputString = "";
        queryString = "";

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        currentYear = cal.get(Calendar.YEAR);

        map = new HashMap<String, String>() {{
            put("пн", "1");
            put("вт", "2");
            put("ср", "3");
            put("чт", "4");
            put("пт", "5");
            put("сб", "6");
            put("вс", "0");
        }};

        Calendar calendar = new GregorianCalendar(new Locale("ru_RU"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        currentMonthNum = dateFormat.format(calendar.getTime());

        dateFormat = new SimpleDateFormat("MMMM", new Locale("ru_RU"));

        currentMonthName = dateFormat.format(calendar.getTime());

        calendar.add(Calendar.MONTH, 1);
        dateFormat = new SimpleDateFormat("MM");


        listv = new MyArrayList<String[]>(zzz.length * 2);

        for (String iterable_element : zzz) {
            if(iterable_element.contains("###"))
            {

                listv.add(new String[]{iterable_element.replace("###", currentMonthName),
                        iterable_element.replace("###", currentMonthName) + ".*"});
            }
            else
                listv.add(new String[]{iterable_element, iterable_element + ".*"});
        }
        for (String iterable_element : ttt) {
            if(iterable_element.contains("###")) {
                listv.add(new String[]{iterable_element.replace("###", currentMonthName),
                        ".*" + iterable_element.replace("###", currentMonthName) + ".*"});
            }
            else
                listv.add(new String[]{iterable_element,".*" + iterable_element + ".*"});
        }
    }

    public String getFormedQuery(String s) {

        if(s.equals("ежедневно"))
            return "";

        resetQueryString();
        setInputString(s);

        for(String[] current : listv)
        {
            if(containsPart(current[1]))
            {
                getGeneralizedSQL(extractPart(current[0]), current[0], listv.indexOf(current));
            }
        }
        return queryString;
    }


    public String getInputString() {
        return inputString;
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void resetQueryString() {
        queryString = "";
    }
    public void resetStrings() {
        inputString = "";
        queryString = "";
    }

    public boolean containsPart(String regExpr) {
        return inputString.matches(regExpr);
    }

    public String extractPart(String regExpr) {
        Pattern pattern = Pattern.compile(regExpr);
        Matcher matcher = pattern.matcher(inputString);
        matcher.find();
        return matcher.group(0);
    }

    public void appendQueryString(String sqlQuery) {
        StringBuilder sb = new StringBuilder(queryString);
        sb.append(sqlQuery);
        this.queryString = sb.toString();
    }

    public void removePart(String regExpr) {
        inputString.replaceAll(regExpr, "");
    }

    public boolean isInputStringEmpty() {
        return inputString.isEmpty();
    }

    public boolean isQueryStringEmpty() {
        return queryString.isEmpty();
    }
    /////////////////////////
    private void getGeneralizedSQL(String partString, String regExpr, int param) {

        String formedString = "";
        Pattern patterm;
        Matcher mather;
        String[] dayAndMounth;
        switch (param) {
            case 0: //getDateSQL
                patterm = Pattern.compile("\\d{2}\\.\\d{2}");
                mather = patterm.matcher(partString);
                while (mather.find()) {
                    dayAndMounth = mather.group().split("\\.");
                    if (formedString.equals("")) {
                        formedString = "(" + dateCol + " = \"" + currentYear + "-" + dayAndMounth[1] + "-" + dayAndMounth[0] + "\"";
                    } else {
                        formedString += " OR " + dateCol + " = \"" + currentYear + "-" + dayAndMounth[1] + "-" + dayAndMounth[0] + "\"";
                    }
                }
                formedString += ") ";
                break;

            case 1: //getWeekdaySQL
                patterm = Pattern.compile("(пн|вт|ср|чт|пт|сб|вс)+");
                mather = patterm.matcher(partString);
                while (mather.find()) {
                    if (formedString.equals("")) {
                        formedString = "(" + "strftime('%w', " + dateCol + ") = \"" + map.get(mather.group()) + "\"";
                    } else {
                        formedString += " OR strftime('%w', " + dateCol + ") = \"" + map.get(mather.group()) + "\"";
                    }
                }
                formedString += ")";
                break;

            case 2: //getWeekendsSQL
                formedString = "(strftime('%w', " + dateCol + ") = \"0\" OR strftime('%w', " + dateCol + ") = \"6\") ";
                break;

            case 3: //getWorkdaysSQL
                formedString = "(strftime('%w', " + dateCol + ") != \"0\" AND strftime('%w', " + dateCol + ") != \"6\") ";
                break;

            case 4: //getOddDaysSQL
                formedString = "(strftime('%d', " + dateCol + ")%2=1) ";
                break;

            case 5: //getEvenDaysSQL
                formedString = "(strftime('%d', " + dateCol + ")%2=0) ";
                break;

            case 6: //getAfterDateSQL
                Pattern p3 = Pattern.compile("\\d{1,2}");
                //patterm = Pattern.compile("\\d{2}\\.\\d{2}");
                Matcher m3 = p3.matcher(partString);
                while (m3.find()) {
                    formedString = "(" + dateCol + " >= \"" + currentYear + "-" + currentMonthNum + "-" + m3.group() + "\")";
                    //dayAndMounth = mather.group().split("\\.");
                    //formedString = "(" + dateCol + " >= \""+ currentYear + "-" + dayAndMounth[1] + "-" + dayAndMounth[0] + "\")";
                }
                break;

            case 7: //getBeforeDateSQL
                patterm = Pattern.compile("\\d{1,2}");
                //patterm = Pattern.compile("\\d{2}\\.\\d{2}");
                mather = patterm.matcher(partString);
                while (mather.find()) {
                    formedString = "(" + dateCol + " <= \"" + currentYear + "-" + currentMonthNum + "-" + mather.group() + "\")";
                    //dayAndMounth = mather.group().split("\\.");
                    //formedString = "(" + dateCol + " <= \"" + currentYear + "-" + dayAndMounth[1] + "-" + dayAndMounth[0] + "\")";
                }

                break;

            case 8: //getExceptDateSQL
                patterm = Pattern.compile("\\d{2}\\.\\d{2}");
                mather = patterm.matcher(partString);
                while (mather.find()) {
                    dayAndMounth = mather.group().split("\\.");
                    if (formedString.equals(""))
                    {
                        formedString = "(" + dateCol + " != ";
                        formedString += "\"" + currentYear + "-" + dayAndMounth[1] + "-" + dayAndMounth[0] + "\"";
                    }
                    else
                    {
                        formedString += " AND " + dateCol + " != \"" + currentYear + "-" + dayAndMounth[1] + "-" + dayAndMounth[0] + "\"";
                    }
                }
                formedString += ")";
                break;

            case 9: //getExceptWeekDaySQL

                patterm = Pattern.compile("(пн|вт|ср|чт|пт|сб|вс)+");
                mather = patterm.matcher(partString);
                while (mather.find()) {
                    if (formedString.equals(""))
                    {
                        formedString = "(" + "strftime('%w', " + dateCol + ") != \"" + map.get(mather.group()) + "\"";
                    }
                    else
                    {
                        formedString += " AND strftime('%w', " + dateCol + ") != \"" + map.get(mather.group()) + "\"";
                    }
                }
                formedString += ")";
                break;

            default:
                formedString = "(date('NOW') = date('1990.01.01'))";
                break;
        }

        if(isQueryStringEmpty())
            formedString = " WHERE " + formedString;
        else
            formedString = " AND " + formedString;

        appendQueryString(formedString);
        removePart(regExpr);
    }

}
