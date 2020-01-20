package com.njp.library.indicator.indicators;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.njp.library.R;
import com.njp.library.indicator.Indicator;
import com.njp.library.indicator.IndicatorHelper;

import org.jetbrains.annotations.NotNull;

/**
 * 滑动效果的Indicator
 */
public class SlideIndicator extends RelativeLayout implements Indicator {

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

    private ImageIndicator mImageIndicator;
    private ImageView mSelectedIndicator;

    public SlideIndicator(@NonNull Context context) {
        super(context);
        initialize(context, null);
    }

    public SlideIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public SlideIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    @Override
    public void setItemCount(int count) {
        if (mItemCount != count) {
            mItemCount = count;
            mImageIndicator.setItemCount(count);
            refresh();
        }
    }

    /**
     * 初始化
     */
    private void initialize(Context context, AttributeSet attrs) {

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        setLayoutParams(layoutParams);

        mImageIndicator = new ImageIndicator(context);
        mSelectedIndicator = new ImageView(context);
        addView(mImageIndicator);
        addView(mSelectedIndicator);

        if (attrs != null) {
            initAttrs(context, attrs);
        }

        initIndicators();
    }

    /**
     * 初始化属性
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlideIndicator);

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

    private void initIndicators() {
        mImageIndicator.setItemWidth(mItemWidth);
        mImageIndicator.setItemHeight(mItemHeight);
        mImageIndicator.setSelectedItemWidth(mItemWidth);
        mImageIndicator.setSelectedItemHeight(mItemHeight);
        mImageIndicator.setItemMargin(mItemMargin);
        mImageIndicator.setItemImageResource(mItemImageResource);
        mImageIndicator.setSelectedItemImageResource(mItemImageResource);


        LayoutParams layoutParams = new LayoutParams(mSelectedItemWidth, mSelectedItemHeight);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mSelectedIndicator.setLayoutParams(layoutParams);
        mSelectedIndicator.setImageResource(mSelectedItemImageResource);
    }

    private void refresh() {
        mSelectedIndicator.setTranslationX(calculateTranslation(mSelectedIndex));
    }

    private float calculateTranslation(int position) {
        float i = (mItemCount - 1) / 2.0f;
        float j = position - i;
        float t;
        if (mItemCount % 2 == 0) {
            t = j * (mItemMargin + mItemWidth);
        } else {
            t = j * mItemMargin;
        }
        return t;
    }

            @NotNull
            @Override
            public IndicatorHelper getHelper() {
                return mHelper;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("BBBB", "position:" + position + ", offset:" + positionOffset);
                if (position < mItemCount - 1) {
                    mSelectedIndicator.setTranslationX(calculateTranslation(position) + positionOffset * (mItemMargin));
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (mSelectedIndex != position) {
                    mSelectedIndex = position;
            refresh();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //DO NOTHING
    }
}
