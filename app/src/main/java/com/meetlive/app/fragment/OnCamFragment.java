package com.meetlive.app.fragment;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.meetlive.app.R;
import com.meetlive.app.activity.MainActivity;
import com.meetlive.app.activity.OnCamFriendsActivity;
import com.meetlive.app.databinding.FragmentOnCamBinding;
import com.meetlive.app.response.OnCamResponse;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.utils.Constant;

import java.io.IOException;
import java.util.NoSuchElementException;

@SuppressWarnings("deprecation")
public class OnCamFragment extends Fragment implements ApiResponseInterface, SurfaceHolder.Callback {
    //Field declaration
    public static final String USER_ID = "user_id";
    public static final String LIKED_GIRL_IMG_URL = "imgURL";
    public static final String LIKED_GIRL_NAME = "name";
    FragmentOnCamBinding binding;
    ApiManager apiManager;
    OnCamResponse response;
    SurfaceHolder surfaceHolder;
    int totalPages, currentPage;
    String bgImageURL;
    boolean isHeartSplashing;
    Transition transition;
    private Camera camera;
    String userId;

   //int CAMERA_REQUEST_CODE = 1;

    public OnCamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_on_cam, container, false);
        currentPage = -1;
        isHeartSplashing = false;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //creating transition
        transition = new Fade();
        transition.setDuration(300);

        //startBtn onClick listener that makes the instructions layout visibility gone.
        (binding.oncamInstructions).startBtn.setOnClickListener(view1 -> {
            transition.addTarget(R.id.oncam_instructions);
            TransitionManager.beginDelayedTransition(binding.rootLayout, transition);
            binding.oncamInstructions.rootLayout.setVisibility(View.GONE);

            (binding.oncamConnecting.pumpingHeart).playAnimation();
            (binding.oncamConnecting.circularWaves).playAnimation();
            (binding.oncamConnecting.pumpingHeart).setRepeatCount(LottieDrawable.INFINITE);
            (binding.oncamConnecting.circularWaves).setRepeatCount(LottieDrawable.INFINITE);
            FetchGirlsData();
        });
        //nextBtn onClick listener
        binding.onCamConnected.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalPages == (currentPage + 1)) {
                    Toast.makeText(getContext(), "No more girls available online", Toast.LENGTH_LONG).show();
                } else {
                    binding.onCamConnected.placeHolder.setVisibility(View.VISIBLE);
                    binding.onCamConnected.linearProgress.pauseAnimation();
                    binding.onCamConnected.linearProgress.setProgress(0);
                    binding.onCamConnected.linearProgress1.pauseAnimation();
                    binding.onCamConnected.linearProgress1.setProgress(0);
                    setData();
                }
            }
        });
        //heartSplash onClick listener that open a full screen dialog box
        (binding.onCamConnected).heartSplash.setOnClickListener(view12 -> {
            if (!isHeartSplashing) {
                isHeartSplashing = true;
                binding.onCamConnected.heartSplash.playAnimation();
            }
        });
        //heartSplash animatorListener
        binding.onCamConnected.heartSplash.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                try {
                    camera.stopPreview();
                    binding.onCamConnected.linearProgress.pauseAnimation();
                    binding.onCamConnected.linearProgress1.pauseAnimation();
                } catch (Exception e) {
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                try {
                    Intent intent = new Intent(getActivity(), OnCamFriendsActivity.class);
                    intent.putExtra(USER_ID, userId = String.valueOf(response.getResult().get(currentPage).getUser_id()));
                    intent.putExtra(LIKED_GIRL_IMG_URL, bgImageURL);
                    intent.putExtra(LIKED_GIRL_NAME, binding.onCamConnected.callerName.getText().toString());
                    intent.putExtra("tokenUserId", String.valueOf(response.getResult().get(currentPage).getUser().getProfile_id()));
                    startActivity(intent);
                    isHeartSplashing = false;
                    binding.onCamConnected.placeHolder.setVisibility(View.VISIBLE);
                    binding.onCamConnected.videoView.pause();

                    ((MainActivity) getActivity()).loadHomeFragment();

                } catch (Exception e) {
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        binding.onCamConnected.linearProgress.addValueCallback(new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    }
                });

        binding.onCamConnected.linearProgress1.addValueCallback(new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    }
                });

        //linear progress animatorListener
        binding.onCamConnected.linearProgress.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                (binding.onCamConnected.placeHolder).setVisibility(View.VISIBLE);
                binding.onCamConnected.linearProgress.setProgress(1);
                binding.onCamConnected.linearProgress1.setProgress(1);
                binding.onCamConnected.videoView.stopPlayback();
                setData();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        //VideoView listeners
        binding.onCamConnected.videoView.setOnPreparedListener(mp -> {
            //Log.e("m here", "OnPreparedListener!");
            //if (binding.onCamConnected.linearProgress.getProgress() == 0 || binding.onCamConnected.linearProgress.getProgress() == 1) {
            //Log.e("m here", "in it!");

            try {
                binding.onCamConnected.placeHolder.setVisibility(View.GONE);
                (binding.onCamConnected.linearProgress).playAnimation();
                (binding.onCamConnected.linearProgress1).playAnimation();
                mp.setVolume(0f, 0f);
                mp.start();
            } catch (Exception e) {
            }


        });
        binding.onCamConnected.videoView.setOnErrorListener((mp, what, extra) -> {
            //Log.e("m here", "OnErrorListener!");
            setData();
            return true;
        });

        binding.onCamConnected.videoView.setOnCompletionListener(mp -> {
            //Log.e("m here", "OnCompletionListener!");
            mp.stop();
            (binding.onCamConnected.placeHolder).setVisibility(View.VISIBLE);
            binding.onCamConnected.nextBtn.performClick();
        });

        // getCameraPermission();
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((binding.oncamConnecting.rootLayout).getVisibility() == View.GONE) {
            showCameraPreview();
            binding.onCamConnected.heartSplash.setProgress(0);
            binding.onCamConnected.linearProgress.resumeAnimation();
            binding.onCamConnected.linearProgress1.resumeAnimation();
            binding.onCamConnected.placeHolder.setVisibility(View.GONE);
            binding.onCamConnected.videoView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Toast.makeText(getContext(), "m in pause mode", Toast.LENGTH_SHORT).show();
        //((MainActivity) getActivity()).loadHomeFragment();
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
   //     ((MainActivity) getActivity()).loadHomeFragment();
    }*/

    private void FetchGirlsData() {
        if (apiManager == null)
            apiManager = new ApiManager(getContext(), this);
        apiManager.getVideoList();
    }


    private void setData() {
        if (totalPages == (currentPage + 1)) {
            noMoreGirlsOnline();
            return;
        }
        currentPage++;
        //setting name
        binding.onCamConnected.callerName.setText(response.getResult().get(currentPage).getUser().getName());
        //settings bg image
        if (response.getResult().get(currentPage).getUser().getProfile_images() != null &&
                response.getResult().get(currentPage).getUser().getProfile_images().size() > 0) {

            bgImageURL = response.getResult().get(currentPage).getUser().getProfile_images().get(0).getImage_name();
            if (!bgImageURL.equals("")) {
                Glide.with(getContext())
                        .load(bgImageURL)
                        .apply(new RequestOptions().placeholder(R.drawable.female_placeholder).error(R.drawable.female_placeholder))
                        .into(binding.onCamConnected.backgroundImage);
            }
        }
        //setting video
        setVideoViewData(response.getResult().get(currentPage).getVideo_name());

        if (currentPage == 0)
            prepareCamera();
    }

    void setVideoViewData(String video_name) {
        if (video_name != null && video_name.length() > 1) {
            //Log.e("videoUrl", video_name);
            binding.onCamConnected.videoView.setVideoPath(video_name);

         /*   binding.onCamConnected.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setVolume(0f, 0f);
                    mp.setLooping(true);
                }
            });
            binding.onCamConnected.videoView.start();*/
            //   binding.onCamConnected.videoView.setVideoPath("https://www.infinityandroid.com/videos/video3.mp4");
        }
    }

    private void prepareCamera() {
        transition.addTarget(R.id.oncam_connecting);
        TransitionManager.beginDelayedTransition(binding.rootLayout, transition);
        binding.oncamConnecting.rootLayout.setVisibility(View.GONE);

        (binding.oncamConnecting.pumpingHeart).pauseAnimation();
        (binding.oncamConnecting.circularWaves).pauseAnimation();
        surfaceHolder = (binding.onCamConnected.surfaceView).getHolder();
        surfaceHolder.addCallback(this);
        try {
            camera = getFrontFacingCamera();
            surfaceCreated(surfaceHolder);
        } catch (Exception e) {
            e.printStackTrace();
            getCameraPermission();
            //Toast.makeText(getContext(), "Camera access denied :(", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCameraPreview() {
        surfaceCreated(surfaceHolder);
    }

    public Camera getFrontFacingCamera() throws NoSuchElementException {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int cameraIndex = 0; cameraIndex < Camera.getNumberOfCameras(); cameraIndex++) {
            Camera.getCameraInfo(cameraIndex, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    return Camera.open(cameraIndex);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new NoSuchElementException("Can't find front camera.");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(640, 480);
            parameters.setPreviewFrameRate(24);
            camera.setDisplayOrientation(90);
            camera.setParameters(parameters);
            camera.setPreviewDisplay(holder);
            camera.stopFaceDetection();
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException r) {
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    private void noMoreGirlsOnline() {
        binding.onCamConnected.linearProgress.pauseAnimation();
        binding.onCamConnected.linearProgress.setProgress(1);
        binding.onCamConnected.linearProgress1.pauseAnimation();
        binding.onCamConnected.linearProgress1.setProgress(1);
        Toast.makeText(getContext(), "No more girls available online", Toast.LENGTH_LONG).show();
    }

    private void noDataFetched() {
        transition.addTarget(R.id.oncam_instructions);
        TransitionManager.beginDelayedTransition(binding.rootLayout, transition);
        binding.oncamInstructions.rootLayout.setVisibility(View.VISIBLE);
        binding.oncamConnecting.circularWaves.pauseAnimation();
        binding.oncamConnecting.pumpingHeart.pauseAnimation();
        binding.oncamConnecting.circularWaves.setProgress(0);
        binding.oncamConnecting.pumpingHeart.setProgress(0);
    }

    private void getCameraPermission() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
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
    public void isSuccess(Object response, int ServiceCode) {
        if (ServiceCode == Constant.GET_ONCAM_LIST) {
            this.response = (OnCamResponse) response;
            // Log.e("videoList", new Gson().toJson(response));
            totalPages = ((OnCamResponse) response).getResult().size();
            if (totalPages == 0) {
                noDataFetched();
                Toast.makeText(getContext(), "No girls available online right now", Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(getContext(), totalPages + " girls online", Toast.LENGTH_SHORT).show();
                setData();
            }
        }
    }

    @Override
    public void isError(String errorCode) {
        noDataFetched();
    }
}

/*
public void autoSwipeViewpager(int currentPage) {
        if (currentPage == NUM_PAGES - 1) {
            currentPage = 0;
            binding.viewpager.setCurrentItem(currentPage, true);
        } else {
            currentPage = currentPage + 1;
            binding.viewpager.setCurrentItem(currentPage, true);
        }
*/



/*
After setting the adapter use the timer */
       /* final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES - 1) {
                    currentPage = 0;
                }
                binding.viewpager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
*/


  /*
   Glide.with(context)
                        .load(result.getVideo_thumbnail())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_photo_library_gray_100dp).error(R.drawable.ic_photo_library_gray_100dp))
                        .into(imageView);
        }
    }
*/


 /*
  @Override
    public void isSuccess(Object response, int ServiceCode) {

        if (ServiceCode == Constant.GET_ONCAM_LIST) {
            OnCamResponse rsp = (OnCamResponse) response;
            // List<OnCamResponse.ResultDataNewProfile> list = rsp.getResult();
            List<OnCamResponse.ResultDataNewProfile> list = new ArrayList<>();

            list.add(new OnCamResponse.ResultDataNewProfile(1, "https://www.infinityandroid.com/videos/video1.mp4",
                    "https://www.infinityandroid.com/videos/video1.mp4", null));
            list.add(new OnCamResponse.ResultDataNewProfile(2, "https://www.infinityandroid.com/videos/video2.mp4",
                    "https://www.infinityandroid.com/videos/video2.mp4", null));
            list.add(new OnCamResponse.ResultDataNewProfile(3, "https://www.infinityandroid.com/videos/video3.mp4",
                    "https://www.infinityandroid.com/videos/video3.mp4", null));
            list.add(new OnCamResponse.ResultDataNewProfile(4, "https://www.infinityandroid.com/videos/video4.mp4",
                    "https://www.infinityandroid.com/videos/video4.mp4", null));
            list.add(new OnCamResponse.ResultDataNewProfile(5, "https://www.infinityandroid.com/videos/video5.mp4",
                    "https://www.infinityandroid.com/videos/video5.mp4", null));

            if (list != null && list.size() > 0) {
                binding.viewpager.setAdapter(new OnCamAdapter(this, list));
                NUM_PAGES = list.size();
            }
        }
    }
}
*/


