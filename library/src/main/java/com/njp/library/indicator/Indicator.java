package com.njp.library.indicator;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import org.jetbrains.annotations.NotNull;

/**
 * Indicator抽象接口
 */
public interface Indicator extends ViewPager.OnPageChangeListener {

    int getItemCount();

    void setItemCount(int count);

    default void registerViewPager2(@NotNull ViewPager2 viewPager2){
        getHelper().bind(viewPager2);
    }

    default void unregisterviewPager2(@NotNull ViewPager2 viewPager2){
        getHelper().unBind(viewPager2);
    }

    @NotNull IndicatorHelper getHelper();


}
