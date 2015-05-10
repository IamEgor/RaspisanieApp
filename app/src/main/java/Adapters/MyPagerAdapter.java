package Adapters;

import Fragments.FragmentOneStation;
import Fragments.FragmentTwoStation;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class MyPagerAdapter extends FragmentStatePagerAdapter {


    private final String Title[] = new String[]{"По маршруту", "По станции"};

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        super.destroyItem(container, position, object);

    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos) {


            case 0:
                return FragmentTwoStation.newInstance(1);
            case 1:
                return FragmentOneStation.newInstance(2);

            default:
                return FragmentTwoStation.newInstance(1);
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

    @Override
    public Parcelable saveState() {
        return null;
    }

    public int getItemPosition(Object object) {


        return POSITION_NONE;
    }

}
   