package com.example.bremme.eva_projectg6;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;

/**
 * Created by BREMME on 22/10/15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence titles[];//titles van tabs
    private int numbTabs;//aantal tabs
    private RegisterTab1 tab1;
    private RegisterTab2 tab2;
    private RegisterTab3 tab3;
    public ViewPagerAdapter(FragmentManager fm ,CharSequence[] titles, int numbTabs) {
        super(fm);
        this.numbTabs = numbTabs;
        this.titles = titles;
        this.tab1 = new RegisterTab1();
        this.tab2 = new RegisterTab2();
        this.tab3 = new RegisterTab3(tab1,tab2);

    }
//returns fragment
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {

            case 0: return tab1;
            case 1: return tab2;
            case 2: return tab3;
            default: return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return numbTabs;
    }

}
