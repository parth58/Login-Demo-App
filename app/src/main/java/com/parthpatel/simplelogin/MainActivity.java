package com.parthpatel.simplelogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.parthpatel.simplelogin.R;
import com.parthpatel.simplelogin.Registration;
import com.parthpatel.simplelogin.helloMsg;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private EditText email;
    private EditText password;
    private TextView info;
    private TextView register1;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private int count=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login=(Button)findViewById(R.id.btnLogin);
        email=(EditText)findViewById(R.id.etEmail);
        password=(EditText)findViewById(R.id.etPassword);
        info=(TextView) findViewById(R.id.tvInfo);
        register1=(TextView)findViewById(R.id.tvRegister);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        progressDialog=new ProgressDialog(this);

        if(user!=null){
            finish();
            startActivity(new Intent(MainActivity.this,helloMsg.class));
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName=email.getText().toString();
                String uPassword=password.getText().toString();
                if(uName.isEmpty() || uPassword.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter all the details",Toast.LENGTH_SHORT).show();
                }else{
                    validate(email.getText().toString(),password.getText().toString());
                }

            }
        });

        register1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Registration.class));
            }
        });

    }

    private void validate(String userName,String userPassword)
    {
        progressDialog.setMessage("Verifying...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){
                  progressDialog.dismiss();
                  Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                  startActivity(new Intent(MainActivity.this,helloMsg.class));
              }else{
                  Toast.makeText(MainActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                  --count;
                  progressDialog.dismiss();
                  info.setText("No of Attempts Remaining: "+count);
                  if(count==0){
                      login.setEnabled(false);
                  }
              }
            }
        });


    }
}
