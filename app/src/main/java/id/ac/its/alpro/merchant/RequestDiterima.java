package id.ac.its.alpro.merchant;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Locale;

public class RequestDiterima extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_diterima);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
    public void callCustomer(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:082121212093"));
        startActivity(intent);
    }

    public void getCustomerLocation(View view){

        String map = "http://maps.google.co.in/maps?q=" + "Institut Teknologi Sepuluh Nopember";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(intent);

    }
    public void makeReport(View view){
        Intent intent = new Intent(this, LaporanService.class);
        startActivity(intent);
    }

}
