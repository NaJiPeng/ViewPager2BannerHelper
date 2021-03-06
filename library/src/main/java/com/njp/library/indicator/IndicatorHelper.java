package com.njp.library.indicator;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import androidx.core.view.ViewKt;
import com.njp.library.adapter.Countable;

/**
 * 处理Indicator通用逻辑的类
 */
public class IndicatorHelper extends ViewPager2.OnPageChangeCallback{

    private Indicator mIndicator;

    public IndicatorHelper(Indicator mIndicator) {
        this.mIndicator = mIndicator;
    }

    void bind(ViewPager2 viewPager2) {

        //为了监听Adapter变动和数据个数变动
        ViewKt.doOnNextLayout(viewPager2, view -> {
            bind(viewPager2);
            return null;
        });

        int count = 0;
        RecyclerView.Adapter adapter = viewPager2.getAdapter();
        if (adapter != null) {
            if (adapter instanceof Countable) {
                count = ((Countable) adapter).getRealItemCount();
            } else {
                count = adapter.getItemCount();
            }
        }
        mIndicator.setItemCount(count);

        viewPager2.unregisterOnPageChangeCallback(this);
        viewPager2.registerOnPageChangeCallback(this);

        mIndicator.onPageSelected(viewPager2.getCurrentItem() % mIndicator.getItemCount());
    }

    void unBind(ViewPager2 viewPager2) {
        viewPager2.unregisterOnPageChangeCallback(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mIndicator.onPageScrolled(position % mIndicator.getItemCount(), positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        mIndicator.onPageSelected(position % mIndicator.getItemCount());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mIndicator.onPageScrollStateChanged(state);
    }
}
