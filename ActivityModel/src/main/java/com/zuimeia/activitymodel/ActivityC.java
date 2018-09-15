package com.zuimeia.activitymodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.imczy.common_util.log.LogUtil;

/**
 * Created by chenzhiyong on 15/11/4.
 */
public class ActivityC extends Activity
{
    public static final String TAG = "Activity";

    private TextView mTextView;
    private Button mButton;

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        LogUtil.d(TAG, "ActivityC onNewIntent ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        LogUtil.d(TAG, "ActivityC onCreate taskId = " + getTaskId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.txt);
        mButton = (Button) findViewById(R.id.btn);

        mTextView.setText(getClass().getSimpleName());
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);
                finish();
                //                Intent intent = new Intent(getContxt(), ActivityD.class);
                //                startActivity(intent);
            }
        });
    }

    public Context getContxt()
    {
        return this;
    }
}
