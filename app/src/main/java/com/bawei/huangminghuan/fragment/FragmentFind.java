package com.bawei.huangminghuan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bawei.huangminghuan.bean.ManHuaBean;
import com.bawei.huangminghuan.R;
import com.bawei.huangminghuan.TwoActiviity;
import com.bawei.huangminghuan.utils.NetUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huangminghuan on 2017/6/2.
 */

public class FragmentFind extends Fragment {

    private LinearLayout ll;
    private ViewPager vp;
    private List<String> listIv;
    private MyAdapter adapter;
    private List<ImageView> listiv;
    private List<String> list1;
    private List<String> list2;

    private GridView gv;

    private List<ManHuaBean.ResultBean.IndexProductsBean> indexProducts;

//    4.轮播图+小圆点实现（数据brands）(10分)

    private Handler ha = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 发送延时消息 接收到消息之后向下选择一页
            if (msg.what == 0) {

                // 返回当前页的位置
                int current = vp.getCurrentItem();

                vp.setCurrentItem(current + 1);

                // 再次发送
                ha.sendEmptyMessageDelayed(0, 3000);

            }
        }
    };
    //将返回的String类型字符串转化成数组，方便适配，避免出现三张轮播图的BUG
    private String[] listIv1;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_find,container,false);

        ll = (LinearLayout) view.findViewById(R.id.ll);
        vp = (ViewPager) view.findViewById(R.id.viewpage);
        gv = (GridView) view.findViewById(R.id.gv);

        TextView tv = (TextView) view.findViewById(R.id.tvmore);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TwoActiviity.class);

                i.putStringArrayListExtra("list1", (ArrayList<String>) list1);
                i.putStringArrayListExtra("list2", (ArrayList<String>) list2);
                startActivity(i);
            }
        });



//        判断当前网络类型为WIFI
        if ( NetUtils.GetNetype(getActivity()).equals("WIFI")){

            new Thread(new Runnable() {
                @Override
                public void run() {

                    initData();

                }
            }).start();

        }else{

            Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
            startActivity(intent);

        }

        return view;
    }

    private void initData() {

        //请求数据
        RequestParams rp = new RequestParams("http://www.babybuy100.com/API/getShopOverview.ashx");

        x.http().get(rp, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Gson gson = new Gson();
                ManHuaBean mainBean = gson.fromJson(result, ManHuaBean.class);


                // 5.GridView展示indexProducts里的数据(name,pic)并使用imageloader加载图片 (pic)(10分)
                indexProducts = mainBean.getResult().getIndexProducts();
                MyGv adapterGv = new MyGv();
                gv.setAdapter(adapterGv);

                // 6.点击更多跳转到频道页面并传递数据category和nations(10分)
                List<ManHuaBean.ResultBean.CategoryBean> category = mainBean.getResult().getCategory();
                List<ManHuaBean.ResultBean.NationsBean> nations = mainBean.getResult().getNations();

                list1 = new ArrayList<String>();
                list2 = new ArrayList<String>();

                for (ManHuaBean.ResultBean.CategoryBean mc:category ) {
                    list1.add(mc.getName());
                }

                for (ManHuaBean.ResultBean.NationsBean mn:nations ) {

                    list2.add(mn.getName());
                }

                listIv = new ArrayList<>();
                // 4.轮播图+小圆点实现（数据brands）(10分)
                List<ManHuaBean.ResultBean.BrandsBean> brandsBeanList = mainBean.getResult().getBrands();
                for (ManHuaBean.ResultBean.BrandsBean mr:brandsBeanList ) {

                    listIv.add(mr.getPic());
                }

                listIv1 = new String[listIv.size()];

                for (int i = 0; i <listIv.size(); i++) {
                    listIv1[i] = listIv.get(i);
                }

                adapter = new MyAdapter();
                vp.setAdapter(adapter);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("msg","onError");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("msg","onCancelled");
            }

            @Override
            public void onFinished() {
                Log.e("msg","onFinished");

                // 加载圆点
                initCirl();

                Log.e("msg",listiv.size()+"==="+listIv.size()+"");

                vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int arg0) {

                        for (int i = 0; i < listIv1.length; i++) {

                            if (i == arg0%listiv.size()) {

                                ImageView iv = listiv.get(i);
                                iv.setImageResource(R.drawable.a);
                            } else {

                                ImageView iv = listiv.get(i);
                                iv.setImageResource(R.drawable.b);

                            }
                        }

                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {

                    }
                });

                // 发送延时消息
                ha.sendEmptyMessageDelayed(0, 3000);

                // 将初始位置设置在中间
                vp.setCurrentItem(listIv.size() * 10000);
            }
        });

    }

//画点
    private void initCirl() {

        listiv = new ArrayList<ImageView>();

        listiv.clear();
        ll.removeAllViews();

        for (int i = 0; i < listIv.size(); i++) {

            ImageView iv = new ImageView(getActivity());

            if (i == 0) {

                iv.setImageResource(R.drawable.a);
            } else {

                iv.setImageResource(R.drawable.b);
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(5, 0, 5, 0);

            ll.addView(iv, params);

            listiv.add(iv);

        }

    }

//轮播的适配
    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {

            //            return listIv != null ? listIv.size() : 0;
            return  Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            ImageView imageView = new ImageView(getActivity());


            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:

                            ha.removeCallbacksAndMessages(null);

                            break;

                        case MotionEvent.ACTION_MOVE:

                            ha.removeCallbacksAndMessages(null);

                            break;

                        case MotionEvent.ACTION_UP:

                            ha.sendEmptyMessageDelayed(0, 3000);

                            break;

                        case MotionEvent.ACTION_CANCEL:

                            ha.sendEmptyMessageDelayed(0, 3000);

                            break;

                    }


                    return true;
                }
            });

            ImageLoader.getInstance().displayImage(listIv1[position % listIv1.length] , imageView);

            container.addView(imageView);

            return  imageView ;
        }
    }


    class MyGv extends BaseAdapter {

        @Override
        public int getCount() {
            return indexProducts!=null?indexProducts.size():0;
        }

        @Override
        public Object getItem(int position) {
            return indexProducts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder vh;

            if (convertView == null){

                vh = new ViewHolder();

                convertView = View.inflate(getActivity(),R.layout.gridview,null);

                vh.iv = (ImageView) convertView.findViewById(R.id.gride_iv);
                vh.tv = (TextView) convertView.findViewById(R.id.gride_tv1);

                convertView.setTag(vh);
            }else{

                vh = (ViewHolder) convertView.getTag();

            }

            ImageLoader.getInstance().displayImage(indexProducts.get(position).getPic(),vh.iv);
            vh.tv.setText(indexProducts.get(position).getName());



            return convertView;
        }

        class ViewHolder {

            ImageView iv;
            TextView tv;
        }
    }


}
