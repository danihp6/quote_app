package com.example.quote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void dashboardNavigate(View view) throws Exception {
        Button button = (Button) view;
        Intent intent;
        switch (button.getId()){
            case R.id.b_dashboard_1: intent = new Intent(this,QuotationActivity.class);break;
            case R.id.b_dashboard_2: intent = new Intent(this,FavouriteActivity.class);break;
            case R.id.b_dashboard_3: intent = new Intent(this,SettingsActivity.class);break;
            case R.id.b_dashboard_4: intent = new Intent(this,AboutActivity.class);break;
            default:throw new Exception("Error in DashboardActivity: dashboardNavigate received " + view.toString());
        }
        startActivity(intent);
    }
}