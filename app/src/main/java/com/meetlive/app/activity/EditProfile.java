package com.meetlive.app.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.meetlive.app.R;
import com.meetlive.app.SpannableGridLayoutManager;
import com.meetlive.app.adapter.ProfilePicsAdapter;
import com.meetlive.app.databinding.ActivityEditProfileBinding;
import com.meetlive.app.dialog.EditProfileBottomSheet;
import com.meetlive.app.response.ProfileDetailsResponse;
import com.meetlive.app.response.UserListResponse;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.utils.Constant;
import com.meetlive.app.utils.DateCallback;
import com.meetlive.app.utils.DateFormatter;
import com.meetlive.app.utils.Datepicker;
import com.meetlive.app.utils.RecyclerTouchListener;
import com.squareup.picasso.Picasso;
import com.stripe.android.model.SourceOrder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfile extends AppCompatActivity implements ApiResponseInterface {

    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;
    List<UserListResponse.UserPics> imageList;
    List<ProfileDetailsResponse> imageListPoster;
    ActivityEditProfileBinding binding;
    ApiManager apiManager;
    boolean is_album = false;
    ProfilePicsAdapter profilePicsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        binding.setClickListener(new EventHandler(this));
        SpannableGridLayoutManager gridLayoutManager = new SpannableGridLayoutManager(new SpannableGridLayoutManager.GridSpanLookup() {
            @Override
            public SpannableGridLayoutManager.SpanInfo getSpanInfo(int position) {
                if (position == 0) {
                    return new SpannableGridLayoutManager.SpanInfo(2, 2);
                    //this will count of row and column you want to replace
                } else {
                    return new SpannableGridLayoutManager.SpanInfo(1, 1);
                }
            }
        }, 3, 1f); // 3 is the number of coloumn , how nay to display is 1f
        binding.pictureRecyclerview.setLayoutManager(gridLayoutManager);


        apiManager = new ApiManager(this, this);
        apiManager.getProfileDetails();

        binding.stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                binding.stateTextview.setText(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.pictureRecyclerview.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), binding.pictureRecyclerview, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.e("picUrl", imageList.get(position).getImage_name());
                if (position == 0) {
                    is_album = false;
                    pickImage();
                    return;
                }
                if (imageList.get(position).getImage_name().equals("add_pic")) {
                    if (position == 0) {
                        is_album = false;
                    } else {
                        is_album = true;
                    }
                    pickImage();
                    return;
                }
                if (!imageList.get(position).getImage_name().equals("")) {
                    Log.e("I am here","in if condition"+position);
                    new EditProfileBottomSheet(EditProfile.this, imageList, position);
                } else if (position == 0) {
                    Log.e("I am here", "in else if condition" + position);
                    is_album = false;
                    pickImage();
                } else {
                    Log.e("I am here", "in else  condition" + position);
                    is_album = true;
                    pickImage();

                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        // Clearing older images from cache directory
        // don't call this line if you want to choose multiple images in the same activity
        // call this once the bitmap(s) usage is over
        ImagePickerActivity.clearCache(this);
    }

    public void pickImage() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent intent = new Intent(EditProfile.this, ImagePickerActivity.class);
                        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
                        // setting aspect ratio
                        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
                        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 3); // 16x9, 1x1, 3:4, 3:2
                        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 4);
                        startActivityForResult(intent, PICK_IMAGE_GALLERY_REQUEST_CODE);

                        /*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY_REQUEST_CODE);*/

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            // navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                // Uri selectedImageUri = data.getData();
                Uri selectedImageUri = data.getParcelableExtra("path");
                Log.e("selectimg", "selectedImageUri==" + selectedImageUri);
                String picturePath = selectedImageUri.getPath();
                //loadProfile(selectedImageUri.toString());
                Log.e("picture", picturePath);

                if (!picturePath.equals("Not found")) {
                    File picture = new File(picturePath);
                    // Compress Image
                    File file = new Compressor(this).compressToFile(picture);
                    Log.e("selectedImageEdit", "selectedImageEdit:" + file);
                    Log.e("is_album", "is_album:" + is_album);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    //RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part picToUpload = MultipartBody.Part.createFormData("profile_pic[]", file.getName(), requestBody);

                    //Create request body with text description and text media type
                    //RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
                    //RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), "image-type");

                    apiManager.updateProfileDetails("", "", "", "", picToUpload, is_album);
                } else {
                    Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.e("picturewee", e.toString());
                Toast.makeText(this, "Please select another image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    public void refreshData() {
        apiManager.getProfileDetails();

    }


    public class EventHandler {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void dob() {
            new Datepicker().selectDateFrom(mContext, binding.birthday, "", new DateCallback() {
                @Override
                public void onDateGot(String date, long timeStamp) {
                    binding.birthday.setText(DateFormatter.getInstance().format(date));
                }
            });
        }

        public void updateData() {
            apiManager.updateProfileDetails(binding.userName.getText().toString(), binding.stateTextview.getText().toString(),
                    binding.birthday.getText().toString(),
                    binding.aboutUser.getText().toString(), null, false);
        }

        public void onBack() {
            onBackPressed();
        }
    }

    @Override
    public void isError(String errorCode) {
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.PROFILE_DETAILS) {
            ProfileDetailsResponse rsp = (ProfileDetailsResponse) response;
            imageList = rsp.getSuccess().getProfile_images();

            Log.e("imgAlbum", new Gson().toJson(rsp.getSuccess().getProfile_images()));
            Log.e("imgAlbumsize", String.valueOf(imageList.size()));

            //Allow add pic if less than 6
            if (imageList.size() <= 5) {
                for (int i = imageList.size(); i < 6; i++) {
                    Log.e("addPlusisData", String.valueOf(imageList.size()));
                    UserListResponse.UserPics pic = new UserListResponse.UserPics();
                    pic.setImage_name("add_pic");
                    imageList.add(pic);
                }
            }
            //imageListPoster = new ArrayList<>();
            //imageListPoster.add(rsp);
            // Log.e("profileImallgesNEW", new Gson().toJson(imageListPoster));
            Log.e("profileImallgesNEW", "ArraySize " + imageList.size());
            Log.e("profileImallgesNEW", "ArraySizeData " + new Gson().toJson(imageList));

           /* for (int i = 0; i < imageList.size(); i++) {
                if (imageList.get(i).getIs_profile_image() == 1) {
                    //Log.e("profileImallgesALLOT", imageList.get(i).getImage_name());
                    //Log.e("profileImallgesNEW", "inArrayChange" + new Gson().toJson(imageListPoster));
                    Log.e("profileImallgesNEW", "inArrayChangeB" + new Gson().toJson(imageList));
                    Collections.swap(imageList, i, 0);
                    Log.e("profileImallgesNEW", "inArrayChangeA" + new Gson().toJson(imageList));

                }
            }*/

            Log.e("profileImallgesNEW", "ArraySizeImage " + imageList.size());
            profilePicsAdapter = new ProfilePicsAdapter(this, imageList);
            binding.pictureRecyclerview.setAdapter(profilePicsAdapter);
            profilePicsAdapter.notifyDataSetChanged();
            //binding.pictureRecyclerview.setAdapter(new ProfilePicsAdapter(this, imageList));

            binding.userName.setText(rsp.getSuccess().getName());
            binding.stateTextview.setText(rsp.getSuccess().getCity());
            binding.birthday.setText(rsp.getSuccess().getDob());
            binding.aboutUser.setText(rsp.getSuccess().getAbout_user());

            if (ServiceCode == Constant.UPDATE_PROFILE) {
                apiManager.getProfileDetails();
            }
        }

    }


}