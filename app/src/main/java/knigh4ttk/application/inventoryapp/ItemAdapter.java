package knigh4ttk.application.inventoryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.Holder> {

    private List<Item> mItems = new ArrayList<>();
    private OnItemClick listener;
    private Context cont;

    public ItemAdapter(Context context) {
        this.cont = context;
    }

    public interface OnItemClick {
        void onItemClick(int position, Item item);
    }

    public void setItems(List<Item> items) {
        if (items != null) {
            this.mItems.clear();
            this.mItems.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new Holder(view);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView name;
        TextView price;
        TextView quantity;
        TextView supplier;
        ImageView image;

        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_TextView);
            price = itemView.findViewById(R.id.price_TextView);
            quantity = itemView.findViewById(R.id.quantity_TextView);
            supplier = itemView.findViewById(R.id.supplier_TextView);
            image = itemView.findViewById(R.id.image_ImageView);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getAdapterPosition(), mItems.get(position));
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Item currentItem = mItems.get(position);
        holder.name.setText(currentItem.getName());
        holder.price.setText(currentItem.getPrice().toString());
        holder.quantity.setText(currentItem.getQuantity().toString());
        holder.supplier.setText(currentItem.getSupplier());
        if (currentItem.getImageUri() != null) {
            Glide
                    .with(cont)
                    .load(currentItem.getImageUri())
                    .into(holder.image);
        } else {
            Glide
                    .with(cont)
                    .load(R.drawable.no_image)
                    .into(holder.image);
        }
    }

    public void setOnItemClickListener(OnItemClick listener) {
        this.listener = listener;
    }
}