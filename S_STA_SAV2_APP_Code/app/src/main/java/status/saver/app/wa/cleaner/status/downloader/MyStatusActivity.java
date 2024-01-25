package status.saver.app.wa.cleaner.status.downloader;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import status.saver.app.wa.cleaner.status.downloader.fragments.StaPhotos;
import status.saver.app.wa.cleaner.status.downloader.fragments.StaVideos;
import status.saver.app.wa.cleaner.status.downloader.util.AdManager;

import java.util.ArrayList;
import java.util.List;

public class MyStatusActivity extends AppCompatActivity {

    ImageView backIV;
    TextView topTV;

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_status);

        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        topTV = findViewById(R.id.topTV);

        viewPager = findViewById(R.id.pagerdiet);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tab_layoutdiet);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                    AdManager.adCounter++;
                    AdManager.showInterAd(MyStatusActivity.this, null,0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        LinearLayout adContainer = findViewById(R.id.banner_container);

            //admob
            AdManager.initAd(MyStatusActivity.this);
            AdManager.loadBannerAd(MyStatusActivity.this, adContainer);
            AdManager.loadInterAd(MyStatusActivity.this);

    }

    @Override
    public void onBackPressed() {
        AdManager.adCounter++;
        if (AdManager.adCounter == AdManager.adDisplayCounter) {
                AdManager.showInterAd(MyStatusActivity.this, null,0);
        } else {
            super.onBackPressed();
        }
    }

    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Photos");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabtwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabtwo.setText("Videos");
        tabLayout.getTabAt(1).setCustomView(tabtwo);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager());

        adapter.addFragment(new StaPhotos(), "Photos");
        adapter.addFragment(new StaVideos(), "Videos");

        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return this.mFragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.mFragmentTitleList.get(position);
        }
    }
}
