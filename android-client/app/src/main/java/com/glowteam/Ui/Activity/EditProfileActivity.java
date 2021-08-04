package com.glowteam.Ui.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.Adapters.HairColorAdapter;
import com.glowteam.Adapters.HaircareAdapter;
import com.glowteam.Adapters.SkincareAdapter;
import com.glowteam.Models.User;
import com.glowteam.R;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.glowteam.Constant.Constant.BASE_URL;
import static com.glowteam.Constant.Constant.EYE_COLOR;
import static com.glowteam.Constant.Constant.EYE_COLOR_NAME;
import static com.glowteam.Constant.Constant.HAIRCARE;
import static com.glowteam.Constant.Constant.HAIR_COLOR;
import static com.glowteam.Constant.Constant.HAIR_COLOR_NAME;
import static com.glowteam.Constant.Constant.HAIR_TEXTURE_NAME;
import static com.glowteam.Constant.Constant.HAIR_TYPE_NAME;
import static com.glowteam.Constant.Constant.SKINCARE;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView hair_color_recycler;
    private RecyclerView hair_texture_recycler;
    private RecyclerView hair_type_recycler;
    private RecyclerView eye_color_recycler;
    private RecyclerView skincare_recycler;
    private RecyclerView haircare_recycler;
    public static ArrayList<String> skincare = new ArrayList<>();
    public static ArrayList<String> haircare = new ArrayList<>();
    private TextView edit_back;
    private TextView edit_done;
    private CircularImageView user_profile;
    private String path;
    private EditText user_name;
    private EditText name;
    private EditText user_about;
    private EditText user_website;
    private EditText user_email;
    private EditText user_birthday;
    private User user;
    private SkincareAdapter skincareAdapter;
    private String token;
    private int read_storage;
    private HaircareAdapter haircareAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        user = getMainUser();
        initView();
        initListener();
        initSet();
    }

    private void initSet() {
        if (!TextUtils.isEmpty(user.getProfilePic())) {
            Glide.with(this).load(BASE_URL + user.getProfilePic()).placeholder(R.drawable.user)
                    .error(R.drawable.user).diskCacheStrategy(DiskCacheStrategy.ALL).into(user_profile);
        }
        if (!TextUtils.isEmpty(user.getUserName())) {
            user_name.setText(user.getUserName());
        }
        if (!TextUtils.isEmpty(user.getName())) {
            name.setText(user.getName());
        }
        if (!TextUtils.isEmpty(user.getAbout())) {
            user_about.setText(user.getAbout());
        }
        if (!TextUtils.isEmpty(user.getWebsite())) {
            user_website.setText(user.getWebsite());
        }
        if (!TextUtils.isEmpty(user.getEmail())) {
            user_email.setText(user.getEmail());
        }
        if (!TextUtils.isEmpty(user.getBirthDate())) {
            user_birthday.setText(user.getBirthDate());
        }
        try {
            if (user.getSkinCareInterests() != null && user.getSkinCareInterests().size() != 0) {
                JSONArray newJArray = new JSONArray(user.getSkinCareInterests().get(0));
                skincare.clear();
                for (int i = 0; i < newJArray.length(); i++) {
                    skincare.add(newJArray.get(i).toString());
                }
            }
            if (user.getHairCareInterests() != null && user.getHairCareInterests().size() != 0) {
                JSONArray newJArray1 = new JSONArray(user.getHairCareInterests().get(0));
                haircare.clear();
                for (int i = 0; i < newJArray1.length(); i++) {
                    haircare.add(newJArray1.get(i).toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initAdapter();
    }

    private void initAdapter() {
        HairColorAdapter hairColorAdapter = new HairColorAdapter(EditProfileActivity.this, HAIR_COLOR, HAIR_COLOR_NAME);
        hair_color_recycler.setLayoutManager(new GridLayoutManager(EditProfileActivity.this, 4, RecyclerView.VERTICAL, false));
        hair_color_recycler.setAdapter(hairColorAdapter);

        HairColorAdapter hairColorAdapter1 = new HairColorAdapter(EditProfileActivity.this, HAIR_COLOR, HAIR_TEXTURE_NAME);
        hair_texture_recycler.setLayoutManager(new GridLayoutManager(EditProfileActivity.this, 4, RecyclerView.VERTICAL, false));
        hair_texture_recycler.setAdapter(hairColorAdapter1);

        HairColorAdapter hairColorAdapter2 = new HairColorAdapter(EditProfileActivity.this, HAIR_COLOR, HAIR_TYPE_NAME);
        hair_type_recycler.setLayoutManager(new GridLayoutManager(EditProfileActivity.this, 4, RecyclerView.VERTICAL, false));
        hair_type_recycler.setAdapter(hairColorAdapter2);

        HairColorAdapter hairColorAdapter3 = new HairColorAdapter(EditProfileActivity.this, EYE_COLOR, EYE_COLOR_NAME);
        eye_color_recycler.setLayoutManager(new GridLayoutManager(EditProfileActivity.this, 4, RecyclerView.VERTICAL, false));
        eye_color_recycler.setAdapter(hairColorAdapter3);

        skincareAdapter = new SkincareAdapter(EditProfileActivity.this, SKINCARE);
        skincare_recycler.setLayoutManager(new GridLayoutManager(EditProfileActivity.this, 2, RecyclerView.VERTICAL, false));
        skincare_recycler.setAdapter(skincareAdapter);

        haircareAdapter = new HaircareAdapter(EditProfileActivity.this, HAIRCARE);
        haircare_recycler.setLayoutManager(new GridLayoutManager(EditProfileActivity.this, 2, RecyclerView.VERTICAL, false));
        haircare_recycler.setAdapter(haircareAdapter);
    }

    private void initListener() {
        edit_back.setOnClickListener(this);
        edit_done.setOnClickListener(this);
        user_profile.setOnClickListener(this);
    }

    private void initView() {
        hair_color_recycler = findViewById(R.id.hair_color_recycler);
        hair_texture_recycler = findViewById(R.id.hair_texture_recycler);
        hair_type_recycler = findViewById(R.id.hair_type_recycler);
        eye_color_recycler = findViewById(R.id.eye_color_recycler);
        skincare_recycler = findViewById(R.id.skincare_recycler);
        haircare_recycler = findViewById(R.id.haircare_recycler);
        edit_back = findViewById(R.id.edit_back);
        user_name = findViewById(R.id.user_name);
        name = findViewById(R.id.name);
        user_about = findViewById(R.id.user_about);
        user_website = findViewById(R.id.user_website);
        user_email = findViewById(R.id.user_email);
        user_birthday = findViewById(R.id.user_birthday);
        edit_done = findViewById(R.id.edit_done);
        user_profile = findViewById(R.id.user_profile);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        skincare.clear();
        haircare.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_back:
                onBackPressed();
                break;
            case R.id.edit_done:
                token = user.getToken();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", name.getText().toString());
                hashMap.put("userName", user_name.getText().toString());
                hashMap.put("email", user_email.getText().toString());
                hashMap.put("about", user_about.getText().toString());
                hashMap.put("website", user_website.getText().toString());
                hashMap.put("birthDate", user_birthday.getText().toString());
                hashMap.put("skinTone", "#ffffff");
                hashMap.put("skinType", "normal");
                hashMap.put("hairColor", "#ffffff");
                hashMap.put("hairTexture", "wavy");
                hashMap.put("hairType", "fine");
                hashMap.put("eyeColor", "#ffffff");
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < skincare.size(); i++) {
                    jsonArray.put(skincare.get(i));
                }
                hashMap.put("skinCareInterests", jsonArray.toString());
                JSONArray jsonArray1 = new JSONArray();
                for (int i = 0; i < haircare.size(); i++) {
                    jsonArray1.put(haircare.get(i));
                }
                hashMap.put("hairCareInterests", jsonArray1.toString());
                updateProfile(hashMap);
                break;
            case R.id.user_profile:
                read_storage = ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (read_storage == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 100);
                } else {
                    ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //TODO: action
            Uri imageUri = data.getData();
            path = getRealPathFromURI(imageUri);
            uploadImages(getRealPathFromURI(imageUri));
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 100);
                } else {
                    Toast.makeText(EditProfileActivity.this, "Please allow this permission...", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        super.OnResponse(o, type);
        if (type == ApiCall.UPDATE_PROFILE) {
            try {
                JSONObject object = o.getJSONObject("data");
                User users = new Gson().fromJson(object.toString(), User.class);
                users.setToken(token);
                String json = new Gson().toJson(users);
                setMainUser(json);
                /*user = getMainUser();
                user_name.setText(user.getUserName());
                name.setText(user.getName());
                user_about.setText(user.getAbout());
                user_website.setText(user.getWebsite());
                user_email.setText(user.getEmail());
                user_birthday.setText(user.getBirthDate());
                Log.d("Data", "onResponse: ====>" + users.getSkinCareInterests().get(0));
                JSONArray newJArray = new JSONArray(users.getSkinCareInterests().get(0));
                for (int i = 0; i < newJArray.length(); i++) {
                    Log.d("Data", "onResponse: ====> " + newJArray.get(i).toString());
                }*/
                /*skincare.add("Acne");
                skincare.add("Blackheads");
                haircare.add("Anti-Aging");
                haircare.add("Hold");
                haircare.add("Shine");
                skincareAdapter.notifyDataSetChanged();
                skincareAdapter1.notifyDataSetChanged();*/
                onBackPressed();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (type == ApiCall.UPLOAD_IMAGE) {
            try {
                JSONObject object = o.getJSONObject("data");
                String pic = object.getString("profilePic");
                user.setProfilePic(pic);
                Gson gson = new Gson();
                String json = gson.toJson(user);
                setMainUser(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onBackPressed();
        }
    }
}
