package com.example.discoverpic.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discoverpic.R;
import com.example.discoverpic.model.Post;
import com.example.discoverpic.ui.profile.ProfileFragmentDirections;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;


class PostViewHolder extends RecyclerView.ViewHolder{
    TextView nameTv;
    TextView locationTv;
    ImageView avatarImage;
    ImageView editBtn;
    List<Post> data;

    public PostViewHolder(@NonNull View itemView, List<Post> data) {
        super(itemView);
        this.data = data;
        nameTv = itemView.findViewById(R.id.postrow_name_tv);
        locationTv = itemView.findViewById(R.id.postrow_location_tv);
        avatarImage = itemView.findViewById(R.id.postrow_avatar_img);
        editBtn = itemView.findViewById(R.id.postrow_edit);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                Post post = data.get(pos);
                ProfileFragmentDirections.ActionNavigationProfileToNavigationAddpost action = ProfileFragmentDirections.actionNavigationProfileToNavigationAddpost();
                action.setPostId(post.getId());
                action.setCountry(post.getCountry());
                action.setCity(post.getCity());
                action.setDescription(post.getName());
                action.setImgUrl(post.getImgUrl());
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    public void bind(Post post) {
        nameTv.setText(post.name);
        locationTv.setText(post.city + ", " + post.country);

        if (!Objects.equals(post.getImgUrl(), "")) {
            Picasso.get().load(post.getImgUrl()).placeholder(R.drawable.tree).into(avatarImage);
        }else{
            avatarImage.setImageResource(R.drawable.tree);
        }
    }
}

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder>{
    LayoutInflater inflater;
    List<Post> data;
    public void setData(List<Post> data){
        this.data = data;
        notifyDataSetChanged();
    }
    public PostRecyclerAdapter(LayoutInflater inflater, List<Post> data){
        this.inflater = inflater;
        this.data = data;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.post_row,parent,false);
        return new PostViewHolder(view, data);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = data.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

}

