package com.njp.library.indicator.indicators;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.njp.library.R;
import com.njp.library.indicator.Indicator;
import com.njp.library.indicator.IndicatorHelper;
import com.njp.library.indicator.IndicatorTransformer;

import org.jetbrains.annotations.NotNull;

/**
 * ImageView作为指示器
 */
public class ImageIndicator extends LinearLayout implements Indicator {

    private IndicatorHelper mHelper = new IndicatorHelper(this);
    private int mItemCount = 0;
    private int mSelectedIndex = -1;
    private int mItemWidth = getContext().getResources().getDimensionPixelSize(R.dimen.item_size);
    private int mItemHeight = getContext().getResources().getDimensionPixelSize(R.dimen.item_size);
    private int mSelectedItemWidth = getContext().getResources().getDimensionPixelSize(R.dimen.item_size);
    private int mSelectedItemHeight = getContext().getResources().getDimensionPixelSize(R.dimen.item_size);
    private int mItemMargin = getContext().getResources().getDimensionPixelSize(R.dimen.item_margin);
    private int mItemImageResource = R.drawable.default_item;
    private int mSelectedItemImageResource = R.drawable.active_item;
    private IndicatorTransformer mIndicatorTransformer = null;

    public ImageIndicator(Context context) {
        super(context);
        initialize(context, null);
    }

    public ImageIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public ImageIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    /**
     * 初始化
     */
    private void initialize(Context context, AttributeSet attrs) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        setLayoutParams(layoutParams);
        setGravity(Gravity.CENTER);

        if (attrs != null) {
            initAttrs(context, attrs);
        }
    }

    /**
     * 初始化属性
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageIndicator);

        mItemWidth = typedArray.getDimensionPixelSize(
                R.styleable.ImageIndicator_item_width,
                getContext().getResources().getDimensionPixelSize(R.dimen.item_size)
        );
        mItemHeight = typedArray.getDimensionPixelSize(
                R.styleable.ImageIndicator_item_height,
                getContext().getResources().getDimensionPixelSize(R.dimen.item_size)
        );
        mSelectedItemWidth = typedArray.getDimensionPixelSize(
                R.styleable.ImageIndicator_selected_item_width,
                mItemWidth
        );
        mSelectedItemHeight = typedArray.getDimensionPixelSize(
                R.styleable.ImageIndicator_selected_item_height,
                mItemHeight
        );
        mItemMargin = typedArray.getDimensionPixelSize(
                R.styleable.ImageIndicator_item_margin,
                getContext().getResources().getDimensionPixelSize(R.dimen.item_margin)
        );
        mItemImageResource = typedArray.getResourceId(
                R.styleable.ImageIndicator_item_src,
                R.drawable.default_item
        );
        mSelectedItemImageResource = typedArray.getResourceId(
                R.styleable.ImageIndicator_selected_item_src,
                R.drawable.active_item
        );
        typedArray.recycle();
    }

    /**
     * 初始化indicators
     */
    private void initIndicators() {
        removeAllViews();
        for (int i = 0; i < mItemCount; i++) {
            int margin = mItemMargin / 2;
            int horizontalMargin = getOrientation() == HORIZONTAL ? margin : 0;
            int verticalMargin = getOrientation() == VERTICAL ? margin : 0;

            ImageView itemView = new ImageView(getContext());
            LayoutParams layoutParams = new LayoutParams(mItemWidth, mItemHeight);
            layoutParams.setMargins(horizontalMargin, verticalMargin, horizontalMargin, verticalMargin);
            itemView.setLayoutParams(layoutParams);
            itemView.setImageResource(mItemImageResource);
            addView(itemView);
        }
    }

    /**
     * 刷新视图状态
     */
    private void refresh() {
        for (int index = 0; index < getChildCount(); index++) {
            ImageView itemView = (ImageView) getChildAt(index);
            int width = index == mSelectedIndex ? mSelectedItemWidth : mItemWidth;
            int height = index == mSelectedIndex ? mSelectedItemHeight : mItemHeight;

            int margin = mItemMargin / 2;
            int horizontalMargin = getOrientation() == HORIZONTAL ? margin : 0;
            int verticalMargin = getOrientation() == VERTICAL ? margin : 0;

            int imageResource = index == mSelectedIndex ? mSelectedItemImageResource : mItemImageResource;

            LayoutParams layoutParams = (LayoutParams) itemView.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            layoutParams.setMargins(horizontalMargin, verticalMargin, horizontalMargin, verticalMargin);

            itemView.setLayoutParams(layoutParams);
            itemView.setImageResource(imageResource);
        }
    }

    public void setItemWidth(int itemWidth) {
        if (this.mItemWidth != itemWidth) {
            this.mItemWidth = itemWidth;
            refresh();
        }
    }

    public void setItemHeight(int itemHeight) {
        if (this.mItemHeight != itemHeight) {
            this.mItemHeight = itemHeight;
            refresh();
        }
    }

    public void setSelectedItemWidth(int selectedItemWidth) {
        if (mSelectedItemHeight != selectedItemWidth) {
            this.mSelectedItemWidth = selectedItemWidth;
            refresh();
        }
    }

    public void setSelectedItemHeight(int selectedItemHeight) {
        if (this.mSelectedItemHeight != selectedItemHeight) {
            this.mSelectedItemHeight = selectedItemHeight;
            refresh();
        }
    }

    public void setItemMargin(int itemMargin) {
        if (this.mItemMargin != itemMargin) {
            this.mItemMargin = itemMargin;
            refresh();
        }
    }

    public void setItemDrawableResource(int imageResource) {
        if (this.mItemImageResource != imageResource) {
            this.mItemImageResource = imageResource;
            refresh();
        }
    }

    public void setSelectedItemDrawableResource(int selectedImageResource) {
        if (this.mSelectedItemImageResource != selectedImageResource) {
            this.mSelectedItemImageResource = selectedImageResource;
            refresh();
        }
    }

    public void setIndicatorTransformer(IndicatorTransformer indicatorTransformer) {
        this.mIndicatorTransformer = indicatorTransformer;
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    @Override
    public void setItemCount(int count) {
        if (count != mItemCount) {
            this.mItemCount = count;
            initIndicators();
        }
    }

    @NotNull
    @Override
    public IndicatorHelper getHelper() {
        return mHelper;
    }

    @Override
    public void onPageSelected(int position) {
        if (position != mSelectedIndex) {
            mSelectedIndex = position;
            refresh();
        }
    }


    /**
     * 0表示未选中状态，1表示选中状态
     * 以下为从第二页转到第三页的回调状态：
     * 0.0——1.0——0.0——0.0
     * 0.0——0.8——0.2——0.0
     * 0.0——0.5——0.5——0.0
     * 0.0——0.3——0.7——0.0
     * 0.0——0.0——1.0——0.0
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mIndicatorTransformer != null) {
            View itemView = getChildAt(position % mItemCount);
            if (itemView != null) {
                mIndicatorTransformer.transformIndicator(itemView, 1 - positionOffset);
            }
            itemView = getChildAt((position + 1) % mItemCount);
            if (itemView != null) {
                mIndicatorTransformer.transformIndicator(itemView, positionOffset);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //DO NOTHING
    }
}
