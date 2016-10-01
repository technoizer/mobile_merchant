package id.ac.its.alpro.merchant;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.ac.its.alpro.merchant.adaptor.HistoryRequestListAdaptor;
import id.ac.its.alpro.merchant.adaptor.NewRequestListAdaptor;
import id.ac.its.alpro.merchant.asynctask.AsyncTaskLogout;
import id.ac.its.alpro.merchant.component.Auth;
import id.ac.its.alpro.merchant.component.Request;

public class HistoryRequestActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static List<Request> unpaidRequestsList = new ArrayList<>();
    private static List<Request> finishedRequestsList = new ArrayList<>();
    private ViewPager mViewPager;
    private static String TOKEN;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_request);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Auth auth = (Auth) getIntent().getSerializableExtra("Auth");
        TOKEN = auth.getToken();

        tabs = (TabLayout) findViewById(R.id.tabs);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            new AsyncTaskLogout(this, HistoryRequestActivity.this,TOKEN).execute("hehe");
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
            View rootView = inflater.inflate(R.layout.fragment_history_request, container, false);
            ListView listView = (ListView) rootView.findViewById(R.id.fragmenList);
            int section = getArguments().getInt(ARG_SECTION_NUMBER);
            if (section == 1){
                HistoryRequestListAdaptor adaptor = new HistoryRequestListAdaptor(getContext(),R.layout.item_history_request,unpaidRequestsList, section);
                listView.setAdapter(adaptor);
                listView.setEmptyView(rootView.findViewById(R.id.empty));
            }
            else{
                HistoryRequestListAdaptor adaptor = new HistoryRequestListAdaptor(getContext(),R.layout.item_new_request,finishedRequestsList, section);
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
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Unpaid";
                case 1:
                    return "Finished";
            }
            return null;
        }
    }

    private class AsyncTaskList extends AsyncTask<String, Integer, Double> {
        private ProgressDialog dialog;

        public AsyncTaskList(){
            dialog = new ProgressDialog(HistoryRequestActivity.this);
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
            String url = getResources().getString(R.string.url) + "api/provider/request/unpaid/"+TOKEN;
            HttpGet httpGet = new HttpGet(url);
            Log.d("URL", url);

            try {
                HttpResponse response = httpclient.execute(httpGet);
                Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                Gson baru = new Gson();

                Request[] request = baru.fromJson(reader, Request[].class);

                for (int i = 0; i < request.length; i++){
                    unpaidRequestsList.add(request[i]);
                }

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }

            url = getResources().getString(R.string.url) + "api/provider/request/history/"+TOKEN;
            httpGet = new HttpGet(url);
            Log.d("URL", url);

            try {
                HttpResponse response = httpclient.execute(httpGet);
                Reader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                Gson baru = new Gson();

                Request[] request = baru.fromJson(reader, Request[].class);

                for (int i = 0; i < request.length; i++){
                    finishedRequestsList.add(request[i]);
                }

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            finally {
            }
        }
    }

    public void refreshContent() {
        unpaidRequestsList.clear();
        finishedRequestsList.clear();
        new AsyncTaskList().execute("hehe");
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
        TextView totalHarga = (TextView)myDialog.findViewById(R.id.perkiraan_harga);
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
        totalHarga.setText("Harga Total : Rp. "+ temp+",-");
        catatan.setText(tmp.getCatatancustomer());

        myDialog.show();
    }

    public void callCustomer(View view){
        Request tmp = (Request) view.getTag();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tmp.getNohp()));
        startActivity(intent);
    }
}
