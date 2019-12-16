package cf.snowberry.smartcustomer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cf.snowberry.smartcustomer.CompanyActivity;
import cf.snowberry.smartcustomer.Models.ModelPost;
import cf.snowberry.smartcustomer.PostDetailsActivity;
import cf.snowberry.smartcustomer.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyHolder> {

    Context context;
    List<ModelPost> postList;

    private DatabaseReference postsRef;

    String uid;
    boolean likeProcess = false;

    public AdapterPost(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;


        postsRef = FirebaseDatabase.getInstance().getReference("Posts");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_feed, parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {


        final String pId = postList.get(position).getpId();
        uid = postList.get(position).getUid();
        String pTitle = postList.get(position).getpTitle();
        String pDescription = postList.get(position).getpDescription();
        String pImage = postList.get(position).getpImage();
        String pTimeStamp = postList.get(position).getpTime();
        String uName = postList.get(position).getuName();
        String uDp = postList.get(position).getuDp();
        String pLikes = postList.get(position).getpLikes();
        String pCommentCount = postList.get(position).getpComments();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();


        String readMore = "<font color='#0000ff'>Read more...</font>";

        holder.pTitleTv.setText(pTitle);
        makeTextViewResizable(holder.pDescriptionTv, 5, readMore);
        holder.pDescriptionTv.setText(pDescription);
        holder.pTimeTv.setText(pTime);
        holder.cNameTv.setText(uName);
        holder.pLikesTv.setText(pLikes + " Likes");
        holder.pCommentCountTv.setText(pCommentCount + " Comments");

        setLikes(holder, pId);

        try {
            Picasso.get()
                    .load(pImage)
                    .placeholder(R.drawable.post_place_holder)
                    .into(holder.pImageIv);
        }catch (Exception e){
            Picasso.get().load(R.drawable.post_place_holder).into(holder.pImageIv);
        }

        try {
            Picasso.get()
                    .load(uDp)
                    .resizeDimen(R.dimen.company_logo_post_width, R.dimen.company_logo_post_height)
                    .centerCrop()
                    .placeholder(R.drawable.company_place_holder)
                    .into(holder.pLogoCi);
        }catch (Exception e){
            Picasso.get().load(R.drawable.company_place_holder).into(holder.pImageIv);
        }

        holder.pLikeIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                likeProcess = true;
                String postId = postList.get(position).getpId();
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
                ref.child("Likes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (likeProcess){
                            if (dataSnapshot.hasChild(uid)){

                                ref.child("Likes").child(uid).removeValue();
                                ref.child("Likes").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        ref.child("pLikes").setValue(String.valueOf(dataSnapshot.getChildrenCount()));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                likeProcess = false;
                            }else {

                                ref.child("Likes").child(uid).setValue("Liked");
                                ref.child("Likes").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        ref.child("pLikes").setValue(String.valueOf(dataSnapshot.getChildrenCount()));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                likeProcess = false;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        holder.pLogoCi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CompanyActivity.class);
                intent.putExtra("thisCompanyId",uid);
                context.startActivity(intent);

            }
        });

        holder.pCommentIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("thisPostId",pId);
                context.startActivity(intent);
            }
        });

        holder.pImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("thisPostId",pId);
                context.startActivity(intent);
            }
        });

        holder.pDescriptionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("thisPostId",pId);
                context.startActivity(intent);
            }
        });

    }

    private void makeTextViewResizable(final TextView textView, final int maxLine, final String expandText) {

        if (textView.getTag() == null){
            textView.setTag(textView.getText());
        }

        ViewTreeObserver vto = textView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = textView.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);

                  if (textView.getLineCount() >= maxLine){

                     int lineEndIndex = textView.getLayout().getLineEnd(maxLine - 1);
                     String text = textView.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                     textView.setText(Html.fromHtml(text));

                }
            }
        });
    }

    private void setLikes(final MyHolder holder, final String postKey) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postKey);
        ref.child("Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(uid)){
                    holder.pLikeIb.setImageResource(R.drawable.ic_liked_acc);
                }else{
                    holder.pLikeIb.setImageResource(R.drawable.ic_like_black);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView pImageIv;
        CircleImageView pLogoCi;
        TextView cNameTv, pTimeTv, pTitleTv, pDescriptionTv, pLikesTv, pCommentCountTv;
        ImageButton pCommentIb, pLikeIb;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            pImageIv = itemView.findViewById(R.id.pImageIv);
            pLogoCi = itemView.findViewById(R.id.pLogoCi);
            cNameTv = itemView.findViewById(R.id.cNameTv);
            pTimeTv = itemView.findViewById(R.id.pTimeTv);
            pTitleTv = itemView.findViewById(R.id.pTitleTv);
            pDescriptionTv = itemView.findViewById(R.id.pDescriptionTv);
            pLikeIb = itemView.findViewById(R.id.pLikeIb);
            pCommentIb = itemView.findViewById(R.id.pCommentIb);
            pLikesTv = itemView.findViewById(R.id.pLikesTv);
            pCommentCountTv = itemView.findViewById(R.id.pCommentsTv);

        }
    }
}
