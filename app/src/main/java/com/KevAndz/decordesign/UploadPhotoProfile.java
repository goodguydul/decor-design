package com.KevAndz.decordesign;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.KevAndz.decordesign.controller.PrefManager;
import com.KevAndz.decordesign.controller.URLS;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.IOException;
import java.util.UUID;

public class UploadPhotoProfile extends AppCompatActivity {

    private static final String TAG = "AndroidUploadService";

    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Uri to store the image uri
    private Uri filePath;

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upload_photo_profile);
        imageView = (ImageView) findViewById(R.id.profilePict);

        findViewById(R.id.sdBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.profilePict).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    public void uploadMultipart() {
        final Bundle bundle = getIntent().getExtras();
        String id = String.valueOf(bundle.getInt("user_id", 0));

        if (filePath == null){
            Toast.makeText(this, "Select photo first!", Toast.LENGTH_SHORT).show();
        }else{
            //Uploading code
            Log.d("SAVEBTN", "Clicked");
            try {
                //getting the actual path of the image
                String path = getPath(filePath);
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                assert bundle != null;
                new MultipartUploadRequest(this, uploadId, "https://ddapi.000webhostapp.com/api/upload.php")
                        .addFileToUpload(path, "image") //Adding file
                        .addParameter("id", id) //Adding text parameter to the request
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {  //Add these lines to get upload status
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {

                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse,
                                                Exception exception) {
                                Toast.makeText(context, "Something Went Wrong!", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                Toast.makeText(context, "Profile Photo Uploaded!", Toast.LENGTH_SHORT).show();
                                User user = PrefManager.getInstance(context).getUser();
                                User updatedData = new User(
                                        user.getId(),
                                        user.getUsername(),
                                        user.getEmail(),
                                        user.getName(),
                                        user.getBirthdate(),
                                        user.getBirthdate(),
                                        user.getPhonenumber(),
                                        "https://ddapi.000webhostapp.com/api/uploads/" + user.getId() + ".jpg",
                                        user.getUserLevel()
                                );
                                //storing new updated data in shared preferences
                                PrefManager.getInstance(getApplicationContext()).setUserLogin(updatedData);
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
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
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

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
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