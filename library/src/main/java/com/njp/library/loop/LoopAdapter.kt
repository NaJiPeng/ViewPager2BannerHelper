package com.njp.library.loop

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*

/**
 * 实现了无限滚动效果的RecyclerViewAdapter
 */
abstract class LoopAdapter<VH : ViewHolder> : RecyclerView.Adapter<VH>() {

    //mRecyclerView用来初始化下标位置
    private var mRecyclerView: RecyclerView? = null

    //返回最大数值来实现无限滚动效果
    final override fun getItemCount(): Int = if (getRealItemCount() == 0) 0 else Int.MAX_VALUE

    //返回真实的数据条目数
    abstract fun getRealItemCount(): Int

    //通过取余计算出当前的下标
    private fun calculateRealPosition(position: Int) = position % getRealItemCount()

    private val mObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            if (getRealItemCount() != 0) {
                //初始化：起始坐标设置到Int.MAX_VALUE/2附近，保证前后都可无限滚动
                mRecyclerView?.scrollToPosition(Int.MAX_VALUE / 2 - Int.MAX_VALUE / 2 % getRealItemCount())
            }
        }
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        //直接计算出当前下标，交给子类实现
        onBindRealViewHolder(holder, calculateRealPosition(position))
    }

    abstract fun onBindRealViewHolder(holder: VH, realPosition: Int)

    final override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        //直接计算出当前下标，交给子类实现
        onBindRealViewHolder(holder, calculateRealPosition(position), payloads)
    }

    fun onBindRealViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        onBindRealViewHolder(holder, calculateRealPosition(position))
    }

    final override fun getItemId(position: Int): Long {
        //直接计算出当前下标，交给子类实现
        return getRealItemId(calculateRealPosition(position))
    }

    fun getRealItemId(realPosition: Int): Long {
        return NO_ID
    }

    final override fun getItemViewType(position: Int): Int {
        //直接计算出当前下标，交给子类实现
        return getRealItemViewType(calculateRealPosition(position))
    }

    fun getRealItemViewType(realPosition: Int): Int {
        return 0
    }

    /**
     * 注册初始化事件
     * 因onAttachedToRecyclerView和onChanged在数据准备好或者数据变动的时候总有一个会被回调
     * 所以在这里注册监听并手动调用onChanged，以保证初始化操作的执行
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
        registerAdapterDataObserver(mObserver)
        mObserver.onChanged()
    }

    /**
     * 注销监听并移除强引用，防止出现内存泄漏的问题
     */
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null
        unregisterAdapterDataObserver(mObserver)
    }
}