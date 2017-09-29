package in.projectmanas.manasliaison.activities;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.UploadCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import in.projectmanas.manasliaison.R;
import pub.devrel.easypermissions.EasyPermissions;

public class UploadCVActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static File getFileForUri(final Context context, final Uri uri) {
        String path = null;
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    path = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris
                        .withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                path = getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                path = getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            path = getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // File
            path = uri.getPath();
        }

        if (path != null) {
            return new File(path);
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 007:
                if (resultCode == RESULT_OK) {
                    final File file = getFileForUri(this, data.getData());
                    String fileName = null;
                    try {
                        if (file != null) {
                            fileName = URLEncoder.encode(file.getName(), "UTF-8");
                        } else {
                            Log.e("error", "Couldn't fetch file");
                            finish();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.d("File name  ", fileName);
                    final String finalFileName = fileName;
                    Backendless.Files.upload(file, "/CVs/" + getRegistrationNumber(), true, new UploadCallback() {
                        @Override
                        public void onProgressUpdate(Integer progress) {
                            //progress
                        }
                    }, new AsyncCallback<BackendlessFile>() {
                        @Override
                        public void handleResponse(BackendlessFile response) {
                            final Intent intent = new Intent();
                            intent.putExtra("statusCV", "CV uploaded successfully");
                            final String fileUrl = response.getFileURL();
                            Log.d("File uploaded", response.getFileURL());
                            Backendless.Files.renameFile("/CVs/" + getRegistrationNumber() + "/" + finalFileName, getRegistrationNumber() + ".pdf", new AsyncCallback<String>() {
                                @Override
                                public void handleResponse(String response) {
                                    Log.d("File renamed", response);
                                    intent.putExtra("urlCV", response);
                                    setResult(1, intent);
                                    finish();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Log.d("Fault", fault.toString());
                                    intent.putExtra("urlCV", fileUrl);
                                    setResult(2, intent);
                                    finish();
                                }
                            });
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            if (fault.getCode().equals("IllegalArgumentException")) {
                                final Intent intent = new Intent();
                                intent.putExtra("statusCV", "CV Already Present");
                            } else {
                                final Intent intent = new Intent();
                                intent.putExtra("statusCV", fault.getMessage());
                                setResult(3, intent);
                                finish();
                            }
                        }
                    });
                } else
                    finish();
                break;
            default:
                finish();

        }
    }

    private String getRegistrationNumber() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("regNumber", "regNumber");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_cv);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 007);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, InterviewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
