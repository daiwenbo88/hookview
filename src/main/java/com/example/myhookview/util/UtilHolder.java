package com.example.myhookview.util;

import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * Created by daiwenbo on 17/3/20.
 */

public class UtilHolder {

    public static void hookView(View view) {
        try {
            //通过一个类的全量限定名获得View对象
            Class viewClazz = Class.forName("android.view.View");
            //得到该类getListenerInfo的方法，不包括父类的 参数分别是方法名
            Method listenerInfoMethod = viewClazz.getDeclaredMethod("getListenerInfo");
            if (!listenerInfoMethod.isAccessible()) {
                //设置是否允许访问，因为该方法是private的，所以要手动设置允许访问
                listenerInfoMethod.setAccessible(true);
            }
            //执行view的getListenerInfo()方法；
            Object listenerInfoObj = listenerInfoMethod.invoke(view);
            //反射获取View.ListenerInfo对象
            Class listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");
            //获取成员变量 mOnClickListener
            Field onClickListenerField = listenerInfoClazz.getDeclaredField("mOnClickListener");
            if (!onClickListenerField.isAccessible()) {
                //设置是否允许访问，因为该成员变量是private的，所以要手动设置允许访问
                onClickListenerField.setAccessible(true);
            }
            //通过对象得到该属性的实例
            View.OnClickListener mOnClickListener = (View.OnClickListener) onClickListenerField.get(listenerInfoObj);
            //自定义代理事件监听器
            View.OnClickListener onClickListenerProxy = new OnClickListenerProxy(mOnClickListener);
            //更换
            onClickListenerField.set(listenerInfoObj, onClickListenerProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//自定义的代理事件监听器

    private static class OnClickListenerProxy implements View.OnClickListener {

        private View.OnClickListener object;

        private int MIN_CLICK_DELAY_TIME = 1000;

        private long lastClickTime = 0;

        private OnClickListenerProxy(View.OnClickListener object) {
            this.object = object;
        }

        @Override
        public void onClick(View v) {
            //点击时间控制
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                Log.e("OnClickListenerProxy", "OnClickListenerProxy");
                if (object != null) object.onClick(v);
            }
        }
    }
}

