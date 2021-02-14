package com.studios.digiwallet.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.studios.digiwallet.Models.User;
import com.studios.digiwallet.R;

import java.util.HashMap;

import static com.studios.digiwallet.MyApplication.activeUser;
import static com.studios.digiwallet.MyApplication.firebase;

public class MyAccountActivity extends AppCompatActivity {

    EditText etFName, etLName, etEmail, etPhone, etBalance, etPass, etNewPass, etReNewPass;
    ImageView ivUpdate, ivPassLock;
    LinearLayout llNewPass, llReNewPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        init();
        listeners();
    }

    @SuppressLint("SetTextI18n")
    private void init(){
        etFName = findViewById(R.id.etFirstName_MyAccount);
        etFName.setText(activeUser.fName);
        etLName = findViewById(R.id.etLastName_MyAccount);
        etLName.setText(activeUser.lName);
        etEmail = findViewById(R.id.etEmail_MyAccount);
        etEmail.setText(activeUser.email);
        etPhone = findViewById(R.id.etPhone_MyAccount);
        etPhone.setText(activeUser.phone);
        etBalance = findViewById(R.id.etAmount_MyAccount);
        etBalance.setText(activeUser.balance+"");
        etPass = findViewById(R.id.etPassword_MyAccount);
        etPass.setText(activeUser.pass);
        etNewPass = findViewById(R.id.etNewPassword_MyAccount);
        etReNewPass = findViewById(R.id.etReNewPassword_MyAccount);
        ivUpdate = findViewById(R.id.imvUpdate_MyAccount);
        ivUpdate.setImageResource(R.drawable.ic_baseline_autorenew_24);
        ivUpdate.setTag(false);
        ivPassLock = findViewById(R.id.imvPassword_MyAccount);
        ivPassLock.setTag(true);
        llNewPass = findViewById(R.id.llNewPassword_MyAccount);
        llReNewPass = findViewById(R.id.llReNewPassword_MyAccount);
    }

    private void listeners() {
        ivPassLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((boolean)ivPassLock.getTag()){
                    etPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivPassLock.setImageResource(R.drawable.ic_baseline_lock_open_24);
                    ivPassLock.setTag(false);
                }
                else{
                    etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivPassLock.setImageResource(R.drawable.ic_baseline_lock_24);
                    ivPassLock.setTag(true);
                }
            }
        });

        ivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((boolean)ivUpdate.getTag()){
                    if(!isEmpty()){
                        if(etNewPass.getText().toString().equals(etReNewPass.getText().toString())){
                            update();
                            setResult(RESULT_OK);
                            finish();
                        }
                        else
                            Toast.makeText(MyAccountActivity.this, "Passwords don't match! Please Re-Enter", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(MyAccountActivity.this, "Input all fields", Toast.LENGTH_LONG).show();
                }
                else{
                    editable(true);
                }
            }
        });
    }

    private void update() {
        HashMap<String, String> users = new HashMap<>();
        users.put("FName", etFName.getText().toString());
        users.put("LName", etLName.getText().toString());
        users.put("Email", activeUser.email);
        users.put("Phone", activeUser.phone);
        users.put("CNIC", activeUser.cnic);
        if(!etNewPass.getText().toString().equals(""))
            users.put("Password", etNewPass.getText().toString());
        else
            users.put("Password", etPass.getText().toString());
        users.put("Balance", activeUser.balance+"");
        
        firebase.child(User.encodeUserEmail(activeUser.email)+"-"+activeUser.cnic).setValue(users)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MyAccountActivity.this, "Information Updated Successfully", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(MyAccountActivity.this, "Error Updating", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean isEmpty() {
        return etFName.getText().toString().equals("") || etLName.getText().toString().equals("") || (etNewPass.getText().toString().equals("") && !etReNewPass.getText().toString().equals("")) || (!etNewPass.getText().toString().equals("") && etReNewPass.getText().toString().equals(""));
    }

    private void editable(boolean x){
        etFName.setEnabled(x);
        etLName.setEnabled(x);
        if(x) {
            llNewPass.setVisibility(View.VISIBLE);
            llReNewPass.setVisibility(View.VISIBLE);
        }else{
            llNewPass.setVisibility(View.GONE);
            llReNewPass.setVisibility(View.GONE);
        }
        etNewPass.setText("");
        etReNewPass.setText("");
        ivUpdate.setTag(x);
    }

    @Override
    public void onBackPressed() {
        if((boolean)ivUpdate.getTag())
            editable(false);
        else
            super.onBackPressed();
    }
}