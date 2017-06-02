package com.bawei.huangminghuan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.bawei.huangminghuan.R;
import com.bawei.huangminghuan.bean.ManHuaBean;

import java.util.List;

/**
 * Created by Huangminghuan on 2017/6/2.
 */

public class FragmentHome extends Fragment {
    @Nullable
    private GridView gv;
    private List<ManHuaBean.ResultBean.IndexProductsBean> indexProducts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment2, container, false);

        return view;
    }


}
