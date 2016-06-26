package com.example.zhanyuzhen.memo;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

//public class DisplayNote extends ListActivity
public class DisplayNote extends ExpandableListActivity
        //implements LoaderManager.LoaderCallbacks<Cursor>
{


    private NotesDbAdapter mDbHelper;
    int ADD = 1;
    MySimpleCursorAdapter notes;
    //Loader<Cursor> loader;

    ExpandableListView ELV ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);
        ELV = (ExpandableListView) findViewById(android.R.id.list);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        //create new note button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
               addNewNote();
           }
        });

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //cursor loader
       /* loader = getLoaderManager().getLoader(-1);
        if(loader != null && !loader.isReset()){
            getLoaderManager().restartLoader(-1, null, this);
        } else{
            getLoaderManager().initLoader(-1, null, this);
        }*/

        //database helper
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        fillData();

    }

    public void addNewNote(){
        Intent intent = new Intent(this, AddNote.class);
        startActivityForResult(intent, ADD);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }

    private void fillData(){
        //同步資料, first try no to use cursor loader
       // getLoaderManager().initLoader(0, null, this);

        Cursor notesCursor = mDbHelper.fetchAllNotes();
        String[] from_group = new String[]{NotesDbAdapter.KEY_TITLE};
        int[] to_group = new int[]{R.id.test_text1};
        String[] from_child = new String[]{NotesDbAdapter.KEY_BODY};
        int[] to_child = new int[]{R.id.body_list};

        notesCursor = mDbHelper.fetchAllNotes();
        //notes = new MySimpleCursorAdapter(this, R.layout.note_list_row, notesCursor, from, to, 0);
        /*ListView note_list = (ListView) findViewById(android.R.id.list);
        note_list.setAdapter(notes);*/
        notes = new MySimpleCursorAdapter(this, notesCursor, R.layout.note_list_row, from_group, to_group, R.layout.note_list_row_child, from_child, to_child);
        ELV.setAdapter(notes);
    }

    public class MySimpleCursorAdapter extends SimpleCursorTreeAdapter {

        public MySimpleCursorAdapter(Context context, Cursor cursor, int group_layout, String[] from_group, int[] to_group, int child_layout, String[] from_child, int[] to_child) {
            super(context, cursor, group_layout, from_group, to_group, child_layout, from_child, to_child);
        }

        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            final long row_id = groupCursor.getLong(groupCursor.getColumnIndex(NotesDbAdapter.KEY_ROWID));
            return mDbHelper.fetchNote(row_id);
        }

        protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded){
            super.bindGroupView(view, context, cursor, isExpanded);
            final long row_id = cursor.getLong(cursor.getColumnIndex(NotesDbAdapter.KEY_ROWID));
            String color = cursor.getString(cursor.getColumnIndex(NotesDbAdapter.KEY_COLOR));
            view.setBackgroundColor(Color.parseColor(color));
            System.out.println("in bind group view!"+row_id);
            Button btn_edit = (Button) view.findViewById(R.id.edit_note);
            Button btn_del = (Button) view.findViewById(R.id.delete_note);
            btn_edit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), AddNote.class);
                    i.putExtra(NotesDbAdapter.KEY_ROWID, row_id);
                    startActivityForResult(i, ADD);
                }
            });
            btn_del.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mDbHelper.deleteNote(row_id);
                    Cursor cs = mDbHelper.fetchAllNotes();
                    notes.setGroupCursor(cs);
                    notifyDataSetChanged();
                }
            });
        }
        @Override
        protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild){
            System.out.println("In bind child view!");
            super.bindChildView(view, context, cursor, isLastChild);
            String color = cursor.getString(cursor.getColumnIndex(NotesDbAdapter.KEY_COLOR));
            view.setBackgroundColor(Color.parseColor(color));
        }
        @Override
        public View newChildView(Context context, Cursor cursor, boolean isLastChild,
                                 ViewGroup parent) {
            System.out.println("In new child view!");
            return super.newChildView(context, cursor, isLastChild, parent);
        }
    }


    /*public class MySimpleCursorAdapter extends SimpleCursorAdapter{

        public MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flag) {
            super(context, layout, c, from, to, flag);
        }
        @Override
        public void bindView(View view, Context context, Cursor cursor){
            super.bindView(view, context, cursor);
            final long row_id = cursor.getLong(cursor.getColumnIndex(mDbHelper.KEY_ROWID));
            //row_id = cursor.getColumnIndex(mDbHelper.KEY_ROWID);
            Button btn_edit = (Button) view.findViewById(R.id.edit_note);
            Button btn_del = (Button) view.findViewById(R.id.delete_note);
            btn_edit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //long vid = v.getId(); //but this is the button's id?
                    Intent i = new Intent(v.getContext(), AddNote.class);
                    i.putExtra(NotesDbAdapter.KEY_ROWID, row_id);
                    startActivityForResult(i, ADD);
                }
            });
            btn_del.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //long vid = v.getId();//but this is the button's id?
                    mDbHelper.deleteNote(row_id);
                    Cursor cursor = mDbHelper.fetchAllNotes();
                    notes.swapCursor(cursor);
                    notifyDataSetChanged();
                //    getLoaderManager().restartLoader(0, null, );//this is fault
                }
            });
        }
        public View newView(Context context, Cursor cursor, ViewGroup viewgroup){
            super.newView(context, cursor, viewgroup);
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.note_list_row, viewgroup, false);
            bindView(v, context, cursor);
            return v;
        }
    }*/

/*    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this);
        String[] PROJECTION = new String[]
                {NotesDbAdapter.KEY_ROWID, NotesDbAdapter.KEY_TITLE, NotesDbAdapter.KEY_BODY, NotesDbAdapter.KEY_DATE};
        cursorLoader.setProjection(PROJECTION);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int id = loader.getId();
        if(id != -1){
            //child cursor
            if(!data.isClosed()){
                try{
                    notes.setChildrenCursor(id, data);
                } catch (NullPointerException e) {

                }
            } else{
                notes.setGroupCursor(data);
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        if(id != -1){
            try{
                notes.setChildrenCursor(id, null);
            } catch (NullPointerException e){}
        } else {
            notes.setGroupCursor(null);
        }
    }*/


}
