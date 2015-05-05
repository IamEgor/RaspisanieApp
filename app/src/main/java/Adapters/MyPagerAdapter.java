package Adapters;

import Fragments.FragmentHistory;
import Fragments.FragmentOneStation;
import Fragments.FragmentTwoStation;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
	 
	 private String Title[]= new String[] {"История","По маршруту","По станции"};

       public MyPagerAdapter(FragmentManager fm) {
           super(fm);
       }

       @Override
       public Fragment getItem(int pos) {
           switch(pos) {

           case 0: return FragmentHistory.newInstance(1);
           case 1: return FragmentTwoStation.newInstance(2);
           case 2: return FragmentOneStation.newInstance(3);
           
           default: return FragmentTwoStation.newInstance(2);
           }
       }
       
       @Override
       public int getCount() {
           return 3;
       }   
       
       @Override
   	public CharSequence getPageTitle(int position) {
   		return Title[position];
   	}
   }