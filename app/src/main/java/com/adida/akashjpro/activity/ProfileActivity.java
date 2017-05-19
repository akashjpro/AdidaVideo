package com.adida.akashjpro.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adida.akashjpro.fragment.DatePickerFragment;
import com.adida.akashjpro.livevideo.R;
import com.andexert.library.RippleView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;


public class ProfileActivity extends AppCompatActivity {

    private TextView nickNameText, emailText, logoutText, nameText, neoAnhbia;
    private TextView mPhone, mLive, mGender, mBirthday;
    private FirebaseAuth mAuth;
    private ImageButton  mBack;
    private ImageView    mProfilePicture;
    private ImageView    mCoverPhoto;


    private TextView mNameEdit, mEmailEdit, mPhoneEdtit, mLiveEdit, mGanderEdit, mBirthdayEdit;

    private RippleView mRippleProfileActivity;
    private LinearLayout mLayoutActivity;
    FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String mIdUser;

    //Control dialog update profile
    private Button   mUpdateButton, mExitButton;
    private EditText mUpdateText;
    private String field = "";

    private RadioGroup mGenderRadio;
    private Button     mUpdateGenderButton, mExitGenderButton;
    public String gender = "";

    private Button   mmUpdateBirthday, mExitBirthday;

    private Button mUpdateLiveButton, mExitLiveButton;
    private AutoCompleteTextView mAutoEdit;

    private Calendar myCalendar;

    private LinearLayout mLayoutProfile;

    private final int REQUEST_CODE_CAPTURE = 12345;
    private final int REQUEST_CODE_FOLDER  = 111;
    private final int REQUEST_CODE_FOLDER1  = 123;
    Bitmap mBitmap;

    public static String id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        storage = FirebaseStorage.getInstance();
        mDatabase              = FirebaseDatabase.getInstance().getReference();
        storageRef = storage.getReferenceFromUrl("gs://adidavideo-c1515.appspot.com");
        addControls();


        // get userId

            id      = mUser.getUid();

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                id = bundle.getString("idUser");
            }

        if(!id.equals(mUser.getUid())){
            mNameEdit.setVisibility(View.GONE);
            mEmailEdit.setVisibility(View.GONE);
            mPhoneEdtit.setVisibility(View.GONE);
            mLiveEdit.setVisibility(View.GONE);
            mGanderEdit.setVisibility(View.GONE);
            mBirthdayEdit.setVisibility(View.GONE);
        }else {
            mNameEdit.setVisibility(View.VISIBLE);
            mEmailEdit.setVisibility(View.VISIBLE);
            mPhoneEdtit.setVisibility(View.VISIBLE);
            mLiveEdit.setVisibility(View.VISIBLE);
            mGanderEdit.setVisibility(View.VISIBLE);
            mBirthdayEdit.setVisibility(View.VISIBLE);
        }


        if(!MainActivity.isLoginFirst) {
            // check null emai
            String email = mUser.getEmail();
            if (email != null) {
                if (email.equals("")) {
                    email = "XXXXXX";
                }
            } else {
                email = "XXXXXX";
            }

            String phone = "XXXXXX";

            String birthday = MainActivity.birthday;
            if (birthday != null) {
                if (birthday.equals("")) {
                    birthday = "XXXXXX";
                }
            } else {
                birthday = "XXXXXX";
            }

            String gender = MainActivity.gender;
            if (gender != null) {
                if (gender.equals("")) {
                    gender = "XXXXXX";
                }
            } else {
                gender = "XXXXXX";
            }

            String location = MainActivity.location;
            if (location != null) {
                if (location.equals("")) {
                    location = "XXXXXX";
                }
            } else {
                location = "XXXXXX";
            }

            mDatabase.child("User").child(id).child("email").setValue(email);
            mDatabase.child("User").child(id).child("birthday").setValue(birthday);
            mDatabase.child("User").child(id).child("location").setValue(location);
            mDatabase.child("User").child(id).child("gender").setValue(gender);
            mDatabase.child("User").child(id).child("phone").setValue(phone);
            MainActivity.isLoginFirst = true;

        }

            mDatabase.child("User").child(id).child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    nameText.setText(dataSnapshot.getValue().toString());
                    nickNameText.setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mDatabase.child("User").child(id).child("email").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    emailText.setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mDatabase.child("User").child(id).child("phone").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mPhone.setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mDatabase.child("User").child(id).child("location").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mLive.setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mDatabase.child("User").child(id).child("profilePic").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String url = dataSnapshot.getValue().toString();
                    if(url != null && !url.isEmpty()){
                        Picasso.with(ProfileActivity.this).load(url)
                                .fit().centerCrop()
                                .placeholder(R.drawable.loading_profile)
                                .error(R.drawable.no_image)
                                .into(mProfilePicture);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mDatabase.child("User").child(id).child("gender").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mGender.setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mDatabase.child("User").child(id).child("birthday").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mBirthday.setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mDatabase.child("User").child(id).child("coverPhoto").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String url = dataSnapshot.getValue().toString();
                    if(url != null && !url.isEmpty()){
                        Picasso.with(ProfileActivity.this).load(url)
                                .fit().centerCrop()
                                .placeholder(R.drawable.load_cover)
                                .error(R.drawable.no_video)
                                .into(mCoverPhoto);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
//                if(LoginManager.getInstance() != null){
//                    LoginManager.getInstance().logOut();
//                }
                //ShowHideProgessDialog.showHideProgessDialog(ProfileActivity.this);
                Toast.makeText(ProfileActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
//                startActivity(intent);
                //overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });

        enventListeners();

    }


    private void addControls() {
        mRippleProfileActivity = (RippleView) findViewById(R.id.rippleProfileActivity);
        mLayoutActivity        = (LinearLayout) findViewById(R.id.activity_profile);
        nickNameText           = (TextView) findViewById(R.id.nickname);
        nameText               = (TextView) findViewById(R.id.name);
        emailText              = (TextView) findViewById(R.id.email);

        mNameEdit              = (TextView) findViewById(R.id.editName);
        mEmailEdit             = (TextView) findViewById(R.id.editEmail);
        mPhoneEdtit            = (TextView) findViewById(R.id.editPhone);
        mLiveEdit              = (TextView) findViewById(R.id.editLive);
        mGanderEdit            = (TextView) findViewById(R.id.editGender);
        mBirthdayEdit          = (TextView) findViewById(R.id.editBirthday);

        mPhone                 = (TextView) findViewById(R.id.SDT);
        mLive                  = (TextView) findViewById(R.id.live);
        mBirthday              = (TextView) findViewById(R.id.birthay);
        mGender                = (TextView) findViewById(R.id.gender);

        logoutText             = (TextView) findViewById(R.id.logoutText);
        mBack                  = (ImageButton) findViewById(R.id.backButton);
        mProfilePicture        = (ImageView) findViewById(R.id.imageViewProfile);
        mCoverPhoto            = (ImageView) findViewById(R.id.coverPhoto);
        mAuth                  = FirebaseAuth.getInstance();
        mDatabase              = FirebaseDatabase.getInstance().getReference();
        mUser                  = mAuth.getCurrentUser();
        mLayoutProfile         = (LinearLayout) findViewById(R.id.profileUser);

        neoAnhbia              = (TextView) findViewById(R.id.neoAnhbia);
    }

    private void updateProfile(int i) {
        final int kt = i;
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.edit_profile);
        dialog.setCanceledOnTouchOutside(false);// hủy khi chạm ở bên ngoài;

        mUpdateButton          = (Button)   dialog.findViewById(R.id.updateButton);
        mExitButton            = (Button)   dialog.findViewById(R.id.exitButton);
        mUpdateText            = (EditText) dialog.findViewById(R.id.updateText);

        switch (i){
            case 1:
                mUpdateText.setText(nameText.getText().toString().trim());
                break;
            case 2:
                field = "email";
                mUpdateText.setText(emailText.getText().toString().trim());
                mUpdateText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT);
                break;

            case 3:
                field = "phone";
                if(!mPhone.getText().toString().trim().equals("XXXXXX")) {
                    mUpdateText.setText(mPhone.getText().toString().trim());
                }
                mUpdateText.setInputType(InputType.TYPE_CLASS_PHONE);
                break;

            case 4:
                field = "location";
                break;

            case 6:
                field = "birthday";
                break;

        }
        if(!field.equals("phone")) {
            mUpdateText.setSelection(mUpdateText.getText().length());
        }else {
            if (!mPhone.getText().toString().trim().equals("XXXXXX")){
                mUpdateText.setSelection(mUpdateText.getText().length());
            }
        }

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String s = mUpdateText.getText().toString();
                if(s.isEmpty()){
                    return;
                }
                String id = mUser.getUid();
                if(kt != 1 ){
                    mDatabase.child("User").child(id).child(field).setValue(s);

                }else {
                    mDatabase.child("User").child("profile").child(id).child("name").setValue(s);
                }

                dialog.dismiss();
            }
        });

        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void enventListeners() {

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation =  AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.scale_out_center);
                mLayoutActivity.startAnimation(animation);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },500);
            }
        });

        mProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ProfileActivity.this, mProfilePicture);
                popupMenu.getMenuInflater().inflate(R.menu.menu_profile, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.viewPic :
                                    Intent intent = new Intent(ProfileActivity.this, ProfilePicture.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.scale_zoom_center, R.anim.normal);
                                break;
                            case R.id.editNewPic:
                                    Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(intent1, REQUEST_CODE_FOLDER);
                                break;
                            case R.id.cameraNew:
                                    Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent2, REQUEST_CODE_CAPTURE);
                                break;

                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });

        mCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ProfileActivity.this, neoAnhbia);
                popupMenu.getMenuInflater().inflate(R.menu.menu_cover_photo, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent1, REQUEST_CODE_FOLDER1);
                        return false;
                    }
                });
                popupMenu.show();

            }
        });

        mNameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(1);
            }
        });

        mEmailEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(2);
            }
        });

        mPhoneEdtit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(3);
            }
        });

        mLiveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLive();
            }
        });

        mGanderEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGender();
            }
        });

        mBirthdayEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update birthday custom DatePickerFragment
                DatePickerFragment fragment = (DatePickerFragment) getFragmentManager().findFragmentByTag("datePicker");
                if(fragment != null) {
                    return;
                }else {
                    DatePickerFragment pickerFragment = new DatePickerFragment();
                    pickerFragment.show(getFragmentManager(), "datePicker");
                }
            }
        });


    }

    private void updateLive() {
        final  Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.edit_live_profile);
        dialog.setCanceledOnTouchOutside(false);// hủy khi chạm ở bên ngoài;

        mAutoEdit           = (AutoCompleteTextView) dialog.findViewById(R.id.autoEdit);
        mUpdateLiveButton   = (Button)     dialog.findViewById(R.id.updateLiveButton);
        mExitLiveButton     = (Button)     dialog.findViewById(R.id.exitLiveButton);



        if(!mLive.getText().toString().trim().equals("XXXXXX")){
            String province = mLive.getText().toString().trim();
            mAutoEdit.setText(province);
            mAutoEdit.setSelection(province.length());
        }

        String array[];
        array = getResources().getStringArray(R.array.array_province);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                ProfileActivity.this,
                android.R.layout.simple_list_item_1,
                array
        );
        mAutoEdit.setAdapter(arrayAdapter);
        mUpdateLiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String province = mAutoEdit.getText().toString().trim();
                mDatabase.child("User").child(mUser.getUid()).child("location").setValue(province);
                dialog.dismiss();
            }
        });

        mExitLiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void updateGender() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.edit_gender_profile);
        dialog.setCanceledOnTouchOutside(false);// hủy khi chạm ở bên ngoài;

        mGenderRadio        = (RadioGroup) dialog.findViewById(R.id.genderRadioGroup);
        mUpdateGenderButton = (Button)     dialog.findViewById(R.id.updateButtonGender);
        mExitGenderButton   = (Button)     dialog.findViewById(R.id.exitButtonGender);


        RadioButton maleRadio = (RadioButton) dialog.findViewById(R.id.radioButtonMale);
        RadioButton femaleRadio = (RadioButton) dialog.findViewById(R.id.radioButtonFemale);
        gender =  mGender.getText().toString().trim();
        if(gender.equals("nam")){
            maleRadio.setChecked(true);
            femaleRadio.setChecked(false);
        }else if (gender.equals("nữ")){
            maleRadio.setChecked(false);
            femaleRadio.setChecked(true);
        }else {
            gender = "nam";
            maleRadio.setChecked(true);
            femaleRadio.setChecked(false);
        }


        mGenderRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButtonMale:
                        gender = "nam";
                        break;
                    case  R.id.radioButtonFemale:
                        gender = "nữ";
                        break;
                }
            }
        });


        mUpdateGenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("User").child(mUser.getUid()).child("gender").setValue(gender);
                dialog.dismiss();
            }
        });

        mExitGenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CAPTURE && resultCode == RESULT_OK && data != null){
            mBitmap = (Bitmap) data.getExtras().get("data");

            //Set name images timecurent
            Calendar calendar = Calendar.getInstance();
            StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            final byte[] data1 = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(data1);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")
                    Uri uri =  taskSnapshot.getDownloadUrl();
                    Toast.makeText(ProfileActivity.this, "Thay đổi thành công!!!", Toast.LENGTH_SHORT).show();
                    mDatabase.child("User").child(mUser.getUid()).child("profilePic").setValue(String.valueOf(uri));
                }
            });
        }

        if(requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            mBitmap = BitmapFactory.decodeFile(picturePath);

            Calendar calendar = Calendar.getInstance();
            StorageReference mountainRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

            final byte[] data1 = baos.toByteArray();

            UploadTask uploadTask = mountainRef.putBytes(data1);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")
                    Uri uri = taskSnapshot.getDownloadUrl();
                    Toast.makeText(ProfileActivity.this, "Thay đổi thành công!!!", Toast.LENGTH_SHORT).show();
                    mDatabase.child("User").child(mUser.getUid()).child("profilePic").setValue(String.valueOf(uri));
                }
            });

        }

        if(requestCode == REQUEST_CODE_FOLDER1 && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            mBitmap = BitmapFactory.decodeFile(picturePath);

            Calendar calendar = Calendar.getInstance();
            StorageReference mountainRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

            final byte[] data1 = baos.toByteArray();

            UploadTask uploadTask = mountainRef.putBytes(data1);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")
                    Uri uri = taskSnapshot.getDownloadUrl();
                    mDatabase.child("User").child(mUser.getUid()).child("coverPhoto").setValue(String.valueOf(uri));
                    Toast.makeText(ProfileActivity.this, "Thay đổi thành công!!!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
