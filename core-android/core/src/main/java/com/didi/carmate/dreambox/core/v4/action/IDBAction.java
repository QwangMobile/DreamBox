package com.didi.carmate.dreambox.core.v4.action;

import com.didi.carmate.dreambox.core.v4.base.DBModel;

/**
 * author: chenjing
 * date: 2020/4/30
 */
public interface IDBAction {
    /**
     * 执行某个节点
     */
    void invoke();

    /**
     * 执行某个节点
     */
    void invoke(DBModel model);
}
