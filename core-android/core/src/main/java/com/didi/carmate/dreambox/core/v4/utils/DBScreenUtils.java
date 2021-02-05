package com.didi.carmate.dreambox.core.v4.utils;

import android.content.Context;
import android.view.ViewGroup;

import com.didi.carmate.dreambox.core.v4.base.DBConstants;
import com.didi.carmate.dreambox.core.v4.base.DBContext;
import com.didi.carmate.dreambox.wrapper.v4.Wrapper;

/**
 * author: chenjing
 * date: 2020/5/9
 */
public class DBScreenUtils {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int processSize(DBContext dBContext, String strSize, int defaultSize) {
        int intSize = defaultSize;

        if (DBUtils.isEmpty(strSize)) {
            return defaultSize;
        } else if (strSize.equals(DBConstants.FILL_TYPE_WRAP)) {
            intSize = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else if (strSize.equals(DBConstants.FILL_TYPE_FILL)) {
            intSize = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            // [单位]&数值 判断及单位容错，默认是dp
            String rawSize;
            String unit;
            int startIdx = strSize.length() - 2;
            if (startIdx < 1) {
                if (DBUtils.isNumeric(strSize)) {
                    rawSize = strSize;
                    unit = DBConstants.UNIT_TYPE_DP;
                } else {
                    Wrapper.get(dBContext.getAccessKey()).log().e("[size] error -> " + strSize);
                    return defaultSize;
                }
            } else {
                rawSize = strSize.substring(0, strSize.length() - 2);
                unit = strSize.substring(startIdx);
            }

            if (!unit.equals(DBConstants.UNIT_TYPE_DP) && !unit.equals(DBConstants.UNIT_TYPE_PX)) {
                Wrapper.get(dBContext.getAccessKey()).log().e("[unit] error -> " + strSize);
                return intSize;
            }

            // 负数判断
            boolean isNegative = false;
            String size;
            if (!rawSize.startsWith("-")) {
                size = rawSize;
            } else {
                size = rawSize.substring(1);
                isNegative = true;
            }

            if (!DBUtils.isNumeric(size)) {
                Wrapper.get(dBContext.getAccessKey()).log().e("size error -> " + strSize);
                return intSize;
            }

            if (unit.equals(DBConstants.UNIT_TYPE_DP)) {
                intSize = DBScreenUtils.dip2px(dBContext.getContext(), Float.parseFloat(size));
            } else {
                intSize = Integer.parseInt(size);
            }
            if (isNegative) {
                intSize = -intSize;
            }
        }
        return intSize;
    }
}
