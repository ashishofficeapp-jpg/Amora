package com.amora.app;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.amora.app.activity.SocialLogin.currentVersion;


public class GetAppVersion extends AsyncTask<Void, String, String> {
    private Activity ctx;
    Dialog dialog;

    public GetAppVersion(Activity ctx) {
        this.ctx = ctx;
        dialog = new Dialog(ctx);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String newVersion = null;
        try {
            Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + ctx.getPackageName() + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
            if (document != null) {
                Elements element = document.getElementsContainingOwnText("Current Version");
                for (Element ele : element) {
                    if (ele.siblingElements() != null) {
                        Elements sibElemets = ele.siblingElements();
                        for (Element sibElemet : sibElemets) {
                            newVersion = sibElemet.text();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newVersion;

    }


    @Override
    protected void onPostExecute(String onlineVersion) {
        super.onPostExecute(onlineVersion);
        if (onlineVersion != null && !onlineVersion.isEmpty()) {

            Log.e("version", "appVersion " + onlineVersion);
            if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                UpdateMeeDialog updateMeeDialog = new UpdateMeeDialog();
                updateMeeDialog.showDialogAddRoute(ctx);
                Log.d("updateDialog", "Current version " + currentVersion + " playstore version " + onlineVersion);
            }

        }

         Log.d("updateDialog", "Current version " + currentVersion + "  playstore version " + onlineVersion);

    }


    public class UpdateMeeDialog {
        private Button buttonUpdate;
        ConstraintLayout parentLayout;
        CircleImageView onCancel;

        public void showDialogAddRoute(Activity context) {
            dialog.setContentView(R.layout.dialog_update);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationWindMill;

            buttonUpdate = dialog.findViewById(R.id.buttonUpdate);
            onCancel = dialog.findViewById(R.id.img_Cancel);
            parentLayout = dialog.findViewById(R.id.parentLayout);
            dialog.show();

            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.zeeplive.app")));
                    dialog.dismiss();
                }
            });

            onCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }



    }


}