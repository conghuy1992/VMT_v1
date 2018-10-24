package com.monamedia.vmt.view.photosDialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.monamedia.vmt.R;

public class SelectPhotoDialog extends BottomSheetDialog {
    private SelectPhotoCallBack callBack;

    public SelectPhotoDialog(@NonNull Context context, SelectPhotoCallBack callBack) {
        super(context);
        this.callBack = callBack;
        init(context);
    }

    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_photo_layout);

        Button btnTake = (Button) findViewById(R.id.btnTake);
        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (callBack != null) callBack.onCamera();
            }
        });

        Button btnChoose = (Button) findViewById(R.id.btnChoose);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (callBack != null) callBack.onSelectPhoto();
            }
        });
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
