package com.bawei.huangminghuan;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bawei.huangminghuan.bean.ItemBean;
import com.bawei.huangminghuan.bean.ManHuaBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huangminghuan on 2017/6/2.
 */

public class TwoActiviity extends Activity {

    private RecyclerView mineView;
    private RecyclerView otherView;
    private List<ManHuaBean.ResultBean.CategoryBean> category = new ArrayList<>();
    private List<ManHuaBean.ResultBean.NationsBean> nations = new ArrayList<>();
    private List<ItemBean> mineData = new ArrayList<ItemBean>();
    private List<ItemBean> otherData = new ArrayList<ItemBean>();
    private MyrecyAdapter otherAdapter;
    private TextView edit;
    boolean isediting = false;
    private MyrecyAdapter mineAdapter;
    private ArrayList<String> list1;
    private ArrayList<String> list2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        list1 = new ArrayList<String>();
        list1 = getIntent().getStringArrayListExtra("list1");

        list2 = new ArrayList<String>();
        list2 = getIntent().getStringArrayListExtra("list2");


        initView();
        initData();
        mineAdapter.notifyDataSetChanged();
        otherAdapter.notifyDataSetChanged();
    }

    private void initView() {
        {
            mineView = (RecyclerView) findViewById(R.id.mine_recycleView);
            edit = (TextView) findViewById(R.id.edit);
            mineAdapter = new MyrecyAdapter(this, mineData, 0);

            mineView.setLayoutManager(new GridLayoutManager(TwoActiviity.this, 3));
            mineView.setAdapter(mineAdapter);

            otherView = (RecyclerView) findViewById(R.id.other_recycleView);
            otherView.setLayoutManager(new GridLayoutManager(TwoActiviity.this, 3));
            otherAdapter = new MyrecyAdapter(this, otherData, 1);
            otherView.setAdapter(otherAdapter);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isediting) {
                        edit.setText("编辑");
                        for (int i = 0; i < mineData.size(); i++) {
                            mineData.get(i).setSelect(false);
                        }
                    } else {
                        edit.setText("完成");
                        for (int i = 0; i < mineData.size(); i++) {
                            mineData.get(i).setSelect(true);
                        }
                    }
                    isediting = !isediting;
                    mineAdapter.notifyDataSetChanged();

                }
            });
            //下面的条目触摸事件
            otherView.addOnItemTouchListener(new RecyclerItemClickListener(TwoActiviity.this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, final int position, RecyclerView.ViewHolder childViewViewHolder) {

                    final ImageView moveImageView = getView(view);

                    if (moveImageView != null) {
                        TextView newTextView = (TextView) view.findViewById(R.id.channel_tv);
                        final int[] startLocation = new int[2];
                        newTextView.getLocationInWindow(startLocation);
                        final ItemBean channel = otherAdapter.getItem(position);
                        if (isediting) {
                            channel.setSelect(true);
                        } else {
                            channel.setSelect(false);
                        }
                        mineAdapter.addItem(channel);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    RecyclerView.LayoutManager mine_layoutManager = mineView.getLayoutManager();
                                    if (mine_layoutManager instanceof LinearLayoutManager) {
                                        LinearLayoutManager linearManager = (LinearLayoutManager) mine_layoutManager;
                                        int lastItemPosition = linearManager.findLastVisibleItemPosition();
                                        mineView.getChildAt(lastItemPosition).getLocationInWindow(endLocation);
                                        //                                    MoveAnim(moveImageView, startLocation, endLocation);
                                        otherAdapter.deleteItem(position);
                                    }
                                } catch (Exception localException) {
                                }
                            }
                        }, 50L);
                    }
                }

                @Override
                public void onLongClick(View view, int posotion, RecyclerView.ViewHolder childViewViewHolder) {

                }
            }));

        }


    }

    private void initData() {


        for (String s:list1) {

            mineData.add(new ItemBean(s, false));
        }

        for (String s:list2) {

            otherData.add(new ItemBean(s, false));
        }
    }

    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

    public void GetDate(List<ManHuaBean.ResultBean.CategoryBean> category, List<ManHuaBean.ResultBean.NationsBean> nations) {
        this.category.addAll(category);
        this.nations.addAll(nations);

    }


    public class MyrecyAdapter extends RecyclerView.Adapter<MyrecyAdapter.MyViewHolder> implements ItemTouchHelperAdapter {

        private List<ItemBean> mData;
        private int type;
        private Context context;

        public MyrecyAdapter(Context context, List<ItemBean> list, int type) {
            this.context = context;
            this.mData = list;
            this.type = type;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(context, R.layout.list_view, null);
            MyViewHolder holder = new MyViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            //给每一个View设置内容
            holder.channel_tv.setText(mData.get(position).getText());
            //48-51  去掉other前面的图片
            if (type == 1) {
                holder.delete_tv.setVisibility(View.GONE);
            }
            //52-56去掉mine前面的图片
            else if (type == 0) {
                if (position == 0 || position == 1) {
                    holder.delete_tv.setVisibility(View.GONE);
                } else if (mData.get(position).isSelect()) {
                    holder.delete_tv.setVisibility(View.VISIBLE);
                } else {
                    holder.delete_tv.setVisibility(View.GONE);
                }
            }

            holder.delete_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type == 0) {
                        ItemBean positionItemBean = mineAdapter.getItem(position);
                        positionItemBean.setSelect(false);
                        mineAdapter.deleteItem(position);
                        otherAdapter.addItem(positionItemBean);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public ItemBean getItem(int position) {
            return mData.get(position);
        }

        public void addItem(ItemBean channel) {
            mData.add(channel);
            notifyDataSetChanged();
        }


        public void deleteItem(int position) {
            mData.remove(position);
            notifyDataSetChanged();
        }

        //数据交换
        @Override
        public void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
            if (type == 0) {
                int fromPosition = source.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                if (toPosition == 0 || toPosition == 1) {
                } else {
                    if (fromPosition < mData.size() && toPosition < mData.size()) {
                        //交换数据位置
                        //                        Collections.swap(mData, fromPosition, toPosition);
                        ItemBean itemBean = mData.get(fromPosition);
                        mData.remove(fromPosition);
                        mData.add(toPosition, itemBean);
                        //刷新位置交换
                        notifyItemMoved(fromPosition, toPosition);
                    }
                }
                //移动过程中移除view的放大效果
                onItemClear(source);
            }
        }

        //数据删除
        @Override
        public void onItemDissmiss(RecyclerView.ViewHolder source) {
            if (type == 0) {
                int position = source.getAdapterPosition();
                mData.remove(position); //移除数据
                notifyItemRemoved(position);//刷新数据移除
            }
        }

        @Override
        public void onItemSelect(RecyclerView.ViewHolder viewHolder) {
            if (type == 0) {
                //当拖拽选中时放大选中的view
                int position = viewHolder.getAdapterPosition();
                viewHolder.itemView.setScaleX(1.2f);
                viewHolder.itemView.setScaleY(1.2f);
            }
        }

        @Override
        public void onItemClear(RecyclerView.ViewHolder viewHolder) {
            //拖拽结束后恢复view的状态
            if (type == 0) {
                viewHolder.itemView.setScaleX(1.0f);
                viewHolder.itemView.setScaleY(1.0f);
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView channel_tv;
            private TextView delete_tv;
            private RelativeLayout channel_rl;

            public MyViewHolder(View itemView) {
                super(itemView);

                channel_tv = (TextView) itemView.findViewById(R.id.channel_tv);
                delete_tv = (TextView) itemView.findViewById(R.id.delete_tv);
                channel_rl = (RelativeLayout) itemView.findViewById(R.id.channel_rl);
            }


        }
    }


}
