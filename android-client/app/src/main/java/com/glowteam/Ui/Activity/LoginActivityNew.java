package com.glowteam.Ui.Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.glowteam.CustomViews.CustomEditText;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivityNew extends BaseActivity {


    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 100;

    LinearLayout mSignInView, mSignUpView, mSignInUpLayout, mMailView;
    CustomEditText mEmailTxt, mEmailTxt1, mPasswordTxt, mPasswordTxt1, mPasswordTxt2;
    CustomTextView mSignIn, mSignUp, mTermsOfService, mSignInUpLine, mSignInUp, mMailTxt, mForgotPassword;
    Button mOpenMail;
    String email;
    boolean isSignIn = true;
    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    LoginButton loginButton;
    ImageView mFacebook, mGoogle;

    //    C:\Users\DHAVAL\AppData\Local\Android\sdk\platform-tools\adb shell am start -W -a android.intent.action.VIEW -d "glow://password=123" com.glowteam
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("AIzaSyDisoQsciVdirn1fVtQO_4HzbOv16gD-3E")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        callbackManager = CallbackManager.Factory.create();
//        isSignIn = getIntent().getBooleanExtra("signIn", false);

//        Intent intent1 = getIntent();
//        if (intent1 != null) {
//            Uri data = intent1.getData();
//            if (data != null) {
//                if (data.getHost() != null) {
//                    String host = data.getAuthority();
//                    if (!TextUtils.isEmpty(host)) {
//                        String[] ss = host.split("&");
//                        if (ss.length == 2) {
//                            String email = ss[0].substring(ss[0].indexOf("=") + 1);
//                            String pass = ss[1].substring(ss[1].indexOf("=") + 1);
//                            if (TextUtils.isEmpty(email)) {
//                                showToast("Can't find email address!");
//                                return;
//                            }
//                            if (TextUtils.isEmpty(pass)) {
//                                showToast("Can't find password!");
//                                return;
//                            }
//                            verifyOTP(email, pass);
//                        }
//                    }
//
//                }
//            }
//        }

        mFacebook = findViewById(R.id.con_fb);
        mGoogle = findViewById(R.id.con_google);
        mTermsOfService = findViewById(R.id.terms);
        mForgotPassword = findViewById(R.id.forgot_password);

        loginButton = findViewById(R.id.login_button);
        loginButton.setPermissions(Arrays.asList(EMAIL));

        mSignInUpLayout = findViewById(R.id.signInUpLayout);
        mSignUpView = findViewById(R.id.signUpLay);
        mSignInView = findViewById(R.id.signInLay);
        mMailView = findViewById(R.id.mailView);

        mEmailTxt = findViewById(R.id.emailEdt);
        mPasswordTxt = findViewById(R.id.passwordEdt);
        mPasswordTxt1 = findViewById(R.id.passwordEdt1);
        mPasswordTxt2 = findViewById(R.id.passwordEdt2);
//        mNameEdt = findViewById(R.id.nameEdt);
        mEmailTxt1 = findViewById(R.id.emailEdt1);

        mMailTxt = findViewById(R.id.mailTxt);
        mOpenMail = findViewById(R.id.open_mail);

        mSignInUpLine = findViewById(R.id.signInUpLine);
        mSignInUp = findViewById(R.id.signInUp);

        mSignIn = findViewById(R.id.signInBtn);
        mSignUp = findViewById(R.id.signUpBtn);


        mFacebook.setOnClickListener(v -> {
           /* Intent i = new Intent(this, MainActivity.class);
            startActivity(i);*/
//            signUpFb();
            calculateHashKey("com.glowteam");
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            AccessToken accessToken = loginResult.getAccessToken();
                            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
                                try {
//                                    JSONObject obj = object.getJSONObject("picture").getJSONObject("data");
                                    String id, name;
                                    if (object.has("name")) {
                                        name = object.getString("name");
                                    } else {
                                        name = "";
                                    }
                                    if (object.has("id")) {
                                        id = object.getString("id");
                                    } else {
                                        id = accessToken.getUserId();
                                    }
                                    signUpFb(id, name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name");
                            request.setParameters(parameters);
                            request.executeAsync();
                        }

                        @Override
                        public void onCancel() {
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                            Log.e("error",exception.toString());
                        }
                    });
            loginButton.callOnClick();

        });
        mGoogle.setOnClickListener(v -> {
//            connectGoogle();
            signIn();
//            Intent i = new Intent(this, WebViewGoogle.class);
//            startActivity(i);
        });

        mForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });

        mSignUp.setOnClickListener(v -> {
//            String name = mNameEdt.getText().toString().trim();
            email = mEmailTxt1.getText().toString().trim();
            String password = mPasswordTxt1.getText().toString().trim();
            String confirmPassword = mPasswordTxt2.getText().toString().trim();
//            if (TextUtils.isEmpty(name)) {
//                showToast("Please enter Name!");
//                return;
//            }
            if (TextUtils.isEmpty(email)) {
                showToast("Please enter Email address!");
                return;
            }
            if (!isValidEmail(email)) {
                showToast("Please enter valid Email address!");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                showToast("Please enter password!");
                return;
            }
            if (password.length() < 6) {
                showToast("Please enter at least 6 character password!");
                return;
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                showToast("Please enter confirm password!");
                return;
            }
            if (!password.equalsIgnoreCase(confirmPassword)) {
                showToast("Your password and confirmation password do not match!");
                return;
            }
            signUpEmail(email, password);

        });
        mSignIn.setOnClickListener(v -> {
            String email = mEmailTxt.getText().toString().toLowerCase().trim();
            String pass = mPasswordTxt.getText().toString().trim();

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
        mSignInUp.setOnClickListener(v -> {
            if (isSignIn) {
                showView(2);
            } else {
                showView(1);
            }
        });
        mOpenMail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            startActivity(intent);
            startActivity(Intent.createChooser(intent, "Choose Your mail app"));
        });

//        mVerify.setOnClickListener(view -> {
//            String otp = mPasswordTxt1.getText().toString().trim();
//            if (TextUtils.isEmpty(otp)) {
//                showToast("Please enter password!");
//                return;
//            }
//            verifyOTP(email, otp);
//        });
        if (!isGooglePlayServicesAvailable()) {
            showAlertDialog("", "Your device has not google play service hence google login might not work!");
        }

    }
    private void calculateHashKey(String yourPackageName) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    yourPackageName,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    private void showView(int type) {
        mSignInView.setVisibility(View.GONE);
        mSignUpView.setVisibility(View.GONE);
        if (type == 1) {
            isSignIn = true;
            mSignInUpLayout.setVisibility(View.VISIBLE);
            mMailView.setVisibility(View.GONE);
            mSignInView.setVisibility(View.VISIBLE);
            mSignInUpLine.setText("Don't Have an account? ");
            mSignInUp.setText("Register");
        } else if (type == 2) {
            isSignIn = false;
            mSignInUpLayout.setVisibility(View.VISIBLE);
            mMailView.setVisibility(View.GONE);
            mSignUpView.setVisibility(View.VISIBLE);
            mSignInUpLine.setText("Have an account? ");
            mSignInUp.setText("Login");
        } else if (type == 3) {
            mSignInUpLayout.setVisibility(View.GONE);
            mMailView.setVisibility(View.VISIBLE);
            mMailTxt.setText(email);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_SIGN_IN) {
            showToast("No Error");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                signUpGoogle(account.getId(), account.getDisplayName());
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
        } else if (mSignUpView.getVisibility() == View.VISIBLE || mMailView.getVisibility() == View.VISIBLE) {
            showView(1);
        }
    }
}
