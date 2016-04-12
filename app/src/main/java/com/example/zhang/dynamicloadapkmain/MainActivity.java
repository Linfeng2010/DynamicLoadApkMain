package com.example.zhang.dynamicloadapkmain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
