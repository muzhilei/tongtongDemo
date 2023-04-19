package com.homjay.tongtongbiaobaidemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Homjay
 * @date 2022/7/4 14:18
 * @describe
 */
public class HideActivity extends AppCompatActivity {

    private TextView tvOk;
    private EditText etPhone;
    private EditText etSms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide);

        tvOk = findViewById(R.id.tv_ok);
        etPhone = findViewById(R.id.et_phone);
        etSms = findViewById(R.id.et_sms);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhone.getEditableText().toString().equals("") && etSms.getEditableText().toString().equals("")){
                    Toast.makeText(HideActivity.this,"服务员号码/发送信息不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    String phone = etPhone.getEditableText().toString();
                    String sms = etSms.getEditableText().toString();
                    TongTongApplication.getIns().setPhone(phone);
                    TongTongApplication.getIns().setSms(sms);
                    Intent intent = new Intent();
                    intent.setClass(HideActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
