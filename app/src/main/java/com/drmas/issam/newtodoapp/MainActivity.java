package com.drmas.issam.newtodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mTaskList;
    private DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        mTaskList = (RecyclerView) findViewById( R.id.task_list);
        mTaskList.setHasFixedSize( true );
        mTaskList.setLayoutManager( new LinearLayoutManager( this ));
        mdatabase = FirebaseDatabase.getInstance().getReference().child( "Tasks" );

        //adding the date values
        TextView bannerDay = (TextView) findViewById( R.id.bannerDay );
        TextView bannerTime = (TextView) findViewById( R.id.bannerTime );

        SimpleDateFormat sdf = new SimpleDateFormat( "EEEE" );
        Date d = new Date( );
        String dayOfTheWeek = sdf.format( d );
        bannerDay.setText( dayOfTheWeek );

        long date = System.currentTimeMillis();
        SimpleDateFormat sdff = new SimpleDateFormat( "dd MM yyyy 'at' hh:mm:ss ");
        String dateString = sdff.format( date );
        bannerTime.setText( dateString);


    } ///// end main

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public TaskViewHolder(View itemView) {
            super( itemView );
            mView = itemView;
        }
        public void setName(String name){
            TextView task_name = (TextView) mView.findViewById( R.id.taskName );
            task_name.setText( name );
        }
        public void setTime(String time){
            TextView task_time = (TextView) mView.findViewById( R.id.taskTime );
            task_time.setText( time );
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Task, TaskViewHolder> FBRA = new FirebaseRecyclerAdapter<Task, TaskViewHolder>(
                Task.class,
                R.layout.task_row,
                TaskViewHolder.class,
                mdatabase
        ) {

            @Override
            protected void populateViewHolder(TaskViewHolder viewHolder, Task model, int position) {

             final String task_key = getRef( position ).getKey().toString();
             viewHolder.setName( model.getName());
             viewHolder.setTime( model.getTime());

            viewHolder.mView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent singleTaskActivity = new Intent( MainActivity.this, SingleTask.class );
                    singleTaskActivity.putExtra( "TaskId", task_key );
                    startActivity( singleTaskActivity );
                }
            } );
            }
        };
        mTaskList.setAdapter( FBRA );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addTask){
            Intent addIntent = new Intent (MainActivity.this, AddTask.class);
            startActivity( addIntent );
        }

        return super.onOptionsItemSelected( item );
    }





}//////////////////////////
