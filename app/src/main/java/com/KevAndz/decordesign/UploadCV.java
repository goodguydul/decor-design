package com.KevAndz.decordesign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.KevAndz.decordesign.controller.PrefManager;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;

public class UploadCV extends AppCompatActivity {

    //Declaring views
    private Button buttonChoose;
    private Button buttonUpload;

    private EditText editText;

    //Pdf request code
    private int PICK_PDF_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;


    //Uri to store the image uri
    private Uri filePath;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_c_v);
        requestStoragePermission();

        WebView pdfView = (WebView) findViewById(R.id.cv_view);

        final Bundle bundle = getIntent().getExtras();
        assert bundle != null;

        User user = PrefManager.getInstance(this).getUser();

        String cv_url = bundle.getString("cv_url");

        if( cv_url.equals("null") ){
            pdfView.setVisibility(View.GONE);
            Log.d("CV_URL_2",bundle.getString("cv_url") );
            Log.d("CV_URL_2", user.getCV_url());
        }else{
            pdfView.getSettings().setJavaScriptEnabled(true);
            pdfView.getSettings().setPluginState(WebSettings.PluginState.ON);;
            pdfView.getSettings().setAllowFileAccess(true);
            pdfView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + bundle.getString("cv_url"));
            Log.d("CV_URL",bundle.getString("cv_url") );
        }

        findViewById(R.id.sdBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.selectCVBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        findViewById(R.id.saveCVBtn).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

        //getting the actual path of the image

        Log.d("FILEPATH", String.valueOf(filePath));
        if (filePath == null) {
            Toast.makeText(this, "File Not Found! Try move your file to internal storage", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();
                String path = FilePath.getPath(this, filePath);
                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, "http://ddapi.ulasanproduk.com/api/upload.php")
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("id", id) //Adding text parameter to the request
                        .addParameter("action", "cv") //Adding text parameter to the request
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
                                Log.d("UPLOADERROR", String.valueOf(exception));
                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                Toast.makeText(context, "CV Uploaded!", Toast.LENGTH_SHORT).show();
                                User user = PrefManager.getInstance(context).getUser();
                                User updatedData = new User(
                                        user.getId(),
                                        user.getUsername(),
                                        user.getEmail(),
                                        user.getName(),
                                        user.getBirthdate(),
                                        user.getBirthdate(),
                                        user.getPhonenumber(),
                                        user.getProf_img_url(),
                                        "http://ddapi.ulasanproduk.com/api/uploads/cv/" + user.getId() + ".pdf",
                                        user.getUserLevel()
                                );
                                //storing new updated data in shared preferences
                                PrefManager.getInstance(getApplicationContext()).setUserLogin(updatedData);
                                setResult(Activity.RESULT_OK);
                                finish();
                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {
                                Toast.makeText(context, "Upload Cancelled!", Toast.LENGTH_SHORT).show();
//
                            }
                        }).startUpload();

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("UPLOAD_LOG",exc.getMessage());
            }
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select CV Pdf"), PICK_PDF_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
        }

    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
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

