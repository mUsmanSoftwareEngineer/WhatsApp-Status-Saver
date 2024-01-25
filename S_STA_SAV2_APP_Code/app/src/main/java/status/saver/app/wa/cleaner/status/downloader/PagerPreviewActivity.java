package status.saver.app.wa.cleaner.status.downloader;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.viewpager.widget.ViewPager;


import status.saver.app.wa.cleaner.status.downloader.adapter.FullscreenImageAdapter;
import status.saver.app.wa.cleaner.status.downloader.model.StatusModel;
import status.saver.app.wa.cleaner.status.downloader.util.AdManager;
import status.saver.app.wa.cleaner.status.downloader.util.Utils;

import java.io.File;
import java.util.ArrayList;

public class PagerPreviewActivity extends AppCompatActivity {

    ViewPager viewPager;
    ArrayList<StatusModel> imageList;
    int position;


    ImageView shareIV, deleteIV, wAppIV;
    LinearLayout downloadIV;
    FullscreenImageAdapter fullscreenImageAdapter;
    String statusdownload;
    ImageView backIV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_preview);


        backIV = findViewById(R.id.backIV);


        viewPager = findViewById(R.id.viewPager);

        shareIV = findViewById(R.id.shareIV);

        downloadIV = findViewById(R.id.downloadIV);

        deleteIV = findViewById(R.id.deleteIV);
        wAppIV = findViewById(R.id.wAppIV);


        imageList = getIntent().getParcelableArrayListExtra("images");
        position = getIntent().getIntExtra("position", 0);
        statusdownload = getIntent().getStringExtra("statusdownload");

        if (statusdownload.equals("download")) {
            downloadIV.setVisibility(View.GONE);
        } else {
            downloadIV.setVisibility(View.VISIBLE);
        }

        fullscreenImageAdapter = new FullscreenImageAdapter(PagerPreviewActivity.this, imageList);
        viewPager.setAdapter(fullscreenImageAdapter);
        viewPager.setCurrentItem(position);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                    AdManager.adCounter++;
                    AdManager.showInterAd(PagerPreviewActivity.this, null,0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        downloadIV.setOnClickListener(clickListener);
        shareIV.setOnClickListener(clickListener);
        deleteIV.setOnClickListener(clickListener);
        backIV.setOnClickListener(clickListener);
        wAppIV.setOnClickListener(clickListener);

        LinearLayout adContainer = findViewById(R.id.banner_container);
            //admob
            AdManager.initAd(PagerPreviewActivity.this);
            AdManager.loadBannerAd(PagerPreviewActivity.this, adContainer);
            AdManager.loadInterAd(PagerPreviewActivity.this);


    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backIV:
                    onBackPressed();
                    break;
                case R.id.downloadIV:
                    if (imageList.size() > 0) {

                        //ads
                            AdManager.adCounter++;
                            AdManager.showInterAd(PagerPreviewActivity.this, null,0);
                        try {
                            Utils.download(PagerPreviewActivity.this, imageList.get(viewPager.getCurrentItem()).getFilePath());
                            Toast.makeText(PagerPreviewActivity.this, "Status saved successfully", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(PagerPreviewActivity.this, "Sorry we can't move file.try with other file.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        finish();
                    }
                    break;

                case R.id.shareIV:
                    if (imageList.size() > 0) {
                        Utils.shareFile(PagerPreviewActivity.this, Utils.isVideoFile(PagerPreviewActivity.this, imageList.get(viewPager.getCurrentItem()).getFilePath()),imageList.get(viewPager.getCurrentItem()).getFilePath());
                    } else {
                        finish();
                    }
                    break;

                case R.id.deleteIV:
                    if (imageList.size() > 0) {
                         AdManager.adCounter++;
                            AdManager.showInterAd(PagerPreviewActivity.this, null,0);


                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PagerPreviewActivity.this);
                        alertDialog.setTitle("Confirm Delete....");
                        alertDialog.setMessage("Are you sure, You Want To Delete This Status?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                int currentItem = 0;

                                if (statusdownload.equals("download")) {
                                    File file = new File(imageList.get(viewPager.getCurrentItem()).getFilePath());
                                    if (file.exists()) {
                                        boolean del = file.delete();
                                        delete(currentItem);
                                    }
                                }else {
                                    DocumentFile fromTreeUri = DocumentFile.fromSingleUri(PagerPreviewActivity.this, Uri.parse(imageList.get(viewPager.getCurrentItem()).getFilePath()));
                                    if (fromTreeUri.exists()) {
                                        boolean del = fromTreeUri.delete();
                                        delete(currentItem);
                                    }
                                }
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        finish();
                    }
                    break;

                case R.id.wAppIV:
                    Utils.repostWhatsApp(PagerPreviewActivity.this, Utils.isVideoFile(PagerPreviewActivity.this, imageList.get(viewPager.getCurrentItem()).getFilePath()),imageList.get(viewPager.getCurrentItem()).getFilePath());
                    break;

                default:
                    break;
            }
        }
    };

    void delete(int currentItem){
        if (imageList.size() > 0 && viewPager.getCurrentItem() < imageList.size()) {
            currentItem = viewPager.getCurrentItem();
        }
        imageList.remove(viewPager.getCurrentItem());
        fullscreenImageAdapter = new FullscreenImageAdapter(PagerPreviewActivity.this, imageList);
        viewPager.setAdapter(fullscreenImageAdapter);

        Intent intent = new Intent();
        setResult(10, intent);

        if (imageList.size() > 0) {
            viewPager.setCurrentItem(currentItem);
        } else {
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        AdManager.adCounter++;
        if (AdManager.adCounter == AdManager.adDisplayCounter) {
                AdManager.showInterAd(PagerPreviewActivity.this, null,0);
        } else {
            super.onBackPressed();
        }
    }
}
