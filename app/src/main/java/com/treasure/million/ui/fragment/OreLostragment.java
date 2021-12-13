package com.treasure.million.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.treasure.million.R;
import com.treasure.million.adapter.FragmentAdapter;
import com.treasure.million.base.BaseFragment1;
import com.treasure.million.ui.TabActivity;
import com.treasure.million.util.StatusBarUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zt
 * @date: 2020/10/20
 * @name:OreLostragment
 */
public class OreLostragment extends BaseFragment1 implements ViewPager.OnPageChangeListener, View.OnClickListener {
    public TextView text_tab1, text_tab2, text_tab3, text_tab4,  text_name, text_history;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentAdapter mFragmentAdapter;
    private ViewPager mViewPager;



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_orelost;
    }

    @Override
    protected void initView() {
        text_history = getView(rootView, R.id.text_history);
        text_name = getView(rootView, R.id.text_name);
        mViewPager = getView(rootView, R.id.viewPager);
        text_tab1 = getView(rootView, R.id.text_tab1);
        text_tab2 = getView(rootView, R.id.text_tab2);
        text_tab3 = getView(rootView, R.id.text_tab3);
        text_tab4 = getView(rootView, R.id.text_tab4);
        text_tab1.setOnClickListener(new MyOnClickListener(0));
        text_tab2.setOnClickListener(new MyOnClickListener(1));
        text_tab3.setOnClickListener(new MyOnClickListener(2));
        text_tab4.setOnClickListener(new MyOnClickListener(3));
        mFragmentList.add(new OreFragment());
        mFragmentList.add(new TabFragment2());
        mFragmentList.add(new TabFragment4());
        mFragmentList.add(new TabFragment5());
        mFragmentAdapter = new FragmentAdapter(getChildFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(mFragmentList.size()); //预加载
        mViewPager.setOnPageChangeListener(this);
        text_history.setOnClickListener(this);
    }


    @Override
    protected void loadData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_history:
                startActivity(new Intent(getContext(), TabActivity.class));
                break;
        }
    }


    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            setView(index);
            mViewPager.setCurrentItem(index);
        }
    }

    public void setView(int index) {
        clealView();
        switch (index) {
            case 0:
                text_name.setText("行情");
                text_tab1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case 1:
                text_name.setText("挖矿");
                text_tab2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case 2:
                text_name.setText("资产");
                text_tab3.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case 3:
                text_name.setText("社区");
                text_tab4.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
        }
    }

    public void clealView() {
        text_history.setVisibility(View.GONE);
        text_tab1.setBackgroundColor(Color.parseColor("#BDCAFB"));
        text_tab2.setBackgroundColor(Color.parseColor("#BDCAFB"));
        text_tab3.setBackgroundColor(Color.parseColor("#BDCAFB"));
        text_tab4.setBackgroundColor(Color.parseColor("#BDCAFB"));
    }


    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtil.setTranslucentStatus(getActivity());
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setView(position);
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
