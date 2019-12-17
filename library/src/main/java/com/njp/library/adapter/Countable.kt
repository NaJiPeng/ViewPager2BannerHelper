package com.njp.library.adapter

/**
 * 通过此接口返回真实Item条目数
 */
interface Countable {
    fun getRealItemCount(): Int
}