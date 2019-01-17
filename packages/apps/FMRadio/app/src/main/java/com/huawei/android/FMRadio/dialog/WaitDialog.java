package com.huawei.android.FMRadio.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.android.FMRadio.R;


public class WaitDialog {
    private static Dialog mLoadingDialog;


    public static void showDialog(Activity mContext, String text, boolean cancelable) {
        showDialog(mContext, text, cancelable, null);
    }


    public static void showDialog(Activity mContext, String text, boolean cancelable, final OnCancleListener listener) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.loading_dialog, null);

        LinearLayout dialog_layout = (LinearLayout) mView.findViewById(R.id.loading_dialog_root);// 加载布局
        TextView tv = (TextView) mView.findViewById(R.id.loading_dialog_desc);
        tv.setText(text);
        final ImageView mRollImageView = (ImageView) mView.findViewById(R.id.loading_dialog_img);
        final Animation mRollAnimation = (Animation) AnimationUtils.loadAnimation(mContext, R.anim.loading_dialog_anim);

        mLoadingDialog = new Dialog(mContext, R.style.loading_dialog);// 创建自定义样式dialog
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (listener != null) {
                    listener.onCancle();
                }
            }
        });
        mLoadingDialog.setContentView(dialog_layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

        if (mContext != null && !mContext.isFinishing() && mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
            mRollImageView.startAnimation(mRollAnimation);
        }

    }


    public static void dismissDialog(Activity mContext) {
        if (mContext != null && !mContext.isFinishing() && mLoadingDialog != null && mLoadingDialog.isShowing()) {
            try{
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }catch (Exception e){

            }
        }
    }


    public interface OnCancleListener {
        void onCancle();
    }
}
