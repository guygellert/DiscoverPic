package com.example.discoverpic.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.discoverpic.R;
import com.example.discoverpic.databinding.FragmentProfileBinding;
import com.example.discoverpic.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {
    private FirebaseUser user;
    private Button logoutButton;
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = FirebaseAuth.getInstance().getCurrentUser();
        logoutButton = root.findViewById(R.id.logout);
        if(user != null) {
            String name = user.getDisplayName();
            binding.profileName.setText(name);
            if(user.getPhotoUrl() != null) {
                Picasso.get().load(user.getPhotoUrl()).into(binding.profileImg);
            }
        }
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null){
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                }
            }
        });
        return root;
    }
}