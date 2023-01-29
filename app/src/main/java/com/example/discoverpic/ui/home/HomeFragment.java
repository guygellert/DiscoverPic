package com.example.discoverpic.ui.home;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.discoverpic.R;
import com.example.discoverpic.databinding.FragmentHomeBinding;
import com.example.discoverpic.model.Post;
import com.example.discoverpic.ui.PostRecyclerAdapter;
import com.example.discoverpic.ui.profile.ProfileViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button hintButton;
    private Button newGameButton;
    private TextView hintText;
    private ImageView imageView;
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private List<Post> gameList;
    private int correctButton;

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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View homeView = getView();
//        LiveData<List<Post>> pLiveList = homeViewModel.getAllPosts();
//        List<Post> pList= pLiveList.getValue();
//        Post p = pList.get(0);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        button1 = root.findViewById(R.id.Button1);
        button2 = root.findViewById(R.id.Button2);
        button3 = root.findViewById(R.id.Button3);
        button4 = root.findViewById(R.id.Button4);
        newGameButton = root.findViewById(R.id.newButton);
        hintButton = root.findViewById(R.id.HintButton);
        imageView = root.findViewById(R.id.qImage);
        hintText = root.findViewById(R.id.hintText);
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
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame(gameList);
            }
        });
//        PostRecyclerAdapter adapter = new PostRecyclerAdapter(getLayoutInflater(),homeViewModel.getAllPosts().getValue());
        homeViewModel.getAllPosts().observe(getViewLifecycleOwner(),list->{
            gameList = list;
            newGame(gameList);
//            Random rnd = new Random();
//            List<Post> usedPost = new ArrayList<Post>();
//            Post p = list.get(getRandomPostId(list,usedPost));
//            usedPost.add(p);
//            button1.setText(p.getCity() + ", " + p.getCountry());
//            p = list.get(getRandomPostId(list,usedPost));
//            usedPost.add(p);
//            button2.setText(p.getCity() + ", " + p.getCountry());
//            p = list.get(getRandomPostId(list,usedPost));
//            usedPost.add(p);
//            button3.setText(p.getCity() + ", " + p.getCountry());
//            p = list.get(getRandomPostId(list,usedPost));
//            usedPost.add(p);
//            button4.setText(p.getCity() + ", " + p.getCountry());
//            correctButton= rnd.nextInt(4);
//            p = usedPost.get(correctButton);
//            Picasso.get().load(p.getImgUrl()).into(imageView);

        });
//        if(p != null) {
//
//        }
        Activity homeActivity = this.getActivity();
        return root;
    }
    private void newGame(List<Post> list)
    {
        Random rnd = new Random();
        List<Post> usedPost = new ArrayList<Post>();
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
        p = usedPost.get(correctButton);
        hintText.setText(p.getName());
        hintText.setVisibility(View.INVISIBLE);
        Picasso.get().load(p.getImgUrl()).into(imageView);

    }

    private int getRandomPostId(List<Post> list,List<Post>usedPost)
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
    private boolean comparePost(Post p, List<Post> usedPost)
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

}