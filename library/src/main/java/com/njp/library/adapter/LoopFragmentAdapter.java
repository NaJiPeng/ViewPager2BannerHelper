package com.njp.library.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * 实现了无限滚动效果的FragmentStateAdapter
 */
public abstract class LoopFragmentAdapter extends FragmentStateAdapter implements Countable {
    public LoopFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public LoopFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public LoopFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    //mRecyclerView用来初始化下标位置
    private RecyclerView mRecyclerView = null;

    //返回最大数值来实现无限滚动效果
    @Override
    public int getItemCount() {
        int realCount = getRealItemCount();
        return realCount > 1 ? Integer.MAX_VALUE : realCount;
    }

    //通过取余计算出当前的下标
    private int calculateRealPosition(int position) {
        return position % getRealItemCount();
    }

    private final RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (getRealItemCount() != 0 && mRecyclerView != null) {
                //初始化：起始坐标设置到Int.MAX_VALUE/2附近，保证前后都可无限滚动
                mRecyclerView.scrollToPosition(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % getRealItemCount());
            }
        }
    };

    @NonNull
    @Override
    public final Fragment createFragment(int position) {
        return createRealFragment(calculateRealPosition(position));
    }

    @NonNull
    public abstract Fragment createRealFragment(int realPosition);


    @Override
    public final int getItemViewType(int position) {
        return getRealItemViewType(calculateRealPosition(position));
    }

    public int getRealItemViewType(int position) {
        return 0;
    }

    /**
     * 注册初始化事件
     * 因onAttachedToRecyclerView和onChanged在数据准备好或者数据变动的时候总有一个会被回调
     * 所以在这里注册监听并手动调用onChanged，以保证初始化操作的执行
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        registerAdapterDataObserver(mObserver);
        mObserver.onChanged();
    }

    /**
     * 注销监听并移除引用，防止出现内存泄漏的问题
     */
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
        unregisterAdapterDataObserver(mObserver);

    }
}
