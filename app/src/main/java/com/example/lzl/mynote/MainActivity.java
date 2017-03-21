package com.example.lzl.mynote;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private Toolbar toolbar;
    private Button textbtn;
    private Button imgbtn;
    private Button videobtn;
    private ListView lv;
    private Intent i;
    private MyAdapter adapter;
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    public void initView() {
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        textbtn = (Button) findViewById(R.id.text);
        imgbtn = (Button) findViewById(R.id.img);
        videobtn = (Button) findViewById(R.id.video);
        lv = (ListView) findViewById(R.id.lv);
        setSupportActionBar(toolbar);
//		// 显示应用的Logo
////		getSupportActionBar().setDisplayShowHomeEnabled(true);
////		getSupportActionBar().setDisplayUseLogoEnabled(true);//这两句不添加也可以，只有下一条语句即可添加Logo
//		getSupportActionBar().setLogo(R.drawable.ic_launcher);

        // 显示标题和子标题
//		getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
//		toolbar.setSubtitle("the detail of toolbar");

        textbtn.setOnClickListener(this);
        imgbtn.setOnClickListener(this);
        videobtn.setOnClickListener(this);
        notesDB = new NotesDB(this);
        dbReader = notesDB.getReadableDatabase();
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                cursor.moveToPosition(position);
                Intent i = new Intent(MainActivity.this, SelectAct.class);
                i.putExtra(NotesDB.ID,
                        cursor.getInt(cursor.getColumnIndex(NotesDB.ID)));
                i.putExtra(NotesDB.CONTENT, cursor.getString(cursor
                        .getColumnIndex(NotesDB.CONTENT)));
                i.putExtra(NotesDB.TIME,
                        cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
                i.putExtra(NotesDB.PATH,
                        cursor.getString(cursor.getColumnIndex(NotesDB.PATH)));
                i.putExtra(NotesDB.VIDEO,
                        cursor.getString(cursor.getColumnIndex(NotesDB.VIDEO)));
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View v) {
        i = new Intent(this, AddContent.class);
        switch (v.getId()) {
            case R.id.text:
                i.putExtra("flag", "1");
                startActivity(i);
                break;
            case R.id.img:
                i.putExtra("flag", "2");
                startActivity(i);
                break;
            case R.id.video:
                i.putExtra("flag", "3");
                startActivity(i);
                break;
        }

    }

    public void selectDB() {
        // Cursor cursor = dbReader.query(notesDB.TABLE_NAME, null, null, null,
        // null, null, null, null);
        cursor = dbReader.query(NotesDB.TABLE_NAME, null, null, null, null,
                null, null, null);// 去除最初的cursor，因为一开始已经创建了cursor对象，
        //如果这里在创建则会覆盖以前的cursor，导致cursor初始化后为空指针
        adapter = new MyAdapter(this, cursor);
        lv.setAdapter(adapter);
    }

    protected void onResume() {
        super.onResume();
        selectDB();
    }

}

