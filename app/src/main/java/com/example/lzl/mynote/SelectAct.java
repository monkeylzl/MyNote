package com.example.lzl.mynote;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;
import android.view.View.OnClickListener;


public class SelectAct extends Activity implements OnClickListener {

    private Button s_back, s_delete;
    private ImageView s_img;
    private VideoView s_video;
    private EditText s_text;
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        s_back = (Button) findViewById(R.id.back);
        s_delete = (Button) findViewById(R.id.delete);
        s_img = (ImageView) findViewById(R.id.s_img);
        s_video = (VideoView) findViewById(R.id.s_video);
        s_text = (EditText) findViewById(R.id.s_text);
        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();
        s_back.setOnClickListener(this);
        s_delete.setOnClickListener(this);
//		s_text.setOnClickListener(this);
        if (getIntent().getStringExtra(NotesDB.PATH).equals("null")) {
            s_img.setVisibility(View.GONE);
        } else {
            s_img.setVisibility(View.VISIBLE);
        }
        if (getIntent().getStringExtra(NotesDB.VIDEO).equals("null")) {
            s_video.setVisibility(View.GONE);
        } else {
            s_video.setVisibility(View.VISIBLE);
        }

        Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra(
                NotesDB.PATH));
        s_img.setImageBitmap(bitmap);
        s_video.setVideoURI(Uri
                .parse(getIntent().getStringExtra(NotesDB.VIDEO)));
        s_video.start();
        s_text.setText(getIntent().getStringExtra(NotesDB.CONTENT));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                editDB();
                finish();
                break;
            case R.id.delete:
                deleteData();
                finish();
                break;
        }

    }

    public void deleteData() {
        dbWriter.delete(NotesDB.TABLE_NAME,
                "_id=" + getIntent().getIntExtra(NotesDB.ID, 0), null);
    }

    // 如何完成数据的更新
    public void editDB(){
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT, s_text.getText().toString());
//		cv.put(notesDB.TIME, getTime());
//		cv.put(notesDB.PATH, phoneFile + "");
//		cv.put(notesDB.VIDEO, videoFile + "");
//		dbWriter.insert(notesDB.TABLE_NAME, notesDB.CONTENT,cv);
        dbWriter.update(NotesDB.TABLE_NAME, cv ,"content=?",new String[]{getIntent().getStringExtra(NotesDB.CONTENT)});
    }
}

