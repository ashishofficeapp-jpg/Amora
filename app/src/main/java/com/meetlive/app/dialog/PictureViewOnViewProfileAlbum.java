package com.meetlive.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.meetlive.app.R;
import com.meetlive.app.adapter.AlbumAdapterViewProfile;
import com.meetlive.app.response.UserListResponseNew.FemaleImage;

import java.util.List;

public class PictureViewOnViewProfileAlbum extends Dialog {
    Context context;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    ImageView close;
    List<FemaleImage> profile_images;
    int selectedPic;

    public PictureViewOnViewProfileAlbum(Context context, List<FemaleImage> profile_images, int selectedPic) {
        super(context, R.style.AppTheme);
        this.context = context;
        this.profile_images = profile_images;
        this.selectedPic = selectedPic;

        init();
    }

    void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_pictureview_album);

        close = findViewById(R.id.close_dialog);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.indicator_dot);
        viewPager.setAdapter(new AlbumAdapterViewProfile(getContext(), profile_images, false));
        viewPager.setCurrentItem(selectedPic);

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        // tab.setText(" " + (position + 1));
                    }
                }
        ).attach();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        show();
    }
}