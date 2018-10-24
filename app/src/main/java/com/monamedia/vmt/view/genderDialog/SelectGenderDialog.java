package com.monamedia.vmt.view.genderDialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.monamedia.vmt.R;
import com.monamedia.vmt.model.AccountDto;

public class SelectGenderDialog extends BottomSheetDialog {
    private SelectGenderCallBack callBack;

    public SelectGenderDialog(@NonNull Context context, SelectGenderCallBack callBack) {
        super(context);
        this.callBack = callBack;
        init(context);
    }

    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_gender_layout);

        Button btnTake = (Button) findViewById(R.id.btnTake);
        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (callBack != null) callBack.onSelect(AccountDto.MALE);
            }
        });

        Button btnChoose = (Button) findViewById(R.id.btnChoose);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (callBack != null) callBack.onSelect(AccountDto.FEMALE);
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
