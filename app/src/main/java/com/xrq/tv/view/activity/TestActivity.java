package com.xrq.tv.view.activity;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;

import com.xrq.tv.R;
import com.xrq.tv.utils.Log;

public class TestActivity extends AppCompatActivity {
    private TextView tv1;
    private TextView tv2;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tv1 = (TextView) findViewById(R.id.textView1);
        tv2 = (TextView) findViewById(R.id.textView2);

        //获取运行内存的信息
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(info);
        StringBuilder sb = new StringBuilder();
        sb.append("可用RAM:");
        sb.append(info.availMem + "B");
        sb.append(",总RAM:");
        sb.append(info.totalMem + "B");
        sb.append("\r\n");
        sb.append(Formatter.formatFileSize(getBaseContext(), info.availMem));
        sb.append(",");
        Log.iii("TAG","totalMem:"+info.totalMem);
        sb.append(Formatter.formatFileSize(getBaseContext(), info.totalMem));
        tv1.setText(sb);



        sb.setLength(0);
        //获取ROM内存信息
        //调用该类来获取磁盘信息（而getDataDirectory就是内部存储）
        final StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long totalCounts = statFs.getBlockCountLong();//总共的block数
        long availableCounts = statFs.getAvailableBlocksLong() ; //获取可用的block数
        long size = statFs.getBlockSizeLong(); //每格所占的大小，一般是4KB==
        long availROMSize = availableCounts * size;//可用内部存储大小
        long totalROMSize = totalCounts *size; //内部存储总大小
        sb.append("可用block数:" + availableCounts);
        sb.append("block总数:" + totalCounts);
        sb.append("\r\n");
        sb.append(" 每个block大小:" + size);
        sb.append("\r\n");
        sb.append(" 可用ROM:" + availROMSize + "B");
        sb.append(" 总ROM:" + totalROMSize + "B");
        tv2.setText(sb);


    }

}
