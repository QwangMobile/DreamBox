package com.didi.carmate.dreambox.core.v4.render;

import android.view.ViewGroup;

import com.didi.carmate.dreambox.core.v4.base.DBConstants;
import com.didi.carmate.dreambox.core.v4.base.DBContext;
import com.didi.carmate.dreambox.core.v4.base.DBModel;
import com.didi.carmate.dreambox.core.v4.base.INodeCreator;
import com.didi.carmate.dreambox.core.v4.data.DBData;
import com.didi.carmate.dreambox.core.v4.base.DBBaseView;
import com.didi.carmate.dreambox.core.v4.base.DBContainer;
import com.didi.carmate.dreambox.core.v4.render.view.flow.DBFlowAdapter;
import com.didi.carmate.dreambox.core.v4.render.view.flow.DBFlowLayout;
import com.didi.carmate.dreambox.core.v4.render.view.list.AdapterCallback;
import com.didi.carmate.dreambox.core.v4.utils.DBLogger;
import com.didi.carmate.dreambox.core.v4.utils.DBScreenUtils;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.didi.carmate.dreambox.core.v4.base.DBConstants.PAYLOAD_CELL;

/**
 * author: chenjing
 * date: 2020/5/11
 */
public class DBFlow extends DBBaseView<DBFlowLayout> {
    private DBFlowAdapter<JsonObject> mAdapter;
    private List<JsonObject> src = new ArrayList<>();
    private int hSpace;
    private int vSpace;

    protected DBFlow(DBContext dbContext) {
        super(dbContext);
    }

    @Override
    public void onParserAttribute(Map<String, String> attrs) {
        super.onParserAttribute(attrs);

        src = getJsonObjectList(attrs.get("src"));
        hSpace = DBScreenUtils.processSize(mDBContext, attrs.get("hSpace"), 0);
        vSpace = DBScreenUtils.processSize(mDBContext, attrs.get("vSpace"), 0);
        // 因为数据源需要从各个Item里获取，所以Item子节点属性处理在Adapter的[onBindItemView]回调里处理
    }

    @Override
    protected DBFlowLayout onCreateView() {
        return new DBFlowLayout(mDBContext);
    }

    @Override
    protected void onChildrenBind(Map<String, String> attrs, List<DBContainer<ViewGroup>> children) {
        super.onChildrenBind(attrs, children);

        // 子节点渲染处理在Adapter的[onBindViewHolder]回调里处理
        DBContainer<ViewGroup> cell = null;
        for (DBContainer<ViewGroup> container : children) {
            if (PAYLOAD_CELL.equals(container.getAttrs().get(DBConstants.UI_PAYLOAD))) {
                cell = container;
                container.setParentAttrs(attrs);
            }
        }

        DBFlowLayout flowLayout = (DBFlowLayout) mNativeView;
        FlowAdapterCallback adapterCallback = new FlowAdapterCallback(mDBContext, cell);
        mAdapter = new DBFlowAdapter<>(mDBContext, flowLayout, src, adapterCallback, cell);

        flowLayout.setAdapter(mAdapter);
        flowLayout.setChildSpacing(hSpace);
        flowLayout.setRowSpacing(vSpace);
    }

    @Override
    protected void onDataChanged(final String key, final Map<String, String> attrs, final DBModel model) {
        mDBContext.observeDataPool(new DBData.IDataObserver() {
            @Override
            public void onDataChanged(String key) {
                DBLogger.d(mDBContext, "key: " + key);
                if (null != mAdapter) {
                    src = getJsonObjectList(attrs.get("src"));
                    mAdapter.setData(src);
                }
            }

            @Override
            public String getKey() {
                return key;
            }
        });
    }

    private static final class FlowAdapterCallback extends AdapterCallback {
        private final DBContext mDBContext;
        private final DBContainer<ViewGroup> mFlowCell;

        private FlowAdapterCallback(DBContext dbContext, DBContainer<ViewGroup> flowCell) {
            mDBContext = dbContext;
            mFlowCell = flowCell;
        }

        @Override
        public void onBindItemView(ViewGroup itemRoot, JsonObject data, int position) {
            if (null == mFlowCell) {
                DBLogger.e(mDBContext, "flow child can not be null.");
                return;
            }

            // 子节点属性处理
            mFlowCell.parserAttribute();
            // 子节点渲染处理
            mFlowCell.bindView(itemRoot, NODE_TYPE.NODE_TYPE_ADAPTER, false, data, position);
        }
    }

    public static String getNodeTag() {
        return "flow";
    }

    public static class NodeCreator implements INodeCreator {
        @Override
        public DBFlow createNode(DBContext dbContext) {
            return new DBFlow(dbContext);
        }
    }
}
