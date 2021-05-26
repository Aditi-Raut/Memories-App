package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MemoryDetails extends AppCompatActivity {
    String s;
    TextView titleTextView;
    TextView descriptionTextView;

    public void editMemory(View view)
    {
        Intent intent = new Intent(getApplicationContext(),CreateMemoryActivity.class);
        intent.putExtra("edit",true);
        intent.putExtra("title",titleTextView.getText().toString());
        intent.putExtra("content",descriptionTextView.getText().toString());
        intent.putExtra("key",s);
        startActivity(intent);
        finish();
    }

    public void deleteMemory(View view)
    {
        ParseQuery<ParseObject> query = new ParseQuery<>("Memory");
        query.whereEqualTo("objectId",s);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        for(ParseObject obj : objects)
                        {
                            obj.deleteInBackground();
                        }

                    }
                    Intent intent =new Intent(getApplicationContext(),MemoriesActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                   Log.i("Info","Sorry");
                }
                }

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_details);

        Intent intent  = getIntent();
        s = intent.getStringExtra("key");
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);

        setTitle("Kioku");


        ParseQuery<ParseObject> query = new ParseQuery<>("Memory");
        query.whereEqualTo("objectId",s);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        for(ParseObject obj : objects)
                        {
                            titleTextView.setText(obj.getString("title"));
                            descriptionTextView.setText(obj.getString("description"));

                        }
                    }
                }
            }
        });

    }
}