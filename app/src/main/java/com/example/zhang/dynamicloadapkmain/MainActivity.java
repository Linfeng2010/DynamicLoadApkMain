package com.example.zhang.dynamicloadapkmain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {

        File file = new File(Environment.getExternalStorageDirectory(),"DynamicLoadPlug");
        if(!file.exists())
        {
            try {
                file.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if( file.listFiles().length == 0)
        {
            Toast.makeText(getApplicationContext(),Environment.getExternalStorageDirectory().getAbsolutePath()+"/DynamicLoadPlug下暂无插件，请先将插件放入目录中再运行程序",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getApplicationContext(),ProxyActivity.class);
        intent.putExtra(ProxyActivity.EXTRA_DEX_PATH,file.getAbsoluteFile()+File.separator+"target.apk");
        startActivity(intent);
    }

}
