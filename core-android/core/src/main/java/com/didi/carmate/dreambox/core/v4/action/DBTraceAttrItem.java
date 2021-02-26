package com.didi.carmate.dreambox.core.v4.action;

import com.didi.carmate.dreambox.core.v4.base.DBContext;
import com.didi.carmate.dreambox.core.v4.base.DBModel;
import com.didi.carmate.dreambox.core.v4.base.DBNode;
import com.didi.carmate.dreambox.core.v4.utils.DBUtils;

import java.util.Map;

/**
 * author: chenjing
 * date: 2020/10/26
 */
public class DBTraceAttrItem extends DBNode {
    private Map<String, String> attrs;
    private String key;
    private String value;

    public DBTraceAttrItem(DBContext dbContext) {
        super(dbContext);
    }

    @Override
    protected void onParserAttribute(Map<String, String> attrs) {
        super.onParserAttribute(attrs);

        this.attrs = attrs;
    }

    public void doInvoke(DBModel model) {
        key = attrs.get("key");
        value = getString(attrs.get("value"), model);
        if (DBUtils.isEmpty(value)) {
            value = String.valueOf(getInt(attrs.get("value"), model));
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
