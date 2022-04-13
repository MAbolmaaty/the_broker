package com.emupapps.the_broker.ui;

import static android.app.Activity.RESULT_OK;
import static com.emupapps.the_broker.utils.Constants.EMAIL_ADDRESS;
import static com.emupapps.the_broker.utils.Constants.JWT;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.PHONE_NUMBER;
import static com.emupapps.the_broker.utils.Constants.USERNAME;
import static com.emupapps.the_broker.utils.Constants.USER_ID;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.emupapps.the_broker.R;
import com.emupapps.the_broker.databinding.FragmentRegisterBinding;
import com.emupapps.the_broker.models.login.response.User;
import com.emupapps.the_broker.models.register.AuthenticationModelResponse;
import com.emupapps.the_broker.utils.FilePath;
import com.emupapps.the_broker.utils.FileUtils;
import com.emupapps.the_broker.utils.ProgressRequestBody;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.emupapps.the_broker.viewmodels.AuthenticationViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding mBinding;

    private static final String TAG = RegisterFragment.class.getSimpleName();

    private static final int REQUEST_PHOTO_CAMERA = 7007;
    private static final int REQUEST_PHOTO_GALLERY = 7008;
    private static final int TAKE_PHOTO_REQUEST_PERMISSION = 8008;
    private File mFile;
    private Uri mUri;
    private BottomSheetDialog mDialogSelectImage;
    private AuthenticationViewModel mAuthenticationViewModel;
    private String mLocale;
    private MultipartBody.Part mPartFile = null;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        mLocale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        mBinding.countryCodePicker.setCountryForPhoneCode(+966);
        mAuthenticationViewModel = new ViewModelProvider(requireActivity()).
                get(AuthenticationViewModel.class);
        mDialogSelectImage = new BottomSheetDialog(getActivity());
        mDialogSelectImage.setContentView(R.layout.dialog_select_image);

        mBinding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        mBinding.viewProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfilePicture();
            }
        });

        mBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == TAKE_PHOTO_REQUEST_PERMISSION) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast toast = Toast.makeText(getContext(), getString(R.string.permission_denied),
                            Toast.LENGTH_SHORT);
                    toast.show();
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
            mBinding.add.setVisibility(View.INVISIBLE);
        } else if (requestCode == REQUEST_PHOTO_GALLERY && getContext() != null && resultCode == RESULT_OK) {
            mUri = data.getData();
            String selectedFilePath = FilePath.getPath(getActivity(), mUri);
            if (selectedFilePath != null) {
                mFile = new File(selectedFilePath);
                Glide.with(getContext()).load(mUri).into(mBinding.userImage);
                mBinding.add.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    public void close() {
        getActivity().onBackPressed();
    }


    public void addProfilePicture() {
        if (getContext() != null) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        TAKE_PHOTO_REQUEST_PERMISSION);
            }
        }
    }

    public void createAccount() {
        String username = mBinding.username.getText().toString();
        String email = mBinding.email.getText().toString();
        String password = mBinding.password.getText().toString();
        String passwordConfirmation =
                mBinding.passwordConfirmation.getText().toString();
        String phoneNumber = "+" + mBinding.countryCodePicker.getSelectedCountryCode() +
                mBinding.phoneNumber.getText().toString();

        if (hasEmptyFields(username, email, password, passwordConfirmation, phoneNumber)) {
            return;
        }

        if (passwordsDoNotMatch(password, passwordConfirmation)) {
            return;
        }

        register(username, email, password, phoneNumber);
    }

    private void register(String username, String email, String password, String phoneNumber) {
        mBinding.progress.setVisibility(View.VISIBLE);
        mBinding.createNewAccount.setVisibility(View.VISIBLE);

        RequestBody data;
        String userData;

        if (TextUtils.isEmpty(mBinding.phoneNumber.getText())) {
            userData = String.format("{\"username\":\"%s\",\"email\":\"%s\"," +
                    "\"password\":\"%s\",\"passwordConfirmation\":\"%s\"" +
                    "}", username, email, password, password);
        } else {
            userData = String.format("{\"username\":\"%s\",\"email\":\"%s\"," +
                    "\"password\":\"%s\",\"passwordConfirmation\":\"%s\"," +
                    "\"phoneNumber\":\"%s\"}", username, email, password, password, phoneNumber);
        }

        data = RequestBody.create(MultipartBody.FORM, userData);

        SoftKeyboard.dismissKeyboardInActivity(getContext());
        AsyncTask.execute(() -> {
            if (mFile != null) {
                mUri = FileUtils.getUri(mFile);
                mPartFile = prepareFilePart("files.profilePicture");
            }

            getActivity().runOnUiThread(() -> {
                mAuthenticationViewModel.register(data, mPartFile);

                mAuthenticationViewModel.getResult().observe(RegisterFragment.this,
                        new Observer<AuthenticationModelResponse>() {
                            @Override
                            public void onChanged(AuthenticationModelResponse
                                                          authenticationModelResponse) {
                                mBinding.progress.setVisibility(View.INVISIBLE);
                                mBinding.createNewAccount.setVisibility(View.INVISIBLE);
                                if (authenticationModelResponse != null) {
                                    getActivity().onBackPressed();
                                } else {
                                    Toast toast = Toast.makeText(getContext(),
                                            getString(R.string.something_went_wrong),
                                            Toast.LENGTH_SHORT
                                    );
                                    toast.show();
                                }
                            }
                        });

                mAuthenticationViewModel.failure().observe(this, failure -> {
                    if (failure) {
                        Toast toast = Toast.makeText(getActivity(),
                                R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                        toast.show();
                        mBinding.progress.setVisibility(View.INVISIBLE);
                        mBinding.createNewAccount.setVisibility(View.INVISIBLE);
                    }
                });
            });
        });
    }

    private boolean hasEmptyFields(String username, String email, String password,
                                   String passwordConfirmation, String phoneNumber) {
        Toast toast = null;

        if (TextUtils.isEmpty(username)) {
            toast = Toast.makeText(getContext(), getString(R.string.enter_username),
                    Toast.LENGTH_SHORT);
        } else if (TextUtils.isEmpty(email)) {
            toast = Toast.makeText(getContext(), getString(R.string.enter_email),
                    Toast.LENGTH_SHORT);
        } else if (TextUtils.isEmpty(password)) {
            toast = Toast.makeText(getContext(), getString(R.string.enter_password),
                    Toast.LENGTH_SHORT);
        } else if (TextUtils.isEmpty(passwordConfirmation)) {
            toast = Toast.makeText(getContext(), getString(R.string.enter_confirm_password),
                    Toast.LENGTH_SHORT);
        }

        if (toast != null) {
            toast.show();
            return true;
        }

        return false;
    }

    private boolean passwordsDoNotMatch(String password, String passwordConfirmation) {
        Toast toast = null;

        if (!password.equals(passwordConfirmation)) {
            toast = Toast.makeText(getContext(), getString(R.string.password_not_match),
                    Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }
        return false;
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
                        "com.emupapps.the_broker.fileprovider",
                        mFile);

                captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                startActivityForResult(captureImageIntent, REQUEST_PHOTO_CAMERA);
            }
        }
    }

    private void takeGalleryPhoto() {
        mDialogSelectImage.dismiss();
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.pick_photo)),
                REQUEST_PHOTO_GALLERY);
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
                }));

        return MultipartBody.Part.createFormData(partName, mFile.getName(), requestFile);
    }
}
