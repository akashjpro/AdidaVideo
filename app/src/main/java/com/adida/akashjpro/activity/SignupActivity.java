package com.adida.akashjpro.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adida.akashjpro.activity.utils.ShowHideProgessDialog;
import com.adida.akashjpro.livevideo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

import static com.adida.akashjpro.activity.MainActivity.mUser;

public class SignupActivity extends AppCompatActivity {
    private EditText mEmailEditText, mPasswordEditTex, mNameEditText;
    private Button mSignUpButton, mSelectProfileButton;
    private FirebaseAuth mAuth;
    private ImageButton mBackSignupButton;
    private String mEmail = "";
    private String mPassword = "";
    private String mName = "";
    private TextInputLayout mContainEamil, mContainPassword, mContaintName;
    private final int REQUEST_CODE_FOLDER = 456;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private DatabaseReference mDatabase;

    private String mLinkImage;

    private TextView mProfilePic;
    private ImageView mImageProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        addControls();
        storage = FirebaseStorage.getInstance();
        mDatabase              = FirebaseDatabase.getInstance().getReference();
        storageRef = storage.getReferenceFromUrl("gs://adidavideo-c1515.appspot.com");
        mAuth = FirebaseAuth.getInstance();
        eventListeners();
    }

    /**
     * Events Listerners
     */
    private void eventListeners() {
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        mBackSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            }
        });

        /**
         * Test email is empty
         */
        mEmailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mEmail    = mEmailEditText.getText().toString().trim();
                if(!hasFocus){
                    if(mEmail.equals("")){
                        mContainEamil.setErrorEnabled(true);
                        mContainEamil.setError("Email không được để trống");
                        return;
                    }
                }else {
                    mContainEamil.setError("");
                }
            }
        });

        /**
         * Test Password is empty
         */
        mPasswordEditTex.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mPassword = mPasswordEditTex.getText().toString().trim();
                if(!hasFocus){
                    if(mPassword.equals("")){
                        mContainPassword.setErrorEnabled(true);
                        mContainPassword.setError("Mật khẩu không được để trống");
                        return;
                    }
                }else {
                    mContainPassword.setError("");
                }

            }
        });

        /**
         * Test name is empty
         */
        mNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mName = mNameEditText.getText().toString().trim();
                if(!hasFocus){
                    if(mName.equals("")){
                        mContaintName.setErrorEnabled(true);
                        mContaintName.setError("Tên không được để trống");
                        return;
                    }
                }else {
                    mContaintName.setError("");
                }
            }
        });

        /**
         * Select image in folder
         */
        mSelectProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentFolder = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intentFolder, REQUEST_CODE_FOLDER);
                Random random = new Random();
                ArrayList<Integer> ArrayColor = new ArrayList<>();
                ArrayColor.add(255);  // array[0] 255
                ArrayColor.add(0);    // array[1] 0
                ArrayColor.add(1);   // array[2] radom

                int color = random.nextInt(256);
                ArrayColor.set(2, color);

                Collections.shuffle(ArrayColor); // trộn thứ tự mảng
                int R = ArrayColor.get(0);
                int G = ArrayColor.get(1);
                int B = ArrayColor.get(2);

                String name =  mNameEditText.getText().toString().trim();
                name = name.substring(0, 1);
                name = name.toUpperCase();
                mProfilePic.setText(name);

                ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
                drawable.getPaint().setColor(Color.rgb(R, G, B));

                mProfilePic.setBackground(drawable);
                mProfilePic.setDrawingCacheEnabled(true);
                Bitmap bitmap= Bitmap.createBitmap(mProfilePic.getDrawingCache());
                mImageProfile.setImageBitmap(bitmap);


            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK &&  data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

        }

    }

    /**
     * add controls form file xml
     */
    private void addControls() {
        MainActivity.isLoginFirst = false; // never login

        mEmailEditText       = (EditText) findViewById(R.id.editTextEamilSignup);
        mPasswordEditTex     = (EditText) findViewById(R.id.editTextPasswordSignup);
        mNameEditText        = (EditText) findViewById(R.id.editTextName);

        mSignUpButton        = (Button) findViewById(R.id.buttonSignUp);
        mSelectProfileButton = (Button) findViewById(R.id.selectProfileButton);

        mBackSignupButton    = (ImageButton) findViewById(R.id.menuBackSignup);

        mContaintName        = (TextInputLayout) findViewById(R.id.containName);
        mContainPassword     = (TextInputLayout) findViewById(R.id.containPasswordSignup);
        mContainEamil        = (TextInputLayout) findViewById(R.id.containEmailSignup);

        mProfilePic          = (TextView) findViewById(R.id.profilePicTxt);

        mImageProfile        = (ImageView) findViewById(R.id.imageProfile);
    }

    /**
     * fuction register
     */
    private void register(){
        //ShowHideProgessDialog.showHideProgessDialog(this);
        mEmail = mEmailEditText.getText().toString().trim();
        mPassword = mPasswordEditTex.getText().toString().trim();
        mName = mNameEditText.getText().toString().trim();
        if(mName.equals("")){
            mContaintName.setErrorEnabled(true);
            mContaintName.setError("Tên không được để trống");
            return;
        }

        if(mEmail.equals("")){
            mContainEamil.setErrorEnabled(true);
            mContainEamil.setError("Email không được để trống");
            return;
        }
        if(mPassword.equals("")){
            mContainPassword.setErrorEnabled(true);
            mContainPassword.setError("Mật khẩu không được để trống");
            return;
        }


        /**
         * Authentication with sigup account
         */
        mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            //                                    ShowHideProgessDialog.showHideProgessDialog(SignupActivity.this);//show dialog
                            mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                                    .addOnCompleteListener(( SignupActivity.this), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if(task.isSuccessful()){

                                                ShowHideProgessDialog.showHideProgessDialog(SignupActivity.this);
                                                Calendar calendar = Calendar.getInstance();
                                                final StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");
                                                // Radom color
                                                Random random = new Random();
                                                ArrayList<Integer> ArrayColor = new ArrayList<>();
                                                ArrayColor.add(255);  // array[0] 255
                                                ArrayColor.add(0);    // array[1] 0
                                                ArrayColor.add(1);   // array[2] radom

                                                int color = random.nextInt(256);
                                                ArrayColor.set(2, color);

                                                Collections.shuffle(ArrayColor); // trộn thứ tự mảng
                                                int R = ArrayColor.get(0);
                                                int G = ArrayColor.get(1);
                                                final int B = ArrayColor.get(2);

                                                //set text for profile
                                                String name =  mNameEditText.getText().toString().trim();
                                                name = name.substring(0, 1);
                                                name = name.toUpperCase();
                                                mProfilePic.setText(name);


                                                ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
                                                drawable.getPaint().setColor(Color.rgb(R, G, B));
                                                mProfilePic.setBackground(drawable);

                                                mProfilePic.setDrawingCacheEnabled(true);
                                                Bitmap bitmap= Bitmap.createBitmap(mProfilePic.getDrawingCache());

                                                //            Bitmap bitmap = ((BitmapDrawable)mProfilePic.getDrawable()).getBitmap();
                                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                final byte[] data = baos.toByteArray();

                                                UploadTask uploadTask = mountainsRef.putBytes(data);
                                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        // Handle unsuccessful uploads
                                                        Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                                        @SuppressWarnings("VisibleForTests")
                                                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                                        mLinkImage = String.valueOf(downloadUrl);
                                                        mUser = mAuth.getCurrentUser();
                                                        String id = mUser.getUid();
//
//                                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                                                                .setDisplayName(name)
//                                                                .setPhotoUri(Uri.parse(profile))
//                                                                .build();
//
//                                                        mUser.updateProfile(profileUpdates)
//                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                    @Override
//                                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                                        if (task.isSuccessful()) {
//                                                                            Log.d("TAG", "User profile updated.");
//                                                                            String name  =  mUser.getDisplayName();
//                                                                            String profile = mUser.getPhotoUrl().toString();
//                                                                        }
//                                                                    }
//                                                                });

//                                                        mDatabase.child("User").child("profile").child(id).child("name").setValue(mName);
//                                                        mDatabase.child("User").child("profile").child(id).child("profilePic").setValue(mLinkImage);

                                                        mDatabase.child("User").child(id).child("name").setValue(mName);
                                                        mDatabase.child("User").child(id).child("profilePic").setValue(mLinkImage);
                                                        mDatabase.child("User").child(id).child("coverPhoto").setValue(MainActivity.mUrlCoverPhoto);


                                                        ShowHideProgessDialog.showHideProgessDialog(SignupActivity.this);
                                                        Handler handler = new Handler();
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(SignupActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                                Intent intent = new Intent(SignupActivity.this, ProfileActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }, 3000);



                                                    }



                                                });


                                            }else {
                                                Toast.makeText(SignupActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }else {
                            if(task.getException().toString().contains("formatted")){
                                ShowHideProgessDialog.hideProgessDialog();
                                mContainEamil.setError("Email không đúng định dạng, xin nhập lại!!!");
                                //                                mEmailEditText.requestFocus();
                                return;
                            }
                            if(task.getException().toString().contains("least 6 characters")){
                                ShowHideProgessDialog.hideProgessDialog();
                                mContainPassword.setError("Mật khẩu phải ít nhất 6 ký tự, xin nhập lại!!!");
                                //                                mPasswordEditTex.requestFocus();
                                return;
                            }

                            if(mName.length()>100){
                                ShowHideProgessDialog.hideProgessDialog();
                                mContaintName.setError("Xin nhập tên không quá 100 ký tự");
                            }
                            if(task.getException().toString().contains("email address is already in use")){
                                ShowHideProgessDialog.hideProgessDialog();
                                mContainEamil.setError("Email đã đăng ký rồi, xin đăng nhập!!!");
                                //                                mEmailEditText.requestFocus();
                                return;
                            }

                            Toast.makeText(SignupActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }
}
