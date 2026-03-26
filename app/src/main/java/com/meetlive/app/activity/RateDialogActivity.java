package com.meetlive.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.meetlive.app.R;
import com.meetlive.app.adapter.SendRatingAdapter;
import com.meetlive.app.databinding.ActivityRateDialogBinding;
import com.meetlive.app.response.Rating.RatingResponce;
import com.meetlive.app.response.SendTagModel;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.utils.Constant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import de.hdodenhof.circleimageview.CircleImageView;


public class RateDialogActivity extends AppCompatActivity {
    ActivityRateDialogBinding binding;
    String hostId;
    String name;
    String image;
    String callTime;
    String tagValue = "";
    GridView gridview;
    Stack<String> setTags = new Stack<>();
    int MAX_TAGS_ALLOWED = 3;
    int backposition = 3;
    String defaultRating = "4.0";
    private ArrayList<SendTagModel> tagModelArrayList = new ArrayList<SendTagModel>();
    private SendRatingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rate_dialog);

        gridview = findViewById(R.id.gridview);
        tagModelArrayList.add(new SendTagModel("Beautiful Voice"));
        tagModelArrayList.add(new SendTagModel("Young"));
        tagModelArrayList.add(new SendTagModel("Lady"));
        tagModelArrayList.add(new SendTagModel("A Born Beauty"));
        tagModelArrayList.add(new SendTagModel("Elegant"));
        tagModelArrayList.add(new SendTagModel("Sports Talent"));
        setTags.clear();
        adapter = new SendRatingAdapter(this, tagModelArrayList);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.makeAllUnselect(position);
                adapter.notifyDataSetChanged();
                //set value in stack here 8/5/21
                tagValue = tagModelArrayList.get(position).getTag_name();
                setTags.push(tagValue);
                // Toast.makeText(RateDialogActivity.this, tagModelArrayList.get(position).getTag_name(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.setClickListener(new EventHandler(this));
        init();
    }

    void init() {
        try {
            Intent in = getIntent();
            Bundle b = in.getExtras();
            if (b != null) {
                name = b.getString("host_name");
                hostId = b.getString("host_id");
                image = b.getString("host_image");
                callTime = b.getString("end_time");
            }
            CircleImageView img_profile = findViewById(R.id.img_profile);
            TextView tv_hostname = findViewById(R.id.tv_hostname);
            TextView tv_hostId = findViewById(R.id.tv_hostId);
            TextView call_duration = findViewById(R.id.tv_call_duration);


            Glide.with(getApplicationContext()).load(image).circleCrop().into(((CircleImageView) findViewById(R.id.img_profile)));
            tv_hostname.setText(name);
            tv_hostId.setText(hostId);
            call_duration.setText("Call Duration: " + String.valueOf(callTime));

            //if rating value is changed,
            //display the current rating value in the result (textview) automatically
            RatingBar ratingBar = (RatingBar) findViewById(R.id.RatingBar);
            ratingBar.setRating(4.0f);
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    // binding.tvRateing.setText(String.valueOf(rating));
                    // Log.i(TAG, "onRatingChanged: rating : "+rating);
                    defaultRating = String.valueOf(rating);
                    if (rating == 1.0) {
                        tagModelArrayList.clear();
                        tagModelArrayList.add(new SendTagModel("Prefunctory"));
                        tagModelArrayList.add(new SendTagModel("Fraud"));
                        tagModelArrayList.add(new SendTagModel("Silent"));
                        tagModelArrayList.add(new SendTagModel("Fake Gender"));
                        tagModelArrayList.add(new SendTagModel("Sex Behavious"));
                        tagModelArrayList.add(new SendTagModel("Ill-Mannered"));
                        setTags.clear();
                        adapter = new SendRatingAdapter(RateDialogActivity.this, tagModelArrayList);
                        gridview.setAdapter(adapter);
                    } else if (rating == 2.0) {
                        tagModelArrayList.clear();
                        tagModelArrayList.add(new SendTagModel("Ugly"));
                        tagModelArrayList.add(new SendTagModel("Inarticulate"));
                        tagModelArrayList.add(new SendTagModel("Boring"));
                        tagModelArrayList.add(new SendTagModel("Indecent Posture"));
                        tagModelArrayList.add(new SendTagModel("Black Screeen"));
                        tagModelArrayList.add(new SendTagModel("Hoarse Voice"));
                        setTags.clear();
                        adapter = new SendRatingAdapter(RateDialogActivity.this, tagModelArrayList);
                        gridview.setAdapter(adapter);
                    } else if (rating == 3.0) {
                        tagModelArrayList.clear();
                        tagModelArrayList.add(new SendTagModel("Off the camera"));
                        tagModelArrayList.add(new SendTagModel("Good Attitude"));
                        tagModelArrayList.add(new SendTagModel("Unattractive Voice"));
                        tagModelArrayList.add(new SendTagModel("Plain-Locking"));
                        tagModelArrayList.add(new SendTagModel("Shy"));
                        tagModelArrayList.add(new SendTagModel("Upright Shitting"));
                        setTags.clear();
                        adapter = new SendRatingAdapter(RateDialogActivity.this, tagModelArrayList);
                        gridview.setAdapter(adapter);
                    } else if (rating == 4.0) {
                        tagModelArrayList.clear();
                        tagModelArrayList.add(new SendTagModel("Beautiful Voice"));
                        tagModelArrayList.add(new SendTagModel("Young"));
                        tagModelArrayList.add(new SendTagModel("Lady"));
                        tagModelArrayList.add(new SendTagModel("A Born Beauty"));
                        tagModelArrayList.add(new SendTagModel("Elegant"));
                        tagModelArrayList.add(new SendTagModel("Sports Talent"));
                        setTags.clear();
                        adapter = new SendRatingAdapter(RateDialogActivity.this, tagModelArrayList);
                        gridview.setAdapter(adapter);
                    } else if (rating == 5.0) {
                        tagModelArrayList.clear();
                        tagModelArrayList.add(new SendTagModel("Sweet Voice"));
                        tagModelArrayList.add(new SendTagModel("Elegent"));
                        tagModelArrayList.add(new SendTagModel("Cute"));
                        tagModelArrayList.add(new SendTagModel("Charming Smile"));
                        tagModelArrayList.add(new SendTagModel("Good-Looking"));
                        tagModelArrayList.add(new SendTagModel("Fashion & Fun"));
                        setTags.clear();
                        adapter = new SendRatingAdapter(RateDialogActivity.this, tagModelArrayList);
                        gridview.setAdapter(adapter);
                    }

                }
            });

        } catch (Exception e) {
        }
    }

    private String getTags(@NonNull Stack<String> userInput) {
        HashSet<String> tags = new HashSet<>();
        while (!userInput.isEmpty()) {
            if (tags.size() < MAX_TAGS_ALLOWED) {
                tags.add(userInput.pop());
            } else {
                break;
            }
        }
        return TextUtils.join(",", tags);
    }


    /*public void closeRatingpage() {
        finish();
    }
*/
    public class EventHandler implements ApiResponseInterface {
        Context mContext;

        public EventHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void sendRating() {
            //send final formattedTags value here 8/5/21
            String formattedTags = getTags(setTags);
            if (TextUtils.isEmpty(formattedTags)) {
                //Toast.makeText(mContext, "Select one Tag ", Toast.LENGTH_SHORT).show();
                formattedTags = "";
                new ApiManager(RateDialogActivity.this, this).getUserRating(
                        binding.tvHostId.getText().toString(),
                        defaultRating,
                        formattedTags);
            } else {
                new ApiManager(RateDialogActivity.this, this).getUserRating(
                        binding.tvHostId.getText().toString(),
                        defaultRating,
                        formattedTags);
            }

        }


        @Override
        public void isError(String errorCode) {
            Toast.makeText(RateDialogActivity.this, errorCode, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void isSuccess(Object response, int ServiceCode) {
            if (ServiceCode == Constant.GET_USER_RATING) {
                RatingResponce reportResponse = (RatingResponce) response;
                if (reportResponse.getResult() != null) {
                    Toast.makeText(mContext, "Rating Submitted ", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


}