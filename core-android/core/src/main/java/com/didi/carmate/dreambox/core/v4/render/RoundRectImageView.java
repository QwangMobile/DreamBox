package com.didi.carmate.dreambox.core.v4.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import java.lang.ref.WeakReference;

public class RoundRectImageView extends AppCompatImageView {
    private final Paint mImagePaint;
    private final Path mImagePath;
    private final RectF mImageRectF;

    private final Paint mBoarderPaint;
    private final Path mBorderPath;
    private final RectF mBorderRectF;

    private final Rect mRectSrc;
    private final Rect mRectDest;
    private final Canvas mCanvas;
    private final Matrix mMatrix;
    private WeakReference<Bitmap> mLastBitmap;

    private final float[] mCorners;
    private float mCornerX, mCornerY;
    private int mBorderWidth;
    private int mBorderPadding;
    private int mLastDrawable;
    private boolean mIsDrawCorner;
    private boolean mIsDrawEachCorner;
    private boolean mConfig565;

    public RoundRectImageView(Context context) {
        this(context, null);
    }

    public RoundRectImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundRectImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mImagePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mImagePath = new Path();
        mImageRectF = new RectF();

        mBoarderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBoarderPaint.setColor(Color.TRANSPARENT);
        mBoarderPaint.setStyle(Paint.Style.STROKE);
        mBorderPath = new Path();
        mBorderRectF = new RectF();

        mRectSrc = new Rect();
        mRectDest = new Rect();
        mCanvas = new Canvas();
        mMatrix = new Matrix();
        mCorners = new float[]{
                0, 0,        // Top left radius in px
                0, 0,        // Top right radius in px
                0, 0,        // Bottom right radius in px
                0, 0         // Bottom left radius in px
        };
        mCornerX = mCornerY = 0;
    }

    public void setBorderWidth(int borderWidth) {
        mBorderWidth = borderWidth;
        mBorderPadding = mBorderWidth / 2;
        mBoarderPaint.setStrokeWidth(borderWidth);
    }

    public void setBorderColor(int color) {
        mBoarderPaint.setColor(color);
    }

    public void setRadius(int radius) {
        if (radius > 0) {
            mCornerX = radius;
            mCornerY = radius;
            mIsDrawCorner = true;
        }
    }

    public void setRoundRadius(int radius, int lt, int rt, int rb, int lb) {
        if (lt != 0) {
            mCorners[0] = lt;
            mCorners[1] = lt;
            mIsDrawCorner = true;
            mIsDrawEachCorner = true;
        } else {
            mCorners[0] = radius;
            mCorners[1] = radius;
        }
        if (rt != 0) {
            mCorners[2] = rt;
            mCorners[3] = rt;
            mIsDrawCorner = true;
            mIsDrawEachCorner = true;
        } else {
            mCorners[2] = radius;
            mCorners[3] = radius;
        }
        if (rb != 0) {
            mCorners[4] = rb;
            mCorners[5] = rb;
            mIsDrawCorner = true;
            mIsDrawEachCorner = true;
        } else {
            mCorners[4] = radius;
            mCorners[5] = radius;
        }
        if (lb != 0) {
            mCorners[6] = lb;
            mCorners[7] = lb;
            mIsDrawCorner = true;
            mIsDrawEachCorner = true;
        } else {
            mCorners[6] = radius;
            mCorners[7] = radius;
        }
    }

    /**
     * 绘制圆角矩形图片
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (mIsDrawCorner) {
            Drawable drawable = getDrawable();
            if (null != drawable) {
                if (drawable.hashCode() == mLastDrawable) {
                    canvas.drawBitmap(mLastBitmap.get(), mRectSrc, mRectDest, mImagePaint);
                } else {
                    Bitmap bitmap = getBitmapFromDrawable(drawable);
                    Bitmap b = getRoundBitmapByShader(bitmap, getWidth(), getHeight());
                    mRectSrc.set(0, 0, b.getWidth(), b.getHeight());
                    mRectDest.set(0, 0, getWidth(), getHeight());
                    canvas.drawBitmap(b, mRectSrc, mRectDest, mImagePaint);
                    mLastBitmap = new WeakReference<>(b);
                }

                mLastDrawable = drawable.hashCode();
            }
        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 把资源图片转换成Bitmap
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config;
        if (drawable.getOpacity() != PixelFormat.OPAQUE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                config = Bitmap.Config.ARGB_8888;
            } else {
                config = Bitmap.Config.RGB_565;
                mConfig565 = true;
            }
        } else {
            config = Bitmap.Config.RGB_565;
            mConfig565 = true;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return bitmap;
    }

    private Bitmap getRoundBitmapByShader(Bitmap bitmap, int outWidth, int outHeight) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (outWidth <= 0) {
            outWidth = width;
        }
        if (outHeight <= 0) {
            outHeight = height;
        }
        float widthScale = outWidth * 1f / width;
        float heightScale = outHeight * 1f / height;

        mMatrix.setScale(widthScale, heightScale);
        //创建输出的bitmap
        Bitmap desBitmap = Bitmap.createBitmap(outWidth, outHeight, mConfig565 ? Bitmap.Config.RGB_565 : Bitmap.Config.ARGB_8888);
        //创建canvas并传入desBitmap，这样绘制的内容都会在desBitmap上
        mCanvas.setBitmap(desBitmap);
        //创建着色器
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //给着色器配置matrix
        bitmapShader.setLocalMatrix(mMatrix);
        mImagePaint.setShader(bitmapShader);

        //创建真实图片矩形区域
        mImageRectF.set(mBorderWidth, mBorderWidth, outWidth - mBorderWidth, outHeight - mBorderWidth);
        if (mIsDrawEachCorner) {
            mImagePath.addRoundRect(mImageRectF, mCorners, Path.Direction.CW);
        } else {
            mImagePath.addRoundRect(mImageRectF, mCornerX, mCornerY, Path.Direction.CW);
        }
//        mCanvas.drawColor(Color.WHITE);
        mCanvas.drawPath(mImagePath, mImagePaint);

        //创建边框矩形区域
        if (mBorderWidth > 0) {
            mBorderRectF.set(mBorderPadding, mBorderPadding, outWidth - mBorderPadding, outHeight - mBorderPadding);
            if (mIsDrawEachCorner) {
                mBorderPath.addRoundRect(mBorderRectF, mCorners, Path.Direction.CW);
            } else {
                mBorderPath.addRoundRect(mBorderRectF, mCornerX, mCornerY, Path.Direction.CW);
            }
            mCanvas.drawPath(mBorderPath, mBoarderPaint);
        }

        return desBitmap;
    }
}
