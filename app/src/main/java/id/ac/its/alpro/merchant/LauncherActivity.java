package id.ac.its.alpro.merchant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class LauncherActivity extends AppCompatActivity {

    protected boolean _isActive = true;
    protected int _splashTime = 3000; //SplashActivity will be visible for 2s
    final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        final Thread splashThread = new Thread() {

            public void run() {

                try {
                    int wait = 0;

                    while (_isActive && (_splashTime > wait)) { //will show only on the first time
                        sleep(100);

                        if (_isActive) {
                            wait += 100;
                        }
                    }
                } catch (InterruptedException e) {
                    Log.d(TAG, e.getMessage());

                } finally {
                    startActivity(new Intent(LauncherActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        splashThread.start();
    }
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _isActive = false;
        }
        return true;
    }
}
