package com.example.administrator.counter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.administrator.counter.CountService.LocalBinder;
/**
 * Created by Administrator on 2015-09-18.
 */
public class Count extends Activity {
    CountService mCountService = null;

    private ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected( ComponentName name,
                                        IBinder service )
        {
            Log.d( "superdroid", "onServiceConnected()" );
            LocalBinder binder = (LocalBinder) service;
            mCountService = binder.getCountService();
        }

        @Override
        public void onServiceDisconnected( ComponentName name )
        {
            Log.d( "superdroid", "onServiceDisconnected()" );
        }
    };

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // 카운트 서비스 연결
        // ====================================================================
        Intent serviceIntent =
                new Intent("com.superdroid.service.CountService");
        bindService( serviceIntent, mConnection, BIND_AUTO_CREATE );
        // ====================================================================
    }

    @Override
    protected void onDestroy()
    {
        // 카운트 서비스 해제
        // ====================================================================
        unbindService( mConnection );
        // ====================================================================

        super.onDestroy();
    }

    public void onClick( View v )
    {
        switch( v.getId() )
        {
            // 1. 카운트 시작
            // ================================================================
            case R.id.start_count_btn:
            {
                Intent serviceIntent =
                        new Intent("com.example.administrator.counter.CountService");

                startService( serviceIntent );

                break;
            }
            // ================================================================

            // 2. 카운트 종료
            // ================================================================
            case R.id.stop_count_btn:
            {
                Intent serviceIntent =
                        new Intent("com.example.administrator.counter.CountService");

                stopService(serviceIntent);
                break;
            }
            // ================================================================

            // 3. 현재까지 카운트 된 수치 보기
            // ================================================================
            case R.id.get_cur_number_btn:
            {

                Toast.makeText( Count.this,
                        "Cur Count : " +
                                mCountService.getCurCountNumber(),
                        Toast.LENGTH_LONG ).show();
                break;
            }
            // ================================================================
        }
    }
}