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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import id.ac.its.alpro.merchant.R;
import id.ac.its.alpro.merchant.component.Request;

/**
 * Created by ALPRO on 24/02/2016.
 */
public class HistoryRequestListAdaptor extends ArrayAdapter<Request> {

    private List<Request> items;
    private int layoutResourceId;
    private Context context;
    private int mode;

    public HistoryRequestListAdaptor(Context context, int layoutResourceId, List<Request> items, int mode){
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
        this.mode = mode;
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
        holder.tanggal_selesai = (TextView)row.findViewById(R.id.tanggal_selesai);
        holder.harga_total = (TextView)row.findViewById(R.id.harga);
        holder.detail = (ImageButton)row.findViewById(R.id.detailButton);
        holder.call = (ImageButton)row.findViewById(R.id.callButton);
        holder.wrapper = (LinearLayout)row.findViewById(R.id.wrapper);
        setupItem(holder);
        return row;
    }

    private void setupItem(NewRequestHolder holder){
        holder.nama_customer.setText(holder.item.getNamacustomer());
        holder.jenis_servis.setText(holder.item.getTipejasa());
        holder.tanggal_servis.setText("Tanggal request : " + holder.item.getTanggalrequest());
        holder.tanggal_selesai.setText("Tanggal selesai : " + holder.item.getTanggalselesai());
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        String temp = formatter.format(holder.item.getHargatotal().longValue());

        holder.harga_total.setText("Rp. " + temp + ",-");

        if (mode == 1){
            holder.wrapper.setVisibility(View.GONE);
        }
        holder.call.setTag(holder.item);
    }

    public static class NewRequestHolder{
        Request item;
        TextView nama_customer;
        TextView jenis_servis;
        TextView tanggal_servis;
        TextView tanggal_selesai;
        TextView harga_total;
        ImageButton detail,call;
        LinearLayout wrapper;
    }
}
