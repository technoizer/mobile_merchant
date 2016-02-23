package id.ac.its.alpro.merchant;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import id.ac.its.alpro.merchant.adaptor.NewRequestListAdaptor;
import id.ac.its.alpro.merchant.adaptor.RequestDiterimaListAdaptor;
import id.ac.its.alpro.merchant.asynctask.AsyncTaskLogout;
import id.ac.its.alpro.merchant.component.Auth;
import id.ac.its.alpro.merchant.component.Request;

public class RequestDiterimaActivity extends AppCompatActivity {

    private static List<Request> requestsDiterima = new ArrayList<>();
    private static String TOKEN;
    private ListView listView;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_diterima);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Auth auth = (Auth) getIntent().getSerializableExtra("Auth");
        TOKEN = auth.getToken();

        listView = (ListView) findViewById(R.id.fragmenList);
        empty = (TextView)findViewById(R.id.empty);

        refreshContent();

        ImageButton tmp = (ImageButton) findViewById(R.id.refreshBtn);
        tmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshContent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            new AsyncTaskLogout(this, RequestDiterimaActivity.this,TOKEN).execute("hehe");
            Log.d("TOKEN", TOKEN);
        }

        return super.onOptionsItemSelected(item);
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

    private class AsyncTaskList extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;

        public AsyncTaskList(){
            dialog = new ProgressDialog(RequestDiterimaActivity.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result) {
            RequestDiterimaListAdaptor adaptor = new RequestDiterimaListAdaptor(RequestDiterimaActivity.this,R.layout.item_request_diterima,requestsDiterima);
            listView.setAdapter(adaptor);
            listView.setEmptyView(empty);
            dialog.dismiss();
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please Wait a Moment...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public void postData() {
            HttpClient httpclient = new DefaultHttpClient();
            String url = "http://servisin.au-syd.mybluemix.net/api/provider/terima/"+TOKEN;
            HttpGet httpGet = new HttpGet(url);
            Log.d("URL", url);

            try {
                HttpResponse response = httpclient.execute(httpGet);
                Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                Gson baru = new Gson();

                Request request[] = baru.fromJson(reader, Request[].class);
                for (int i = 0; i < request.length; i++){
                    requestsDiterima.add(request[i]);
                }

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }
    public void refreshContent() {
        requestsDiterima.clear();
        new AsyncTaskList().execute("hehe");
    }

}
