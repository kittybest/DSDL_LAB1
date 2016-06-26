package com.example.zhanyuzhen.memo;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.graphics.Color;

/*import com.example.zhanyuzhen.memo.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;*/


public class Calendar extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        final String[][][] textofdate = new String[3][12][31];
        for(int i = 0 ; i <3 ; i++)
            for(int j = 0 ; j < 12 ; j++)
                for (int k = 0; k < 31; k++)
                {
                    textofdate[i][j][k]="本日沒有安排行程喔喔喔";
                }
        CalendarView cv;
        cv=(CalendarView)findViewById(R.id.calendarView);
        cv.setOnDateChangeListener(new OnDateChangeListener(){

            @Override
            public void onSelectedDayChange(CalendarView view,final int year,
                                            final int month, final int dayOfMonth) {
                // TODO Auto-generated method stub
                //使用Toast显示用户选择的日期
                Toast.makeText(Calendar.this,
                        "你選擇的是"+year+"年"+month+"月"+dayOfMonth+"日"
                        , Toast.LENGTH_SHORT).show();

                final TextView myTextView = (TextView)findViewById(R.id.showtext);
                //String word = textofdate[year - 2016][month-1][dayOfMonth-1].getText().toString();
                //String word = myTextView.getText().toString();
                if(textofdate[year - 2016][month - 1][dayOfMonth - 1].isEmpty() ||  textofdate[year - 2016][month - 1][dayOfMonth - 1].equals("輸入完成請再按一次按鈕"))
                    textofdate[year - 2016][month - 1][dayOfMonth - 1] = "本日沒有安排行程喔喔喔";
                myTextView.setText(textofdate[year - 2016][month - 1][dayOfMonth - 1]);
                myTextView.setTextColor(Color.BLUE);
                myTextView.setVisibility(View.VISIBLE);
                final EditText editwhat = (EditText)findViewById(R.id.editwhat);
                editwhat.setVisibility(View.INVISIBLE);

                //editwhat.setText("輸入完成請再按一次按鈕");

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //          .setAction("Action", null).show();
                        //  myTextView.setVisibility(View.INVISIBLE);
                        myTextView.setText("輸入完成請再按一次按鈕");
                        //editwhat.setText("輸入完成請再按一次按鈕");
                        editwhat.setVisibility(View.VISIBLE);
                        textofdate[year - 2016][month - 1][dayOfMonth - 1] = editwhat.getText().toString();


                    }

                });
            }
        });
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

   /* @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.angelocsc.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.angelocsc.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }*/
}
