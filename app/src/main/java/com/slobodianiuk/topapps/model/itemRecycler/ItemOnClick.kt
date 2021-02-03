package com.slobodianiuk.topapps.model.itemRecycler

import android.view.View

interface ItemOnClick {
    fun onItemClick(v: View?, position: Int)
    fun onLongItemClick(v: View?, position: Int)
}