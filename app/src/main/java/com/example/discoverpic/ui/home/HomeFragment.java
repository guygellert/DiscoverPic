package com.example.discoverpic.ui.home;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.discoverpic.R;
import com.example.discoverpic.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {
    private EditText firstName;
    private EditText lastName;
    private EditText address;
    private EditText password;
    private Button register;
    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View homeView = getView();

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        firstName = root.findViewById(R.id.firstName);
        lastName = root.findViewById(R.id.lastName);
        address = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        register = root.findViewById(R.id.register);
        Activity homeActivity = this.getActivity();
//        register.setOnClickListener( new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                checkData();
//                register(address,password);
//            }
//            void checkData(){
//                if(isEmpty(firstName)){
//                    Toast t = Toast.makeText(homeActivity,"You Must Enter First Name",Toast.LENGTH_SHORT);
//                    t.show();
//                }
//            }
//            boolean isEmpty(EditText text){
//                CharSequence str = text.getText().toString();
//                        return TextUtils.isEmpty(str);
//            }
//        });
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void  registerUi(View v){
        address = v.findViewById(R.id.email);
        password = v.findViewById(R.id.password);
        register(address,password);
    }
    public void register(EditText UserEmail, EditText UserPassword){
        Activity homeActivity = this.getActivity();
        String userEmail = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                    }

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
    }


}