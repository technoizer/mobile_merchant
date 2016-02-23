package id.ac.its.alpro.merchant;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.ac.its.alpro.merchant.asynctask.AsyncTaskLogout;
import id.ac.its.alpro.merchant.adaptor.NewRequestListAdaptor;
import id.ac.its.alpro.merchant.component.Auth;
import id.ac.its.alpro.merchant.component.Request;

public class NewRequestActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static List<id.ac.its.alpro.merchant.component.Request> broadcast = new ArrayList<>();
    private static List<id.ac.its.alpro.merchant.component.Request> direct = new ArrayList<>();
    private static String TOKEN;
    private ViewPager mViewPager;
    private TabLayout tabs;
    private int hour, minute;
    private EditText jamservis, perkiraanharga;
    String urlAmbil;
    Dialog myDialog;
    TextInputLayout jamservis_l, perkiraanharga_l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Auth auth = (Auth) getIntent().getSerializableExtra("Auth");
        TOKEN = auth.getToken();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        tabs = (TabLayout) findViewById(R.id.tabs);

        mViewPager = (ViewPager) findViewById(R.id.container);

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
            new AsyncTaskLogout(this, NewRequestActivity.this,TOKEN).execute("hehe");
            Log.d("TOKEN", TOKEN);
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_new_request, container, false);
            ListView listView = (ListView) rootView.findViewById(R.id.fragmenList);
            int section = getArguments().getInt(ARG_SECTION_NUMBER);
            if (section == 1){
                NewRequestListAdaptor adaptor = new NewRequestListAdaptor(getContext(),R.layout.item_new_request,broadcast, section);
                listView.setAdapter(adaptor);
                listView.setEmptyView(rootView.findViewById(R.id.empty));
            }
            else{
                NewRequestListAdaptor adaptor = new NewRequestListAdaptor(getContext(),R.layout.item_new_request,direct, section);
                listView.setAdapter(adaptor);
                listView.setEmptyView(rootView.findViewById(R.id.empty));
            }
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "BROADCAST";
                case 1:
                    return "DIRECT";
            }
            return null;
        }
    }

    private class AsyncTaskList extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;

        public AsyncTaskList(){
            dialog = new ProgressDialog(NewRequestActivity.this);
        }
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result) {
            dialog.dismiss();
            mViewPager.setAdapter(mSectionsPagerAdapter);
            tabs.setupWithViewPager(mViewPager);
            tabs.setTabGravity(TabLayout.GRAVITY_FILL);
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
            String url = "http://servisin.au-syd.mybluemix.net/api/provider/recentrequest/"+TOKEN;
            HttpGet httpGet = new HttpGet(url);
            Log.d("URL", url);

            try {
                HttpResponse response = httpclient.execute(httpGet);
                Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                Gson baru = new Gson();

                NewRequest request = baru.fromJson(reader, NewRequest.class);

                Log.d("Hehe", request.getBroadcast().get(0).toString());
                for (int i = 0; i < request.broadcast.size(); i++){
                    broadcast.add(request.getBroadcast().get(i));
                }

                for (int i = 0; i < request.direct.size(); i++){
                    direct.add(request.getDirect().get(i));
                }

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }

    public void refreshContent() {
        broadcast.clear();
        direct.clear();
        new AsyncTaskList().execute("hehe");
    }

    private static class NewRequest{
        private List<id.ac.its.alpro.merchant.component.Request> direct, broadcast;

        public NewRequest(List<id.ac.its.alpro.merchant.component.Request> direct, List<id.ac.its.alpro.merchant.component.Request> broadcast) {
            this.direct = direct;
            this.broadcast = broadcast;
        }

        public List<id.ac.its.alpro.merchant.component.Request> getDirect() {
            return direct;
        }

        public void setDirect(List<id.ac.its.alpro.merchant.component.Request> direct) {
            this.direct = direct;
        }

        public List<id.ac.its.alpro.merchant.component.Request> getBroadcast() {
            return broadcast;
        }

        public void setBroadcast(List<id.ac.its.alpro.merchant.component.Request> broadcast) {
            this.broadcast = broadcast;
        }
    }

    public void ambilRequestHandler(View v) {
        myDialog = new Dialog(this);
        myDialog.setTitle("Ambil Request");
        myDialog.setContentView(R.layout.dialog_ambil_request);
        myDialog.setCancelable(false);
        EditText nama_customer = (EditText) myDialog.findViewById(R.id.nama_customer);
        EditText lokasi = (EditText) myDialog.findViewById(R.id.lokasi_servis);
        EditText jenis_servis = (EditText) myDialog.findViewById(R.id.jenis_servis);
        jamservis = (EditText)myDialog.findViewById(R.id.jam_servis);
        perkiraanharga = (EditText)myDialog.findViewById(R.id.perkiraan_harga);
        jamservis_l = (TextInputLayout)myDialog.findViewById(R.id.jam_servis_layout);
        perkiraanharga_l = (TextInputLayout)myDialog.findViewById(R.id.perkiraan_harga_layout);
        Button ambil = (Button) myDialog.findViewById(R.id.dialog_ambil);
        Button batal = (Button) myDialog.findViewById(R.id.dialog_batal);
        Request request = (id.ac.its.alpro.merchant.component.Request) v.getTag();
        urlAmbil = request.getUrlAmbil();
        nama_customer.setText(request.getNamacustomer());
        lokasi.setText(request.getLokasi());
        jenis_servis.setText(request.getTipejasa());

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        jamservis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
                Dialog tmp = new TimePickerDialog(NewRequestActivity.this, timePickerListener, hour, minute, true);
                tmp.show();
            }
        });

        ambil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateHarga() && validateJam())
                    new AsyncTaskAmbil(jamservis.getText().toString().trim(),perkiraanharga.getText().toString().trim(),myDialog).execute("hehe");
            }
        });

        myDialog.show();

    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int selectedHourOfDay, int selectedMinute) {
            hour = selectedHourOfDay;
            minute = selectedMinute;
            jamservis.setText(new StringBuilder().append(hour < 10 ? "0" + hour : hour).append(":").append(minute < 10 ? "0" + minute : minute));
        }
    };

    private class AsyncTaskAmbil extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;
        private String jamServis, perkiraanHarga;
        private int status;
        private Res result;
        private Dialog myDialog;

        public AsyncTaskAmbil(String jamServis, String perkiraanHarga, Dialog myDialog){
            super();
            dialog = new ProgressDialog(NewRequestActivity.this);
            this.jamServis = jamServis;
            this.perkiraanHarga = perkiraanHarga;
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
                Toast.makeText(getApplicationContext(),"Request Berhasil di Ambil, Menunggu Persetujuan Customer",Toast.LENGTH_LONG).show();
            else if (result.getStatus().equals("failed"))
                Toast.makeText(getApplicationContext(),"Request Sudah Pernah di Ambil",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(),"Request Sudah Tidak Berlaku",Toast.LENGTH_LONG).show();
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
            String url = urlAmbil+TOKEN;
            HttpPost httpPost = new HttpPost(url);

            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("jamservis", jamServis));
            postParameters.add(new BasicNameValuePair("hargaperkiraan", perkiraanHarga));

            Log.d("URL", url);
            Log.d("harga",perkiraanHarga);

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
            public Res(String status) {
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

    private boolean validateJam(){
        if (jamservis.getText().toString().trim().isEmpty()) {
            jamservis_l.setError("Silahkan isi jam terlebih dahulu");
            return false;
        } else {
            jamservis_l.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateHarga(){
        if (perkiraanharga.getText().toString().trim().isEmpty()) {
            perkiraanharga_l.setError("Silahkan isi perkiraan harga terlebih dahulu");
            return false;
        } else {
            perkiraanharga_l.setErrorEnabled(false);
        }
        return true;
    }
}
