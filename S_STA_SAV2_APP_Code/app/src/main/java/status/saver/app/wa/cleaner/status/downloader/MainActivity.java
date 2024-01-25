package status.saver.app.wa.cleaner.status.downloader;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import status.saver.app.wa.cleaner.status.downloader.fragments.Utils;
import status.saver.app.wa.cleaner.status.downloader.util.AdManager;
import status.saver.app.wa.cleaner.status.downloader.util.SharedPrefs;
import status.saver.app.wa.cleaner.status.downloader.waweb.WAWebActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String[] permissionsList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    ImageView wappBtn, modeIV;
    LinearLayout rateBtn, shareBtn, policyBtn, moreBtn, helpBtn;
    RelativeLayout recentBtn, statusBtn, cleanBtn, wbCleanBtn, directBtn, wbWebBtn;
    Animation blink;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wappBtn = findViewById(R.id.wappBtn);
        blink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        wappBtn.startAnimation(blink);
        wappBtn.setOnClickListener(this);

        recentBtn = findViewById(R.id.recentBtn);
        recentBtn.setOnClickListener(this);

        statusBtn = findViewById(R.id.statusBtn);
        statusBtn.setOnClickListener(this);

        cleanBtn = findViewById(R.id.cleanBtn);
        cleanBtn.setOnClickListener(this);

        wbCleanBtn = findViewById(R.id.wbCleanBtn);
        wbCleanBtn.setOnClickListener(this);

        directBtn = findViewById(R.id.directBtn);
        directBtn.setOnClickListener(this);

        wbWebBtn = findViewById(R.id.wbWebBtn);
        wbWebBtn.setOnClickListener(this);

        rateBtn = findViewById(R.id.rateBtn);
        rateBtn.setOnClickListener(this);

        shareBtn = findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(this);

        policyBtn = findViewById(R.id.policyBtn);
        policyBtn.setOnClickListener(this);

        moreBtn = findViewById(R.id.moreBtn);
        moreBtn.setOnClickListener(this);

        helpBtn = findViewById(R.id.helpBtn);
        helpBtn.setOnClickListener(this);

        modeIV = findViewById(R.id.modeIV);
        modeIV.setOnClickListener(this);

        int mode = SharedPrefs.getAppNightDayMode(this);
        if (mode == AppCompatDelegate.MODE_NIGHT_YES){
            modeIV.setImageResource(R.drawable.dark_mode);
        }else {
            modeIV.setImageResource(R.drawable.light_mode);
        }

        wAppAlert();
         //admob
            AdManager.initAd(MainActivity.this);
            AdManager.loadInterAd(MainActivity.this);


    }


    void wAppAlert() {
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.popup_lay);

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RelativeLayout btnWapp = dialog.findViewById(R.id.btn_wapp);
        RelativeLayout btnWappBus = dialog.findViewById(R.id.btn_wapp_bus);

        btnWapp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    startActivity(getPackageManager().getLaunchIntentForPackage("com.whatsapp"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Please Install WhatsApp For Download Status!!!!!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });

        btnWappBus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    startActivity(getPackageManager().getLaunchIntentForPackage("com.whatsapp.w4b"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Please Install WhatsApp Business For Download Status!!!!!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

    }

    public static boolean checkPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wappBtn:
                dialog.show();
                break;

            case R.id.recentBtn:
                if (!checkPermissions(this, permissionsList)) {
                    ActivityCompat.requestPermissions(this, permissionsList, 21);
                }else {
                    startActivityes(new Intent(MainActivity.this, RecStatusActivity.class));
                }
                break;

            case R.id.statusBtn:
                if (!checkPermissions(this, permissionsList)) {
                    ActivityCompat.requestPermissions(this, permissionsList, 21);
                }else {
                    startActivityes(new Intent(MainActivity.this, MyStatusActivity.class));
                }
                break;

            case R.id.directBtn:
                if (!checkPermissions(this, permissionsList)) {
                    ActivityCompat.requestPermissions(this, permissionsList, 21);
                }else {
                    startActivityes(new Intent(MainActivity.this, DirectActivity.class));
                }
                break;

            case R.id.wbWebBtn:
                if (!checkPermissions(this, permissionsList)) {
                    ActivityCompat.requestPermissions(this, permissionsList, 21);
                }else {
                    startActivityes(new Intent(MainActivity.this, WAWebActivity.class));
                }
                break;

            case R.id.cleanBtn:
                Utils.iswApp = true;
                cleaner();
                break;

            case R.id.wbCleanBtn:
                Utils.iswApp = false;
                cleaner();
                break;

            case R.id.rateBtn:
                rateUs();
                break;

            case R.id.shareBtn:
                shareApp();
                break;

            case R.id.policyBtn:
                if (!checkPermissions(this, permissionsList)) {
                    ActivityCompat.requestPermissions(this, permissionsList, 21);
                }else {
                    startActivityes(new Intent(MainActivity.this, PrivacyActivity.class));
                }
                break;

            case R.id.moreBtn:
                moreApp();
                break;

            case R.id.helpBtn:
                startActivityes(new Intent(MainActivity.this, HelpActivity.class));
                break;

            case R.id.modeIV:
                int mode = SharedPrefs.getAppNightDayMode(this);
                if (mode == AppCompatDelegate.MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    SharedPrefs.setInt(this, SharedPrefs.PREF_NIGHT_MODE,AppCompatDelegate.MODE_NIGHT_YES);
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    SharedPrefs.setInt(this, SharedPrefs.PREF_NIGHT_MODE,AppCompatDelegate.MODE_NIGHT_NO);
                }
                break;

            default:
                break;
        }
    }

    void cleaner(){
        if (!checkPermissions(this, permissionsList)) {
            ActivityCompat.requestPermissions(this, permissionsList, 21);
        }else {
            startActivityes(new Intent(MainActivity.this, CleanOptionActivity.class));
        }
    }

    void startActivityes(Intent intent) {
            AdManager.adCounter++;
            AdManager.showInterAd(MainActivity.this, intent,0);

    }

    public void shareApp() {
        Intent myapp = new Intent(Intent.ACTION_SEND);
        myapp.setType("text/plain");
        myapp.putExtra(Intent.EXTRA_TEXT, "Download this awesome app\n https://play.google.com/store/apps/details?id=" + getPackageName() + " \n");
        startActivity(myapp);
    }

    public void rateUs() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void moreApp() {
        startActivity(new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/dev?id=7081479513420377164&hl=en")));
    }

    @Override
    public void onBackPressed() {
        exitAlert();
    }

    void exitAlert() {
        final Dialog exitDialog = new Dialog(MainActivity.this);
        exitDialog.setContentView(R.layout.exit_popup_lay);

        exitDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));


        TextView noBtn = exitDialog.findViewById(R.id.noBtn);
        TextView yesBtn = exitDialog.findViewById(R.id.yesBtn);

        noBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                exitDialog.dismiss();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                exitDialog.dismiss();
                MainActivity.super.onBackPressed();
            }
        });
        exitDialog.show();
    }
}
