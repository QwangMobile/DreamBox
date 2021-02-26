package com.didi.carmate.dreambox.core.v4.render.view.list;

import android.view.ViewGroup;

import com.google.gson.JsonObject;

/**
 * author: chenjing
 * date: 2020/7/2
 */
public interface IAdapterCallback {
    void onBindHeaderView(ViewGroup rootView);

    void onBindFooterView(ViewGroup rootView);

    void onBindItemView(ViewGroup itemRootView, JsonObject data, int position);
}
