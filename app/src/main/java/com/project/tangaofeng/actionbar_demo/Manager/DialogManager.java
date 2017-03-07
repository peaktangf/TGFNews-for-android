package com.project.tangaofeng.actionbar_demo.Manager;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by tangaofeng on 16/7/15.
 */
public class DialogManager {

    public MyOnClickListener myOnClickListener;
    private static DialogManager dialogManager;

    public interface MyOnClickListener {
        public void sureClick();
        public void cancelClick();
    }

    public static DialogManager shareDialog() {
        if (dialogManager == null) {
            dialogManager = new DialogManager();
        }
        return dialogManager;
    }

    public void showDialog(Context context, String title, String content, final MyOnClickListener myOnClickListener) {
        this.myOnClickListener = myOnClickListener;
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(title).setMessage(content);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myOnClickListener.sureClick();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myOnClickListener.cancelClick();
            }
        });
        builder.show();
    }
}
