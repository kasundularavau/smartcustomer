package cf.snowberry.smartcustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateAccActivity extends AppCompatActivity {

    TextView alreadyHaveAccTxt;
    Button createAccBtn;
    TextInputEditText nameEt, emailEt, phoneEt, passwordEt, confirmPasswordEt;
    ProgressDialog progressDialog;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create a new account");

        alreadyHaveAccTxt = findViewById(R.id.alreadyHaveAccTxt);
        createAccBtn = findViewById(R.id.createAccBtn);
        nameEt = findViewById(R.id.nameEt);
        emailEt = findViewById(R.id.emailEt);
        phoneEt = findViewById(R.id.phoneEt);
        passwordEt = findViewById(R.id.passwordEt);
        confirmPasswordEt = findViewById(R.id.confirmPasswordEt);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();


        alreadyHaveAccTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateAccActivity.this, LoginActivity.class));
                finish();
            }
        });

        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEt.getText().toString().trim();
                String email = emailEt.getText().toString().trim();
                String phone = phoneEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                String confirmPassword = confirmPasswordEt.getText().toString().trim();

                if (!TextUtils.isEmpty(name)){

                    if (!TextUtils.isEmpty(email)){

                        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                            if (!TextUtils.isEmpty(phone)){

                                if (Patterns.PHONE.matcher(phone).matches()){

                                    if (!TextUtils.isEmpty(password)){

                                        if (password.length()<6){

                                            passwordEt.setError("passwords must include 6 characters");
                                            passwordEt.setFocusable(true);

                                        }else {

                                            if (!TextUtils.isEmpty(confirmPassword)){

                                                if (password.equals(confirmPassword)){

                                                    registerUser(email, password);

                                                }else {
                                                    confirmPasswordEt.setError("passwords does not match!");
                                                    confirmPasswordEt.setFocusable(true);
                                                }

                                            }else {
                                                confirmPasswordEt.setError("please confirm your password");
                                                confirmPasswordEt.setFocusable(true);
                                            }

                                        }

                                    }else {
                                        passwordEt.setError("please enter your password");
                                        passwordEt.setFocusable(true);
                                    }

                                }else {
                                    phoneEt.setError("invalid phone number");
                                    phoneEt.setFocusable(true);
                                }

                            }else {
                                phoneEt.setError("please enter your phone number");
                                phoneEt.setFocusable(true);
                            }

                        }else {
                            emailEt.setError("invalid email address!");
                            emailEt.setFocusable(true);
                        }

                    }else{
                        emailEt.setError("please enter your email address");
                        emailEt.setFocusable(true);
                    }
                }else {
                    nameEt.setError("please enter your name");
                    nameEt.setFocusable(true);
                }
            }
        });
    }

    private void registerUser(String email, String password) {

        progressDialog.setMessage("Registering user...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            String name = nameEt.getText().toString().trim();
                            String email = emailEt.getText().toString().trim();
                            String phone = phoneEt.getText().toString().trim();
                            String uid = user.getUid();

                            HashMap<Object, String> hashMap = new HashMap<>();

                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", name);
                            hashMap.put("phone", phone);
                            hashMap.put("profile_pic", "");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap);

                            progressDialog.dismiss();
                            startActivity(new Intent(CreateAccActivity.this, DashBoardActivity.class));
                            finish();

                        } else {

                            progressDialog.dismiss();
                            Toast.makeText(CreateAccActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
