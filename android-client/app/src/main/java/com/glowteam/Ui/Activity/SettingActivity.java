package com.glowteam.Ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.LoginManager;
import com.glowteam.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;


public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView edit_profile, mLogout;
    private ImageView setting_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initListener();
    }

    private void initListener() {
        edit_profile.setOnClickListener(this);
        setting_back.setOnClickListener(this);
        mLogout.setOnClickListener(this);
    }

    private void initView() {
        edit_profile = findViewById(R.id.edit_profile);
        setting_back = findViewById(R.id.setting_back);
        mLogout = findViewById(R.id.logout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_profile:
                startActivity(new Intent(SettingActivity.this, EditProfileActivity.class));
                break;
            case R.id.setting_back:
                onBackPressed();
                break;
            case R.id.logout:
                if (!TextUtils.isEmpty(getMainUser().getGoogleId())) {
                    GoogleSignInOptions gso = new GoogleSignInOptions.
                            Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                            build();

                    GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
                    googleSignInClient.signOut().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            setMainUser("");
                            finish();
                            Intent intent = new Intent(getApplicationContext(), LoginActivityNew.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            showToast("Can't logout at the moment! Please try again later!");
                        }
                    }).addOnFailureListener(e -> showToast("Can't logout at the moment! Please try again later!"));
                } else if (!TextUtils.isEmpty(getMainUser().getFacebookId())) {
                    LoginManager.getInstance().logOut();
                    setMainUser("");
                    finish();
                    Intent intent = new Intent(getApplicationContext(), LoginActivityNew.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    setMainUser("");
                    finish();
                    Intent intent = new Intent(getApplicationContext(), LoginActivityNew.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
