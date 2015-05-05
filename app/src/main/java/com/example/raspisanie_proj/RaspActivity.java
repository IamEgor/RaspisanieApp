package com.example.raspisanie_proj;

import Adapters.MyPagerAdapterRasp;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;

public class RaspActivity extends FragmentActivity {
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_rasp);


        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapterRasp(getSupportFragmentManager()));
        PagerTabStrip strip = (PagerTabStrip) findViewById(R.id.pagerTabStrip);
        strip.setTabIndicatorColor(0x0060b7);
        strip.setMinimumHeight(7);


    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
		
	}

	
}