package com.didi.carmate.dreambox.core.v4.base;

import android.view.View;

import com.didi.carmate.dreambox.core.v4.bridge.IDBEventReceiver;
import com.google.gson.JsonObject;

/**
 * author: chenjing
 * date: 2020/5/8
 */
public interface IDBCoreView {
    void putExtProperty(String key, String value);

    void putExtProperty(String key, int value);

    void putExtProperty(String key, boolean value);

    void setExtData(JsonObject extJsonObject);

    void requestRender();

    void bindData();

    View getView();

    View getRootView();

    void sendEvent(String eid, String msgTo);

    void registerEventReceiver(String eid, IDBEventReceiver eventReceiver);

    void unRegisterEventReceiver(String eid);
}
