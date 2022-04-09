package com.emupapps.the_broker.ui;


import static android.app.Activity.RESULT_OK;
import static com.emupapps.the_broker.utils.Constants.LOCALE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.emupapps.the_broker.R;
import com.emupapps.the_broker.utils.FilePath;
import com.emupapps.the_broker.utils.FileUtils;
import com.emupapps.the_broker.utils.ProgressRequestBody;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.emupapps.the_broker.viewmodels.LoginViewModel;
import com.emupapps.the_broker.viewmodels.RegisterViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private static final String TAG = RegisterFragment.class.getSimpleName();

    ImageView mClose;
    View mViewImage;
    CircleImageView mUserImage;
    ImageView mAdd;
    Button mCreate;
    EditText mUsername;
    EditText mPhoneNumber;
    TextInputEditText mPassword;
    ProgressBar mProgress;
    CountryCodePicker mCountryCodePicker;

    private static final int REQUEST_PHOTO_CAMERA = 7007;
    private static final int REQUEST_PHOTO_GALLERY = 7008;
    private static final int TAKE_PHOTO_REQUEST_PERMISSION = 8008;
    private File mFile;
    private Uri mUri;
    private BottomSheetDialog mDialogSelectImage;
    private String mCountryCode;
    private String mNumber;
    private Toast mToast;
    private RegisterViewModel mViewModelRegister;
    private LoginViewModel mViewModelLogin;
    private String mLocale;
    private MultipartBody.Part mPartFile = null;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mLocale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        mCountryCodePicker.setCountryForPhoneCode(+966);
        mViewModelRegister = ViewModelProviders.of(this).get(RegisterViewModel.class);
        mViewModelLogin = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        mDialogSelectImage = new BottomSheetDialog(getActivity());
        mDialogSelectImage.setContentView(R.layout.dialog_select_image);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
            Glide.with(getContext()).load(mFile).into(mUserImage);
            mAdd.setVisibility(View.INVISIBLE);
        } else if (requestCode == REQUEST_PHOTO_GALLERY && getContext() != null && resultCode == RESULT_OK) {
            mUri = data.getData();
            String selectedFilePath = FilePath.getPath(getActivity(), mUri);
            if (selectedFilePath != null) {
                mFile = new File(selectedFilePath);
                Glide.with(getContext()).load(mUri).into(mUserImage);
                mAdd.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void close() {
        getActivity().onBackPressed();

    }


    public void addProfilePhoto() {
        if (getContext() != null) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, TAKE_PHOTO_REQUEST_PERMISSION);
            }
        }
    }

    public void createAccount() {
        if (mToast != null)
            mToast.cancel();

        String username = mUsername.getText().toString();
        mCountryCode = "+" + mCountryCodePicker.getSelectedCountryCode();
        mNumber = mPhoneNumber.getText().toString();
        String password = mPassword.getText().toString();

//        if (TextUtils.isEmpty(username)) {
//            Toast.makeText(getContext(), getString(R.string.enter_username), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (TextUtils.isEmpty(mNumber)) {
//            Toast.makeText(getContext(), getString(R.string.enter_phone_number), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (TextUtils.isEmpty(password)) {
//            Toast.makeText(getContext(), getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
//            return;
//        }

        register(username, "email", password, mCountryCode, mNumber, mLocale, mFile);
    }

    private void register(String username, String email, String password,
                          String countryCode, String phoneNumber, String locale, File photoFile) {

        RequestBody data;
        String newUser = "{\"username\":\"em12\",\"email\":\"em12@emupapps.com\"," +
                "\"password\":\"1234567\",\"passwordConfirmation\":\"1234567\"," +
                "\"phoneNumber\":\"1234567\"}";

        data = RequestBody.create(MultipartBody.FORM, newUser);

        mProgress.setVisibility(View.VISIBLE);
        SoftKeyboard.dismissKeyboardInActivity(getContext());
        AsyncTask.execute(() -> {
            if (mFile != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(mFile.getPath());
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(mFile);
                    bitmap.compress(Bitmap.CompressFormat.WEBP, 77, fileOutputStream);
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    mProgress.setVisibility(View.INVISIBLE);
                }
                mUri = FileUtils.getUri(mFile);

                mPartFile = prepareFilePart("files.profilePicture");

            }

            getActivity().runOnUiThread(() -> {
                mViewModelRegister.register(data, mPartFile);

                mViewModelRegister.getResult().observe(RegisterFragment.this, registerModelResponse -> {

                });
                mViewModelRegister.failure().observe(this, failure -> {
                    if (failure) {
                        if (mToast != null)
                            mToast.cancel();
                        mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                        mToast.show();
                        mProgress.setVisibility(View.INVISIBLE);
                    }
                });
            });
        });
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

        RequestBody requestFile =
                new ProgressRequestBody(getContext(), mFile, (progressInPercent, totalBytes)
                        -> getActivity().runOnUiThread(() -> {

                    if (progressInPercent == 100) {
                        mProgress.setVisibility(View.VISIBLE);
                    } else {

                    }
                }));

        return MultipartBody.Part.createFormData(partName, mFile.getName(), requestFile);
    }
}
