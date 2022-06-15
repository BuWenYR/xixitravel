package com.example.zhengjun.helloandroid;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.zhengjun.helloandroid.fragment.FirstFragment;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.viewpager.SViewPager;

import com.example.zhengjun.helloandroid.fragment.MineFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends FragmentActivity {
    private IndicatorViewPager indicatorViewPager;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_main);
        SViewPager viewPager = (SViewPager) findViewById(R.id.main_viewPager);
        Indicator indicator = (Indicator) findViewById(R.id.main_indicator);
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));


    }

    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private String[] tabNames = {"首页", "我的"};
        private int[] tabIcons = {R.drawable.main_1_selector,
                R.drawable.main_4_selector};
        private LayoutInflater inflater;

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            inflater = LayoutInflater.from(getApplicationContext());
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }

        @Override
        public View getViewForTab(int position, View convertView,
                                  ViewGroup container) {
            if (convertView == null) {
                convertView = (TextView) inflater.inflate(R.layout.main,
                        container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(tabNames[position]);
            Drawable d = getResources().getDrawable(tabIcons[position]);
            d.setBounds(0, 0, 46, 46);
            textView.setCompoundDrawables(null, d, null, null);
            return textView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new FirstFragment();
                    break;
                case 1:
                    fragment = new MineFragment();
                    break;

                default:
                    break;
            }
            return fragment;
        }
    }

}