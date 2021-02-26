package com.didi.carmate.dreambox.core.v4.action;

import com.didi.carmate.dreambox.core.v4.base.DBContext;
import com.didi.carmate.dreambox.core.v4.base.DBModel;
import com.didi.carmate.dreambox.core.v4.base.INodeCreator;
import com.didi.carmate.dreambox.wrapper.v4.Dialog;
import com.didi.carmate.dreambox.wrapper.v4.Wrapper;

import java.util.Map;

/**
 * author: chenjing
 * date: 2020/4/30
 */
public class DBDialog extends DBActionWithCallback {
    private DBDialog(DBContext dbContext) {
        super(dbContext);
    }

    @Override
    protected void doInvoke(Map<String, String> attrs) {
        doInvoke(attrs, null);
    }

    @Override
    protected void doInvoke(Map<String, String> attrs, DBModel model) {
        String title = getString(attrs.get("title"), model);
        String msg = getString(attrs.get("msg"), model);
        String positiveBtn = getString(attrs.get("positiveBtn"), model);
        String negativeBtn = getString(attrs.get("negativeBtn"), model);

        Dialog dialog = Wrapper.get(mDBContext.getAccessKey()).dialog();
        Dialog.OnClickListener onPositive = new Dialog.OnClickListener() {
            @Override
            public void onClick() {
                doCallback("onPositive", getCallbacks());
            }
        };
        Dialog.OnClickListener onNegative = new Dialog.OnClickListener() {
            @Override
            public void onClick() {
                doCallback("onNegative", getCallbacks());
            }
        };
        dialog.show(mDBContext.getContext(), title, msg, positiveBtn, negativeBtn, onPositive, onNegative);
    }

    public static class NodeCreator implements INodeCreator {
        @Override
        public DBDialog createNode(DBContext dbContext) {
            return new DBDialog(dbContext);
        }
    }

    public static String getNodeTag() {
        return "dialog";
    }
}