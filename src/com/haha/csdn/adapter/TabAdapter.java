
package com.haha.csdn.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.haha.csdn.fragment.MainFragment;
import com.haha.splider.Utils.Type;

public class TabAdapter extends FragmentPagerAdapter {

    public static final Type[] TITLES = new Type[] {
            Type.BUSINESS, Type.MOBILE, Type.DEV, Type.PROGAMMER, Type.CLOUD
    };

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        MainFragment fragment = new MainFragment(TITLES[position]);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Type type = TITLES[position % TITLES.length];
        return type.getName();
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

}
