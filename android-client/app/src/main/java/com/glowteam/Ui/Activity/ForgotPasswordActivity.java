package com.glowteam.Ui.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.glowteam.CustomViews.CustomEditText;
import com.glowteam.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends BaseActivity {


    CustomEditText mEmail;
    Button mSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mEmail = findViewById(R.id.emailEdt);
        mSubmit = findViewById(R.id.verify);

        mSubmit.setOnClickListener(v->{
           String email = mEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                showToast("Please enter Email address!");
                return;
            }
            if (!isValidEmail(email)) {
                showToast("Please enter valid Email address!");
                return;
            }
            forgotPassword(email);
        });

    }


    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        if(type == ApiCall.FORGOT_PASSWORD){
            try {
                showToast(o.getString("msg"));
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
