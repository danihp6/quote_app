package com.example.quote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import databases.RoomDB;
import databases.RoomDao;
import databases.SQLiteOpenHelper;
import models.Quotation;
import services.QuotationService;
import threads.DatabaseThread;
import threads.QuotationServiceThread;

public class QuotationActivity extends AppCompatActivity {
    MenuItem addItem;
    boolean addVisible = false;
    TextView tv_text;
    TextView tv_author;
    SQLiteOpenHelper helperDb;
    RoomDao roomDb;
    boolean useRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);

        useRoom = prefs.getString("db","0").equals("0");

        if( useRoom) roomDb = RoomDB.getInstance(this).room();
        else helperDb = SQLiteOpenHelper.getInstance(this);

        String defaultName = getString(R.string.quotation_default_name);
        String username = prefs.getString("username",defaultName);

        tv_text = findViewById(R.id.tv_text);
        tv_author = findViewById(R.id.tv_author);

        if(savedInstanceState == null)
            tv_text.setText(username + " " +tv_text.getText());
         else {

            tv_text.setText(savedInstanceState.getString("quoteText"));
            tv_author.setText(savedInstanceState.getString("quoteAuthor"));

            addVisible = savedInstanceState.getBoolean("addVisible");
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putString("quoteText",tv_text.getText().toString());
        outState.putString("quoteAuthor",tv_author.getText().toString());

        outState.putBoolean("addVisible",addVisible);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_quotation_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        addItem = menu.findItem(R.id.i_add_favourites);
        addItem.setVisible(addVisible);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.i_add_favourites: addFavourite(); break;
            case R.id.mi_refresh: refresh(); break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addFavourite(){
        Quotation quotation = new Quotation(tv_text.getText().toString(),tv_author.getText().toString());
        addVisible = false;
        addItem.setVisible(addVisible);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if( useRoom) roomDb.addQuotation(quotation);
                else helperDb.addQuotation(quotation);
            }
        }).start();

    }

    public void refresh() {
        if(isConnected()){
            addVisible = false;
            new QuotationServiceThread(this,useRoom).start();
        } else {
            Toast.makeText(QuotationActivity.this,R.string.quotation_red_problems,Toast.LENGTH_LONG).show();
        }

    }

    public boolean isConnected() {
        boolean result = false;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > 22) {
            final Network activeNetwork = manager.getActiveNetwork();
            if (activeNetwork != null) {
                final NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(activeNetwork);
                result = networkCapabilities != null && (
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
            }
        } else {
            NetworkInfo info = manager.getActiveNetworkInfo();
            result = ((info != null) && (info.isConnected()));
        }
        return result;
    }

    public void setAddVisible(boolean visible){
        addVisible = visible;
    }

    public void setQuotation(Quotation quotation){
        tv_text.setText("\""+quotation.getQuote()+"\"");
        tv_author.setText(quotation.getAuthor());
    }
}