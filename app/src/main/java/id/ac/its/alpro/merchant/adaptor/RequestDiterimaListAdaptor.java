package id.ac.its.alpro.merchant.adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import id.ac.its.alpro.merchant.R;
import id.ac.its.alpro.merchant.component.Request;

/**
 * Created by ALPRO on 31/12/2015.
 */
public class RequestDiterimaListAdaptor extends ArrayAdapter<Request> {
    private List<Request> items;
    private int layoutResourceId;
    private Context context;

    public RequestDiterimaListAdaptor(Context context, int layoutResourceId, List<Request> items){
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        NewRequestHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId,parent,false);
        holder = new NewRequestHolder();
        holder.item = items.get(position);
        holder.nama_customer = (TextView)row.findViewById(R.id.nama_pelanggan);
        holder.jenis_servis = (TextView)row.findViewById(R.id.jenis_service);
        holder.tanggal_servis = (TextView)row.findViewById(R.id.tanggal);
        holder.CustomerLocation = (ImageButton)row.findViewById(R.id.locationButton);
        holder.customerCall = (ImageButton)row.findViewById(R.id.callButton);
        holder.makeReport = (ImageButton)row.findViewById(R.id.reportButton);
        holder.span = (LinearLayout)row.findViewById(R.id.span);
        setupItem(holder);
        return row;
    }

    private void setupItem(NewRequestHolder holder){
        holder.nama_customer.setText(String.valueOf(holder.item.getNamacustomer()));
        holder.jenis_servis.setText(holder.item.getNamajasa());
        holder.tanggal_servis.setText(holder.item.getTanggalrequest() + " at " + holder.item.getJam());
        holder.customerCall.setTag(holder.item);
        holder.CustomerLocation.setTag(holder.item);
        holder.makeReport.setTag(holder.item);
    }

    public static class NewRequestHolder{
        Request item;
        TextView nama_customer;
        TextView jenis_servis;
        TextView tanggal_servis;
        ImageButton makeReport, customerCall, CustomerLocation;
        LinearLayout span;
    }
}
