package id.ac.its.alpro.merchant;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    private EditText catatanpenyedia,hargatotal;
    String urlAmbil;
    TextInputLayout catatanpenyedia_l, hargatotal_l;

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
        Request tmp = (Request) view.getTag();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tmp.getNohp()));
        startActivity(intent);
    }

    public void getCustomerLocation(View view){
        Request tmp = (Request) view.getTag();
        String map = "http://maps.google.co.in/maps?q=" + tmp.getLat() + "," + tmp.getLng();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(intent);
    }

    public void makeReport(View view){
        Request tmp = (Request) view.getTag();
        final Dialog myDialog = new Dialog(this);
        myDialog.setTitle("Detail Servis");
        myDialog.setContentView(R.layout.dialog_isi_report);

        urlAmbil = tmp.getUrlAmbil();
        catatanpenyedia = (EditText)myDialog.findViewById(R.id.catatan_servis);
        hargatotal = (EditText)myDialog.findViewById(R.id.harga_total);
        catatanpenyedia_l = (TextInputLayout)myDialog.findViewById(R.id.catatan_servis_layout);
        hargatotal_l = (TextInputLayout)myDialog.findViewById(R.id.harga_total_layout);
        final Button ambil = (Button) myDialog.findViewById(R.id.dialog_ambil);
        Button batal = (Button) myDialog.findViewById(R.id.dialog_batal);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });


        ambil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateHarga() && validateCatatan())
                    new AsyncTaskAmbil(catatanpenyedia.getText().toString().trim(),hargatotal.getText().toString().trim(),myDialog).execute("hehe");
            }
        });

        myDialog.show();
    }

    public void seeDetail(View view){
        Request tmp = (Request) view.getTag();
        final Dialog myDialog = new Dialog(this);
        myDialog.setTitle("Detail Servis");
        myDialog.setContentView(R.layout.dialog_detail_request);
        TextView nama_customer = (TextView) myDialog.findViewById(R.id.nama_customer);
        TextView lokasi = (TextView) myDialog.findViewById(R.id.lokasi_servis);
        TextView jenis_servis = (TextView) myDialog.findViewById(R.id.jenis_servis);
        TextView jamservis = (TextView)myDialog.findViewById(R.id.jam_servis);
        TextView perkiraanHarga = (TextView)myDialog.findViewById(R.id.perkiraan_harga);
        TextView catatan = (TextView)myDialog.findViewById(R.id.catatan_servis);

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        String temp = formatter.format(tmp.getHargaperkiraan().longValue());

        nama_customer.setText(tmp.getNamacustomer());
        lokasi.setText(tmp.getLokasi());
        jenis_servis.setText(tmp.getTipejasa());
        jamservis.setText(tmp.getTanggalrequest() + " at " + tmp.getJamservis());
        perkiraanHarga.setText("Perkiraan Harga : Rp. " + temp + ",-");
        catatan.setText(tmp.getCatatancustomer());

        myDialog.show();
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

    private class AsyncTaskAmbil extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;
        private String catatan, hargaTotal;
        private int status;
        private Res result;
        private Dialog myDialog;

        public AsyncTaskAmbil(String catatan, String hargaTotal, Dialog myDialog){
            super();
            dialog = new ProgressDialog(RequestDiterimaActivity.this);
            this.hargaTotal = hargaTotal;
            this.catatan = catatan;
            this.myDialog = myDialog;
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double resul) {
            dialog.dismiss();
            myDialog.dismiss();
            if (result.getStatus().equals("success"))
                Toast.makeText(getApplicationContext(), "Data Request Berhasil di Isi", Toast.LENGTH_LONG).show();
            else if (result.getStatus().equals("failed"))
                Toast.makeText(getApplicationContext(),"Data Request Gagal di Isi",Toast.LENGTH_LONG).show();
            refreshContent();
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait a moment...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public void postData() {
            ArrayList<NameValuePair> postParameters;
            HttpClient httpclient = new DefaultHttpClient();
            String url = urlAmbil;
            HttpPost httpPost = new HttpPost(url);

            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("catatanpenyediajasa", catatan));
            postParameters.add(new BasicNameValuePair("hargatotal", hargaTotal));

            Log.d("URL", url);

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
                HttpResponse response = httpclient.execute(httpPost);
                status = response.getStatusLine().getStatusCode();
                if (status == 200){
                    Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                    //Log.d("TES", getStringFromInputStream(reader));
                    Gson baru = new Gson();

                    result = baru.fromJson(reader, Res.class);
                    Log.d("Hehe", result.getStatus());
                }
            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }

        public class Res {
            String status;
            public Res(String status, String token) {
                this.status = status;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

        }
    }

    private boolean validateCatatan(){
        if (catatanpenyedia.getText().toString().trim().isEmpty()) {
            catatanpenyedia_l.setError("Silahkan isi field ini terlebih dahulu");
            return false;
        } else {
            catatanpenyedia_l.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateHarga(){
        if (hargatotal.getText().toString().trim().isEmpty()) {
            hargatotal_l.setError("Silahkan isi perkiraan harga terlebih dahulu");
            return false;
        } else {
            hargatotal_l.setErrorEnabled(false);
        }
        return true;
    }

}
