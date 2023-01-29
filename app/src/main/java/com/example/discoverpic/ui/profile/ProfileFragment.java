package com.example.discoverpic.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.discoverpic.R;
import com.example.discoverpic.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {
    private FirebaseUser user;
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if(user != null) {
            String name = user.getDisplayName();
            TextView profileName = root.findViewById(R.id.profileName);
            ImageView profileImg =  root.findViewById(R.id.profileImg);
            profileName.setText(name);
            if(user.getPhotoUrl() != null) {
                Picasso.get().load(user.getPhotoUrl()).into(profileImg);
            }
        }
        return root;
    }
}