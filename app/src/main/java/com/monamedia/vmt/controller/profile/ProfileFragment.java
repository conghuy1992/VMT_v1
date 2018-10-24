package com.monamedia.vmt.controller.profile;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.monamedia.vmt.MainActivity;
import com.monamedia.vmt.R;
import com.monamedia.vmt.common.HttpRequest;
import com.monamedia.vmt.common.ImageLoader;
import com.monamedia.vmt.common.PrefManager;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.common.interfaces.AccountCallBack;
import com.monamedia.vmt.common.interfaces.CommonModelCallBack;
import com.monamedia.vmt.common.interfaces.DateCallback;
import com.monamedia.vmt.common.interfaces.HttpRequestCallBack;
import com.monamedia.vmt.common.interfaces.Statics;
import com.monamedia.vmt.databinding.ProfileFragmentBinding;
import com.monamedia.vmt.model.AccountDto;
import com.monamedia.vmt.model.CommonModel;
import com.monamedia.vmt.model.HttpRequestDto;
import com.monamedia.vmt.view.genderDialog.SelectGenderCallBack;
import com.monamedia.vmt.view.genderDialog.SelectGenderDialog;
import com.monamedia.vmt.view.photosDialog.SelectPhotoCallBack;
import com.monamedia.vmt.view.photosDialog.SelectPhotoDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Android on 4/11/2018.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private ProfileFragmentBinding binding;
    private String TAG = "ProfileFragment";

    public ProfileFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.profile_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.content.btnReload) {
            Get_Info();
        } else if (v == binding.btnSubmit) {
            submit();
        } else if (v == binding.btnEdit) {
            requestCameraPermission();
        } else if (v == binding.birthday.itemClick) {
            birthdaySelect();
        } else if (v == binding.gender.itemClick) {
            genderDialog();
        }
    }

    public void bind() {
        binding.info.setVisibility(View.GONE);
        binding.load.setVisibility(View.VISIBLE);
        binding.username.iv.setImageResource(R.drawable.ic_username);
        binding.email.iv.setImageResource(R.drawable.ic_email);
        binding.address.iv.setImageResource(R.drawable.ic_address);
        binding.birthday.iv.setImageResource(R.drawable.ic_birthday);
        binding.gender.iv.setImageResource(R.drawable.ic_gender);
        binding.lastFirstName.iv.setImageResource(R.drawable.ic_firstname);

        binding.username.tvTitle.setText(Utils.getMsg(getActivity(), R.string.username));
        binding.username.ed.setEnabled(false);
        binding.address.tvTitle.setText(Utils.getMsg(getActivity(), R.string.address));
        binding.address.ed.setHint(Utils.getMsg(getActivity(), R.string.address));
        binding.email.tvTitle.setText(Utils.getMsg(getActivity(), R.string.email));
        binding.email.ed.setEnabled(false);
        binding.birthday.tvTitle.setText(Utils.getMsg(getActivity(), R.string.birthday));
        binding.birthday.ed.setEnabled(false);
        binding.gender.tvTitle.setText(Utils.getMsg(getActivity(), R.string.gender));
        binding.gender.ed.setEnabled(false);
        binding.lastFirstName.tvFN.setText(Utils.getMsg(getActivity(), R.string.first_name));
        binding.lastFirstName.edFN.setHint(Utils.getMsg(getActivity(), R.string.first_name));
        binding.lastFirstName.tvLN.setText(Utils.getMsg(getActivity(), R.string.last_name));
        binding.lastFirstName.edLN.setHint(Utils.getMsg(getActivity(), R.string.last_name));

//        binding.lastFirstName.root.setBackgroundColor(Utils.getColor(getActivity(), R.color.ed_color_background));
//        binding.email.root.setBackgroundColor(Utils.getColor(getActivity(), R.color.ed_color_background));
//        binding.gender.root.setBackgroundColor(Utils.getColor(getActivity(), R.color.ed_color_background));
        binding.lastFirstName.root.setBackgroundResource(R.drawable.border_view_gray);
        binding.email.root.setBackgroundResource(R.drawable.border_view_gray);
        binding.gender.root.setBackgroundResource(R.drawable.border_view_gray);

        String iMGUser = new PrefManager(getActivity()).iMGUser();
        if (iMGUser.trim().length() > 0) {
            Utils.loadImageFromURL(getActivity(), Statics.ROOT_URL_FOR_IMAGE + iMGUser, binding.iv);
        }
        binding.content.btnReload.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.btnEdit.setOnClickListener(this);
        binding.birthday.itemClick.setOnClickListener(this);
        binding.gender.itemClick.setOnClickListener(this);
        processLoading(false);
        stopLoading("");
        Get_Info();

    }

    private void startLoading() {
        binding.content.tvNodata.setVisibility(View.GONE);
        binding.content.btnReload.setVisibility(View.GONE);
        binding.content.progressBar.setVisibility(View.VISIBLE);
    }

    private void stopLoading(String msg) {
        if (getActivity() == null) return;
        binding.content.tvNodata.setVisibility(View.GONE);
        binding.content.btnReload.setVisibility(View.GONE);
        binding.content.progressBar.setVisibility(View.GONE);
        if (msg != null && msg.length() > 0) Utils.showMsg(getActivity(), msg);
    }

    private void Get_Info() {
        startLoading();
        Map<String, String> params = new HashMap<>();
        params.put("UID", "" + new PrefManager(getActivity()).userId());
        Log.d(TAG, "Get_Info params:" + new Gson().toJson(params));
        new HttpRequest().Get_Info(getActivity(),
                Statics.POST,
                params,
                Statics.Get_Info,
                new AccountCallBack() {
                    @Override
                    public void onSuccess(AccountDto accountDto) {
                        if (getActivity() == null) return;
//                        String msg = accountDto == null
//                                || accountDto.Message == null
//                                || accountDto.Message.length() == 0 ? "Success" : accountDto.Message;
//                        stopLoading(msg);
                        displayInfo(accountDto);
                    }

                    @Override
                    public void onFail(AccountDto accountDto) {
                        if (getActivity() == null) return;
//                        String msg = accountDto == null
//                                || accountDto.Message == null
//                                || accountDto.Message.length() == 0 ? "fail" : accountDto.Message;
//                        stopLoading(msg);
                        binding.content.tvNodata.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVolleyError(VolleyError error) {
                        if (getActivity() == null) return;
                        stopLoading(error.toString());
                        binding.content.btnReload.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void displayInfo(AccountDto accountDto) {
        accountDto.UserName = accountDto.Username;
        binding.info.setVisibility(View.VISIBLE);
        binding.load.setVisibility(View.GONE);
//
        binding.username.ed.setText(accountDto.UserName == null ? "" : accountDto.UserName);
        binding.lastFirstName.edFN.setText(accountDto.FirstName == null ? "" : accountDto.FirstName);
        binding.lastFirstName.edLN.setText(accountDto.LastName == null ? "" : accountDto.LastName);
        binding.address.ed.setText(accountDto.Address == null ? "" : accountDto.Address);
        binding.email.ed.setText(accountDto.Email == null ? "" : accountDto.Email);
        binding.birthday.ed.setText(Utils.formatTime(Utils.getTimeMilisecondFromDate(accountDto.BirthDay == null ? "" : accountDto.BirthDay, Statics.yyyy_MM_dd_T_HH_mm_ss), Statics.DATE_FORMAT_DD_MM_YYYY));
        binding.gender.ed.setText(AccountDto.getName(accountDto.Gender));

        String name = accountDto.FirstName == null ? "" : accountDto.FirstName + " ";
        name += accountDto.LastName == null ? "" : accountDto.LastName;
        binding.tvName.setText(name);
    }

    private void requestCameraPermission() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            showDialog();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            Utils.showMsg(getActivity(), "permission denied");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    public void showDialog() {
        new SelectPhotoDialog(getActivity(), new SelectPhotoCallBack() {
            @Override
            public void onCamera() {
                openCamera();
            }

            @Override
            public void onSelectPhoto() {
                choosePicture();
            }
        }).show();
    }

    private Uri uri;
    private int REQUEST_IMAGE = 3;
    private int PICK_IMAGE_REQUEST = 2;

    private void bindRequestCamera() {
        if (uri != null) {
            try {
                String path = ImageLoader.getPathFromURI(uri, getActivity());
                if (path == null || path.length() == 0) return;
                Log.d(TAG, "path:" + path);
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                updateAdapter(path, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void bindRequestSelectImage(Intent data) {
        Uri uri = data.getData();
        Log.d(TAG, "uri:" + uri);
        String path = ImageLoader.getRealPathFromURI(getActivity(), uri);
        Log.d(TAG, "path:" + path);
        if (path != null && path.length() > 0 && !path.startsWith("http")) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                updateAdapter(path, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
//                Const.showMsg(getActivity(), R.string.can_not_get_path_picture);
        }
    }

    private String pathImageUpload = "";

    private void updateAdapter(String path, Bitmap bitmap) {
        if (bitmap == null) return;
        Log.d(TAG, "bitmap w:" + bitmap.getWidth());
        Log.d(TAG, "bitmap h:" + bitmap.getHeight());
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        metrics.heightPixels;
//        metrics.widthPixels;
        int width = metrics.widthPixels;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        if (width != 0) {
            float percent = (width * 1f) / (w * 1f);
            w = width;
            h = (int) (h * percent);
            Log.d(TAG, "after resize w:" + w + " h:" + h);
            bitmap = Bitmap.createScaledBitmap(bitmap, w, h, false);
        }
        binding.iv.setImageBitmap(bitmap);
        pathImageUpload = path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            bindRequestCamera();
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            bindRequestSelectImage(data);
        }
    }

    public void choosePicture() {
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        if (Build.VERSION.SDK_INT > 23) {
            Uri mPhotoUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
            uri = ImageLoader.convertUri(getActivity(), mPhotoUri);
//            setOutputMediaFileUri_v7(uri);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
            startActivityForResult(intent, REQUEST_IMAGE);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            uri = ImageLoader.getOutputMediaFileUri(getActivity(), Statics.MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, REQUEST_IMAGE);
        }
    }

    private void submit() {
        String fn = binding.lastFirstName.edFN.getText().toString().trim();
        if (fn.length() == 0) {
            Utils.showMsg(getActivity(), Utils.getMsg(getActivity(), R.string.not_input) + " "
                    + Utils.getMsg(getActivity(), R.string.first_name));
            return;
        }
        String ln = binding.lastFirstName.edLN.getText().toString().trim();
        if (ln.length() == 0) {
            Utils.showMsg(getActivity(), Utils.getMsg(getActivity(), R.string.not_input) + " "
                    + Utils.getMsg(getActivity(), R.string.last_name));
            return;
        }
        String address = binding.address.ed.getText().toString().trim();
        if (address.length() == 0) {
            Utils.showMsg(getActivity(), Utils.getMsg(getActivity(), R.string.not_input) + " "
                    + Utils.getMsg(getActivity(), R.string.address));
            return;
        }

        if (pathImageUpload.length() == 0) {
            // call api
            UpdateProfile(new PrefManager(getActivity()).iMGUser());
        } else {
//            upload avatar -> call api
            uploadImage();
        }
    }

    private void uploadImage() {
        List<String> listPath = new ArrayList<>();
        listPath.add(pathImageUpload);
        processLoading(true);
        new HttpRequest().upload(getActivity(), listPath, new HttpRequestCallBack() {
            @Override
            public void onSuccess(HttpRequestDto obj) {
                if (getActivity() == null) return;
                processLoading(false);
//                profileDto.avatar = obj.Message;
//                UpdateProfile(profileDto);
                UpdateProfile(obj.Message);
            }

            @Override
            public void onFail(HttpRequestDto obj) {
                if (getActivity() == null) return;
                processLoading(false);
                Utils.showMsg(getActivity(), "upload fail");
            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (getActivity() == null) return;
                processLoading(false);
                Utils.showMsg(getActivity(), "upload fail");
            }
        }, Statics.Upload);
    }

    private void processLoading(boolean flag) {
        if (flag) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnSubmit.setEnabled(false);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnSubmit.setEnabled(true);
        }
    }

    public void UpdateProfile(String IMGUser) {
        int gender = AccountDto.getGender(binding.gender.ed.getText().toString().trim());
        final Map<String, String> params = new HashMap<>();
        params.put("UID", "" + new PrefManager(getActivity()).userId());
        params.put("FirstName", "" + binding.lastFirstName.edFN.getText().toString().trim());
        params.put("LastName", binding.lastFirstName.edLN.getText().toString().trim());
        params.put("Address", binding.address.ed.getText().toString().trim());
        params.put("IMGUser", IMGUser);
        params.put("Phone", new PrefManager(getActivity()).phone());
        params.put("Email", new PrefManager(getActivity()).email());
        params.put("BirthDay", binding.birthday.ed.getText().toString().trim());
        params.put("Gender", "" + gender);
        params.put("Password", new PrefManager(getActivity()).getUserPw());
        params.put("ConfirmPassword", new PrefManager(getActivity()).getUserPw());
        Log.d(TAG, "UpdateProfile params:" + new Gson().toJson(params));
        processLoading(true);
        new HttpRequest().UpdateProfile(getActivity(),
                Statics.POST,
                params,
                Statics.UpdateProfile,
                new AccountCallBack() {
                    @Override
                    public void onSuccess(AccountDto obj) {
                        if (getActivity() == null) return;
                        processLoading(false);
                        String msg = obj == null || obj.Message == null || obj.Message.length() == 0 ? "Success" : obj.Message;
                        Utils.showMsg(getActivity(), msg);
                        // update UI, save db local
                        saveProfile(obj);
                    }

                    @Override
                    public void onFail(AccountDto obj) {
                        if (getActivity() == null) return;
                        processLoading(false);
                        String msg = obj == null || obj.Message == null || obj.Message.length() == 0 ? "Fail" : obj.Message;
                        Utils.showMsg(getActivity(), msg);
                    }

                    @Override
                    public void onVolleyError(VolleyError error) {
                        if (getActivity() == null) return;
                        processLoading(false);
                        Utils.showMsg(getActivity(), error.toString());
                    }
                });
    }

    private void saveProfile(AccountDto obj) {
        if (obj.Account != null) {
            PrefManager prefManager = new PrefManager(getActivity());
            String IMGUser = obj.Account.IMGUser == null ? "" : obj.Account.IMGUser;
            String FirstName = obj.Account.FirstName == null ? "" : obj.Account.FirstName;
            String LastName = obj.Account.LastName == null ? "" : obj.Account.LastName;
            String Wallet = obj.Account.Wallet == null ? "" : obj.Account.Wallet;
            String Level = obj.Account.Level == null ? "" : obj.Account.Level;

            prefManager.setFirstName(FirstName);
            prefManager.setLastName(LastName);
            prefManager.setIMGUser(IMGUser);
            prefManager.setWallet(Wallet);
            prefManager.setLevel(Level);

            ((MainActivity)getActivity()).refreshProfile();
        }
    }

    private void genderDialog() {
        new SelectGenderDialog(getActivity(), new SelectGenderCallBack() {
            @Override
            public void onSelect(int type) {
                binding.gender.ed.setText(AccountDto.getName(type));
            }
        }).show();
    }

    private void birthdaySelect() {
        Utils.showDateDialog(getActivity(), Calendar.getInstance(), new DateCallback() {
            @Override
            public void onDateSetected(Calendar calendar) {
                binding.birthday.ed.setText(Utils.formatTime(calendar.getTimeInMillis(), Statics.DATE_FORMAT_DD_MM_YYYY));
            }
        });
    }
}

