package com.example.zhanyuzhen.memo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends ListActivity {


    ListView CLV;
    CountDownDbAdapter cDbHelper;
    CountSimpleCursorAdapter countdowns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        CLV = (ListView)findViewById(android.R.id.list);
        cDbHelper = new CountDownDbAdapter(this);
        cDbHelper.open();
        //cDbHelper.createNote("hello Test", "2016/05/25 00:00:00");
        //cDbHelper.createNote("hello Test2", "2016/06/12 00:00:00");
        fillData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void GoToNote(View view){
        Intent intent = new Intent(this, DisplayNote.class);
        startActivity(intent);
    }

    public void GoToDate(View view){
        Intent intent = new Intent(this, Calendar.class);
        startActivity(intent);
    }

    public void GoToCountdown(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Count Down");
       // builder.setMessage("Time example: yyyy/MM/dd HH:mm:ss");
        final View addCountDown = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_countdown, null);
        AlertDialog CountDown;
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText countdowntitle = (EditText) addCountDown.findViewById(R.id.CountDownTitle);
                EditText countdowndate = (EditText) addCountDown.findViewById(R.id.CountDownDate);
                if(countdowntitle.getText() != null && countdowndate.getText() != null) {
                    System.out.println("create Note");
                    cDbHelper.createNote(countdowntitle.getText().toString(), countdowndate.getText().toString());
                    Cursor newdata = cDbHelper.fetchAllNotes();
                    countdowns.swapCursor(newdata);
                    countdowns.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        builder.setView(addCountDown);
        CountDown = builder.create();
        CountDown.show();
    }

    void fillData(){
        Cursor countdownCursor = cDbHelper.fetchAllNotes();
        String[] from = new String[]{CountDownDbAdapter.KEY_TITLE, CountDownDbAdapter.KEY_DATE};
        int[] to = new int[]{R.id.test_text2, R.id.days};
        countdowns = new CountSimpleCursorAdapter(this, R.layout.countdown_list_row, countdownCursor, from, to, 0);
        CLV.setAdapter(countdowns);
    }

    public class CountSimpleCursorAdapter extends SimpleCursorAdapter{

        //Cursor cursor;

        public CountSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flag) {
            super(context, layout, c, from, to, flag);
            //this.cursor = c;
        }
        public void bindView(View v, Context context, Cursor cursor){
            super.bindView(v, context, cursor);
            TextView view = (TextView)v.findViewById(R.id.days);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String future = cursor.getString(2);
            if(future != null) {
                Date date = null;
                try {
                    date = sdf.parse(future);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long msfuture = date.getTime();
                long msNow = System.currentTimeMillis();
                long left = msfuture - msNow;
                long days = (left / 1000) / 86400;
                String days_left = String.valueOf(days);
                view.setText(days_left + "days left.");
            }
        }
    }

    protected void onListItemClick(ListView lv, View v, int position, final long id){
        super.onListItemClick(lv, v, position, id);
        Cursor item = cDbHelper.fetchNote(id);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit CountDown");
        final View CountDown = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_countdown, null);
        final EditText countdowntitle = (EditText) CountDown.findViewById(R.id.CountDownTitle);
        final EditText countdowndate = (EditText)CountDown.findViewById(R.id.CountDownDate);
        countdowntitle.setText(item.getString(item.getColumnIndexOrThrow(CountDownDbAdapter.KEY_TITLE)));
        countdowndate.setText(item.getString(item.getColumnIndexOrThrow(CountDownDbAdapter.KEY_DATE)));

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cDbHelper.updateNote(id, countdowntitle.getText().toString(), countdowndate.getText().toString());
                Cursor newdata = cDbHelper.fetchAllNotes();
                countdowns.swapCursor(newdata);
                countdowns.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cDbHelper.deleteNote(id);
                Cursor newdata = cDbHelper.fetchAllNotes();
                countdowns.swapCursor(newdata);
                countdowns.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.setView(CountDown);
        AlertDialog Edit = builder.create();
        Edit.show();
    }
}
