package cf.snowberry.smartcustomer.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cf.snowberry.smartcustomer.Models.ModelComment;
import cf.snowberry.smartcustomer.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.MyHolder> {

    Context context;
    List<ModelComment> commentList;

    String myUid, postId;

    public AdapterComment(Context context, List<ModelComment> commentList, String myUid, String postId) {
        this.context = context;
        this.commentList = commentList;
        this.myUid = myUid;
        this.postId = postId;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_comments, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String userProPic = commentList.get(position).getuProPic();
        String userName = commentList.get(position).getuName();
        String comment = commentList.get(position).getComment();
        String timeStamp = commentList.get(position).getTimeStamp();
        final String uid = commentList.get(position).getuId();
        final String commentId = commentList.get(position).getcId();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String commentedTime = DateFormat.format("dd/MM/yyyy", cal).toString();

        holder.userNameTv.setText(userName);
        holder.commentTv.setText(comment);
        holder.commentedTimeTv.setText(commentedTime);

        try {
            Picasso.get()
                    .load(userProPic)
                    .resizeDimen(R.dimen.pro_pic_in_comment_width,R.dimen.pro_pic_in_comment_height)
                    .placeholder(R.drawable.user_place_holder)
                    .into(holder.userProPicCv);
        }catch (Exception e){
            Picasso.get().load(R.drawable.user_place_holder).into(holder.userProPicCv);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myUid.equals(uid)){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    builder.setTitle("Delete comment");
                    builder.setMessage("are you sure?");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteComment(commentId);
                        }
                    });
                    builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            }
        });
    }

    private void deleteComment(String commentId) {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        ref.child("Comments").child(commentId).removeValue();

        ref.child("Comments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ref.child("pComments").setValue(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        CircleImageView userProPicCv;
        TextView userNameTv, commentTv, commentedTimeTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            userNameTv = itemView.findViewById(R.id.userNameTv);
            commentTv = itemView.findViewById(R.id.commentTv);
            commentedTimeTv = itemView.findViewById(R.id.commentedTimeTv);
            userProPicCv = itemView.findViewById(R.id.userProPicCv);
        }
    }

}
