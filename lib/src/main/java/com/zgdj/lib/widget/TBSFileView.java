package com.zgdj.lib.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.tencent.smtt.sdk.TbsReaderView;
import com.zgdj.lib.utils.WPSCheckUtils;

import java.io.File;

/**
 * Created by 12457 on 2017/8/29.
 */

public class TBSFileView extends FrameLayout implements TbsReaderView.ReaderCallback {

    private TbsReaderView mTbsReaderView;
    private Context context;
    private Long fileSize;

    public TBSFileView(Context context) {
        this(context, null, 0);
    }

    public TBSFileView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TBSFileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTbsReaderView = new TbsReaderView(context, this);
        this.addView(mTbsReaderView, new LinearLayout.LayoutParams(-1, -1));
        this.context = context;
    }


    private OnGetFilePathListener mOnGetFilePathListener;


    public void setOnGetFilePathListener(Long fileSize, OnGetFilePathListener mOnGetFilePathListener) {
        this.fileSize = fileSize;
        this.mOnGetFilePathListener = mOnGetFilePathListener;
    }


    private TbsReaderView getTbsReaderView(Context context) {
        return new TbsReaderView(context, this);
    }

    public void displayFile(File mFile) {

        if (mFile != null && !TextUtils.isEmpty(mFile.toString())) {
            //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
            String bsReaderTemp = "/storage/emulated/0/TbsReaderTemp";
            File bsReaderTempFile = new File(bsReaderTemp);

            if (!bsReaderTempFile.exists()) {
                boolean mkdir = bsReaderTempFile.mkdir();
                if (!mkdir) {
                }
            }

            //加载文件
            Bundle localBundle = new Bundle();
            localBundle.putString("filePath", mFile.toString());

            localBundle.putString("tempPath", Environment.getExternalStorageDirectory() + "/" + "TbsReaderTemp");

            if (this.mTbsReaderView == null)
                this.mTbsReaderView = getTbsReaderView(context);
            boolean bool = this.mTbsReaderView.preOpen(getFileType(mFile.toString()), false);
            if (bool) {
                this.mTbsReaderView.openFile(localBundle);
            } else {
                //todo 可能 X5核心 初始化失败，用第三方软件打开
                if (context instanceof Activity) {
                    WPSCheckUtils.INSTANCE.openFile((Activity) context, mFile.getPath(), fileSize);
                }
            }
        } else {
        }
    }

    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            return str;
        }
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }


        str = paramString.substring(i + 1);
        return str;
    }

    public void show() {
        if (mOnGetFilePathListener != null) {
            mOnGetFilePathListener.onGetFilePath(this);
        }
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    /***
     * 将获取File路径的工作，“外包”出去
     */
    public interface OnGetFilePathListener {
        void onGetFilePath(TBSFileView mSuperFileView2);
    }


    public void onStopDisplay() {
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
    }
}
