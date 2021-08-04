package com.glowteam.Ui.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.abedelazizshe.lightcompressorlibrary.CompressionListener;
import com.abedelazizshe.lightcompressorlibrary.VideoQuality;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.glowteam.Constant.Constant;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.Models.StickersItem;
import com.glowteam.R;

import org.buffer.android.thumby.ThumbyActivity;
import org.buffer.android.thumby.util.ThumbyUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import ly.img.android.pesdk.VideoEditorSettingsList;
import ly.img.android.pesdk.assets.filter.basic.FilterPackBasic;
import ly.img.android.pesdk.assets.font.basic.FontPackBasic;
import ly.img.android.pesdk.assets.frame.basic.FramePackBasic;
import ly.img.android.pesdk.assets.overlay.basic.OverlayPackBasic;
import ly.img.android.pesdk.assets.sticker.emoticons.StickerPackEmoticons;
import ly.img.android.pesdk.assets.sticker.shapes.StickerPackShapes;
import ly.img.android.pesdk.backend.decoder.ImageSource;
import ly.img.android.pesdk.backend.model.EditorSDKResult;
import ly.img.android.pesdk.backend.model.config.ImageStickerAsset;
import ly.img.android.pesdk.backend.model.constant.Directory;
import ly.img.android.pesdk.backend.model.state.AssetConfig;
import ly.img.android.pesdk.backend.model.state.LoadSettings;
import ly.img.android.pesdk.backend.model.state.SaveSettings;
import ly.img.android.pesdk.backend.model.state.VideoEditorSaveSettings;
import ly.img.android.pesdk.backend.model.state.manager.Settings;
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder;
import ly.img.android.pesdk.ui.model.state.UiConfigFilter;
import ly.img.android.pesdk.ui.model.state.UiConfigFrame;
import ly.img.android.pesdk.ui.model.state.UiConfigOverlay;
import ly.img.android.pesdk.ui.model.state.UiConfigSticker;
import ly.img.android.pesdk.ui.model.state.UiConfigText;
import ly.img.android.pesdk.ui.panels.item.AbstractIdItem;
import ly.img.android.pesdk.ui.panels.item.ImageStickerItem;
import ly.img.android.pesdk.ui.panels.item.StickerCategoryItem;
import ly.img.android.pesdk.ui.utils.DataSourceIdItemList;
import ly.img.android.pesdk.ui.panels.item.PersonalStickerAddItem;
import static com.abedelazizshe.lightcompressorlibrary.VideoCompressor.INSTANCE;
import static org.buffer.android.thumby.ThumbyActivity.EXTRA_THUMBNAIL_POSITION;
import static org.buffer.android.thumby.ThumbyActivity.EXTRA_URI;

public class VideoEditActivity extends BaseActivity implements View.OnClickListener {

    private VideoView videoview;
    private String path;
    private Uri uri, resultUri;
    private CardView finish;
    public int VESDK_RESULT = 1;
    String productName;
    String productUrl;
    private String productThumbnailUrl;
    private String productId;
    private ArrayList<StickersItem> stickersItemArrayList;
    CustomTextView mTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_edit);
        path = getIntent().getStringExtra("path");
        uri = Uri.parse(getIntent().getStringExtra("uri"));
        productId = getIntent().getStringExtra("productId");
        productName = getIntent().getStringExtra("productName");
        productUrl = getIntent().getStringExtra("productUrl");
        productThumbnailUrl = getIntent().getStringExtra("productThumbnailUrl");
        stickersItemArrayList = (ArrayList<StickersItem>) getIntent().getSerializableExtra("stickers");
        initView();
        initListener();

        Log.d("Data", "onCreate: ====>" + productId + " " + productName + " " + productThumbnailUrl + " " + productUrl);
    }

    private void initListener() {
        finish.setOnClickListener(this);
    }

    private void initView() {
//        videoview = (VideoView) findViewById(R.id.videoview);
        finish = findViewById(R.id.finish);
        mTxt = findViewById(R.id.txt);
        openEditor(uri);
        /*videoview.setVideoPath(path);
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                videoview.start();
            }
        });*/


    }

    public void openEditor(Uri inputImage) {
        if (Build.VERSION.SDK_INT >= 18) {
            VideoEditorSettingsList settingsList = createVesdkSettingsList();

//            UiConfigMainMenu uiConfigMainMenu = settingsList.getSettingsModel(UiConfigMainMenu.class);
//            uiConfigMainMenu.setToolList(
//                    new ToolItem(StickerToolPanel.TOOL_ID, R.string.pesdk_sticker_title_name, ImageSource.create(R.drawable.imgly_icon_tool_sticker)),
//                    new ToolItem(TextToolPanel.TOOL_ID, R.string.pesdk_text_title_name, ImageSource.create(R.drawable.imgly_icon_tool_text))
//            );


            Settings setting = settingsList.getSettingsModel(LoadSettings.class);
            LoadSettings it = (LoadSettings) setting;
            it.setSource(inputImage);
//            ((LoadSettings) settingsList.get(Reflection.getOrCreateKotlinClass(LoadSettings.class))).setSource(inputImage);
            (new VideoEditorBuilder(this)).setSettingsList(settingsList).startActivityForResult(this, 1);
        } else {
            Toast.makeText(this, "Lower version not supported", Toast.LENGTH_SHORT).show();
        }
    }

    private final VideoEditorSettingsList createVesdkSettingsList() {

        String s = getIntent().getStringExtra("product_img");

        VideoEditorSettingsList videpSetting = new VideoEditorSettingsList();
        AssetConfig assetConfig = videpSetting.getConfig();
        assetConfig.addAsset(new ImageStickerAsset(
                "stickerAssetId0",
                ImageSource.create(Uri.parse(s))
        ));

        if (stickersItemArrayList != null && stickersItemArrayList.size() != 0) {
            for (int i = 0; i < stickersItemArrayList.size(); i++) {
                String st = Constant.BASE_URL + stickersItemArrayList.get(i).getUrl();
                assetConfig.addAsset(new ImageStickerAsset(
                        "stickerAssetId" + (i + 1),
                        ImageSource.create(Uri.parse(st))
                ));
            }
        }


        File glowDirectory = getDefaultVideoFile();
        for (int i = 0; i < glowDirectory.list().length; i++) {
            if(glowDirectory.list()[i].endsWith(".png")){
                assetConfig.addAsset(new ImageStickerAsset(
                        "localStickerAssetId" + (i + 1),
                        ImageSource.create(Uri.fromFile(new File(glowDirectory.getAbsolutePath(),glowDirectory.list()[i])))
                ));
            }
        }



        Settings setting = videpSetting.getSettingsModel(UiConfigFilter.class);

        UiConfigFilter it = (UiConfigFilter) setting;
        it.setFilterList(FilterPackBasic.getFilterPack());

        setting = videpSetting.getSettingsModel(UiConfigText.class);
        UiConfigText it1 = (UiConfigText) setting;
        ArrayList[] array = new ArrayList[1];
        DataSourceIdItemList dataSource = FontPackBasic.getFontPack();
        array[0] = dataSource;
        it1.setFontList(array);

        setting = videpSetting.getSettingsModel(UiConfigFrame.class);
        UiConfigFrame it2 = (UiConfigFrame) setting;
        it2.setFrameList(FramePackBasic.getFramePack());

        setting = videpSetting.getSettingsModel(UiConfigOverlay.class);
        UiConfigOverlay it3 = (UiConfigOverlay) setting;
        it3.setOverlayList(OverlayPackBasic.getOverlayPack());

        setting = videpSetting.getSettingsModel(UiConfigSticker.class);
        UiConfigSticker it4 = (UiConfigSticker) setting;
        AbstractIdItem[] var10 = new AbstractIdItem[4];
        StickerCategoryItem sticker = StickerPackEmoticons.getStickerCategory();

        ImageStickerItem item = ImageStickerItem.createFromAsset(new ImageStickerAsset(
                "stickerAssetId0",
                ImageSource.create(Uri.parse(s))
        ));
        item.setName("product");
        sticker.getStickerList().add(0, item);
        var10[0] = sticker;
        sticker = StickerPackShapes.getStickerCategory();
        var10[1] = sticker;

        ArrayList<ImageStickerItem> imageStickerItems = new ArrayList<>();
        if (stickersItemArrayList != null && stickersItemArrayList.size() != 0) {
            for (int i = 0; i < stickersItemArrayList.size(); i++) {
                String st = Constant.BASE_URL + stickersItemArrayList.get(i).getUrl();
                ImageStickerItem stickerItem = ImageStickerItem.createFromAsset(new ImageStickerAsset(
                        "stickerAssetId" + (i + 1),
                        ImageSource.create(Uri.parse(st))
                ));
                imageStickerItems.add(stickerItem);
            }
        }
        StickerCategoryItem item1 = new StickerCategoryItem("myUniqStickerCategoryId1",
                R.string.imgly_sticker_category_name_emoticons, ImageSource.create(R.drawable.iv_appicon), imageStickerItems);
        var10[2] = item1;
        ArrayList<ImageStickerItem> imageStickerItemsLocal = new ArrayList<>();
        for (int i = 0; i < glowDirectory.list().length; i++) {
            if(glowDirectory.list()[i].endsWith(".png")){
                ImageStickerItem stickerItem = ImageStickerItem.createFromAsset(new ImageStickerAsset(
                        "stickerAssetIdLocal" + (i + 1),
                        ImageSource.create(Uri.fromFile((new File(glowDirectory.getAbsolutePath() ,glowDirectory.list()[i])))
                )));
                imageStickerItemsLocal.add(stickerItem);
            }
        }
        StickerCategoryItem item2 = new StickerCategoryItem("myUniqStickerCategoryId2",
                "own stickers", ImageSource.create(R.drawable.imgly_icon_save), imageStickerItemsLocal);
//        var10[3] = item2;
        var10[3] = new PersonalStickerAddItem();
//        StickerCategoryItem item1 = new StickerCategoryItem("myUniqStickerCategoryId1",
//                R.string.imgly_sticker_category_name_emoticons, ImageSource.create(R.drawable.imgly_sticker_emoticons_alien), item);
//        var10[2] = item1;
        it4.setStickerLists(var10);

        setting = videpSetting.getSettingsModel(VideoEditorSaveSettings.class);


        VideoEditorSaveSettings it5 = (VideoEditorSaveSettings) setting;
//        Directory.EnvironmentDir var11 = Directory.DCIM;
        File temp = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file = new File(temp, "Glow");
        if (!file.exists()) {
            file.mkdirs();
        }

        /*File output = new File(file, "video_edit");
        it5.setOutputFilePath(output.getPath());*/
//        it5.setExportDir(Directory.DCIM, "Glow");

        it5.setExportDir(file.getAbsolutePath());
//        it5.setExportDir(temp.getPath());
        it5.setExportPrefix("result_");
        it5.setSavePolicy(SaveSettings.SavePolicy.RETURN_ALWAYS_ONLY_OUTPUT);

        return videpSetting;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    EditorSDKResult data = new EditorSDKResult(intent);
//                    data.notifyGallery(-4);
                    data.notifyGallery(EditorSDKResult.UPDATE_RESULT & EditorSDKResult.UPDATE_SOURCE);

                    resultUri = data.getResultUri();
                    if (resultUri != null) {
                        startActivityForResult(ThumbyActivity.Companion.getStartIntent(this, resultUri, 3), 2);
                    }
//                    SettingsList lastState = data.getSettingsList();
//                    (new IMGLYFileWriter(lastState)).writeJson(new File(Environment.getExternalStorageDirectory(), "serialisationReadyToReadWithPESDKFileReader.json"));
                    break;
                case 2:
                    if (intent != null) {
                        showProgressDialog();
                        new Thread(() -> {
                            Uri imageUri = intent.getParcelableExtra(EXTRA_URI);
                            long location = intent.getLongExtra(EXTRA_THUMBNAIL_POSITION, 0);
                            if (imageUri != null) {
                                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                retriever.setDataSource(getRealPathFromURI(imageUri));
                                int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                                int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                                retriever.release();
                                Bitmap bitmap = ThumbyUtils.INSTANCE.getBitmapAtFrame(VideoEditActivity.this, imageUri, location, width, height);
                                File temp = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                                File file = new File(temp, "Glow");
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                File filename = new File(file.getAbsolutePath(), "temp.png");
                                try (FileOutputStream out = new FileOutputStream(filename)) {
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                                    MediaMetadataRetriever retriever1 = new MediaMetadataRetriever();
                                    retriever1.setDataSource(getRealPathFromURI(resultUri));
                                    int width1 = Integer.valueOf(retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                                    int height1 = Integer.valueOf(retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                                    int bitrate = Integer.valueOf(retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
                                    retriever1.release();
                                    runOnUiThread(() -> {
                                        CustomTextView p = mProgressBarDialog.findViewById(R.id.progress);
                                        p.setText("Compressing...");
                                    });
                                    runOnUiThread(() -> {
                                        dismissProgressDialog();
                                        showProgressBarDialog();
                                    });
                                    File filename1 = new File(file.getAbsolutePath(), "glow_" + System.currentTimeMillis() + ".mp4");


                                    INSTANCE.start(getRealPathFromURI(resultUri), filename1.getAbsolutePath(), new CompressionListener() {
                                        @Override
                                        public void onStart() {

                                        }

                                        @Override
                                        public void onSuccess() {
                                            runOnUiThread(() -> {
                                                dismissProgressBarDialog();
                                                showProgressDialog();
                                                MediaScannerConnection.scanFile(VideoEditActivity.this,
                                                        new String[]{filename1.getAbsolutePath()}, null,
                                                        (path, uri) -> {
                                                            uploadVideo(resultUri, Uri.fromFile(filename), productId, productName,
                                                                    productThumbnailUrl, productUrl);
                                                        });
                                            });

                                        }

                                        @Override
                                        public void onFailure() {
                                            runOnUiThread(() -> {
                                                dismissProgressBarDialog();
                                            });
                                        }

                                        @Override
                                        public void onProgress(float v) {
                                            runOnUiThread(() -> {
                                                TextRoundCornerProgressBar p = mProgressBarDialog.findViewById(R.id.progress_bar);
                                                p.setProgress(v);
                                            });
                                        }

                                        @Override
                                        public void onCancelled() {
                                            runOnUiThread(() -> {
                                                dismissProgressBarDialog();
                                            });
                                        }
                                    }, VideoQuality.HIGH, false);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    break;

            }
        } else {
            finish();
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish:
//                uploadVideo(path);
                break;
        }
    }

    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        try {
            if (type == ApiCall.UPLOAD_VIDEO) {
                finish();
//            String path = getRealPathFromURI(resultUri);
                JSONObject object = o.getJSONObject("data");
                String url = object.getString("videoUrl");
                String s = getIntent().getStringExtra("product_img");
                startActivity(new Intent(VideoEditActivity.this, ShareActivity.class)
                        .putExtra("path", Constant.BASE_URL + url)
                        .putExtra("stickerUrl", s));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(String msg, ApiCall type) {
        super.OnError(msg, type);
        mTxt.setVisibility(View.GONE);
        finish();
    }
}
