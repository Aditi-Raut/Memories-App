package com.parse.starter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoriesActivity extends AppCompatActivity {

    public void createNewMemory(View view)
    {
        Intent intent = new Intent(getApplicationContext(),CreateMemoryActivity.class);
        intent.putExtra("edit",false);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.share_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout)
        {
            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memories);
        setTitle("Kioku");

        final ListView listView = ( ListView) findViewById(R.id.listView);
        final ArrayList<String> keys = new ArrayList<>();

        final List<Map<String,String>> memData = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Memory");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername().toString());
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null)
                {
                    for(ParseObject mem: objects)
                    {
                        Map<String,String> memInfo = new HashMap<>();
                        memInfo.put("title",mem.getString("title"));
                        memInfo.put("description",mem.getString("description").substring(0,(mem.getString("description").length()>=10? 10 : mem.getString("description").length()))+"...");
                        keys.add(mem.getObjectId().toString());

                        memData.add(memInfo);
                    }
                    SimpleAdapter simpleAdapter = new SimpleAdapter(MemoriesActivity.this,memData, android.R.layout.simple_expandable_list_item_2,new String[] {"title","description"},new int[] {android.R.id.text1,android.R.id.text2});
                    listView.setAdapter(simpleAdapter);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),MemoryDetails.class);
                intent.putExtra("key",keys.get(position));
                startActivity(intent);
            }
        });


    }
}