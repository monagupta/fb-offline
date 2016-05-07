package com.example.mona.facebookoffline.util;

import android.content.Context;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mona on 5/7/16.
 */
public class BitmapUtil {

    // TODO(mona): Make non-static and solve through injection instead?
    public static byte[] getBytesFromUri(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        return getBytes(inputStream);
    }

    private static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int buffersize = 1024;
        byte[] buffer =  new byte[buffersize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
