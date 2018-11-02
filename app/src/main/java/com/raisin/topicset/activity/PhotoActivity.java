package com.raisin.topicset.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoActivity extends Activity {
    // 拍照
    private final int PHOTO_REQUEST_CAREMA = 1001;
    // 相册
    private final int PHOTO_REQUEST_ALBUM = 1002;
    // 编辑
    private final int PHOTO_REQUEST_CUT = 1003;

    private Uri imageUri;
    private File mPhotoFile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String picPath = null;

//        LogUtils.e("onActivityResult","exe onActivityResult()");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_CAREMA: // 拍照
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    break;

                case PHOTO_REQUEST_ALBUM: // 相册Uri selectedImage = data.getData();
//                    if (data != null) {
//                        Uri uri = data.getData();
//                        crop(uri);
//                        picPath = getRealPathFromURI(uri);
//                        LogUtils.e("onActivityResult","filePath="+filePath);
//                    }
//                    LogUtil.e("onActivityResult", "相册 picPath= empty");
                    break;
            }
        }
    }

    private void openCamera(Activity activity) {
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可以用，可用进行存储
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            String filename = timeStampFormat.format(new Date());
//            MakeFile.makeRootDirectory(Environment.getExternalStorageDirectory().getPath() + "/Android/photo");
            mPhotoFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/photo", filename + ".jpg");
            if (Build.VERSION.SDK_INT < 24) { // 系统版本:6.0.1
                // 从文件中创建uri
                imageUri = Uri.fromFile(mPhotoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, mPhotoFile.getAbsolutePath());
                // 检查是否有存储权限，以免崩溃
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // 申请WRITE_EXTERNAL_STORAGE权限
                    Toast.makeText(this,"请开启存储权限",Toast.LENGTH_SHORT).show();
                    return;
                }
                imageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        activity.startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }

    // 打开相册
    private void OpenAlbum() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) { // 系统版本:4.4.4
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else { // 系统版本:4.4.4以上版本
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }

        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PHOTO_REQUEST_ALBUM);
    }

    // 打开相机
    private void OpenCamera(File out) {
//        LogUtil.e("openCamera", "mPhotoFile=" + mPhotoFile.toString());
        try {
            // 创建拍照Intent并将控制权返回给调用的程序
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            // 创建保存图片的文件
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out));
            // 启动图像捕获Intent
            startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
        } catch (Exception e) {
            e.printStackTrace();
//            ToastUtil.showLongToast(mContext,"当前无拍照权限，请在 设置-> 应用权限-> "+ mContext.getString           (R.string.app_name)+"-> 权限-> 打开相机权限","");
        } finally {
        }
    }

    // 判断相机是否支持
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature( PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

}
