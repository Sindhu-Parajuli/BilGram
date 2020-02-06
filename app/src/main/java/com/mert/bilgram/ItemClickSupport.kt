package com.mert.bilgram

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener

class ItemClickSupport private constructor(private val mRecyclerView: RecyclerView) {
    private var mOnItemClickListener: OnItemClickListener? = null
    private val mOnDoubleClickListener: OnDoubleClickListener = object : OnDoubleClickListener() {
        override fun onDoubleClick(v: View?) {
            if (mOnItemClickListener != null)
                mOnItemClickListener!!.onItemDoubleClicked(mRecyclerView, mRecyclerView.getChildViewHolder(v!!).adapterPosition, v)
        }

        override fun onSingleClick(v: View?) {
            if (mOnItemClickListener != null)
                mOnItemClickListener!!.onItemClicked(mRecyclerView, mRecyclerView.getChildViewHolder(v!!).adapterPosition, v)
        }
    }
    private val mAttachListener: OnChildAttachStateChangeListener = object : OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            if (mOnItemClickListener != null)
                view.setOnClickListener(mOnDoubleClickListener)
        }

        override fun onChildViewDetachedFromWindow(view: View) {}
    }

    fun setOnItemClickListener(listener: OnItemClickListener?): ItemClickSupport {
        mOnItemClickListener = listener
        return this
    }

    private fun detach(view: RecyclerView) {
        view.removeOnChildAttachStateChangeListener(mAttachListener)
        view.setTag(R.id.recyclerView, null)
    }

    interface OnItemClickListener {
        fun onItemClicked(recyclerView: RecyclerView?, position: Int, v: View?)
        fun onItemDoubleClicked(recyclerView: RecyclerView?, position: Int, v: View?)
    }

    companion object {
        fun addTo(view: RecyclerView): ItemClickSupport {
            var support = view.getTag(R.id.recyclerView) as ItemClickSupport?
            if (support == null)
                support = ItemClickSupport(view)
            return support
        }

        fun removeFrom(view: RecyclerView): ItemClickSupport {
            val support = view.getTag(R.id.recyclerView) as ItemClickSupport
            support.detach(view)
            return support
        }
    }

    init {
        mRecyclerView.setTag(R.id.recyclerView, this)
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener)
    }
}