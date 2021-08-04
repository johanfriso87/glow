package com.glowteam.Ui.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.glowteam.Adapters.FeaturedPlaylistsAdapter;
import com.glowteam.Adapters.ProductSearchAdapter;
import com.glowteam.Adapters.ProductSearchAdapter1;
import com.glowteam.Constant.Constant;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.CustomViews.TouchyWebView;
import com.glowteam.Models.FeaturedPlaylistItem;
import com.glowteam.Models.ProductAmazonDetail;
import com.glowteam.Models.ProductAmazonSearch;
import com.glowteam.Models.ProductSearchUrl;
import com.glowteam.R;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakeReview extends BaseBottomFragment {

    private View view;
    private BottomSheetDialog bottomSheetDialog;
    private ImageView scanner;
    private ImageView back;
    ArrayList<ProductAmazonSearch> productSearchUrls = new ArrayList<>();
    ArrayList<ProductSearchUrl> productSearchUrls1 = new ArrayList<>();
    private RecyclerView product_search_recycler, product_search_recycler1;
    private EditText search, mSearchUrl;
    private ImageView clear;
    ProductSearchAdapter productSearchAdapter;
    ProductSearchAdapter1 productSearchAdapter1;

    RecyclerView mPlayListsView;
    FeaturedPlaylistsAdapter featuredPlaylistsAdapter;
    ArrayList<FeaturedPlaylistItem> featuredPlaylistItems = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;


    RelativeLayout mSearchLayout, mBrowserView, mEnterUrlView;
    CustomTextView mTitleTxt;
    LinearLayout mTempView, mPasteUrl, mSearchProductView, mBrowserWebView,show_my_product;

    TouchyWebView mWebView;
    Button mMyProdBtn;
    ImageView mBrowserBack, mClose, mUrlDone;
    CustomTextView mWebTitle;

    String mMainUrl;


    public MakeReview() {
        // Required empty public constructor
    }

    public static MakeReview newInstance(String frag_name) {
        MakeReview f = new MakeReview();
        Bundle args = new Bundle();
        args.putString("frag", frag_name);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_make_review, container, false);
        initView();
        initListener();

        mSearchLayout = view.findViewById(R.id.search_lay);
        mTitleTxt = view.findViewById(R.id.titleTxt);
        mTempView = view.findViewById(R.id.temp_view);
        mPasteUrl = view.findViewById(R.id.paste_url);

        mBrowserView = view.findViewById(R.id.browserView);
        mSearchProductView = view.findViewById(R.id.search_prod_view);
        mSearchUrl = view.findViewById(R.id.search_u);
        mEnterUrlView = view.findViewById(R.id.enter_view);
        mBrowserWebView = view.findViewById(R.id.b_view);

        mWebView = view.findViewById(R.id.webView);
        mMyProdBtn = view.findViewById(R.id.my_prod_btn);
        show_my_product = view.findViewById(R.id.show_my_product);

        mBrowserBack = view.findViewById(R.id.browser_back);
        mWebTitle = view.findViewById(R.id.web_title);
        mClose = view.findViewById(R.id.close);
        mUrlDone = view.findViewById(R.id.u_done);


        mPlayListsView = view.findViewById(R.id.temp_recycler);
        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        mPlayListsView.setLayoutManager(linearLayoutManager);
        setItems();
        featuredPlaylistsAdapter = new FeaturedPlaylistsAdapter(getActivity(), featuredPlaylistItems);
        mPlayListsView.setAdapter(featuredPlaylistsAdapter);


        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSearchProductView.getVisibility() == View.VISIBLE) {
                    bottomSheetDialog.dismiss();
                } else if (mEnterUrlView.getVisibility() == View.VISIBLE) {
                    openSearchView();
                }
            }
        });
        mUrlDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = mSearchUrl.getText().toString().trim();
                searchBrowser(temp);
            }
        });
        mMyProdBtn.setOnClickListener(view -> {
            Log.e("dekhle bhai","hoja to achha he");
            openMainSearch();
            productDetailAmazon(mMainUrl);
        });
        show_my_product.setOnClickListener(
                view ->{
                    openMainSearch();

                    getUsersMyProduct();
                }
        );
        mBrowserBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearchView();
            }
        });
        mPasteUrl.setOnClickListener(view -> {
            openUrlView();
        });
        mSearchUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = mSearchUrl.getText().toString().trim();
                if (!TextUtils.isEmpty(temp)) {
                    if (isValidURL(temp)) {
                        mUrlDone.setVisibility(View.VISIBLE);
                    } else {
                        mUrlDone.setVisibility(View.GONE);
                    }
                } else {
                    mUrlDone.setVisibility(View.GONE);
                }
            }
        });
        mSearchUrl.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String temp = mSearchUrl.getText().toString().trim();
                if (TextUtils.isEmpty(temp)) {
                    return false;
                }
                if (isValidURL(temp)) {
                    searchBrowser(temp);
                } else {
                    showToast("Please enter valid URL!");
                }
                return true;
            }
            return false;
        });

        return view;
    }


    private ProductSearchAdapter.OnClickSticker onClickSticker = position -> {
        if (isPermissionGranted()) {
            String stickerUrl = productSearchUrls.get(position).getThumbnail();
            getProductSticker(stickerUrl);
        }
    };

    private ProductSearchAdapter1.OnClickSticker onClickSticker1 = position -> {
        if (isPermissionGranted()) {
            String stickerUrl = productSearchUrls1.get(position).getImages().get(0);
            getProductSticker(stickerUrl);
        }
    };

    private boolean isPermissionGranted() {
        int write_storage = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read_storage = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (read_storage == PERMISSION_GRANTED && write_storage == PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
        }
        return false;
    }

    private String getNameFromURL() {
        URL url;
        try {
            url = new URL(mMainUrl);
            String host = url.getHost();
            String[] res = host.split("(\\.|//)+(?=\\w)");
            return res[1].substring(0, 1).toUpperCase() + res[1].substring(1).toLowerCase();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isValidURL(String urlString) {
        return Patterns.WEB_URL.matcher(urlString.toLowerCase()).matches();
    }

    public void hideKeyboardFrom(View view) {
        if (getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void setItems() {
        for (int i = 0; i < 10; i++) {
            FeaturedPlaylistItem item1 = new FeaturedPlaylistItem();
            featuredPlaylistItems.add(item1);
        }
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return dialog;
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        if (layoutParams != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            layoutParams.height = (int) (height * 0.8);
            behavior.setPeekHeight((int) (height * 0.2));
            behavior.setHideable(false);
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initView() {
        scanner = view.findViewById(R.id.scanner);
        back = view.findViewById(R.id.back);
        clear = view.findViewById(R.id.clear);
        search = view.findViewById(R.id.search);
        product_search_recycler = view.findViewById(R.id.product_search_recycler);
        product_search_recycler1 = view.findViewById(R.id.product_search_recycler1);


        productSearchAdapter = new ProductSearchAdapter(getActivity(), productSearchUrls);
        product_search_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false));
        productSearchAdapter.setOnClickSticker(onClickSticker);
        product_search_recycler.setAdapter(productSearchAdapter);

        productSearchAdapter1 = new ProductSearchAdapter1(getActivity(), productSearchUrls1);
        product_search_recycler1.setLayoutManager(new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false));
        productSearchAdapter1.setOnClickSticker(onClickSticker1);
        product_search_recycler1.setAdapter(productSearchAdapter1);
    }



    private void searchBrowser(String temp) {
        hideKeyboardFrom(mSearchUrl);
        openBrowserView();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mMainUrl = temp;
        String name = getNameFromURL();
        if (!TextUtils.isEmpty(name)) {
            mWebTitle.setText(name);
        }
        showProgressDialog();
        mWebView.loadUrl(temp);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mMainUrl = String.valueOf(request.getUrl());
                }
                String name = getNameFromURL();
                if (!TextUtils.isEmpty(name)) {
                    mWebTitle.setText(name);
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mMainUrl = url;
                String name = getNameFromURL();
                if (!TextUtils.isEmpty(name)) {
                    mWebTitle.setText(name);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWebView.evaluateJavascript(
                        "(function() { var data={}; data['title'] = document.querySelector(`meta[property='og:title']`).getAttribute('content');data['image'] = document.querySelector(`meta[property='og:image']`).getAttribute('content');  return JSON.stringify(data); })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {

                                try {
                                    JsonParser parser = new JsonParser();
                                    String retVal = parser.parse(s).getAsString();
                                            JSONObject jsonObject = new JSONObject(retVal);
                                    Log.d("title",jsonObject.getString("title"));
                                    Log.d("image",jsonObject.getString("image"));
                                    webViewProductName = jsonObject.getString("title");
                                    webViewProductImage = jsonObject.getString("image");
                                    webViewProductUrl = temp;
                                }catch (Exception err){
                                    Log.d("Error", err.toString());
                                }
                            }





                        });
                dismissProgressDialog();
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());


    }

    String webViewProductName = "";
    String webViewProductUrl="";
    String webViewProductImage="";

    private void openSearchView() {
        mSearchProductView.setVisibility(View.VISIBLE);
        mBrowserView.setVisibility(View.GONE);
        mTitleTxt.setVisibility(View.VISIBLE);
        mTempView.setVisibility(View.VISIBLE);
        mClose.setVisibility(View.VISIBLE);
        back.setVisibility(View.GONE);
        mPasteUrl.setVisibility(View.VISIBLE);
        productSearchUrls.clear();
        productSearchAdapter.notifyDataSetChanged();
        product_search_recycler.setVisibility(View.GONE);
        product_search_recycler1.setVisibility(View.GONE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mSearchLayout.getLayoutParams();
        layoutParams.height = (int) getResources().getDimension(R.dimen._200sdp);
        search.setText("");
    }


    private void openMainSearch() {
        mSearchProductView.setVisibility(View.VISIBLE);
        mBrowserView.setVisibility(View.GONE);
        mTitleTxt.setVisibility(View.GONE);
        mTempView.setVisibility(View.GONE);
        mClose.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        mPasteUrl.setVisibility(View.GONE);
//        product_search_recycler.setVisibility(View.VISIBLE);
//        product_search_recycler1.setVisibility(View.GONE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mSearchLayout.getLayoutParams();
        layoutParams.height = (int) getResources().getDimension(R.dimen._80sdp);
    }

    private void openBrowserView() {
        mBrowserView.setVisibility(View.VISIBLE);
        mSearchProductView.setVisibility(View.GONE);
        mBrowserWebView.setVisibility(View.VISIBLE);
        mEnterUrlView.setVisibility(View.GONE);
        mClose.setVisibility(View.GONE);
    }

    private void openUrlView() {
        mBrowserView.setVisibility(View.VISIBLE);
        mSearchProductView.setVisibility(View.GONE);
        mBrowserWebView.setVisibility(View.GONE);
        mEnterUrlView.setVisibility(View.VISIBLE);
        mClose.setVisibility(View.VISIBLE);
        mSearchUrl.setText("");
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bottomSheetDialog.dismiss();
                openSearchView();
            }
        });
        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), QrCodeActivity.class);
//                startActivityForResult(i, 101);

                final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                        .withActivity(getActivity())
                        .withEnableAutoFocus(true)
                        .withBleepEnabled(false)
                        .withBackfacingCamera()
                        .withText("Scanning...")
                        .withResultListener(barcode -> {
                            Log.d("result", barcode.rawValue);
                            openMainSearch();
                            search.setText(barcode.rawValue);
                            barcodeToAsin(barcode.rawValue);
//                            productSearchAmazon(barcode.rawValue);
                        })
                        .build();
                materialBarcodeScanner.startScan();

            }
        });
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String temp = search.getText().toString().trim();
                    if (TextUtils.isEmpty(temp)) {
                        return false;
                    }
                    openMainSearch();
                    if (URLUtil.isValidUrl(temp)) {
                        productDetailAmazon(temp);
                    } else {
                        productSearchAmazon(temp);
                    }
                    return true;
                }
                return false;
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("tester", "tester");
        if (resultCode != Activity.RESULT_OK) {
            Log.d("Data", "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;
        }
        if (requestCode == 101) {
            Log.d("blah", "blah");
            if (data == null)
                return;
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d("result", result);
            search.setText(result);
            openMainSearch();
            productSearchAmazon(result);
//            productSearchUrl("https://www.amazon.in/SE2219HX-Ultra-Backlit-Computer-Monitor/dp/B07PBHSW26/ref=sr_1_1?dchild=1&keywords=dell+ips+display&qid=1595593590&sr=8-1");
        }
    }

    public void addToMyproducts(ProductSearchUrl search){
        Log.e("ab 11","11 ab");
        addToMyproduct(search.getAsin(),search.getTitle(),search.getUrl(),search.getImages().get(0),search.getBrand());
    }

    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        if (type == ApiCall.PRODUCT_SEARCH_URL) {
            try {
                productSearchUrls1.clear();
                productSearchUrls.clear();
                productSearchAdapter.notifyDataSetChanged();
                product_search_recycler.setVisibility(View.GONE);
                product_search_recycler1.setVisibility(View.VISIBLE);
                JSONObject object = o.getJSONObject("data");
                Log.e("data object",String.valueOf(object));
                ProductSearchUrl search =new ProductSearchUrl();
                search.setAsin("");
                search.setBrand("");
                search.setTitle(object.getString("name"));
                search.setUrl(object.getString("shopUrl"));

                JSONArray arr = object.getJSONArray("image");

                ArrayList<String> list = new ArrayList<String>();
                for (int i=0; i<arr.length(); i++) {
                    list.add( arr.getString(i) );
                    Log.e("list",arr.getString(i));

                }
                search.setImages(list);
                this.addToMyproducts(search);
                productSearchUrls1.add(search);

            } catch (JSONException e) {
                if(webViewProductName != null && webViewProductName.trim() !="" && webViewProductImage != null && webViewProductImage.trim() !="" && webViewProductUrl != null && webViewProductUrl.trim() != ""){
                    productSearchUrls1.clear();
                    productSearchUrls.clear();
                    productSearchAdapter.notifyDataSetChanged();
                    product_search_recycler.setVisibility(View.GONE);
                    product_search_recycler1.setVisibility(View.VISIBLE);


                    ProductSearchUrl search =new ProductSearchUrl();
                    search.setAsin("");
                    search.setBrand("");
                    search.setTitle(webViewProductName);
                    search.setUrl(webViewProductUrl);


                    ArrayList<String> list = new ArrayList<String>();
                    list.add( webViewProductImage );

                    search.setImages(list);
                    productSearchUrls1.add(search);
                    productSearchAdapter1.notifyDataSetChanged();
                    productSearchAdapter1.setListener();
                    webViewProductName = "";
                    webViewProductUrl = "";
                    webViewProductImage = "";
                    addToMyproducts(search);
                }else{
                    e.printStackTrace();
                    Log.e("api error","api error");
                    showToast("Product not found.");
                }
            }
        }else if(type == ApiCall.ADD_TO_MY_PRODUCT){
            Log.e("is this okay",o.toString());
        } else if(type == ApiCall.GET_USER_MY_PRODUCTS){
            Log.e("aaja beta",o.toString());

            try {
                productSearchUrls1.clear();
                productSearchUrls.clear();
                productSearchAdapter1.notifyDataSetChanged();
                product_search_recycler.setVisibility(View.VISIBLE);
                product_search_recycler1.setVisibility(View.GONE);
                Log.e("aaja beta b",o.getString("status"));
                JSONArray dataArr = o.getJSONArray("data");

                for (int i=0; i<dataArr.length(); i++) {
                    ProductAmazonSearch search =new ProductAmazonSearch();
                    JSONObject object  =  dataArr.getJSONObject(i);
                    search.setAsin(object.getString("asin"));
                    search.setTitle(object.getString("title"));
                    search.setUrl(object.getString("url"));
                    JSONArray arr = object.getJSONArray("images");

                    ArrayList<String> list = new ArrayList<String>();
                    for (int j=0; j<arr.length(); j++) {
                        list.add( arr.getString(j) );
                        Log.e("list",arr.getString(j));

                    }
                    search.setThumbnail(list.get(0));
                    productSearchUrls.add(search);
                }
                productSearchAdapter1.notifyDataSetChanged();
                productSearchAdapter1.setListener();
            } catch (JSONException e) {
                e.printStackTrace();
                showToast("Something happened wrong try again after sometime.");
            }
        } else if (type == ApiCall.PRODUCT_SEARCH) {
            /*try {
                productSearchUrls.clear();
                JSONArray object = o.getJSONArray("data");
                for (int i = 0; i < object.length(); i++) {
                    ProductSearchUrl search = new Gson().fromJson(object.get(i).toString(), ProductSearchUrl.class);
                    productSearchUrls.add(search);
                }
                productSearchAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        } else if (type == ApiCall.PRODUCT_SEARCH_AMAZON) {
            try {
                productSearchUrls1.clear();
                productSearchUrls.clear();
                productSearchAdapter1.notifyDataSetChanged();
                product_search_recycler.setVisibility(View.VISIBLE);
                product_search_recycler1.setVisibility(View.GONE);
                JSONArray object = o.getJSONArray("products");
                for (int i = 0; i < object.length(); i++) {
                    ProductAmazonSearch search = new Gson().fromJson(object.get(i).toString(), ProductAmazonSearch.class);
                    productSearchUrls.add(search);
                }
                productSearchAdapter.notifyDataSetChanged();
                productSearchAdapter.setListener();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (type == ApiCall.PRODUCT_DETAIL_AMAZON) {
            try {
                productSearchUrls1.clear();
                productSearchUrls.clear();
                productSearchAdapter.notifyDataSetChanged();
                product_search_recycler.setVisibility(View.GONE);
                product_search_recycler1.setVisibility(View.VISIBLE);
                JSONObject object = o.getJSONObject("product");
                Gson gson = new Gson();
                ProductSearchUrl search = gson.fromJson(object.toString(), ProductSearchUrl.class);
                search.setAsin(getASIN(mMainUrl));
                productSearchUrls1.add(search);
                productSearchAdapter1.notifyDataSetChanged();
                productSearchAdapter1.setListener();
//                productSearchUrls1.clear();
//                productSearchUrls.clear();
//                productSearchAdapter1.notifyDataSetChanged();
//                product_search_recycler.setVisibility(View.VISIBLE);
//                product_search_recycler1.setVisibility(View.GONE);
//                JSONObject object = o.getJSONObject("product");
//                ProductAmazonSearch search = new Gson().fromJson(object.toString(), ProductAmazonSearch.class);
//                productSearchUrls.add(search);
//                productSearchAdapter.notifyDataSetChanged();
//                productSearchAdapter.setListener();
                this.addToMyproducts(search);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (type == ApiCall.GET_PRODUCT_STICKER) {
            try {
                String productSticker = o.getString("data");
                downloadVideoOrImage(Constant.BASE_URL + productSticker);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (type == ApiCall.UPC_TO_ASIN) {
            Log.d("productSticker", "productSticker 614");
            try {
                if (o.has("asin")) {
                    String asin = o.getString("asin");
//                productDetailFromAsinAmazon(asin);
                    productDetailFromAsinAmazon(asin);
                }else{
                    barcodeProductLookUp(search.getText().toString());
//                    showAlertDialog("","Don't know that one yet, try again");
                }
            } catch (Exception e) {
                e.printStackTrace();
                productSearchAmazon(search.getText().toString());
            }
        }
        if (type == ApiCall.PRODUCT_SEARCH_AMAZON_ASIN) {
            Log.d("PRODUCT_SEARCH_AMAZO", "PRODUCT_SEARCH_AMAZON_ASIN 614");
            try {
                productSearchUrls1.clear();
                productSearchUrls.clear();
                productSearchAdapter.notifyDataSetChanged();
                product_search_recycler.setVisibility(View.GONE);
                product_search_recycler1.setVisibility(View.VISIBLE);
                JSONObject object = o.getJSONObject("product");
                Log.d("test",object.get("url").toString());
                Log.d("asin",object.get("asin").toString());
                Gson gson = new Gson();
                ProductSearchUrl search = gson.fromJson(object.toString(), ProductSearchUrl.class);
                productSearchUrls1.add(search);
                productSearchAdapter1.notifyDataSetChanged();
                productSearchAdapter1.setListener();
            } catch (Exception e) {
                e.printStackTrace();
//                productSearchAmazon(search.getText().toString());
            }
        }

        if(type == ApiCall.UPC_LOOKUP){
            try {
                boolean successFlag = o.getBoolean("success");
                Log.d("successFlag",successFlag ? "true ":"false");
                if(successFlag){
                    JSONObject items = o.getJSONObject("items");
                    if(items.has("asin")){
                        String asinCode = items.getString("asin");
                        Log.d("asin",asinCode);
                        Log.d("asin","asinCode");
                        Log.d("asin",asinCode);
                        Log.d("asin","asinCode");
                        Log.d("asin",asinCode);
                        if (asinCode != "") {
                            ProductSearchUrl productData = new ProductSearchUrl();
                            productData.setAsin(asinCode);
                            productData.setUrl("https://www.amazon.co.jp/dp/"+asinCode);
                            productData.setBrand(items.getString("brand"));
                            productData.setTitle(items.getString("title"));
                            JSONArray IMAGESJSONARRAY = items.getJSONArray("images");
                            ArrayList<String> images = new ArrayList<>();
                            for (int i=0;i<IMAGESJSONARRAY.length();i++){
                                images.add(IMAGESJSONARRAY.getString(i));
                            }
                            productData.setImages(images);
                            productSearchUrls1.clear();
                            productSearchUrls.clear();
                            productSearchAdapter.notifyDataSetChanged();
                            product_search_recycler.setVisibility(View.GONE);
                            product_search_recycler1.setVisibility(View.VISIBLE);
                            productSearchUrls1.add(productData);
                            productSearchAdapter1.notifyDataSetChanged();
                            productSearchAdapter1.setListener();
//                        productDetailFromAsinAmazon(asinCode);

                        }else{
                            dismissProgressDialog();
                            showAlertDialog("","Don't know that one yet, try again");
                        }
                    }else{
                        search.setText(items.getString("title"));
                        productSearchAmazon(search.getText().toString());
                    }

                }else{
                    dismissProgressDialog();
                    showAlertDialog("","Don't know that one yet, try again");
                }

            } catch (Exception e) {
                e.printStackTrace();
                productSearchAmazon(search.getText().toString());
            }
        }
    }

    private File getOutputFileSticker() {

        File temp = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file = new File(temp, "Glow");
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(temp, "Stickers");
        if (!file.exists()) {
            file.mkdirs();
        }
        return new File(file.getPath() + File.separator + "Sticker_" + System.currentTimeMillis() + ".png");
    }

    private void downloadVideoOrImage(String url) {
//        progress_count.setVisibility(View.VISIBLE);
        showProgressDialog();
        new Thread(() -> {
            URL u = null;
            InputStream is = null;

            try {
                u = new URL(url);
                is = u.openStream();
                HttpURLConnection huc = (HttpURLConnection) u.openConnection(); //to know the size of video
                int size = huc.getContentLength();
                File file;

                file = getOutputFileSticker();

//                    String fileName = "FILE.mp4";
//                    String storagePath = Environment.getExternalStorageDirectory().toString();
//                    File f = new File(storagePath, fileName);

                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len1 = 0;
                long total = 0;
                int per = 0;
                if (is != null) {
                    while ((len1 = is.read(buffer)) > 0) {
                        total += len1;
                        if (size > 0) // only if total length is known
                            per = (int) (total * 100 / size);
                        int finalPer = per;
//                        runOnUiThread(() -> progress_count.setText(finalPer + "%"));

                        fos.write(buffer, 0, len1);
                    }
                }
                getActivity().runOnUiThread(() -> {
                    dismissProgressDialog();
//                    progress_count.setVisibility(View.GONE);
                    showToast("Sticker Saved");
                });
                fos.close();
            } catch (IOException mue) {
                getActivity().runOnUiThread(() -> {
                    dismissProgressDialog();
//                    progress_count.setVisibility(View.GONE);
                    showToast("Failed!");
                });
                mue.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {
                    // just going to ignore this one
                }
            }
        }).start();
    }


}
