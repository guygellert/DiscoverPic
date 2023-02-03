package com.example.discoverpic.ui.addPost;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;

import com.example.discoverpic.R;
import com.example.discoverpic.databinding.FragmentAddpostBinding;
import com.example.discoverpic.model.Country;
import com.example.discoverpic.model.CountryModel;
import com.example.discoverpic.model.Model;
import com.example.discoverpic.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AddPostFragment extends Fragment {

    private FragmentAddpostBinding binding;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    Boolean isAvatarSelected = false;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AddPostFragmentArgs args = AddPostFragmentArgs.fromBundle(getArguments());

        if(args.getCountry() != null) {
            ((AppCompatActivity) getContext()).getSupportActionBar().setTitle("Edit post");
        }

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    binding.avatarImg.setImageBitmap(result);
                    isAvatarSelected = true;
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null){
                    binding.avatarImg.setImageURI(result);
                    isAvatarSelected = true;
                }
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddpostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ArrayList<String> countries = new ArrayList<String>();
        ArrayList<String> cities = new ArrayList<String>();
        LiveData<List<Country>> data = CountryModel.instance.getCountriesAndCities();

        Spinner countrySpinner = (Spinner) binding.countrySpinner;
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);

        Spinner citySpinner = (Spinner) binding.citySpinner;
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        AddPostFragmentArgs args = AddPostFragmentArgs.fromBundle(getArguments());

        String postName = args.getDescription();
        if(postName != null) {
            binding.nameEt.setText(postName);
        }

        String imgUrl = args.getImgUrl();
        if(imgUrl != null) {
            if (!Objects.equals(imgUrl, "")) {
                Picasso.get().load(imgUrl).placeholder(R.drawable.tree).into(binding.avatarImg);
            }else{
                binding.avatarImg.setImageResource(R.drawable.tree);
            }
        }

        data.observe(getViewLifecycleOwner(),list->{
            list.forEach(item->{
                countries.add(item.getCountry());
            });

            String postCountry = args.getCountry();
            if(postCountry != null) {
                binding.countrySpinner.setSelection(countries.indexOf(postCountry));
            }

            countryAdapter.notifyDataSetChanged();
            binding.progressBarAddPost.setVisibility(View.GONE);
        });

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                data.getValue().forEach(item->{
                    if(item.getCountry().equals(adapterView.getItemAtPosition(i))){
                        cities.clear();
                        for (String city : item.getCities()) {
                            cities.add(city);
                        }

                        String postCity = args.getCity();
                        if(postCity != null) {
                            binding.citySpinner.setSelection(cities.indexOf(postCity));
                        }

                        cityAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("TAG", "onNothingSelected: ");
            }
        });

        binding.saveBtn.setOnClickListener(view1 -> {
            String id;
            String name = binding.nameEt.getText().toString();
            String city = binding.citySpinner.getSelectedItem().toString();
            String country = binding.countrySpinner.getSelectedItem().toString();
            FirebaseUser currentUser = auth.getCurrentUser();

            id = args.getPostId();
            if(id == null) {
                id = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
            }

            Post post = new Post(id,name,"",city, country, currentUser.getUid());

            if (isAvatarSelected){
                binding.avatarImg.setDrawingCacheEnabled(true);
                binding.avatarImg.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) binding.avatarImg.getDrawable()).getBitmap();
                Model.instance().uploadImage(id, bitmap, url->{
                    if (url != null){
                        post.setImgUrl(url);
                    }
                    Model.instance().addPost(post, (unused) -> {
                        Navigation.findNavController(view1).popBackStack();
                    });
                });
            }else {
                String postUrl = args.getImgUrl();
                if(postUrl != null) {
                    post.setImgUrl(postUrl);
                }

                Model.instance().addPost(post, (unused) -> {
                    Navigation.findNavController(view1).popBackStack();
                });
            }
        });

        binding.cancellBtn.setOnClickListener(view1 ->
                Navigation.findNavController(view1).popBackStack()
        );

        binding.cameraButton.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        binding.galleryButton.setOnClickListener(view1->{
            galleryLauncher.launch("image/*");
        });

        return root;
    }
}