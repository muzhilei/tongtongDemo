package com.homjay.tongtongbiaobaidemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private String strSms = "hello word";
    private String phoneNum  = "10086";
    private DashboardView dashboardView;
    private EditText inPut;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv_test);
        dashboardView = findViewById(R.id.DashView);
        inPut = findViewById(R.id.et_input);
        result = findViewById(R.id.tv_result);


        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inPut.getEditableText().toString()!= null){
                    String inputs = inPut.getEditableText().toString();
                    result.setText(CmdUtils.getCheck(inputs));
                }
            }
        });

        dashboardView.setStatusName("正常");
        dashboardView.setStatusValue("100");
//                    dashboardView.setmSection();
//        String[] strings = healthDataBean.getData().getGradientColor().values().toArray(new String[0]);
//        String[] colors =  healthDataBean.getData().getGradientColor().keySet().toArray(new String[0]);
//        int[] mColors = new int[colors.length];
//        for (int i = 0; i < colors.length; i++) {
//            mColors[i] = Color.parseColor(colors[i]);
//        }

        String[] strings = new String[]{"1","2","3","4"};
        int[] mColors = new int[]{ContextCompat.getColor(this, R.color.color_red),
                ContextCompat.getColor(this, R.color.color_orange),
                ContextCompat.getColor(this, R.color.color_green),
                ContextCompat.getColor(this, R.color.color_blue)};
        dashboardView.setmSection(strings.length-1,strings,mColors);

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                try {
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                    intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
                    startActivity(intent);

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this,"没有安装微信",Toast.LENGTH_SHORT).show();
                }
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, HideActivity.class);
//                startActivity(intent);
                return true;
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(TongTongApplication.getIns().getPhone())) {
                    phoneNum = TongTongApplication.getIns().getPhone();
                }

                if (!TextUtils.isEmpty(TongTongApplication.getIns().getSms())) {
                    strSms = TongTongApplication.getIns().getSms();
                }

                /*

                 * 在 Android 2.0 以前 应该使用 android.telephony.gsm.SmsManager 之后应该用

                 * android.telephony.SmsManager;

                 */

                // 获取系统默认的短信管理器

                SmsManager smsManager = SmsManager.getDefault();

                PendingIntent sentIntent = PendingIntent.getBroadcast(

                        MainActivity.this, 0, new Intent(), 0);

                // 如果字数超过70,需拆分成多条短信发送

                // 按照每条短信最大字数来拆分短信

                if (strSms.length() > 70) {

                    List msgs = smsManager.divideMessage(strSms);

                    for (Object msg : msgs) {

                        /*

                         * 发送短信

                         *

                         * smsManager.sendTextMessage(destinationAddress,

                         * scAddress, text, sentIntent, deliveryIntent)

                         *

                         * -- destinationAddress：目标电话号码

                         *

                         * -- scAddress：短信中心号码，测试可以不填

                         *

                         * -- text: 短信内容

                         *

                         * -- sentIntent：发送 -->中国移动 --> 中国移动发送失败 --> 返回发送成功或失败信号

                         * --> 后续处理 即，这个意图包装了短信发送状态的信息

                         *

                         * -- deliveryIntent： 发送 -->中国移动 --> 中国移动发送成功 -->

                         * 返回对方是否收到这个信息 --> 后续处理

                         * 即：这个意图包装了短信是否被对方收到的状态信息(供应商已经发送成功，但是对方没有收到)。

                         */

                        smsManager.sendTextMessage(phoneNum, null, (String) msg,

                                sentIntent, null);

                    }

                } else {

                    smsManager.sendTextMessage(phoneNum, null, strSms,

                            sentIntent, null);

                }

            }
        });
    }

}
