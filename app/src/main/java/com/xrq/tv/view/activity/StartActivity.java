package com.xrq.tv.view.activity;

import android.content.Intent;
import android.os.Bundle;
import com.xrq.tv.R;
import com.xrq.tv.constant.Constant;
import com.xrq.tv.down.ThreadPool;

/**
 * @author 765773123
 * @date 2018/11/21
 * 启动页
 */
public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ThreadPool.getInstance().execute(new StartRunnable());
    }

    /**
     * 延时三秒进入主界面
     */
    private class StartRunnable implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(Constant.LAUNCH_TIME);
                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        }
    }
}
