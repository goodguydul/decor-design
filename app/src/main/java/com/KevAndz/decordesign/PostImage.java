package com.KevAndz.decordesign;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.KevAndz.decordesign.controller.PrefManager;
import com.KevAndz.decordesign.controller.URLS;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.IOException;
import java.util.UUID;

public class PostImage extends AppCompatActivity {
    ProgressBar progressBar;
    private static final String TAG = "AndroidUploadService";

    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Uri to store the image uri
    private Uri filePath;
    private ImageView imageView;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_image);
        imageView = findViewById(R.id.profilePict);
        progressBar = findViewById(R.id.progressBar);



        findViewById(R.id.sdBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        findViewById(R.id.profilePict).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission();
                showFileChooser();
            }
        });

        findViewById(R.id.saveProfilePictureBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadMultipart();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadMultipart() {
        final Bundle bundle = getIntent().getExtras();
        String id = String.valueOf(bundle.getInt("user_id", 0));
        EditText caption = findViewById(R.id.captionText);

        if (filePath == null){
            Toast.makeText(this, "Select photo first!", Toast.LENGTH_SHORT).show();
        }else{
            //Uploading code
            Log.d("SAVEBTN", "Clicked");
            try {
                //getting the actual path of the image
                String path = FilePath.getPath(this,filePath);
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                assert bundle != null;
                new MultipartUploadRequest(this, uploadId, "http://ddapi.ulasanproduk.com/api/upload.php")
                        .addFileToUpload(path, "image") //Adding file
                        .addParameter("id", id) //Adding text parameter to the request
                        .addParameter("action", "post")
                        .addParameter("caption", caption.getText().toString())
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {  //Add these lines to get upload status
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {
                                progressBar.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse,
                                                Exception exception) {
                                Toast.makeText(context, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                                Log.d("UPLOADERROR", String.valueOf(exception));
                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(context, "Profile Photo Uploaded!", Toast.LENGTH_SHORT).show();

                                setResult(Activity.RESULT_OK);
                                finish();
                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {
                                Toast.makeText(context, "Upload Cancelled!", Toast.LENGTH_SHORT).show();
//
                            }
                        }).startUpload();
//                        .startUpload(); //Starting the upload


            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, exc.getMessage());
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int PICK_IMAGE_REQUEST = 1;
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                //Bitmap to get image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

}