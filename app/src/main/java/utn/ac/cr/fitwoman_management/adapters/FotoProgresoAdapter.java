package utn.ac.cr.fitwoman_management.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import utn.ac.cr.fitwoman_management.Entities.FotoProgreso;
import utn.ac.cr.fitwoman_management.R;

public class FotoProgresoAdapter extends BaseAdapter {

    private Context context;
    private List<FotoProgreso> fotosList;
    private LayoutInflater inflater;

    public FotoProgresoAdapter(Context context, List<FotoProgreso> fotosList) {
        this.context = context;
        this.fotosList = fotosList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return fotosList.size();
    }

    @Override
    public Object getItem(int position) {
        return fotosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_foto_progreso, parent, false);
            holder = new ViewHolder();
            holder.ivFotoProgreso = convertView.findViewById(R.id.ivFotoProgreso);
            holder.tvMesFoto = convertView.findViewById(R.id.tvMesFoto);
            holder.tvPesoFoto = convertView.findViewById(R.id.tvPesoFoto);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FotoProgreso foto = fotosList.get(position);

        holder.tvMesFoto.setText(foto.getMes());
        holder.tvPesoFoto.setText(foto.getPeso() + " kg");

        if (foto.getRutaImagen() != null && !foto.getRutaImagen().isEmpty()) {
            try {
                byte[] decodedBytes = Base64.decode(foto.getRutaImagen(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                holder.ivFotoProgreso.setImageBitmap(bitmap);
            } catch (Exception e) {
                holder.ivFotoProgreso.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            holder.ivFotoProgreso.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView ivFotoProgreso;
        TextView tvMesFoto;
        TextView tvPesoFoto;
    }

    public void updateList(List<FotoProgreso> newList) {
        this.fotosList = newList;
        notifyDataSetChanged();
    }
}