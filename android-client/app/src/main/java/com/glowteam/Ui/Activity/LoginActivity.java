package com.glowteam.Ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.glowteam.CustomViews.CustomEditText;
import com.glowteam.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static com.google.android.gms.auth.api.credentials.IdentityProviders.GOOGLE;

public class LoginActivity extends BaseActivity {


    Button mPhone, mFacebook, mGoogle;
    TextView mTermsOfService;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 100;

    LinearLayout mSignInView, mEmailView, mOtpView, mSignUpLayout, mSignInLayout;
    CustomEditText mNameEdt, mEmailTxt, mOtpTXt, mEmailTxt1, mPasswordTxt1;
    Button mLogin, mSubmit, mSignIn;
    String email;
    boolean isSignIn = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("AIzaSyDisoQsciVdirn1fVtQO_4HzbOv16gD-3E")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        isSignIn = getIntent().getBooleanExtra("signIn", false);

        mPhone = findViewById(R.id.con_phn);
        mFacebook = findViewById(R.id.con_fb);
        mGoogle = findViewById(R.id.con_google);
        mTermsOfService = findViewById(R.id.terms);

        mSignUpLayout = findViewById(R.id.signUpLayout);
        mSignInLayout = findViewById(R.id.signInLayout);

        if (isSignIn) {
            mSignInLayout.setVisibility(View.VISIBLE);
            mSignUpLayout.setVisibility(View.GONE);
        } else {
            mSignInLayout.setVisibility(View.GONE);
            mSignUpLayout.setVisibility(View.VISIBLE);
        }

        mEmailTxt1 = findViewById(R.id.emailEdt1);
        mPasswordTxt1 = findViewById(R.id.passwordEdt);
        mSignIn = findViewById(R.id.signIn);

        mSignInView = findViewById(R.id.signUpView);
        mEmailView = findViewById(R.id.emailView);
        mOtpView = findViewById(R.id.otpView);
        mNameEdt = findViewById(R.id.nameEdt);
        mEmailTxt = findViewById(R.id.emailEdt);
        mOtpTXt = findViewById(R.id.otpEdt);
        mLogin = findViewById(R.id.login);
        mSubmit = findViewById(R.id.submit);


        mPhone.setOnClickListener(v -> {
            /*Intent i = new Intent(this, MainActivity.class);
            startActivity(i);*/
            showView(2);
//            signUp();
        });
        mFacebook.setOnClickListener(v -> {
           /* Intent i = new Intent(this, MainActivity.class);
            startActivity(i);*/
//            signUpFb();
        });
        mGoogle.setOnClickListener(v -> {
//            connectGoogle();
            signIn();
//            Intent i = new Intent(this, WebViewGoogle.class);
//            startActivity(i);
        });

        mLogin.setOnClickListener(v -> {
            String name = mNameEdt.getText().toString().trim();
            email = mEmailTxt.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                showToast("Please enter Name!");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                showToast("Please enter Email address!");
                return;
            }
            if (!isValidEmail(email)) {
                showToast("Please enter valid Email address!");
                return;
            }
//            signUpEmail(email, name);

        });
        mSignIn.setOnClickListener(v -> {
            String email = mEmailTxt1.getText().toString().trim();
            String pass = mPasswordTxt1.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                showToast("Please enter Email address!");
                return;
            }
            if (!isValidEmail(email)) {
                showToast("Please enter valid Email address!");
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                showToast("Please enter Password!");
                return;
            }
            signInEmail(email, pass);
        });
        mSubmit.setOnClickListener(view -> {
            String otp = mOtpTXt.getText().toString().trim();
            if (TextUtils.isEmpty(otp)) {
                showToast("Please enter password!");
                return;
            }
            verifyOTP(email, otp);
        });
        if (!isGooglePlayServicesAvailable()) {
            showAlertDialog("", "Your device has not google play service hence google login might not work!");
        }

    }

    private void showView(int type) {
        mSignInView.setVisibility(View.GONE);
        mEmailView.setVisibility(View.GONE);
        mOtpView.setVisibility(View.GONE);
        if (type == 1) {
            mSignInView.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            mEmailView.setVisibility(View.VISIBLE);
        } else if (type == 3) {
            mOtpView.setVisibility(View.VISIBLE);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            showToast("No Error");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
//                signUpGoogle(account.getId());
            } else {
                showToast("No accounts found!");
            }
        } catch (ApiException e) {
            showToast(e.getMessage());
            Log.d("Data", "handleSignInResult: ====>" + e.getMessage());
        }
    }


    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        try {
            if (type == ApiCall.SIGN_UP_WITH_APPLE || type == ApiCall.SIGN_UP_WITH_FB || type == ApiCall.SIGN_UP_WITH_GOOGLE) {
//                showToast("success!");
                JSONObject object = o.getJSONObject("data");
                setMainUser(object.toString());
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
            if (type == ApiCall.SIGN_UP_WITH_EMAIL) {
                showView(3);
                showToast(o.getString("msg"));
            }
            if (type == ApiCall.VERIFY_OTP) {
                JSONObject object = o.getJSONObject("data");
                setMainUser(object.toString());
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
            if (type == ApiCall.SIGN_IN_WITH_EMAIL) {
                JSONObject object = o.getJSONObject("data");
                setMainUser(object.toString());
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (mSignInView.getVisibility() == View.VISIBLE) {
            finish();
        } else if (mEmailView.getVisibility() == View.VISIBLE) {
            showView(1);
        } else if (mOtpView.getVisibility() == View.VISIBLE) {
            showView(2);
        }
    }
}
