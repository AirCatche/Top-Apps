package com.slobodianiuk.topapps.model.itemRecycler

import android.view.View

interface ItemOnClick {
    fun onDoubleItemClick(v: View?, position: Int)
    fun onLongItemClick(v: View?, position: Int)
}