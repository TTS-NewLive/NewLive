package com.xiaoyu.schoolelive.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.xiaoyu.schoolelive.R;
import com.xiaoyu.schoolelive.util.TouchListener;

/**
 * Created by Administrator on 2017/7/11.
 */
public class CustomImageDialogView extends Dialog {

    public CustomImageDialogView(Context context) {
        super(context);
    }

    public CustomImageDialogView(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private Bitmap image;

        public Builder(Context context) {
            this.context = context;
        }

        public Bitmap getImage() {
            return image;
        }

        public void setImage(Bitmap image) {
            this.image = image;
        }

        public CustomImageDialogView create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomImageDialogView dialog = new CustomImageDialogView(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.custom_image_dialog, null);
            dialog.addContentView(layout, new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
            dialog.setContentView(layout);
            ImageView img = (ImageView) layout.findViewById(R.id.dialog_img);
            img.setOnTouchListener(new TouchListener(img));
            img.setImageBitmap(getImage());
            return dialog;
        }
    }
}
