package com.example.discoverpic.ui.addPost;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;

import com.example.discoverpic.databinding.FragmentAddpostBinding;
import com.example.discoverpic.model.Country;
import com.example.discoverpic.model.CountryModel;
import com.example.discoverpic.model.FirebaseModel;
import com.example.discoverpic.model.Model;
import com.example.discoverpic.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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

        if(args != null) {
            String postCountry = args.getCountry();
            String postName = args.getDescription();

            binding.nameEt.setText(postName);

            Log.d("TAG", "onCreateView: " + postCountry);
            Log.d("TAG", "onCreateView: " + countryAdapter.getPosition(postCountry));
            binding.countrySpinner.setSelection(countryAdapter.getPosition(postCountry));
        }

        data.observe(getViewLifecycleOwner(),list->{
            list.forEach(item->{
                countries.add(item.getCountry());
            });

            countryAdapter.notifyDataSetChanged();
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
            String name = binding.nameEt.getText().toString();
            String city = binding.citySpinner.getSelectedItem().toString();
            String country = binding.countrySpinner.getSelectedItem().toString();
            String id = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
            FirebaseUser currentUser = auth.getCurrentUser();
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