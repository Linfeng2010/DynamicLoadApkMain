package com.example.zhang.dynamicloadapkmain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

/**
 * Created by zhang on 16-4-11.
 */
public class ProxyActivity extends Activity {

    private static final String TAG = "ProxyActivity";

    public static final String FROM = "extra.from";
    public static final int FROM_EXTERNAL = 0;
    public static final int FROM_INTERNAL = 1;

    public static final String EXTRA_DEX_PATH = "extra.dex.path";
    public static final String EXTRA_CLASS = "extra.class";

    private String mClass;
    private String mDexPath;

    private Map<String,Method>  targetLifeCircle = new HashMap<String,Method>();
    private Object instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDexPath = getIntent().getStringExtra(EXTRA_DEX_PATH);
        mClass = getIntent().getStringExtra(EXTRA_CLASS);

        Log.d(TAG,"mDexPath>>"+mDexPath+"   mClass>>>"+mClass);
        if(mClass == null)
        {
            launchTargetActivity();
        }else
        {
            launchTargetActivity(mClass);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            targetLifeCircle.get("onStart").invoke(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            targetLifeCircle.get("onRestart").invoke(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            targetLifeCircle.get("onResume").invoke(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            targetLifeCircle.get("onPause").invoke(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            targetLifeCircle.get("onStop").invoke(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            targetLifeCircle.get("onDestroy").invoke(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void initTargetLifeCircle(Class<?> clazz) {

        String[] methodNames = {
                "onRestart",
                "onStart",
                "onResume",
                "onPause",
                "onStop",
                "onDestroy"
        };
        for(String name:methodNames)
        {
            try {
                Method declaredMethod = clazz.getMethod(name, new Class[]{});
                targetLifeCircle.put(name,declaredMethod);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG, targetLifeCircle.toString());

    }

    //如果不指定启动的activity默认启动dexpath中得第一个activity
    @SuppressLint("NewApi")
    private void launchTargetActivity() {
        PackageInfo packageInfo = getPackageManager().getPackageArchiveInfo(mDexPath,1);
        if(packageInfo.activities != null && packageInfo.activities.length>0)
        {
            String activityName = packageInfo.activities[0].name;
            mClass = activityName;
            launchTargetActivity(mClass);
        }

    }

    //启动dexpath中指定的activity
    @SuppressLint("NewApi")
    private void launchTargetActivity(String className)
    {
        Log.d(TAG,"******static launchTargetActivity ,className= "+className);
        File dexOutput = this.getDir("dex",MODE_PRIVATE);
        String dexOutputPath = dexOutput.getAbsolutePath();
        ClassLoader localClassLoder = ClassLoader.getSystemClassLoader();
        DexClassLoader dexClassLoader = new DexClassLoader(mDexPath,dexOutputPath,null,localClassLoder);
        try {
            Class<?> localClass = dexClassLoader.loadClass(className);
            initTargetLifeCircle(localClass);
            Constructor<?> localConstructor = localClass.getConstructor(new Class[]{});
            instance = localConstructor.newInstance(new Object[]{});
            Log.d(TAG,"instance = "+ instance);

            Method setProxy = localClass.getMethod("setProxy",new Class[]{Activity.class});
            setProxy.setAccessible(true);
            setProxy.invoke(instance,this);

            Method onCreate = localClass.getDeclaredMethod("onCreate",new Class[]{Bundle.class});
            onCreate.setAccessible(true);
            Bundle bundle = new Bundle();
            bundle.putInt(FROM,FROM_EXTERNAL);
            onCreate.invoke(instance,bundle);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
