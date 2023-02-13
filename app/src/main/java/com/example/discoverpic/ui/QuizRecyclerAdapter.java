package com.example.discoverpic.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discoverpic.R;
import com.example.discoverpic.model.Post;
import com.example.discoverpic.ui.home.HomeViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


class QuizViewHolder extends RecyclerView.ViewHolder{
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button hintButton;
    private Button newGameButton;
    private TextView hintText;
    private ImageView imageView;

    private HomeViewModel homeViewModel;

    private int correctButton;
    List<Post> data;

    public QuizViewHolder(@NonNull View itemView, List<Post> data) {
        super(itemView);
//        View homeView = getView();
//        LiveData<List<Post>> pLiveList = homeViewModel.getAllPosts();
//        List<Post> pList= pLiveList.getValue();
//        Post p = pList.get(0);
        button1 = itemView.findViewById(R.id.Button1);
        button2 = itemView.findViewById(R.id.Button2);
        button3 = itemView.findViewById(R.id.Button3);
        button4 = itemView.findViewById(R.id.Button4);
        hintButton = itemView.findViewById(R.id.HintButton);
        imageView = itemView.findViewById(R.id.qImage);
        hintText = itemView.findViewById(R.id.hintText);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(v);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(v);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(v);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(v);
            }
        });
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintText.setVisibility(View.VISIBLE);
            }
        });
//        PostRecyclerAdapter adapter = new PostRecyclerAdapter(getLayoutInflater(),homeViewModel.getAllPosts().getValue());
//        homeViewModel.getAllPosts().observe(getViewLifecycleOwner(),list->{
//            gameList = list;
//            newGame(data);
//        });
//        this.data = data;
//        nameTv = itemView.findViewById(R.id.postrow_name_tv);
//        locationTv = itemView.findViewById(R.id.postrow_location_tv);
//        avatarImage = itemView.findViewById(R.id.postrow_avatar_img);
//        editBtn = itemView.findViewById(R.id.postrow_edit);
//
//        editBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int pos = getAdapterPosition();
//                Post post = data.get(pos);
//                ProfileFragmentDirections.ActionNavigationProfileToNavigationAddpost action = ProfileFragmentDirections.actionNavigationProfileToNavigationAddpost();
//                action.setPostId(post.getId());
//                action.setCountry(post.getCountry());
//                action.setCity(post.getCity());
//                action.setDescription(post.getName());
//                action.setImgUrl(post.getImgUrl());
//                Navigation.findNavController(view).navigate(action);
//            }
//        });
    }

    public void bind(Post post, List<Post> list) {
        newGame(post,list);
//        Random rnd = new Random();
//        List<Post> usedPost = new ArrayList<Post>();
//        Post p = list.get(getRandomPostId(list,usedPost));
//        usedPost.add(p);
//
//        button1.setBackgroundColor(Color.rgb(98,0,238));
//        button1.setText(p.getCity() + ", " + p.getCountry());
//        p = list.get(getRandomPostId(list,usedPost));
//        usedPost.add(p);
//        button2.setText(p.getCity() + ", " + p.getCountry());
//        button2.setBackgroundColor(Color.rgb(98,0,238));
//        p = list.get(getRandomPostId(list,usedPost));
//        usedPost.add(p);
//        button3.setText(p.getCity() + ", " + p.getCountry());
//        button3.setBackgroundColor(Color.rgb(98,0,238));
//        p = list.get(getRandomPostId(list,usedPost));
//        usedPost.add(p);
//        button4.setText(p.getCity() + ", " + p.getCountry());
//        button4.setBackgroundColor(Color.rgb(98,0,238));
//        correctButton= rnd.nextInt(4);
//        p = usedPost.get(correctButton);
//        hintText.setText(post.getName());
//        hintText.setVisibility(View.INVISIBLE);
//        if(post.getImgUrl().length() > 0) {
//            Picasso.get().load(post.getImgUrl()).into(imageView);
//        }
    }

    public void checkAnswer(View view)
    {
        int idClicked = -1;
        Button b = view.findViewById(R.id.Button1);;
        switch (view.getId())
        {
            case R.id.Button1: {
                idClicked = 0;
                b = view.findViewById(R.id.Button1);
                break;
            }
            case R.id.Button2: {
                idClicked = 1;
                b = view.findViewById(R.id.Button2);
                break;
            }
            case R.id.Button3: {
                idClicked = 2;
                b = view.findViewById(R.id.Button3);
                break;
            }
            case R.id.Button4: {
                idClicked = 3;
                b = view.findViewById(R.id.Button4);
                break;
            }
        }
        b.setBackgroundColor(Color.RED);
//        if(correctButton == idClicked)
//        {
//            Toast.makeText(view.getContext(),"Correct!",Toast.LENGTH_LONG);
//        }
//        else{
//            Toast.makeText(view.getContext(),"Wrong!",Toast.LENGTH_LONG);
//        }
        colorCorrectButton(view);
    }

    private void colorCorrectButton(View v){
        Button b = v.findViewById(R.id.Button1);;
        switch (correctButton)
        {
            case 0 :
                button1.setBackgroundColor(Color.GREEN);
                break;
            case 1:
                button2.setBackgroundColor(Color.GREEN);
                break;
            case 2:
                button3.setBackgroundColor(Color.GREEN);
                break;
            case 3:
                button4.setBackgroundColor(Color.GREEN);
                break;
        }
    }


    private void changeCorrectButtonText(String text){
        switch (correctButton)
        {
            case 0 :
                button1.setText(text);
                break;
            case 1:
                button2.setText(text);
                break;
            case 2:
                button3.setText(text);
                break;
            case 3:
                button4.setText(text);
                break;
        }
    }

    private void newGame(Post post,List<Post> list)
    {
        Random rnd = new Random();
        List<Post> usedPost = new ArrayList<Post>();
        usedPost.add(post);
        Post p = list.get(getRandomPostId(list,usedPost));
        usedPost.add(p);

        button1.setBackgroundColor(Color.rgb(98,0,238));
        button1.setText(p.getCity() + ", " + p.getCountry());
        p = list.get(getRandomPostId(list,usedPost));
        usedPost.add(p);
        button2.setText(p.getCity() + ", " + p.getCountry());
        button2.setBackgroundColor(Color.rgb(98,0,238));
        p = list.get(getRandomPostId(list,usedPost));
        usedPost.add(p);
        button3.setText(p.getCity() + ", " + p.getCountry());
        button3.setBackgroundColor(Color.rgb(98,0,238));
        p = list.get(getRandomPostId(list,usedPost));
        usedPost.add(p);
        button4.setText(p.getCity() + ", " + p.getCountry());
        button4.setBackgroundColor(Color.rgb(98,0,238));
        correctButton= rnd.nextInt(4);
//        p = usedPost.get(correctButton);
        String correctText = post.getCity() + ", " + post.getCountry();
        changeCorrectButtonText(correctText);
        hintText.setText(post.getName());
        hintText.setVisibility(View.INVISIBLE);
        if(post.getImgUrl().length() > 0) {
            Picasso.get().load(post.getImgUrl()).into(imageView);
        }
        else{
            imageView.setImageResource(R.drawable.tree);
        }

    }

    public int getRandomPostId(List<Post> list,List<Post>usedPost)
    {
        Random rnd = new Random();
        int postId = rnd.nextInt(list.size());
        Post p = list.get(postId);
        while(p.getImgUrl().length() == 0 || comparePost(p,usedPost)){
            postId = rnd.nextInt(list.size());
            p = list.get(postId);
        }

        return postId;
    }
    public boolean comparePost(Post p, List<Post> usedPost)
    {
        for(int i = 0 ; i < usedPost.size(); i++)
        {
            if(p.getCountry().equals(usedPost.get(i).getCountry()) &&
                    p.getCity().equals(usedPost.get(i).getCity())){
                return true;
            }
        }
        return false;
    }

}

public class QuizRecyclerAdapter extends RecyclerView.Adapter<QuizViewHolder>{
    LayoutInflater inflater;
    List<Post> data;
    private static List<Post> usedQuestionPost = new ArrayList<Post>(); ;
    private List<Post> gameList;
    public void setData(List<Post> data){
        this.data = data;
        gameList = data;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        int indexPost;
        if(this.data != null) {
            for (indexPost = 0; indexPost < this.data.size(); ) {
                if (this.data.get(indexPost).getImgUrl().equals("") ||
                        this.data.get(indexPost).getUserId().equals(currentUser.getUid())) {
                    this.data.remove(indexPost);
                } else {
                    indexPost++;
                }
            }
        }
        notifyDataSetChanged();
    }
    public QuizRecyclerAdapter(LayoutInflater inflater, List<Post> data){
        this.inflater = inflater;
        this.data = data;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.quiz_row,parent,false);

        return new QuizViewHolder(view, data);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Random rnd = new Random();
        int postId;
        Post post;
        List<Post> tempList = new ArrayList<Post>();
        tempList.addAll(data);
        tempList.removeAll(usedQuestionPost);
        while(position > usedQuestionPost.size() - 1 )
        {
            postId = getRandomPostId(tempList,usedQuestionPost);
            post  = tempList.get(postId);
            while (comparePostId(post,usedQuestionPost)){
                postId = getRandomPostId(tempList,usedQuestionPost);
                post  = tempList.get(postId);
            }
            usedQuestionPost.add(post);
            tempList.remove(post);
        }
            post = usedQuestionPost.get(position);
            holder.bind(post, data);



    }
    public int getRandomPostId(List<Post> list,List<Post>usedPost)
    {
        Random rnd = new Random();
        int postId = rnd.nextInt(list.size());
        Post p = list.get(postId);
        while(p.getImgUrl().length() == 0 || comparePostId(p,usedPost)){
            postId = rnd.nextInt(list.size());
            p = list.get(postId);
        }

        return postId;
    }
    public boolean comparePostId(Post p, List<Post> usedPost)
    {
        for(int i = 0 ; i < usedPost.size(); i++)
        {
            if(p.getId().equals(usedPost.get(i).getId())){
                return true;
            }
        }
        return false;
    }
    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

}

