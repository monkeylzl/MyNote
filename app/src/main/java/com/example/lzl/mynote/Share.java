package com.example.lzl.mynote;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.view.View.OnClickListener;

/**
 * Created by lzl on 2017/3/21.
 */

public class Share extends Activity implements OnClickListener{

    private ImageView qq,chat;
    private View shareView;
    private PopupWindow pop;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);
        qq = (ImageView) findViewById(R.id.qq);
        chat = (ImageView) findViewById(R.id.wechat);
        qq.setOnClickListener(this);
        chat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.qq:
                finish();//添加权限
                break;
            case R.id.wechat:
                finish();
                break;
        }

    }
}