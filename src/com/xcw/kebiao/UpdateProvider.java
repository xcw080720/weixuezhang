package com.xcw.kebiao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 只读 ContentProvider，把 cache/update.apk 暴露成 content:// URI 交给系统安装器。
 * Android 7+ 禁止把 file:// 直接交给别的应用（FileUriExposedException），又不想引入
 * AndroidX 的 FileProvider，所以自己写这一层。
 */
public class UpdateProvider extends ContentProvider {

    public static final String AUTHORITY = "com.xcw.kebiao.updatefile";
    public static final String APK_NAME = "update.apk";
    public static final String URI_APK = "content://" + AUTHORITY + "/" + APK_NAME;

    @Override
    public boolean onCreate() { return true; }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        if (getContext() == null) throw new FileNotFoundException("no context");
        File f = new File(getContext().getCacheDir(), APK_NAME);
        if (!f.exists()) throw new FileNotFoundException("no update apk");
        return ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_ONLY);
    }

    @Override
    public String getType(Uri uri) {
        return "application/vnd.android.package-archive";
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) { return null; }

    @Override
    public Uri insert(Uri uri, ContentValues values) { return null; }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) { return 0; }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) { return 0; }
}
