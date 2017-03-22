package com.example.lzl.mynote;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.VideoView;

import com.tencent.tauth.Tencent;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

import static android.provider.UserDictionary.Words.APP_ID;


public class AddContent extends Activity implements OnClickListener {

    private View shareView;
    private PopupWindow pop;
    private String val;
    private Button savebtn, deletebtn, sharebtn;
    private ImageView c_img;
    private VideoView c_video;
    private EditText ettext;
    private NotesDB notesDB;
    private SQLiteDatabase dbwriter;
    private File phoneFile, videoFile; // 用于照片的存储
    private final String mbApiKey = "rsOjYx5cit50eGrxy5c9682n";
    private SharePopupWindow menuWindow;
    private PopupWindow mPopupWindow;
    private Tencent mTencent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontent);
        val = getIntent().getStringExtra("flag");
        savebtn = (Button) findViewById(R.id.save);
        deletebtn = (Button) findViewById(R.id.delete);
        sharebtn = (Button) findViewById(R.id.share);
        c_img = (ImageView) findViewById(R.id.c_img);
        c_video = (VideoView) findViewById(R.id.c_video);
        ettext = (EditText) findViewById(R.id.ettext);
        savebtn.setOnClickListener(this);
        deletebtn.setOnClickListener(this);
        sharebtn.setOnClickListener(this);
        notesDB = new NotesDB(this);
        dbwriter = notesDB.getWritableDatabase();
        initView();
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
        // 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
        // 初始化视图
        initViews();
    }

    public void initView() {
        if (val.equals("1")) {
            c_img.setVisibility(View.GONE);
            c_video.setVisibility(View.GONE);
        }
        if (val.equals("2")) {
            c_img.setVisibility(View.VISIBLE);
            c_video.setVisibility(View.GONE);
            Intent iimg = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用设备中的摄像头
            phoneFile = new File(Environment.getExternalStorageDirectory()
                    .getAbsoluteFile() + "/" + getTime() + ".jpg"); // 获取照片的绝对路径，放于数据库中，而不是把照片放入，为了避免照片重名，用时间命名
            iimg.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile)); // putExtra("A",B)中，AB为键值对，第一个参数为键名，第二个参数为键对应的值。
            startActivityForResult(iimg, 1); // 启用带返回值的starActivity，能够看效果
        }
        if (val.equals("3")) {
            c_img.setVisibility(View.GONE);
            c_video.setVisibility(View.VISIBLE);
            Intent ivideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE); // 调用设备中的摄像头
            videoFile = new File(Environment.getExternalStorageDirectory()
                    .getAbsoluteFile() + "/" + getTime() + ".mp4"); // 获取照片的绝对路径，放于数据库中，而不是把照片放入，为了避免照片重名，用时间命名
            ivideo.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile)); // putExtra("A",B)中，AB为键值对，第一个参数为键名，第二个参数为键对应的值。
            startActivityForResult(ivideo, 2); // 启用带返回值的starActivity，能够看效果
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                addDB();
                finish();
                break;
            case R.id.delete:
                finish();
                break;
            case R.id.share:
                menuWindow = new SharePopupWindow(AddContent.this, itemsOnClick);
                menuWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);

//                BaiduOAuth oauthClient = new BaiduOAuth();
//                oauthClient.startOAuth(AddContent.this, mbApiKey, new String[]{"basic"},new BaiduOAuth.OAuthListener() {
//                    @Override
//                    public void onException(String msg) {
//                        Toast.makeText(getApplicationContext(), "Login failed " + msg, Toast.LENGTH_SHORT).show();
//                    }
//                    @Override
//                    public void onComplete(BaiduOAuthResponse response) {
//                        if(null != response){
//                            String accessToken = response.getAccessToken();
//                            Toast.makeText(getApplicationContext(), "Token: " + accessToken + "    User name:" + response.getUserName(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    @Override
//                    public void onCancel() {
//                        Toast.makeText(getApplicationContext(), "Login cancelled", Toast.LENGTH_SHORT).show();
//                    }
//                });
        }

    }

    private OnClickListener itemsOnClick = new OnClickListener() {

        public void onClick(View v) {
//            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.qq:
                    menuWindow.dismiss();
                    // case R.id.btn_pick_photo:
                    // break;
                    // default:
                    // break;
            }

        }

    };

    public void addDB() {
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT, ettext.getText().toString());
        cv.put(NotesDB.TIME, getTime());
        cv.put(NotesDB.PATH, phoneFile + "");
        cv.put(NotesDB.VIDEO, videoFile + "");
        dbwriter.insert(NotesDB.TABLE_NAME, null, cv);
    }

    public String getTime() {
        SimpleDateFormat sformat = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String str = sformat.format(date);
        return str;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Bitmap bitmap = BitmapFactory.decodeFile(phoneFile
                    .getAbsolutePath()); // 获取绝对路径
            c_img.setImageBitmap(bitmap);
        }
        if (requestCode == 2) {
            c_video.setVideoURI(Uri.fromFile(videoFile));
            c_video.start();
        }
    }

}
