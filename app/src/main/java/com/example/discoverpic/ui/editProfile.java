package com.example.discoverpic.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.discoverpic.R;
import com.example.discoverpic.databinding.FragmentEditProfileBinding;
import com.example.discoverpic.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class editProfile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentEditProfileBinding binding;
    private EditText userName;
    private ImageView imageView;
    private ActivityResultLauncher<Void> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;
    private Boolean isAvatarSelected = false;
    private UserProfileChangeRequest profileUpdates;
    private FirebaseUser user;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        editProfileArgs args = editProfileArgs.fromBundle(getArguments());
        View root = binding.getRoot();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userName = root.findViewById(R.id.DisplayName);
        imageView = root.findViewById(R.id.imageView);
        String userNameDesc = args.getUserName();
        Uri urlImage = args.getProfileUrl();
        userName.setText(userNameDesc, TextView.BufferType.EDITABLE);
        if(args.getProfileUrl() != null) {
            Picasso.get().load(urlImage).into(imageView);
        }
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    binding.imageView.setImageBitmap(result);
                    isAvatarSelected = true;
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null){
                    binding.imageView.setImageURI(result);
                    isAvatarSelected = true;
                }
            }
        });

        binding.cameraButton.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        binding.galleryButton.setOnClickListener(view1->{
            galleryLauncher.launch("image/*");
        });
        binding.saveProfile.setOnClickListener(view1 ->{
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(userName.getText().toString()).build();

            if(isAvatarSelected == true) {
                String id = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                binding.imageView.setDrawingCacheEnabled(true);
                binding.imageView.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) binding.imageView.getDrawable()).getBitmap();
                Model.instance().uploadImage(id, bitmap, url -> {
                    profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(userName.getText().toString())
                            .setPhotoUri(Uri.parse(url)).build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Navigation.findNavController(view1).popBackStack();
                            }
                        }
                    });
                });
            }else{

                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Navigation.findNavController(view1).popBackStack();
                        }
                    }
                });
            }
            isAvatarSelected = false;

        });
        return root;
    }
}