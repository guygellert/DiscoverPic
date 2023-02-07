package  com.example.discoverpic.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.discoverpic.MainActivity;
import com.example.discoverpic.R;
import com.example.discoverpic.databinding.ActivityLoginBinding;
import com.example.discoverpic.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.UUID;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {
enum Sign{ SIGNUP,REGISTER}
    private LoginViewModel loginViewModel;
    private FirebaseAuth mAuth;
    private Sign signMethod = Sign.SIGNUP;
    private UserProfileChangeRequest profileUpdates;
    private ActivityResultLauncher<Void> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;
    private ActivityLoginBinding binding;
    private Boolean isAvatarSelected = false;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
     binding = ActivityLoginBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final EditText DisplayName = binding.DisplayName;
//        final Switch registerSwitch = binding.RegisterSwitch;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        binding.switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(signMethod == Sign.SIGNUP) {
                    DisplayName.setVisibility(v.VISIBLE);
                    binding.imageView.setVisibility(v.VISIBLE);
                    binding.galleryButton.setVisibility(v.VISIBLE);
                    binding.cameraButton.setVisibility(v.VISIBLE);
                    binding.switchButton.setText("Sign In Existing Account");
                    signMethod = Sign.REGISTER;
                }
                else if( signMethod == Sign.REGISTER){
                    DisplayName.setVisibility(v.INVISIBLE);
                    binding.imageView.setVisibility(v.INVISIBLE);
                    binding.galleryButton.setVisibility(v.INVISIBLE);
                    binding.cameraButton.setVisibility(v.INVISIBLE);
                    binding.switchButton.setText("Create New Account");
                    signMethod = Sign.SIGNUP;
                }
            }
        });
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
//        profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(DisplayName.getText().toString()).build();



        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                if(signMethod == Sign.SIGNUP) {
                    mAuth.signInWithEmailAndPassword(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    DisplayName.setVisibility(v.VISIBLE);
                    binding.imageView.setVisibility(v.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        String id = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(DisplayName.getText().toString()).build();
                                        if (isAvatarSelected) {
                                            binding.imageView.setDrawingCacheEnabled(true);
                                            binding.imageView.buildDrawingCache();
                                            Bitmap bitmap = ((BitmapDrawable) binding.imageView.getDrawable()).getBitmap();
                                            Model.instance().uploadImage(id, bitmap, url->{
                                                profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(DisplayName.getText().toString())
                                                        .setPhotoUri(Uri.parse(url)).build();
                                                updateProfile(user,profileUpdates);
                                        });
                                        }
                                        else {
                                            updateProfile(user, profileUpdates);
                                        }

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
    private void updateProfile(FirebaseUser user,UserProfileChangeRequest profileUpdates){
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                    }
                });
    }
    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}