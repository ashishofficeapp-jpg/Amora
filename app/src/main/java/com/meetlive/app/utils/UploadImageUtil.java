package com.meetlive.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

public class UploadImageUtil {

    String currentPhotoPath = "";
    int maxWidth = 600, maxHeight = 600;

    public File getImageFile() {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        );
        File file = null;
        try {
            file = File.createTempFile(
                    imageFileName, ".jpg", storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;
    }

    public void openCropActivity(Uri sourceUri, Uri destinationUri, Context context) {
/*        UCrop.of(sourceUri, destinationUri)
                .withMaxResultSize(maxWidth, maxHeight)
                .useSourceImageAspectRatio()
                .start((Activity) context);*/

        //  Uri s = Uri.parse(compressImage(sourceUri.getPath(), context));

        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(50);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setMaxBitmapSize(100);

        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start((Activity) context);
    }
}
