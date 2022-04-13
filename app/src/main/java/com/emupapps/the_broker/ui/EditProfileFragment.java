package com.emupapps.the_broker.ui;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.databinding.FragmentEditProfileBinding;
import com.emupapps.the_broker.utils.FilePath;
import com.emupapps.the_broker.utils.FileUtils;
import com.emupapps.the_broker.utils.ProgressRequestBody;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.emupapps.the_broker.viewmodels.InfoUpdateViewModel;
import com.emupapps.the_broker.viewmodels.ProfileViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hbb20.CountryCodePicker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.emupapps.the_broker.utils.Constants.ADDRESS;
import static com.emupapps.the_broker.utils.Constants.BASE_URL;
import static com.emupapps.the_broker.utils.Constants.BIRTH_DATE;
import static com.emupapps.the_broker.utils.Constants.EMAIL_ADDRESS;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.PHONE_CODE;
import static com.emupapps.the_broker.utils.Constants.PHONE_NUMBER;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;
import static com.emupapps.the_broker.utils.Constants.USERNAME;
import static com.emupapps.the_broker.utils.Constants.USER_ID;
import static com.emupapps.the_broker.utils.Constants.Profile_Picture;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = EditProfileFragment.class.getSimpleName();
    private FragmentEditProfileBinding mBinding;

    private boolean mUpload;
    BottomSheetDialog mDialogSelectImage;
    private File mFile;
    private Uri mUri;
    private Toast mToast;
    private String mNewBirthDate;
    private InfoUpdateViewModel mViewModelUpdateInfo;
    private String mUserId;
    private String mLocale;
    private static final int REQUEST_PHOTO_CAMERA = 8007;
    private static final int REQUEST_PHOTO_GALLERY = 8008;
    private static final int TAKE_PHOTO_REQUEST_PERMISSION = 9008;
    private MultipartBody.Part mPartFile;
    private ProfileViewModel mViewModelInfoUser;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentEditProfileBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        mLocale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (mLocale.equals("ar"))
            view.setRotation(-180);

        mDialogSelectImage = new BottomSheetDialog(getActivity());
        mDialogSelectImage.setContentView(R.layout.dialog_select_image);

        mViewModelUpdateInfo =
                new ViewModelProvider(getActivity()).get(InfoUpdateViewModel.class);
        mViewModelInfoUser =
                new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        mUserId = SharedPrefUtil.getInstance(getActivity()).read(USER_ID, null);
        if (mUserId == null) {
            loadUserInfo();
        } else {
            loadUserInfo(getContext());
        }

        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == TAKE_PHOTO_REQUEST_PERMISSION) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (mToast != null)
                        mToast.cancel();
                    mToast = Toast.makeText(getContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT);
                    mToast.show();
                    return;
                }
            }
            takePhoto();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO_CAMERA && getContext() != null && resultCode == RESULT_OK) {
            Glide.with(getContext()).load(mFile).into(mBinding.userImage);
            mBinding.userImage.setVisibility(View.VISIBLE);
            mBinding.camera.setVisibility(View.INVISIBLE);
            mUpload = false;

        } else if (requestCode == REQUEST_PHOTO_GALLERY && getContext() != null && resultCode == RESULT_OK) {
            mUri = data.getData();
            String selectedFilePath = FilePath.getPath(getContext(), mUri);
            if (selectedFilePath != null) {
                mFile = new File(selectedFilePath);
                Glide.with(getContext()).load(mFile).into(mBinding.userImage);
                mBinding.userImage.setVisibility(View.VISIBLE);
                mBinding.camera.setVisibility(View.INVISIBLE);
                mUpload = false;
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mNewBirthDate = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
        mBinding.birthDate.setText(mNewBirthDate);
    }


    public void clickOnImage() {
        if (mUpload) {
            if (getContext() != null) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, TAKE_PHOTO_REQUEST_PERMISSION);
                }
            }
        } else {
            mBinding.shadow.setVisibility(View.VISIBLE);
            mBinding.delete.setVisibility(View.VISIBLE);
        }
    }


    public void hideDelete() {
        mBinding.shadow.setVisibility(View.GONE);
        mBinding.delete.setVisibility(View.GONE);
    }


    public void delete() {
        mBinding.userImage.setVisibility(View.INVISIBLE);
        mUpload = true;
        mBinding.shadow.setVisibility(View.INVISIBLE);
        mBinding.delete.setVisibility(View.INVISIBLE);
        mBinding.camera.setVisibility(View.VISIBLE);
    }


    public void pickBirthDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setLocale(Locale.ENGLISH);
        datePickerDialog.show(getActivity().getSupportFragmentManager(), "DatePickerDialog");
    }


    public void save() {
        if (mToast != null)
            mToast.cancel();

        String newUsername = mBinding.username.getText().toString();
        mNewBirthDate = mBinding.birthDate.getText().toString();
        String newAddress = mBinding.address.getText().toString();
        String newEmail = mBinding.email.getText().toString();
        String newCountryCode = "+" + mBinding.countryCodePicker.getSelectedCountryCode();
        String newPhoneNumber = mBinding.phoneNumber.getText().toString();

        if (TextUtils.isEmpty(newUsername)) {
            newUsername = SharedPrefUtil.getInstance(getContext()).read(USERNAME, "");
        }

        if (TextUtils.isEmpty(newAddress)) {
            newAddress = SharedPrefUtil.getInstance(getContext()).read(ADDRESS, "");
        }

        if (TextUtils.isEmpty(newEmail)) {
            newEmail = SharedPrefUtil.getInstance(getContext()).read(EMAIL_ADDRESS, "");
        }

        if (TextUtils.isEmpty(newPhoneNumber)) {
            newPhoneNumber = SharedPrefUtil.getInstance(getContext()).read(PHONE_NUMBER, "");
        }

        RequestBody userIdRequest = RequestBody.create(MultipartBody.FORM, mUserId),
                usernameRequest = RequestBody.create(MultipartBody.FORM, newUsername),
                birthDateRequest = RequestBody.create(MultipartBody.FORM, mNewBirthDate),
                addressRequest = RequestBody.create(MultipartBody.FORM, newAddress),
                emailRequest = RequestBody.create(MultipartBody.FORM, newEmail),
                countryCodeRequest = RequestBody.create(MultipartBody.FORM, newCountryCode),
                phoneNumberRequest = RequestBody.create(MultipartBody.FORM, newPhoneNumber),
                localeRequest = RequestBody.create(MultipartBody.FORM, mLocale);
        mBinding.progress.setVisibility(View.VISIBLE);
        SoftKeyboard.dismissKeyboardInActivity(getActivity());
        AsyncTask.execute(() -> {
            if (mFile != null) {
                mUri = FileUtils.getUri(mFile);
                Bitmap bitmap = BitmapFactory.decodeFile(mFile.getPath());

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(mFile);
                    bitmap.compress(Bitmap.CompressFormat.WEBP, 77, fileOutputStream);
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mPartFile = prepareFilePart("photo");
            }
            getActivity().runOnUiThread(() -> {
                mViewModelUpdateInfo.updateInfo(userIdRequest, usernameRequest, emailRequest, phoneNumberRequest,
                        countryCodeRequest, birthDateRequest, addressRequest, localeRequest, mPartFile);
                mViewModelUpdateInfo.getResult().observe(EditProfileFragment.this, updateInfoModelResponse -> {
                    if (updateInfoModelResponse.getKey().equals(SUCCESS)) {
                        InfoUpdateViewModel viewModelInfoUpdate =
                                new ViewModelProvider(getActivity()).
                                        get(InfoUpdateViewModel.class);
                        mToast = Toast.makeText(getActivity(), updateInfoModelResponse.getMessage(), Toast.LENGTH_SHORT);
                        mToast.show();
                        String userId = SharedPrefUtil.getInstance(getContext()).read(USER_ID, null);
                        if (userId != null) {
                            SharedPrefUtil.getInstance(getContext()).write(Profile_Picture, updateInfoModelResponse.getResult().getPhoto());
                            SharedPrefUtil.getInstance(getContext()).write(USERNAME, updateInfoModelResponse.getResult().getName());
                            SharedPrefUtil.getInstance(getContext()).write(BIRTH_DATE, updateInfoModelResponse.getResult().getBirthday());
                            SharedPrefUtil.getInstance(getContext()).write(ADDRESS, updateInfoModelResponse.getResult().getArea());

                            SharedPrefUtil.getInstance(getContext()).write(PHONE_NUMBER, updateInfoModelResponse.getResult().getPhone());
                            SharedPrefUtil.getInstance(getContext()).write(PHONE_CODE, updateInfoModelResponse.getResult().getCode());
                            viewModelInfoUpdate.infoUpdated(true);
                        } else {
                            mViewModelInfoUser.userInfo(mUserId);
//                            mViewModelInfoUser.getUserInfo().observe(EditProfileFragment.this, userInfoModelResponse -> {
//                                if (userInfoModelResponse.getKey().equals(SUCCESS)){
//                                    viewModelInfoUpdate.infoUpdated(true);
//                                }
//                            });
                        }
                    } else {
                        mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
                        mToast.show();
                    }
                });
                mViewModelUpdateInfo.isLoading().observe(EditProfileFragment.this, loading -> {
                    if (loading) {
                        if (mFile != null) {
                            mBinding.progress.setVisibility(View.VISIBLE);
                            mBinding.percentage.setVisibility(View.VISIBLE);
                        }
                        mBinding.progress.setVisibility(View.VISIBLE);
                    } else {
                        mBinding.progress.setVisibility(View.GONE);
                        mBinding.percentage.setVisibility(View.GONE);
                        mBinding.progress.setVisibility(View.GONE);
                    }
                });
                mViewModelUpdateInfo.failure().observe(EditProfileFragment.this, failure -> {
                    if (failure) {
                        if (mToast != null)
                            mToast.cancel();
                        mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                        mToast.show();
                    }
                });
            });
        });

    }

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    private void loadUserInfo(Context context) {
        String userImage = SharedPrefUtil.getInstance(context).read(Profile_Picture, null);
        String username = SharedPrefUtil.getInstance(context).read(USERNAME, "");
        String birthDate = SharedPrefUtil.getInstance(context).read(BIRTH_DATE, "");
        String address = SharedPrefUtil.getInstance(context).read(ADDRESS, "");
        String email = SharedPrefUtil.getInstance(context).read(EMAIL_ADDRESS, "");
        String phoneCode = SharedPrefUtil.getInstance(context).read(PHONE_CODE, "");
        String phoneNumber = SharedPrefUtil.getInstance(context).read(PHONE_NUMBER, "");

        if (userImage != null && !TextUtils.isEmpty(userImage)) {
            //mUserImage.setVisibility(View.VISIBLE);
            //Glide.with(context).load(BASE_URL + userImage).into(mUserImage);
        } else {
            //mUserImage.setVisibility(View.INVISIBLE);
            //mCamera.setVisibility(View.VISIBLE);
        }
        //mUsername.setText(username);
//        mBirthDate.setText(birthDate);
//        mAddress.setText(address);
//        mEmail.setText(email);
        //if (phoneCode != null && phoneCode.length() > 0)
//            mCountryCodePicker.setCountryForPhoneCode(Integer.parseInt(phoneCode));
//        mPhoneNumber.setText(phoneNumber);
    }

    private void loadUserInfo() {
//        viewModelLogin.getUser().observe(this, loginModelResponse -> {
//            mUserId = loginModelResponse.getUser().getId();
//            mViewModelInfoUser.userInfo(mUserId);
//            mViewModelInfoUser.getUserInfo().observe(EditProfileFragment.this, new Observer<UserInfoModelResponse>() {
//                @Override
//                public void onChanged(UserInfoModelResponse userInfoModelResponse) {
//                    String userImage = userInfoModelResponse.getUser().getPhoto();
//                    String username = userInfoModelResponse.getUser().getName();
//                    String birthDate = userInfoModelResponse.getUser().getBirthday();
//                    String address = userInfoModelResponse.getUser().getAddress();
//                    String email = userInfoModelResponse.getUser().getEmail();
//                    String phoneCode = userInfoModelResponse.getUser().getCode();
//                    String phoneNumber = userInfoModelResponse.getUser().getPhone();
//
//                    if (userImage != null && !TextUtils.isEmpty(userImage)) {
//                        mUserImage.setVisibility(View.VISIBLE);
//                        Glide.with(EditProfileFragment.this).load(BASE_URL + userImage).into(mUserImage);
//                    } else {
//                        mUserImage.setVisibility(View.INVISIBLE);
//                        mCamera.setVisibility(View.VISIBLE);
//                    }
//
//                    mUsername.setText(username);
//                    mBirthDate.setText(birthDate);
//                    mAddress.setText(address);
//                    mEmail.setText(email);
//                    if (phoneCode != null && phoneCode.length() > 0)
//                        mCountryCodePicker.setCountryForPhoneCode(Integer.parseInt(phoneCode));
//                    mPhoneNumber.setText(phoneNumber);
//                }
//            });

//            mViewModelInfoUser.isLoading().observe(EditProfileFragment.this, loading -> {
//                if (loading){
//                    mProgress.setVisibility(View.VISIBLE);
//                } else {
//                    mProgress.setVisibility(View.INVISIBLE);
//                }
//            });
        //});
    }

    private void takePhoto() {
        Window dialog = mDialogSelectImage.getWindow();
        TextView camera = dialog.findViewById(R.id.camera);
        TextView gallery = dialog.findViewById(R.id.gallery);
        ImageView close = dialog.findViewById(R.id.close);
        camera.setOnClickListener(v -> takeCameraPhoto());
        gallery.setOnClickListener(v -> takeGalleryPhoto());
        close.setOnClickListener(v -> mDialogSelectImage.dismiss());
        mDialogSelectImage.show();
    }

    private void takeCameraPhoto() {
        mDialogSelectImage.dismiss();
        Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (getContext() != null && captureImageIntent.resolveActivity(getContext().getPackageManager()) != null) {
            try {
                mFile = createPhotoFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (mFile != null) {
                mUri = FileProvider.getUriForFile(getContext(),
                        "com.aqratsa.aeqarat.fileprovider",
                        mFile);

                captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                startActivityForResult(captureImageIntent, REQUEST_PHOTO_CAMERA);
            }
        }
    }

    private void takeGalleryPhoto() {
        mDialogSelectImage.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.pick_photo)), REQUEST_PHOTO_GALLERY);
    }

    private File createPhotoFile() throws IOException {
        if (getContext() != null) {
            String timeStamp = String.valueOf(System.currentTimeMillis());
            String imageFileName = "JPEG_" + timeStamp;

            File storageDirectory = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDirectory
            );

            return image;
        }
        return null;
    }

    private MultipartBody.Part prepareFilePart(String partName) {

        RequestBody requestFile = new ProgressRequestBody(getContext(), mFile, (progressInPercent, totalBytes) -> getActivity().runOnUiThread(() -> {
            //mProgressBar.setProgress(progressInPercent);
            if (progressInPercent == 100) {
                //mProgressBar.setVisibility(View.GONE);
                //mPercentage.setVisibility(View.GONE);
            } else {
                //mPercentage.setText(progressInPercent + " %");
            }
        }));

        return MultipartBody.Part.createFormData(partName, mFile.getName(), requestFile);
    }
}
