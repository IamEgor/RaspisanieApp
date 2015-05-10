package com.example.raspviewproj;

import java.util.ArrayList;

public class RaspisanieContent {
    private String mWent;
    private String mCome;
    private String mTimeOut;
    private String mTimeIn;
    private String mTimeLong;

    public String getmTimeOut() {
        return mTimeOut;
    }

    public void setmTimeOut(String mTimeOut) {
        this.mTimeOut = mTimeOut;
    }

    public String getmTimeIn() {
        return mTimeIn;
    }

    public void setmTimeIn(String mTimeIn) {
        this.mTimeIn = mTimeIn;
    }

    public String getmTimeLong() {
        return mTimeLong;
    }

    public void setmTimeLong(String mTimeLong) {
        this.mTimeLong = mTimeLong;
    }

    public RaspisanieContent(String mTimeOut, String mTimeLong, String mTimeIn, String mWent, String mCome) {
        this.mTimeOut = mTimeOut;
        this.mTimeIn = mTimeIn;
        this.mTimeLong = mTimeLong;
        this.mWent = mWent;
        this.mCome = mCome;
    }

    public RaspisanieContent(String[] strings) {
        this.mTimeOut = strings[0];
        this.mTimeIn = strings[1];
        this.mTimeLong = strings[2];
        this.mWent = strings[3];
        this.mCome = strings[4];
    }

    public static ArrayList<RaspisanieContent> getListRasp(ArrayList<String[]> list) {
        ArrayList<RaspisanieContent> listRaspisanieContent = new ArrayList<RaspisanieContent>(list.size());

        for (String[] strings : list) {
            listRaspisanieContent.add(new RaspisanieContent(strings));
        }

        return listRaspisanieContent;
    }

    public String getmWent() {
        return mWent;
    }

    public void setmWent(String mWent) {
        this.mWent = mWent;
    }

    public String getmCome() {
        return mCome;
    }

    public void setmCome(String mCome) {
        this.mCome = mCome;
    }


}
