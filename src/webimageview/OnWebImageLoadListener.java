package webimageview;

import android.graphics.Bitmap;

public interface OnWebImageLoadListener {
        public void onWebImageLoad(String url, Bitmap bitmap);
        public void onWebImageError();
    }