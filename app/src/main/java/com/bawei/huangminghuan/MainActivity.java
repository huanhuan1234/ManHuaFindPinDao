package com.bawei.huangminghuan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.bawei.huangminghuan.fragment.FragmentFind;
import com.bawei.huangminghuan.fragment.FragmentHome;
import com.bawei.huangminghuan.fragment.FragmentMy;
import com.bawei.huangminghuan.fragment.FragmentVSheQu;


public class MainActivity extends FragmentActivity {

    private ViewPager vp;
    private RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //找控件
        vp = (ViewPager) findViewById(R.id.viwepage);
        rg = (RadioGroup) findViewById(R.id.rg);

        // 设置适配器
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {

                return 3;
            }

            //实例化fragment
            @Override
            public Fragment getItem(int arg0) {

                Fragment fragment = null;

                switch (arg0) {
                    case 0:

                        fragment = new FragmentHome();

                        break;

                    case 1:

                        fragment = new FragmentFind();

                        break;

                    case 2:

                        fragment = new FragmentVSheQu();

                        break;

                    case 3:

                        fragment = new FragmentMy();

                        break;
                }

                return fragment;
            }
        });


        //改变图片 按钮变
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                rg.check(rg.getChildAt(position).getId());

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub

            }
        });

        //点击按钮图片变
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                for (int i = 0; i < 3; i++) {

                    if (checkedId == rg.getChildAt(i).getId()) {
                        //设置切换页面没有滚动效果
                        vp.setCurrentItem(i, false);

                    }

                }

            }
        });

    }
}
