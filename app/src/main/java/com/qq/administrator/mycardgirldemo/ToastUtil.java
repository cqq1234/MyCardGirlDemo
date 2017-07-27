package com.qq.administrator.mycardgirldemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/2/22 0022.
 */
public class ToastUtil {
    private static Toast toast;

    public static void showToastLove(Context context, String content) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.toast_layout,null);
        ImageView imageView=(ImageView)view.findViewById(R.id.toast_image);
        imageView.setBackgroundResource(R.mipmap.love);
        TextView t = (TextView) view.findViewById(R.id.toast_text);
        t.setText(content);
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            //toast.setText(content);
            toast.cancel();
        }
        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    public static void showToastNoLove(Context context, String content) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.toast_layout_one,null);
        ImageView imageView=(ImageView)view.findViewById(R.id.toast_image);
        imageView.setBackgroundResource(R.mipmap.no_like_one);
        TextView t = (TextView) view.findViewById(R.id.toast_text);
        t.setText(content);
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.cancel();
        }
        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        //toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
