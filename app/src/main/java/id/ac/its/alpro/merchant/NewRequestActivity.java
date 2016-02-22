package id.ac.its.alpro.merchant;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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

import android.widget.Button;
import android.widget.EditText;
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

import id.ac.its.alpro.merchant.asynctask.AsyncTaskLogout;
import id.ac.its.alpro.merchant.adaptor.NewRequestListAdaptor;
import id.ac.its.alpro.merchant.component.Auth;
import id.ac.its.alpro.merchant.component.NewRequest;

public class NewRequestActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static List<NewRequest> broadcast = new ArrayList<>();
    private static List<NewRequest> direct = new ArrayList<>();
    private static String TOKEN;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        Auth auth = (Auth) getIntent().getSerializableExtra("Auth");
        TOKEN = auth.getToken();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        tabs = (TabLayout) findViewById(R.id.tabs);

        // Set up the ViewPager with the sections adapter.
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
        getMenuInflater().inflate(R.menu.menu_new_request, menu);
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


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_new_request, container, false);
            ListView listView = (ListView) rootView.findViewById(R.id.fragmenList);
            int section = getArguments().getInt(ARG_SECTION_NUMBER);
            if (section == 1){
                NewRequestListAdaptor adaptor = new NewRequestListAdaptor(getContext(),R.layout.item_new_request,broadcast, section);
                listView.setAdapter(adaptor);
            }
            else{
                NewRequestListAdaptor adaptor = new NewRequestListAdaptor(getContext(),R.layout.item_new_request,direct, section);
                listView.setAdapter(adaptor);
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
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

                Request request = baru.fromJson(reader, Request.class);

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

    private static class Request{
        private List<NewRequest> direct, broadcast;

        public Request(List<NewRequest> direct, List<NewRequest> broadcast) {
            this.direct = direct;
            this.broadcast = broadcast;
        }

        public List<NewRequest> getDirect() {
            return direct;
        }

        public void setDirect(List<NewRequest> direct) {
            this.direct = direct;
        }

        public List<NewRequest> getBroadcast() {
            return broadcast;
        }

        public void setBroadcast(List<NewRequest> broadcast) {
            this.broadcast = broadcast;
        }
    }

    public void ambilRequestHandler(View v)
    {
        final Dialog myDialog = new Dialog(this);
        myDialog.setTitle("Ambil Request");
        myDialog.setContentView(R.layout.dialog_ambil_request);
        myDialog.setCancelable(false);
        EditText nama_customer = (EditText) myDialog.findViewById(R.id.nama_customer);
        EditText lokasi = (EditText) myDialog.findViewById(R.id.lokasi_servis);
        EditText jenis_servis = (EditText) myDialog.findViewById(R.id.jenis_servis);
        Button ambil = (Button) myDialog.findViewById(R.id.dialog_ambil);
        Button batal = (Button) myDialog.findViewById(R.id.dialog_batal);
        NewRequest request = (NewRequest) v.getTag();
        Log.d("URL", request.getUrlAmbil());
        nama_customer.setText(request.getNamacustomer());
        lokasi.setText(request.getLokasi());
        jenis_servis.setText(request.getNamajasa());

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.show();

    }
}
