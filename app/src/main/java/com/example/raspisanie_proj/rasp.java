package com.example.raspisanie_proj;

import java.util.ArrayList;

public class rasp {
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

    public rasp(String mTimeOut, String mTimeLong, String mTimeIn, String mWent, String mCome) {
        this.mTimeOut = mTimeOut;
        this.mTimeIn = mTimeIn;
        this.mTimeLong = mTimeLong;
        this.mWent = mWent;
        this.mCome = mCome;
    }

    public rasp(String[] strings) {
        this.mTimeOut = strings[0];
        this.mTimeIn = strings[1];
        this.mTimeLong = strings[2];
        this.mWent = strings[3];
        this.mCome = strings[4];
    }

    public static ArrayList<rasp> getListRasp(ArrayList<String[]> list) {
        ArrayList<rasp> listRasp = new ArrayList<rasp>(list.size());

        for (String[] strings : list) {
            listRasp.add(new rasp(strings));
        }

        return listRasp;
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
