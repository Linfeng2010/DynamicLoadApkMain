package com.example.zhang.dynamicloadapkmain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS|
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        
    }

    public void onClick(View view) {

        File file = new File("/mnt/sdcard/DynamicLoadPlug");
        if(!file.exists())
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(file.listFiles().length==0)
        {
            Toast.makeText(getApplicationContext(),"/mnt/sdcard/DynamicLoadPlu下暂无插件，请先将插件放入目录中再运行程序",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getApplicationContext(),ProxyActivity.class);
        intent.putExtra(ProxyActivity.EXTRA_DEX_PATH,file.getAbsoluteFile()+File.separator+"app-debug.apk");
        startActivity(intent);
    }

}
