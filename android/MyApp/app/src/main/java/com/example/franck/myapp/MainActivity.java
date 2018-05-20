package com.example.franck.myapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.franck.myapp.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;

    private String[] myDataset = new String[] {"Martini", "Bordo", "Lamanshan", "Antonio", "Loshonio", "Pechorkio",
            "Martini", "Bordo", "Lamanshan", "Antonio", "Loshonio", "Pechorkio"};
    private String[] description = new String[] {"Very taste and delitious", "Smart vino",
            "Very tasty", "Old and tasty", "Very taste and delitious", "Smart vino","Very taste and delitious", "Smart vino",
            "Very tasty", "Old and tasty", "Very taste and delitious", "Smart vino"};
    private List<Integer> imageViews = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initList();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), BluetoothActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(myDataset, imageViews, description);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new MyAdapter.RecyclerTouchListener(getApplicationContext(), mRecyclerView, new MyAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) throws Exception {
                //transport data to fragment
//                Intent intent = new Intent(view.getContext(), .class);
//                intent.putExtra("title", myDataset[position]);
//                intent.putExtra("description", description[position]);
//                view.getContext().startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initList() {
        imageViews.add(R.drawable.a1);
        imageViews.add(R.drawable.a2);
        imageViews.add(R.drawable.a3);
        imageViews.add(R.drawable.a4);
        imageViews.add(R.drawable.a5);
        imageViews.add(R.drawable.a6);
        imageViews.add(R.drawable.a1);
        imageViews.add(R.drawable.a2);
        imageViews.add(R.drawable.a3);
        imageViews.add(R.drawable.a4);
        imageViews.add(R.drawable.a5);
        imageViews.add(R.drawable.a6);
    }

}

