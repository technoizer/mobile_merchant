package id.ac.its.alpro.merchant.adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import id.ac.its.alpro.merchant.R;
import id.ac.its.alpro.merchant.component.NewRequest;

/**
 * Created by ALPRO on 31/12/2015.
 */
public class NewRequestListAdaptor extends ArrayAdapter<NewRequest> {
    private List<NewRequest> items;
    private int layoutResourceId;
    private Context context;
    private int mode;

    public NewRequestListAdaptor(Context context, int layoutResourceId, List<NewRequest> items, int mode){
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
        holder.nama_customer = (TextView)row.findViewById(R.id.nama_customer);
        holder.jenis_servis = (TextView)row.findViewById(R.id.jenis_servis);
        holder.catatan_servis = (TextView)row.findViewById(R.id.catatan_servis);
        holder.tanggal_servis = (TextView)row.findViewById(R.id.tanggal_servis);
        holder.lokasi_servis = (TextView)row.findViewById(R.id.lokasi_servis);
        holder.ambil = (Button)row.findViewById(R.id.ambil);
        holder.tolak = (Button)row.findViewById(R.id.tolak);
        holder.span = (LinearLayout)row.findViewById(R.id.span);
        setupItem(holder);
        return row;
    }

    private void setupItem(NewRequestHolder holder){
        holder.nama_customer.setText(String.valueOf(holder.item.getNamacustomer()));
        holder.jenis_servis.setText(holder.item.getNamajasa());
        holder.catatan_servis.setText(holder.item.getCatatancustomer());
        holder.tanggal_servis.setText(holder.item.getTanggalrequest());
        holder.lokasi_servis.setText("Lokasi : " + holder.item.getLokasi());
        if (mode == 1){
            holder.tolak.setVisibility(View.GONE);
            holder.item.setUrlAmbil("http://servisin.au-syd.mybluemix.net/api/provider/ambilbroadcast/" + holder.item.getId());
            holder.ambil.setTag(holder.item);
        }
        else{
            holder.span.setVisibility(View.GONE);
            holder.item.setUrlAmbil("http://servisin.au-syd.mybluemix.net/provider/ambildirect/" + holder.item.getId());
            holder.ambil.setTag(holder.item);
            holder.tolak.setTag(holder.item);
        }
    }

    public static class NewRequestHolder{
        NewRequest item;
        TextView nama_customer;
        TextView jenis_servis;
        TextView tanggal_servis;
        TextView catatan_servis;
        TextView lokasi_servis;
        Button tolak, ambil;
        LinearLayout span;
    }
}
