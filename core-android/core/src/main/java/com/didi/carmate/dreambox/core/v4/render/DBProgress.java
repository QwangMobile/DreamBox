package com.didi.carmate.dreambox.core.v4.render;

import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

import com.didi.carmate.dreambox.core.v4.base.DBConstants;
import com.didi.carmate.dreambox.core.v4.base.DBContext;
import com.didi.carmate.dreambox.core.v4.base.DBModel;
import com.didi.carmate.dreambox.core.v4.base.INodeCreator;
import com.didi.carmate.dreambox.core.v4.utils.DBScreenUtils;
import com.didi.carmate.dreambox.core.v4.utils.DBUtils;
import com.didi.carmate.dreambox.core.v4.base.DBBaseView;
import com.didi.carmate.dreambox.core.v4.render.view.progress.DBProgressView;
import com.didi.carmate.dreambox.wrapper.v4.ImageLoader;
import com.didi.carmate.dreambox.wrapper.v4.Wrapper;

import java.util.Map;

/**
 * author: chenjing
 * date: 2020/5/11
 */
public class DBProgress extends DBBaseView<DBProgressView> {
    private String value;
    private String barBg;
    private String barFg;
    private String barBgColor;
    private String barFgColor;
    private int radius;
    private String patchType;

    private DBProgress(DBContext dbContext) {
        super(dbContext);
    }

    @Override
    public void onParserAttribute(Map<String, String> attrs) {
        super.onParserAttribute(attrs);

        barBgColor = getString(attrs.get("barBgColor"));
        barFgColor = getString(attrs.get("barFgColor"));
        patchType = attrs.get("patchType");
        if (!(DBConstants.STYLE_PATCH_TYPE_STRETCH.equals(patchType))
                && !(DBConstants.STYLE_PATCH_TYPE_REPEAT.equals(patchType))) {
            patchType = DBConstants.STYLE_PATCH_TYPE_STRETCH;
        }
        radius = DBScreenUtils.processSize(mDBContext, attrs.get("radius"), 0);
    }

    @Override
    protected DBProgressView onCreateView() {
        if (DBUtils.isColor(barBgColor) && DBUtils.isColor(barFgColor)) {
            return new DBProgressView(mDBContext.getContext(), radius, DBUtils.parseColor(DBProgress.this, barFgColor));
        } else {
            return new DBProgressView(mDBContext.getContext(), patchType, radius);
        }
    }

    @Override
    public void onAttributesBind(Map<String, String> attrs, DBModel model) {
        super.onAttributesBind(attrs, model);

        value = getString(attrs.get("value"), model);
        barBg = getString(attrs.get("barBg"), model);
        barFg = getString(attrs.get("barFg"), model);

        doRender();
    }

    private void doRender() {
        final DBProgressView progressView = (DBProgressView) mNativeView;
        final ImageLoader imageLoader = Wrapper.get(mDBContext.getAccessKey()).imageLoader();
        // progress
        final int progress;
        if (DBUtils.isNumeric(value)) {
            progress = Integer.parseInt(value);
        } else {
            progress = 0;
        }
        progressView.setProgress(progress);
        progressView.post(new Runnable() {
            @Override
            public void run() {
                if (!DBUtils.isEmpty(barBgColor) && !DBUtils.isEmpty(barFgColor)) {
                    drawColorProgress(progressView);
                } else {
                    // background image
                    if (!DBUtils.isEmpty(barBg)) {
                        imageLoader.load(barBg, progressView);
                    }
                    // foreground image
                    if (!DBUtils.isEmpty(barFg)) {
                        imageLoader.load(barFg, progressView.getForegroundView());
                    }
                }
            }
        });
    }

    private void drawColorProgress(final DBProgressView progressView) {
        // 外矩形 左上、右上、右下、左下的圆角半径
        float[] bgOuterRadius = {radius, radius, radius, radius, radius, radius, radius, radius};
        // 背景drawable
        RoundRectShape bgShape = new RoundRectShape(bgOuterRadius, null, null);
        ShapeDrawable bgDrawable = new ShapeDrawable(bgShape);
        if (DBUtils.isColor(barBgColor)) {
            bgDrawable.getPaint().setColor(DBUtils.parseColor(DBProgress.this, barBgColor));
        }
        bgDrawable.getPaint().setAntiAlias(true);
        bgDrawable.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);//描边
        progressView.setBackground(bgDrawable);
    }

    public static String getNodeTag() {
        return "progress";
    }

    public static class NodeCreator implements INodeCreator {
        @Override
        public DBProgress createNode(DBContext dbContext) {
            return new DBProgress(dbContext);
        }
    }
}
