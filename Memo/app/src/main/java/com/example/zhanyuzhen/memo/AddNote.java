package com.example.zhanyuzhen.memo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNote extends AppCompatActivity {


    public static int numTitle = 1;
    public static String curDate = "";
    public static String curText = "";
    public static String curColor = "#FFFFFF";
    private EditText mTitleText;
    private EditText mBodyText;
    private TextView mDateText;
    private TextView mTitle;
    private Long mRowId;
    private Cursor note;
    private NotesDbAdapter mDbHelper;
    int ADD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(OnMenuItemClick);
      //  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      //  fab.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View view) {
      //          Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
      //                  .setAction("Action", null).show();
      //      }
      //  });
      // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();

        mTitleText = (EditText)findViewById(R.id.note_title);
        mBodyText = (EditText) findViewById(R.id.note_body);
        mDateText = (TextView) findViewById(R.id.note_date);
        mTitle = (TextView) findViewById(R.id.textView2);

        long msTime = System.currentTimeMillis();
        Date curDateTime = new Date(msTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        curDate = dateFormat.format(curDateTime);
        mDateText.setText(curDate);

        mRowId = (savedInstanceState == null)? null:
                (Long)savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
        if(mRowId == null){
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID):null;
        }


        System.out.println(mRowId);

        populateFields();
    }

    private void populateFields(){
        if(mRowId != null){//if the user is edit it, not add a new note
            note = mDbHelper.fetchNote(mRowId);
            //startManagingCursor(note);
            mTitleText.setText(note.getString(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
            mBodyText.setText(note.getString(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
            curText = note.getString(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY));
            curColor = note.getString(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_COLOR));
            mTitle.setBackgroundColor(Color.parseColor(curColor));
            mTitleText.setBackgroundColor(Color.parseColor(curColor));
            mDateText.setBackgroundColor(Color.parseColor(curColor));
            mBodyText.setBackgroundColor(Color.parseColor(curColor));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId); //自定義的類別＋自定義的類別的內容
    }

    @Override
    protected void onPause(){
        super.onPause();
        //saveState();
    }

    @Override
    protected void onResume(){
        super.onResume();
        populateFields(); //重新顯示剛剛編輯內容
    }

    private void saveState(){ //儲存的概念
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();
        if(mRowId == null){
            long id = mDbHelper.createNote(title, body, curDate, curColor);
            if(id > 0) mRowId = id;
        } else{
            mDbHelper.updateNote(mRowId, title, body, curDate, curColor);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note, menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener OnMenuItemClick = new Toolbar.OnMenuItemClickListener(){
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch(item.getItemId()) {
                case R.id.note_color:
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddNote.this);
                    builder.setTitle(R.string.Color);
                    System.out.println("before View");
                    //buttons
                    final View colors = LayoutInflater.from(AddNote.this).inflate(R.layout.color_chooser, null);
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setView(colors);
                    AlertDialog ColorChooser = builder.create();
                    ColorChooser.show();
                    Button rose = (Button)colors.findViewById(R.id.color_rose);
                    rose.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            curColor = "#FF99CC";
                            mTitle.setBackgroundColor(Color.parseColor("#FF99CC"));
                            mTitleText.setBackgroundColor(Color.parseColor("#FF99CC"));
                            mDateText.setBackgroundColor(Color.parseColor("#FF99CC"));
                            mBodyText.setBackgroundColor(Color.parseColor("#FF99CC"));
                        }
                    });
                    Button orange = (Button)colors.findViewById(R.id.color_orange);
                    orange.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            curColor = "#FFCC33";
                            mTitle.setBackgroundColor(Color.parseColor("#FFCC33"));
                            mTitleText.setBackgroundColor(Color.parseColor("#FFCC33"));
                            mDateText.setBackgroundColor(Color.parseColor("#FFCC33"));
                            mBodyText.setBackgroundColor(Color.parseColor("#FFCC33"));
                        }
                    });
                    Button green = (Button)colors.findViewById(R.id.color_green);
                    green.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            curColor = "#CCFF00";
                            mTitle.setBackgroundColor(Color.parseColor("#CCFF00"));
                            mTitleText.setBackgroundColor(Color.parseColor("#CCFF00"));
                            mDateText.setBackgroundColor(Color.parseColor("#CCFF00"));
                            mBodyText.setBackgroundColor(Color.parseColor("#CCFF00"));
                        }
                    });
                    Button purple = (Button)colors.findViewById(R.id.color_purple);
                    purple.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            curColor = "#CC99FF";
                            mTitle.setBackgroundColor(Color.parseColor("#CC99FF"));
                            mTitleText.setBackgroundColor(Color.parseColor("#CC99FF"));
                            mDateText.setBackgroundColor(Color.parseColor("#CC99FF"));
                            mBodyText.setBackgroundColor(Color.parseColor("#CC99FF"));
                        }
                    });
                    Button tiffany = (Button)colors.findViewById(R.id.color_tiffany);
                    tiffany.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            curColor = "#99FFCC";
                            mTitle.setBackgroundColor(Color.parseColor("#99FFCC"));
                            mTitleText.setBackgroundColor(Color.parseColor("#99FFCC"));
                            mDateText.setBackgroundColor(Color.parseColor("#99FFCC"));
                            mBodyText.setBackgroundColor(Color.parseColor("#99FFCC"));
                        }
                    });
                    Button blue = (Button)colors.findViewById(R.id.color_blue);
                    blue.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            curColor = "#66FFFF";
                            mTitle.setBackgroundColor(Color.parseColor("#66FFFF"));
                            mTitleText.setBackgroundColor(Color.parseColor("#66FFFF"));
                            mDateText.setBackgroundColor(Color.parseColor("#66FFFF"));
                            mBodyText.setBackgroundColor(Color.parseColor("#66FFFF"));
                        }
                    });
                    break;
                case R.id.note_store:
                    //System.out.println("save!");
                    saveState();
                    finish();
                    break;
                case R.id.note_exit:
                    //System.out.println("exit!");
                    finish();
                    break;
                default:
                    return true;
            }
            return true;
        }
    };
}
