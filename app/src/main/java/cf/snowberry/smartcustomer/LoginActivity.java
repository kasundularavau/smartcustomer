package cf.snowberry.smartcustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextView forgotPasswordTxt, haveNotAccTxt;
    Button loginBtn;
    TextInputEditText emailEt, passwordEt;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login to your account");

        forgotPasswordTxt = findViewById(R.id.forgotPasswordTxt);
        haveNotAccTxt = findViewById(R.id.haveNotAccTxt);
        loginBtn = findViewById(R.id.loginBtn);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        haveNotAccTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CreateAccActivity.class));
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailEt.setError("invalid email address!");
                    emailEt.setFocusable(true);
                }else if (password.length()<6){
                    passwordEt.setError("password too short!");
                    passwordEt.setFocusable(true);
                }else{
                    loginUser(email, password);
                }

            }
        });

        forgotPasswordTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shwPasswordRecoverDialog();
            }
        });

    }

    private void shwPasswordRecoverDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover password");

        LinearLayout linearLayout = new LinearLayout(this);

        final EditText recoverEmailEt = new EditText(this);
        recoverEmailEt.setHint("enter your email...");
        recoverEmailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        linearLayout.addView(recoverEmailEt);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);



        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email = recoverEmailEt.getText().toString().trim();
                beginRecover(email);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    private void beginRecover(String email) {

        progressDialog.setMessage("Sending email...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "email sent", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "email sending failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password) {

        progressDialog.setMessage("Logging...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            progressDialog.dismiss();
                            startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                            finish();

                        } else {

                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
