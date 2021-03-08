package com.example.quote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.List;

import adapters.OnClickListener;
import adapters.QuotationAdapter;
import databases.RoomDB;
import databases.RoomDao;
import databases.SQLiteOpenHelper;
import models.Quotation;
import threads.DatabaseThread;

public class FavouriteActivity extends AppCompatActivity {

    static final String url = "https://en.wikipedia.org/wiki/Special:Search?search=";
    QuotationAdapter adapter;
    SQLiteOpenHelper helperDb;
    RoomDao roomDb;
    boolean useRoom;
    MenuItem mi_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);

        useRoom = prefs.getString("db","0").equals("0");

        if(useRoom) roomDb = RoomDB.getInstance(this).room();
        else helperDb = SQLiteOpenHelper.getInstance(this);

        RecyclerView recyclerView = findViewById(R.id.rv_favouriteQuotations);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        adapter = new QuotationAdapter( new OnClickListener() {
            @Override
            public void onClick(int position) {
                navigateWikipedia(position);
            }

            @Override
            public void onLongClick(int position) {
                deleteDialog(position);
            }
        });
        recyclerView.setAdapter(adapter);

        new DatabaseThread(this,useRoom).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_favourites,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mi_clear = menu.findItem(R.id.mi_favourites_clear);
        if(adapter.getItemCount() < 1) mi_clear.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mi_favourites_clear:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(android.R.drawable.stat_sys_warning);
                builder.setMessage(R.string.favourite_dialog_clear);
                builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                    adapter.clearAll();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(useRoom) roomDb.clear();
                            else helperDb.clear();
                        }
                    }).start();

                    item.setVisible(false);
                    Toast.makeText(this,R.string.favourite_dialog_clear_yes,Toast.LENGTH_LONG).show();
                });
                builder.setNegativeButton(android.R.string.no,null);
                builder.create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteDialog(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(FavouriteActivity.this);
        builder.setIcon(android.R.drawable.stat_sys_warning);
        builder.setMessage(R.string.favourite_dialog_remove);
        builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
            Quotation quotation = adapter.getQuotationAt(position);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(useRoom) roomDb.deleteQuotation(quotation);
                    else helperDb.deleteQuotation(quotation);
                }
            }).start();
            adapter.removeQuotationAt(position);
            if(adapter.getItemCount() < 1) mi_clear.setVisible(false);

            Toast.makeText(FavouriteActivity.this,R.string.favourite_dialog_remove_yes,Toast.LENGTH_LONG).show();
        });
        builder.setNegativeButton(android.R.string.no,null);
        builder.create().show();
    }

    public void updateAdapter(List<Quotation> quotations){
        adapter.setQuotations(quotations);
    }

    public void navigateWikipedia(int position) {
        Quotation quotation = adapter.getQuotationAt(position);
        String name = quotation.getAuthor();
        if(name.isEmpty()) Toast.makeText(this,R.string.favourite_empty_author,Toast.LENGTH_LONG).show();
        else{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url + URLEncoder.encode(name) ));
            startActivity(intent);
        }
    }
}