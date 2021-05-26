package com.parse.starter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CreateMemoryActivity extends AppCompatActivity implements View.OnClickListener{
    EditText titleEditText;
    EditText descriptionEditText;
    TextView headerTextView;
    Button addMemoryButton;
    String key;
    boolean mode;


    public void onClick(View view) {
       if(view.getId() == R.id.backgroundLayout2)
        {
            //For Keyboard settings.
            //When touched on any other part of screen close the keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memory);
        ConstraintLayout backgroundLayout = (ConstraintLayout) findViewById(R.id.backgroundLayout2);

        titleEditText = (EditText) findViewById(R.id.titleEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        addMemoryButton = (Button) findViewById(R.id.addMemoryButton);
        headerTextView = (TextView) findViewById(R.id.headerTextView);


        setTitle("Kioku");
        backgroundLayout.setOnClickListener(this);
        Intent intent = getIntent();
        mode = intent.getBooleanExtra("edit",false);
        if(mode)
        {
            titleEditText.setText(intent.getStringExtra("title"));
            key = intent.getStringExtra("key");
            descriptionEditText.setText(intent.getStringExtra("content"));
            headerTextView.setText("Edit Memory");
            addMemoryButton.setText("Modify");
        }

    }

    public void addMemory()
    {
        ParseObject  memory = new ParseObject("Memory");
        memory.put("title",titleEditText.getText().toString());
        memory.put("description",descriptionEditText.getText().toString());
        memory.put("username", ParseUser.getCurrentUser().getUsername());

        memory.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    Intent intent = new Intent(getApplicationContext(),MemoriesActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(CreateMemoryActivity.this,"Something went wrong :{",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void modifyMemory()
    {
        ParseQuery<ParseObject> query = new ParseQuery<>("Memory");
        query.whereEqualTo("objectId",key);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        for(ParseObject obj : objects)
                        {
                            obj.put("title",titleEditText.getText().toString());
                            obj.put("description",descriptionEditText.getText().toString());
                            obj.saveInBackground();
                        }

                    }
                    Intent intent =new Intent(getApplicationContext(),MemoryDetails.class);
                    intent.putExtra("key",key);
                    startActivity(intent);
                    finish();
                }
                else {
                    Log.i("Info","Sorry");
                }
            }

        });
    }

    public void clickCreate(View view)
    {

        addMemoryButton.setEnabled(false);
        if(!mode)
        {
            addMemory();
        }
        else
        {
            modifyMemory();
        }

    }
}