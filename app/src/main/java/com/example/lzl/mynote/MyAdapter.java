package com.example.lzl.mynote;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class MyAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private LinearLayout layout;

    public MyAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        layout = (LinearLayout) inflater.inflate(R.layout.cell, null);
        TextView contenttv = (TextView) layout.findViewById(R.id.list_text);
        TextView timetv = (TextView) layout.findViewById(R.id.list_time);
        ImageView imgiv = (ImageView) layout.findViewById(R.id.list_img);
        ImageView videoiv = (ImageView) layout.findViewById(R.id.list_video);
        cursor.moveToPosition(position);
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        String url = cursor.getString(cursor.getColumnIndex("path")); // 如果是绝对路径，能够定位的，那么用imgUrl是没问题的，而如果是相对路径，那还是不要用ImgUrl的好
        String urlvideo = cursor.getString(cursor.getColumnIndex("video"));
        contenttv.setText(content);
        imgiv.setImageBitmap(getImageThumbnail(url, 200, 200));
        videoiv.setImageBitmap(getVideoThumbnail(urlvideo, 200, 200,
                MediaStore.Images.Thumbnails.MICRO_KIND));
        timetv.setText(time);
        return layout;

    }

    public Bitmap getImageThumbnail(String uri, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options(); // 直接获取缩略图
        // Options中有个属性inJustDecodeBounds。我们可以充分利用它，来避免大图片的溢出问题
        options.inJustDecodeBounds = true; // 配对出现的过程，设置为true可以不加载到内存，直接获取Bitmap宽高
        bitmap = BitmapFactory.decodeFile(uri, options);
        // 获取这张图

        int beWidth = options.outWidth / width; // options.outWidth为获取Bitmap的实际高度，beWidth为压缩的比例计算
        int beHeight = options.outHeight / height;
        int be = 1; // 作出判断，以防跑出界外,计算压缩的比例
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) { // //图片实际大小小于缩略图,不缩放
            be = 1;
        }
        options.inSampleSize = be; // be为压缩的比例，inSampleSize获取的bitmap会按照这个压缩的比例进行压缩，宽或高=宽/be,像素压缩为be的平方
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(uri, options);// 重新获得bitmap图片，此时就是缩略图之后的图片
        // /利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);// /创建所需尺寸居中缩放的位图
        return bitmap;
    }

    private Bitmap getVideoThumbnail(String uri, int width, int height, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(uri, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}

