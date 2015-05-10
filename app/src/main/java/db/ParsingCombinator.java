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
 * добавить StringBuilder
 */

public class ParsingCombinator {
    public static final String[] zzz = new String[]{
            "^(\\d{1,2}\\W*\\s)+(###)",
            "^(((пн|вт|ср|чт|пт|сб|вск)\\W*)+)",
            "^(по выходным)",
            "^(по будням)"};
    public static final String[] ttt = new String[]{
            "(с)\\s\\d{1,2}\\s(###)",
            "(по)\\s\\d{1,2}\\s(###)",
            "(кроме)[\\s\\d{1,2}\\W\\s]+(###)",
            "(кроме)\\s((пн|вт|ср|чт|пт|сб|вск)\\W*)+"};
    private HashMap<String, String> map;
    private String inputString;
    private String queryString;
    private int currentYear;
    private static String currentMonthName;
    //private static String nextMonthName;
    private static String currentMonthNum;
    //private static String nextMonthNum;
    private static final String dateCol = "'NOW'";//"date('NOW' ,'localtime')";
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
            put("вск", "0");
        }};

        Calendar calendar = new GregorianCalendar(new Locale("ru_RU"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        currentMonthNum = dateFormat.format(calendar.getTime());

        dateFormat = new SimpleDateFormat("MMMM", new Locale("ru_RU"));

        currentMonthName = dateFormat.format(calendar.getTime());

        calendar.add(Calendar.MONTH, 1);
        //nextMonthName = dateFormat.format(calendar.getTime());
        dateFormat = new SimpleDateFormat("MM");
        //nextMonthNum = dateFormat.format(calendar.getTime());


        listv = new MyArrayList<String[]>(zzz.length * 2);

        for (String iterable_element : zzz) {
            if (iterable_element.contains("###")) {
                //listv.add(new String[]{iterable_element.replace("###", currentMonthName),
                //    iterable_element.replace("###", currentMonthName) + ".*"});

                listv.add(new String[]{iterable_element.replace("###", currentMonthName),
                        iterable_element.replace("###", currentMonthName) + ".*"});
            } else
                listv.add(new String[]{iterable_element, iterable_element + ".*"});
        }
        for (String iterable_element : ttt) {
            if (iterable_element.contains("###")) {
                //listv.add(new String[]{iterable_element.replace("###", currentMonthName),
                //        ".*" + iterable_element.replace("###", currentMonthName) + ".*"});
                listv.add(new String[]{iterable_element.replace("###", currentMonthName),
                        ".*" + iterable_element.replace("###", currentMonthName) + ".*"});
            } else
                listv.add(new String[]{iterable_element, ".*" + iterable_element + ".*"});
        }
    }

    public String getFormedQuery(String s) {

        if (s.equals("ежедневно"))
            return "";

        resetQueryString();
        setInputString(s);

        for (String[] current : listv) {
            if (containsPart(current[1])) {
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
        /*
		Pattern p;
		if(param != 2 || param != 3)
		{
			if(param == 2 || param == 7)
				p = Pattern.compile("(пн|вт|ср|чт|пт|сб|вск)+");
			else
				p = Pattern.compile("\\d{1,2}");
			Matcher m = p.matcher(partString);
		}
		*/
        String s = "";
        Pattern p;
        Matcher m;
        switch (param) {
            case 0:
                p = Pattern.compile("\\d{1,2}");
                m = p.matcher(partString);
                while (m.find()) {
                    if (s.equals("")) {
                        s = "(" + dateCol + " = \"" + currentYear + "-" + currentMonthNum + "-" + m.group() + "\"";
                    } else {
                        s += " OR " + dateCol + " = \"" + currentYear + "-" + currentMonthNum + "-" + m.group() + "\"";
                    }
                }
                s += ") ";

                break;
            case 1:

                p = Pattern.compile("(пн|вт|ср|чт|пт|сб|вск)+");
                m = p.matcher(partString);
                while (m.find()) {
                    if (s.equals("")) {
                        s = "(" + "strftime('%w', " + dateCol + ") = \"" + map.get(m.group()) + "\"";
                    } else {
                        s += " OR strftime('%w', " + dateCol + ") = \"" + map.get(m.group()) + "\"";
                    }
                }
                s += ")";

                break;
            case 2:
                s = "(strftime('%w', " + dateCol + ") = \"0\" OR strftime('%w', " + dateCol + ") = \"6\") ";
                break;
            case 3:
                s = "(strftime('%w', " + dateCol + ") != \"0\" AND strftime('%w', " + dateCol + ") != \"6\") ";
                break;
            case 4: //getAfterDateSQL
                Pattern p3 = Pattern.compile("\\d{1,2}");
                Matcher m3 = p3.matcher(partString);
                while (m3.find()) {
                    s = "(" + dateCol + " >= \"2015-" + currentMonthNum + "-" + m3.group() + "\")";
                }
                break;
            case 5: //getBeforeDateSQL
                p = Pattern.compile("\\d{1,2}");
                m = p.matcher(partString);
                while (m.find()) {
                    s = "(" + dateCol + " <= \"2015-" + currentMonthNum + "-" + m.group() + "\")";
                }

                break;
            case 6: //getExceptDateSQL
                p = Pattern.compile("\\d{1,2}");
                m = p.matcher(partString);
                while (m.find()) {
                    if (s.equals("")) {
                        s = "(" + dateCol + " != ";
                        s += "\"2015-" + currentMonthNum + "-" + m.group() + "\"";
                    } else {
                        s += " AND " + dateCol + " != \"2015-" + currentMonthNum + "-" + m.group() + "\"";
                    }
                }
                s += ")";
                break;
            case 7: //getExceptWeekDaySQL

                p = Pattern.compile("(пн|вт|ср|чт|пт|сб|вск)+");
                m = p.matcher(partString);
                while (m.find()) {
                    if (s.equals("")) {
                        s = "(" + "strftime('%w', " + dateCol + ") != \"" + map.get(m.group()) + "\"";
                    } else {
                        s += " AND strftime('%w', " + dateCol + ") != \"" + map.get(m.group()) + "\"";
                    }
                }
                s += ")";
                break;

            default:
                s = "(date('NOW') = date('1990.01.01'))";
                break;
        }

        if (isQueryStringEmpty())
            s = " WHERE " + s;
        else
            s = " AND " + s;

        appendQueryString(s);
        removePart(regExpr);
    }

}
