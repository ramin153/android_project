package com.example.androidproject.helper;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class MyToasty {
    Context context ;
    Toast old;

    public MyToasty(Context context) {
        this.context = context;
        old = Toast.makeText(context,"",Toast.LENGTH_LONG);
    }

    public void error(String massage,boolean isShort)
    {
        old.cancel();
        if (isShort)
            old = Toasty.error(context, massage, Toast.LENGTH_SHORT, true);
        else
            old = Toasty.error(context, massage, Toast.LENGTH_LONG, true);
        old.show();

    }

    public void normal(String massage,boolean isShort)
    {
        old.cancel();
        if (isShort)
            old = Toasty.normal(context, massage, Toast.LENGTH_SHORT);
        else
            old = Toasty.normal(context, massage, Toast.LENGTH_LONG);
        old.show();
    }

    public void info(String massage,boolean isShort)
    {
        old.cancel();
        if (isShort)
            old = Toasty.info(context, massage, Toast.LENGTH_SHORT, true);
        else
            old = Toasty.info(context, massage, Toast.LENGTH_LONG, true);
        old.show();
    }

    public void warning(String massage,boolean isShort)
    {
        old.cancel();
        if (isShort)
            old = Toasty.warning(context, massage, Toast.LENGTH_SHORT, true);
        else
            old = Toasty.warning(context, massage, Toast.LENGTH_LONG, true);
        old.show();
    }
    public void success(String massage,boolean isShort)
    {
        old.cancel();
        if (isShort)
            old = Toasty.success(context, massage, Toast.LENGTH_SHORT, true);
        else
            old = Toasty.success(context, massage, Toast.LENGTH_LONG, true);
        old.show();
    }
}
