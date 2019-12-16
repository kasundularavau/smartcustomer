package cf.snowberry.smartcustomer.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cf.snowberry.smartcustomer.Models.ModelChat;
import cf.snowberry.smartcustomer.R;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    Context context;
    List<ModelChat> chatList;

    FirebaseUser firebaseUser;

    public AdapterChat(Context context, List<ModelChat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
            return new MyHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String message = chatList.get(position).getMessage();
        String timeStamp = chatList.get(position).getTimeStamp();
        String image = chatList.get(position).getMessageImage();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String dateAndTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();

        holder.messageTv.setText(message);
        holder.timeTv.setText(dateAndTime);

        if (image.equals("noImage")){
            holder.messageImage.setVisibility(View.GONE);
        }else {
            holder.messageImage.setVisibility(View.VISIBLE);
            try{
                Picasso.get()
                        .load(image)
                        .resizeDimen(R.dimen.zero_dp,R.dimen.chat_image_height)
                        .placeholder(R.drawable.image_place_holder)
                        .into(holder.messageImage);
            }catch (Exception e){
                Picasso.get().load(R.drawable.image_place_holder).into(holder.messageImage);
            }
        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (chatList.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView messageTv, timeTv;
        ImageView messageImage;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            messageTv = itemView.findViewById(R.id.messageTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            messageImage = itemView.findViewById(R.id.messageImage);
        }
    }
}
