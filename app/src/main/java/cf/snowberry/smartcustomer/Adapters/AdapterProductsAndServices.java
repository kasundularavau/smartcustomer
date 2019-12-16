package cf.snowberry.smartcustomer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cf.snowberry.smartcustomer.Models.ModelProductsAndServices;
import cf.snowberry.smartcustomer.R;

public class AdapterProductsAndServices extends RecyclerView.Adapter<AdapterProductsAndServices.MyHolder> {

    Context context;
    List<ModelProductsAndServices> modelProductsAndServicesList;

    public AdapterProductsAndServices(Context context, List<ModelProductsAndServices> modelProductsAndServicesList) {
        this.context = context;
        this.modelProductsAndServicesList = modelProductsAndServicesList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_products_and_services,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String name = modelProductsAndServicesList.get(position).getProductName();
        String image = modelProductsAndServicesList.get(position).getProductImage();

        holder.productInRvTv.setText(name);

        try {
            Picasso.get()
                    .load(image)
                    .resizeDimen(R.dimen.product_rv_width, R.dimen.product_rv_height)
                    .placeholder(R.drawable.image_place_holder)
                    .into(holder.productInRvIv);
        }catch (Exception e){
            Picasso.get().load(R.drawable.image_place_holder).into(holder.productInRvIv);
        }

    }

    @Override
    public int getItemCount() {
        return modelProductsAndServicesList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView productInRvIv;
        TextView productInRvTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            productInRvIv = itemView.findViewById(R.id.productInRvIv);
            productInRvTv = itemView.findViewById(R.id.productInRvTv);

        }
    }

}
