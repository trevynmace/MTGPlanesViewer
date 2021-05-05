package com.trevynmace.mtgplanesviewer

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class InfiniteScrollListener(private val mLayoutManager: GridLayoutManager) : RecyclerView.OnScrollListener() {
    private val mInitialPageIndex = 1

    private var mThreshold = 6
    private var mCurrentPage = 1
    private var mPreviousTotalItemCount = 0
    private var mIsLoadingMoreCards = true

    init {
        mThreshold *= mLayoutManager.spanCount
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView)

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        if (dy > 0) {
            val totalItemCount = mLayoutManager.itemCount
            val lastVisibleItemPosition: Int = mLayoutManager.findLastVisibleItemPosition()

            if (totalItemCount < mPreviousTotalItemCount) {
                mCurrentPage = mInitialPageIndex
                mPreviousTotalItemCount = totalItemCount
                if (totalItemCount == 0) {
                    mIsLoadingMoreCards = true
                }
            }

            if (mIsLoadingMoreCards && totalItemCount > mPreviousTotalItemCount) {
                mIsLoadingMoreCards = false
                mPreviousTotalItemCount = totalItemCount
            }

            if (!mIsLoadingMoreCards && lastVisibleItemPosition + mThreshold >= totalItemCount) {
                mCurrentPage++
                mIsLoadingMoreCards = true
                onLoadMore(mCurrentPage, totalItemCount, view)
            }
        }
    }

    fun resetState() {
        mCurrentPage = mInitialPageIndex
        mPreviousTotalItemCount = 0
        mIsLoadingMoreCards = true
    }
}