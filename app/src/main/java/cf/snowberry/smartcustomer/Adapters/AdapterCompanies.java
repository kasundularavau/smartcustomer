package cf.snowberry.smartcustomer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cf.snowberry.smartcustomer.CompanyActivity;
import cf.snowberry.smartcustomer.Models.ModelCompanies;
import cf.snowberry.smartcustomer.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterCompanies extends RecyclerView.Adapter<AdapterCompanies.MyHolder> {

    Context context;
    List<ModelCompanies> companiesList;


    public AdapterCompanies(Context context, List<ModelCompanies> companiesList) {
        this.context = context;
        this.companiesList = companiesList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_companies, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        final String companyID = companiesList.get(position).getCompanyID();
        String companyLogo = companiesList.get(position).getCompanyLogo();
        String companyName = companiesList.get(position).getCompanyName();
        String companyDescription = companiesList.get(position).getCompanyDescription();

        holder.companyName.setText(companyName);
        holder.companyDescription.setText(companyDescription);
        try{
            Picasso.get()
                    .load(companyLogo)
                    .resizeDimen(R.dimen.company_row_logo_width, R.dimen.company_row_logo_height)
                    .placeholder(R.drawable.company_place_holder)
                    .into(holder.companyLogo);
        }catch (Exception e){
            Picasso.get().load(R.drawable.company_place_holder).into(holder.companyLogo);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CompanyActivity.class);
                intent.putExtra("thisCompanyId",companyID);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return companiesList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView companyName, companyDescription;
        CircleImageView companyLogo;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            companyName = itemView.findViewById(R.id.companyName);
            companyDescription = itemView.findViewById(R.id.companyDescription);
            companyLogo = itemView.findViewById(R.id.companyLogo);
        }
    }
}
