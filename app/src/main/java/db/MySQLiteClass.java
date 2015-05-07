package db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.raspisanie_proj.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Егор on 17.03.2015.
 */
public class MySQLiteClass {
    private static final String DATABASE_NAME       =   "timetable_Db";
    private static final int    DATABASE_VERSION    =   1;

    private static final String SCHEDULE_TABLE_DEPARTURE = "table_departure";
    private static final String SCHEDULE_TABLE_ARRIVAL   = "table_arrival";
    private static final String SCHEDULE_ID              = "sh_id";

    private static final String TRAIN_TABLE             =   "train_table";
    private static final String TRAIN_ID                =   "tr_id";
    private static final String TRAIN_NUMBER            =   "train_number";
    private static final String TRAIN_DIRECTION         =   "train_direction";
    private static final String TRAIN_CLASS             =   "train_class";
    private static final String TRAIN_DAYS              =   "train_days";
    private static final String TRAIN_DAYS_SQL_QUERY    =   "train_query";
    //private static final String TRAIN_STOPS = "train_stops";

    private static final String SET_CHECK = "SELECT \"something\"";

    public static final String LOG_TAG    = "MY_LOG_TAG";
    public static final String STAT_PREFS = "station_prefs";

    private final Context context;
    private DBHelp dbhelp;
    private SQLiteDatabase thisDataBase;
    private ParsingCombinator combinator;

    public MySQLiteClass(Context context) {
        this.context = context;
        combinator = new ParsingCombinator();
    }


    public MySQLiteClass open(boolean writable) throws SQLiteException {

        dbhelp = new DBHelp(context);

        if (writable)
            thisDataBase = dbhelp.getWritableDatabase();
        else
            thisDataBase = dbhelp.getReadableDatabase();
        return this;
    }

    public void close() {
        dbhelp.close();
    }

    private int addTrainUnsafe(String train, String direction, String days, String query)
    {
        //open(true);
        ContentValues values = new ContentValues();

        values.put(TRAIN_NUMBER, train);
        values.put(TRAIN_DIRECTION, direction);
        values.put(TRAIN_DAYS, days);
        values.put(TRAIN_DAYS_SQL_QUERY, query);

        int currentRow = (int) thisDataBase.insert(TRAIN_TABLE, null, values);

        values.clear();
        values.put(SCHEDULE_ID, currentRow);
        thisDataBase.insert(SCHEDULE_TABLE_ARRIVAL, null, values);
        thisDataBase.insert(SCHEDULE_TABLE_DEPARTURE, null, values);
        //close();
        return currentRow;
    }
    private void addArrivalTimeUnsafe(int trainId, String columnName, String arrivalTime, String departureTime)
    {
        //open(true);
        ContentValues values = new ContentValues();

        values.put("\"" + columnName + "\"", departureTime);
        thisDataBase.update(SCHEDULE_TABLE_DEPARTURE, values, SCHEDULE_ID + " = " + trainId, null);

        values.clear();
        values.put("\"" + columnName + "\"", arrivalTime);
        thisDataBase.update(SCHEDULE_TABLE_ARRIVAL, values, SCHEDULE_ID + " = " + trainId , null);//new String[]{String.valueOf(trainId)});


        //close();
    }
    private String getStationName(Document document, String prefix) {
        Elements stationNameElement = document.body().getElementsMatchingText(prefix);
        String messageText = stationNameElement.last().text();
        return messageText.substring(prefix.length() + 1);
    }

    private String getTrainNumber(String stringWithNumber) {
        return stringWithNumber.substring(0, stringWithNumber.indexOf(" "));
    }
    private String getTrainDirection(String stringWithDirection) {
        return stringWithDirection.substring(stringWithDirection.indexOf(" ") + 1);
    }
    private int isTrainExistUnsafe(String tableName, String trainNum, String direct, String days) {

        Cursor c = thisDataBase.rawQuery(
                "SELECT " + TRAIN_ID +
                        " FROM " + tableName +
                        " WHERE " +
                        TRAIN_NUMBER + " = ? AND " +
                        TRAIN_DIRECTION + " = ? AND " +
                        TRAIN_DAYS + " = ?",
                new String[] { trainNum, direct, days});
        int trainId = -1;
        if (c.moveToFirst())
            trainId = c.getInt(c.getColumnIndex(TRAIN_ID));
        c.close();
        return trainId;
    }

    private void addColumnUnsafe(String tableName, String columnName) {
        thisDataBase.execSQL("ALTER TABLE " + tableName + " ADD COLUMN \"" + columnName + "\" TEXT");
    }
    private void dropTables() {
        open(true);
        thisDataBase.execSQL("DROP TABLE" + SCHEDULE_TABLE_ARRIVAL);
        thisDataBase.execSQL("DROP TABLE" + SCHEDULE_TABLE_DEPARTURE);
        thisDataBase.execSQL("DROP TABLE" + TRAIN_TABLE);
        close();
    }

    public static String getCurrentTime()
    {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat(" dd MMMM в HH:mm:ss", new Locale("ru_RU"));
        return dateFormat.format(calendar.getTime());
    }
    public ArrayList<String[]> getScheduleEveryDay(String station1, String station2)
    {

        //open(false);

        ArrayList<String[]> strings = new ArrayList<String[]>();///***
        try {///***
            String set = "SELECT table_arrival.[" + station1 + "], table_departure.[" + station2 + "],\n" +
                    "train_table.train_number, train_table.train_direction, train_table.train_days, train_table.train_query\n" +
                    "FROM table_arrival, table_departure, train_table\n" +
                    "WHERE table_arrival.sh_id = table_departure.sh_id \n" +
                    "AND table_arrival.sh_id = train_table.tr_id\n" +
                    "AND table_arrival.[" + station1 + "] NOT NULL\n" +
                    "AND table_departure.[" + station2 + "] NOT NULL\n" +
                    "AND time(table_arrival.[" + station1 + "]) < time(table_departure.[" + station2 + "])";

            //Log.d(LOG_TAG, set);///***
            String arrival, departure, number, direction, days;///***
            Cursor c = thisDataBase.rawQuery(set, null);

            while (c.moveToNext()) {
                departure = c.getString(c.getColumnIndex(station1));
                arrival = c.getString(c.getColumnIndex(station2));
                number = c.getString(c.getColumnIndex(TRAIN_NUMBER));
                direction = c.getString(c.getColumnIndex(TRAIN_DIRECTION));
                days = c.getString(c.getColumnIndex(TRAIN_DAYS));
                strings.add(new String[]{departure, arrival, timeDifference(departure, arrival), number + direction, "\n" + days});

            }
            c.close();
        }catch (SQLiteException e)
        {
            strings.add(new String[]{""});
        }

        return strings;
    }
    public ArrayList<String[]> getScheduleForOneStation(String station)///***
    {

        //open(false);

        ArrayList<String[]> strings = new ArrayList<String[]>();///***
        try {///***
            String set = "SELECT D.[" + station + "] as departure,  A.[" + station + "] as arrival,\n" +
                    "    T.train_number, T.train_direction, T.train_days, T.train_query\n" +
                    "    FROM table_arrival A, table_departure D, train_table T\n" +
                    "    WHERE D.sh_id = T.tr_id    \n" +
                    "    AND D.sh_id = A.sh_id\n" +
                    "    AND D.[" + station + "] NOT NULL";

            String arrival, departure, number, direction, days;
            Cursor c = thisDataBase.rawQuery(set, null);

            while (c.moveToNext()) {
                arrival = c.getString(c.getColumnIndex("arrival"));
                departure = c.getString(c.getColumnIndex("departure"));
                number = c.getString(c.getColumnIndex(TRAIN_NUMBER));
                direction = c.getString(c.getColumnIndex(TRAIN_DIRECTION));
                days = c.getString(c.getColumnIndex(TRAIN_DAYS));
                strings.add(new String[]{departure, arrival, number + direction, days});

            }
            c.close();
        }catch (SQLiteException e)
        {
            strings.add(new String[]{""});
        }
        return strings;
    }
    public ArrayList<String[]> getScheduleNow(String station1, String station2)
    {

        //open(false);
        ArrayList<String[]> strings = new ArrayList<String[]>();///***
        try {///***
            String set = "SELECT table_arrival.[" + station1 + "], table_departure.[" + station2 + "],\n" +
                    "train_table.train_number, train_table.train_direction, train_table.train_days, train_table.train_query\n" +
                    "FROM table_arrival, table_departure, train_table\n" +
                    "WHERE table_arrival.sh_id = table_departure.sh_id \n" +
                    "AND table_arrival.sh_id = train_table.tr_id\n" +
                    "AND table_arrival.[" + station1 + "] NOT NULL\n" +
                    "AND table_departure.[" + station2 + "] NOT NULL\n" +
                    "AND time(table_arrival.[" + station1 + "]) < time(table_departure.[" + station2 + "])\n" +
                    "AND time(table_arrival.[" + station1 + "]) > time('NOW','localtime')";

            //Log.d(LOG_TAG, set);///***

            String id, arrival, departure, number, direction, days, query;
            Cursor c = thisDataBase.rawQuery(set, null);
            Cursor innerCursor = null;
            while (c.moveToNext()) {
                //id = c.getString(c.getColumnIndex(TRAIN_ID));
                departure = c.getString(c.getColumnIndex(station1));
                arrival = c.getString(c.getColumnIndex(station2));
                number = c.getString(c.getColumnIndex(TRAIN_NUMBER));
                direction = c.getString(c.getColumnIndex(TRAIN_DIRECTION));
                days = c.getString(c.getColumnIndex(TRAIN_DAYS));
                query = c.getString(c.getColumnIndex(TRAIN_DAYS_SQL_QUERY));
                //Log.d(LOG_TAG, "____________");
                innerCursor = thisDataBase.rawQuery(SET_CHECK + query, null);
                while (innerCursor.moveToNext()) {
                    strings.add(new String[]{departure, arrival, timeDifference(departure, arrival), number, direction});
                    //Log.d(LOG_TAG, arrival+" "+departure+" "+number+" "+direction+" "+days);
                    //Log.d(LOG_TAG, " | " + SET_CHECK + query);
                    //Log.d(LOG_TAG, "returned - " + innerCursor.getString(0));
                }

            }
            c.close();
            innerCursor.close();
        }
        catch (SQLiteException e)
        {
            strings.add(new String[]{""});
        }
        //close();
        //Log.d(LOG_TAG, String.valueOf((new Date())));
        return strings;
    }
    public void fillingTest(Resources resources)
    {

        String[] stationURLs = resources.getStringArray(R.array.stations);
        open(true);
        resetDbUnsafe();
        ///////////////
        Log.d("downloading", "start");
        ArrayList<String> prefsArray = new ArrayList<String>();
        ArrayList<Document> documentsArray = new ArrayList<Document>();
        Document documentTemp;
        for (String currentStation : stationURLs) {
            try {
                documentTemp = Jsoup.connect(currentStation).get();
                documentsArray.add(documentTemp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Log.d("downloading", "finish");


        Elements trainElem, arrivalElem, daysElem;
        String stationName;

        for(Document currentDocument : documentsArray)
        {
            trainElem = currentDocument.body().getElementsByClass("b-train");
            arrivalElem = currentDocument.body().getElementsByClass("arrival");//только нечетн
            daysElem = currentDocument.body().getElementsByClass("days-except");
            daysElem.first();
            stationName = getStationName(currentDocument, resources.getString(R.string.prefix_get_station_name));

            addColumnUnsafe(SCHEDULE_TABLE_ARRIVAL, stationName);
            addColumnUnsafe(SCHEDULE_TABLE_DEPARTURE, stationName);

            for (int i = 2; i < arrivalElem.size(); i += 2) {
                String trainNumber = getTrainNumber(trainElem.get(i / 2 - 1).text());
                //trainElem.get(i / 2 - 1).getElementsMatchingOwnText("\\d+\\W").text();
                String direct = trainElem.get(i / 2 - 1).getElementsByTag("a").text();
                String clazz = trainElem.get(i / 2 - 1).getElementsByTag("small").text();
                String days = daysElem.get(i / 2 - 1).text();
                String arrival = arrivalElem.get(i + 1).text();
                String departure = arrivalElem.get(i).text();

                int trainId = isTrainExistUnsafe(TRAIN_TABLE, trainNumber, direct, days);

                if(trainId == -1) {
                    trainId = addTrainUnsafe(trainNumber, direct, days, combinator.getFormedQuery(days));
                    //Log.d("","addTrainUnsafe");
                }
                addArrivalTimeUnsafe(trainId, stationName, arrival, departure);

            }
            prefsArray.add(stationName);
        }
        ////
        StringBuilder sb = new StringBuilder();
        for (String s : prefsArray) {
            sb.append(s).append(",");
        }

        MyPrefs.setPrefs(context, STAT_PREFS, sb.toString());

        close();
        Log.d("downloading","finally finished");
    }
    private void resetDbUnsafe()
    {
        thisDataBase.execSQL("DROP TABLE IF EXISTS " + SCHEDULE_TABLE_ARRIVAL);
        thisDataBase.execSQL("DROP TABLE IF EXISTS " + SCHEDULE_TABLE_DEPARTURE);
        thisDataBase.execSQL("DROP TABLE IF EXISTS " + TRAIN_TABLE);

        thisDataBase.execSQL(DBHelp.CREATE_TRAIN_TABLE);
        thisDataBase.execSQL(DBHelp.CREATE_ARRIVAL_TABLE);
        thisDataBase.execSQL(DBHelp.CREATE_DEPARTURE_TABLE);
    }
    public String timeDifference(String departure, String arrival) {///***

        String hours, minutes;

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date1 = null, date2 = null;
        try {
            date1 = format.parse(departure);
            date2 = format.parse(arrival);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = TimeUnit.MILLISECONDS.toMinutes(date2.getTime() - date1.getTime());
        hours = String.valueOf(difference /60);
        minutes = String.valueOf(difference%60);

        return hours + " ч : " + minutes + " мин";
    }

    public static void copyDb(Context cont)
    {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            if (sd.canWrite()) {
                String packageName = cont.getPackageName();
                String currentDBPath = "//data//"+packageName+"//databases//"+DATABASE_NAME+"";
                String backupDBPath = DATABASE_NAME;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
            Log.d("%%%", "coped");
        } catch (Exception e) {
            Log.d("%%%", "exception");
            e.printStackTrace();
        }
    }

    private class DBHelp extends SQLiteOpenHelper {
        public static final String CREATE_ARRIVAL_TABLE =
                "CREATE TABLE " + SCHEDULE_TABLE_ARRIVAL + "(" +
                        SCHEDULE_ID + " INTEGER, " +
                        "FOREIGN KEY(" + SCHEDULE_ID + ") " +
                        "REFERENCES " + TRAIN_TABLE +"(" + TRAIN_ID + "));";

        public static final String CREATE_DEPARTURE_TABLE =
                "CREATE TABLE " + SCHEDULE_TABLE_DEPARTURE + "(" +
                        SCHEDULE_ID + " INTEGER, " +
                        "FOREIGN KEY(" + SCHEDULE_ID + ") " +
                        "REFERENCES " + TRAIN_TABLE +"(" + TRAIN_ID + "));";

        public static final String CREATE_TRAIN_TABLE =
                "CREATE TABLE " + TRAIN_TABLE + "(" +
                        TRAIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TRAIN_NUMBER + " TEXT NOT NULL, " +
                        TRAIN_DIRECTION + " TEXT NOT NULL, " +
                        TRAIN_DAYS + " TEXT NOT NULL, " +
                        TRAIN_DAYS_SQL_QUERY + " TEXT NOT NULL);";

        public DBHelp(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub

            db.execSQL(CREATE_TRAIN_TABLE);
            db.execSQL(CREATE_ARRIVAL_TABLE);
            db.execSQL(CREATE_DEPARTURE_TABLE);

            Log.d("MySQLiteClass", " onCreate()");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
            // TODO Auto-generated method stub
            dropTables();
            onCreate(db);
        }
    }
}