package com.ocwvar.muzi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ocwvar.muzi.R;

public class PaymentDialog extends AppCompatActivity {
    TextView text_content;
    CheckBox checkBox;
    Button button;
    Intent sourceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_payment);
        init();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    text_content.setText("您需支付" + sourceIntent.getStringExtra("restMoney") + "元");
                }else if (!b){
                    text_content.setText("您需支付" + sourceIntent.getFloatExtra("payCount", -1f) + "元");
                }
            }
        });


    }

    private void init() {
        text_content = (TextView) findViewById(R.id.payment_content);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        button = (Button) findViewById(R.id.button);
        sourceIntent = getIntent();
        if(sourceIntent.getIntExtra("TAG",0)==1){//根据Tag设置弹窗内容

            if(sourceIntent.getFloatExtra("payCount",-1f)!=-1f) {
                text_content.setText("您需支付" + sourceIntent.getFloatExtra("payCount", -1f) + "元");
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setText("使用"+sourceIntent.getStringExtra("useScore")+"积分抵消一定金额");
            }else {
                setResult(0);
                finish();
            }
        }else {

            if(sourceIntent.getFloatExtra("payCount",-1f)!=-1f) {
                text_content.setText("您需支付" + sourceIntent.getFloatExtra("payCount", -1f) + "元");

            }else {
                setResult(0);
                finish();
            }
        }




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()) {
                    setResult(2, sourceIntent);
                }else {
                    setResult(1,sourceIntent);
                }

                finish();
            }
        });
    }
}
