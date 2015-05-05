package Adapters;

import Fragments.FragmentToday;
import Fragments.FragmentTomorrow;
import Fragments.FragmentTwoStation;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapterRasp  extends FragmentPagerAdapter {
	  private final String Title[] = new String[] {"Сегодня","На все дни"};

      public MyPagerAdapterRasp(FragmentManager fm) {
          super(fm);
      }

      @Override
      public Fragment getItem(int pos) {
          switch(pos) {

          case 0: return FragmentToday.newInstance(1);
          case 1: return FragmentTomorrow.newInstance(2);
          
          default: return FragmentTomorrow.newInstance(2);
          }
      }
      
      @Override
      public int getCount() {
          return 2;
      }   
      
      @Override
  	public CharSequence getPageTitle(int position) {
  		return Title[position];
  	}
  }