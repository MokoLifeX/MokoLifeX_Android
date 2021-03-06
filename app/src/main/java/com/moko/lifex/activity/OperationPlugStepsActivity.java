package com.moko.lifex.activity;

import android.os.Bundle;
import android.view.View;

import com.moko.lifex.R;
import com.moko.lifex.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * @Date 2018/6/11
 * @Author wenzheng.liu
 * @Description
 * @ClassPath com.moko.lifex.activity.OperationPlugStepsActivity
 */
public class OperationPlugStepsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plug_operation_steps);
        ButterKnife.bind(this);
    }

    public void back(View view) {
        finish();
    }

    public void plugBlinking(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
