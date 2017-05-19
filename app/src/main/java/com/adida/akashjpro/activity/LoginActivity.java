package com.adida.akashjpro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adida.akashjpro.activity.utils.ShowHideProgessDialog;
import com.adida.akashjpro.livevideo.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import static com.adida.akashjpro.activity.MainActivity.isProgress;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private ImageButton mBackButton;
    private Button mLoginButton, mGoogleButton, mFb;
    private LoginButton mFaceButton;
    private EditText mEmailEditext, mPasswordEdittext;
    private TextView mRegisterText, mResetPasswordText;
    private FirebaseAuth mAuth;
    private LinearLayout mActivityLogin;
    private TextInputLayout mContainEamil, mContainPassword;
    private String mEmail = "";
    private String mPassword = "";
    private final int RC_SIGN_IN = 123;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;
    private DatabaseReference mDatabase;
    public static ShareDialog shareDialog;
    private boolean isLoginGoogle = false;


    private static final  String TAG = "MAIN_ACTIVITY";

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);



        addControls();

        mAuth = FirebaseAuth.getInstance();
        eventListeners();

        mFaceButton = (LoginButton) findViewById(R.id.faceLogin_button);
        mCallbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        mFaceButton.setReadPermissions("email", "public_profile", "user_birthday", "user_location", "user_hometown");
        mFaceButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d(TAG, "Object: "+ object.toString());
//                                Toast.makeText(LoginActivity.this, "object: "+ object.toString(), Toast.LENGTH_SHORT).show();

                                /**
                                 *  get information of user from facebook
                                 */
                                try {
                                    MainActivity.gender = object.getString("gender");
                                    MainActivity.birthday = object.getString("birthday");
                                    MainActivity.location = object.getJSONObject("location").getString("name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                // Cac truong truy xuat lay thong tin from facebook
                parameters.putString("fields", "id,name,email,gender,birthday,location");
                //get day du hon
               // parameters.putString("fields", "id,name,email,gender,birthday,hometown,locale,location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }
    private void handleFacebookAccessToken(AccessToken token) {

       // Toast.makeText(this, "Token: "+ token, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                       // Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        final FirebaseUser user = mAuth.getCurrentUser();
                            String id   = user.getUid();
                            String name = user.getDisplayName();
                            String url  = user.getPhotoUrl().toString();
                            //Push name and profilepicture to firebase
//                        mDatabase.child("User").child("profile").child(id).child("name").setValue(name);
//                        mDatabase.child("User").child("profile").child(id).child("profilePic").setValue(url);
                            mDatabase.child("User").child(id).child("name").setValue(name);
                            mDatabase.child("User").child(id).child("profilePic").setValue(url);
                            mDatabase.child("User").child(id).child("coverPhoto").setValue(MainActivity.mUrlCoverPhoto);
                            ShowHideProgessDialog.showHideProgessDialog(LoginActivity.this);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 3000);




                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    /**
     * Listeners event from controls
     */
    private void eventListeners() {


        mFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFaceButton.performClick();
            }
        });


        //Click button Back menu
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation =  AnimationUtils.loadAnimation(LoginActivity.this, R.anim.scale_out);
                mActivityLogin.startAnimation(animation);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },500);

            }
        });

        //Click button Login
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //Click button Google to login
        mGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoginGoogle = true;
                ShowHideProgessDialog.showHideProgessDialog(LoginActivity.this);
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
//                        .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();

                signIn();
            }
        });



        mRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });

        mEmailEditext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mEmail    = mEmailEditext.getText().toString().trim();
                if(!hasFocus){
                    if(mEmail.isEmpty()){
                        mContainEamil.setErrorEnabled(true);
                        mContainEamil.setError("Email không được để trống");
                        return;
                    }
                }else {
                    mContainEamil.setError("");
                }
            }
        });

        mPasswordEdittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mPassword = mPasswordEdittext.getText().toString().trim();
                if(!hasFocus){
                    if(mPassword.isEmpty()){
                        mContainPassword.setErrorEnabled(true);
                        mContainPassword.setError("Mật khẩu không được để trống");
                        return;
                    }
                }else {
                    mContainPassword.setError("");
                }

            }
        });

        mResetPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditext.getText().toString().trim();
                if(email.isEmpty()){
                    mContainEamil.setErrorEnabled(true);
                    mContainEamil.setError("Nhập email để lấy mật khẩu");
                }else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,
                                        "Xin vui lòng kiểm tra email để đặt lại mật khẩu!",
                                        Toast.LENGTH_SHORT
                                ).show();

                            }else {
                                Toast.makeText(LoginActivity.this,
                                        "Lỗi, xin vui lòng kiểm tra lại email!",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    });


                }

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShowHideProgessDialog.showHideProgessDialog(LoginActivity.this);
        if(!isLoginGoogle){
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }else {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    isProgress = true;
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                } else {
                    // Google Sign In failed, update UI appropriately
                    // ...
                    Toast.makeText(this, "Fail authenticate with Google", Toast.LENGTH_SHORT).show();
                }
            }
        }




    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        FirebaseUser user = mAuth.getCurrentUser();

                        String id   = user.getUid();
                        String name = user.getDisplayName();
                        String url  = user.getPhotoUrl().toString();
                        //Push name and profilepicture to firebase
//                        mDatabase.child("User").child("profile").child(id).child("name").setValue(name);
//                        mDatabase.child("User").child("profile").child(id).child("profilePic").setValue(url);

                        mDatabase.child("User").child(id).child("name").setValue(name);
                        mDatabase.child("User").child(id).child("profilePic").setValue(url);
                        mDatabase.child("User").child(id).child("coverPhoto").setValue(MainActivity.mUrlCoverPhoto);

                        ShowHideProgessDialog.showHideProgessDialog(LoginActivity.this);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 3000);
                        // ...
                    }
                });
    }


    /**
     * function addControls from file xml
     */
    private void addControls() {
        MainActivity.isLoginFirst = false; // never login

        mDatabase              = FirebaseDatabase.getInstance().getReference();

        mBackButton     = (ImageButton) findViewById(R.id.imgbtMenuBack);

        mLoginButton    = (Button) findViewById(R.id.buttonLogin);
        mGoogleButton   = (Button) findViewById(R.id.buttonGoogle);
        mFb             = (Button) findViewById(R.id.fb);

        mEmailEditext       = (EditText) findViewById(R.id.editTextEamil);
        mPasswordEdittext   = (EditText) findViewById(R.id.editTextPassword);

        mRegisterText       = (TextView) findViewById(R.id.textViewRegister);
        mResetPasswordText  = (TextView) findViewById(R.id.textViewResetPassword);
        mActivityLogin      = (LinearLayout) findViewById(R.id.activity_login);

        mContainEamil       = (TextInputLayout) findViewById(R.id.containEmail);
        mContainPassword    = (TextInputLayout) findViewById(R.id.contaiPassword);


    }


//    public void showFragment(int i, FragmentTransaction fragmentTransaction){
//        switch (i){
//            case 0:
//                fragmentTransaction.show(trangChu);
//                break;
//            case 1:
//                fragmentTransaction.show(fragmentFilm);
//                break;
//            case 2:
//                fragmentTransaction.show(fragmentMusic);
//                break;
//            case 3:
//                fragmentTransaction.show(fragmentLesson);
//                break;
//            case 4:
//                fragmentTransaction.show(fragmentAudioBook);
//                break;
//            case 5:
//                fragmentTransaction.show(fragmentVideoFavorite);
//                break;
//        }
 //   }


    /**
     * Fuction login
     */
    private void login(){
        mEmail    = mEmailEditext.getText().toString().trim();
        mPassword = mPasswordEdittext.getText().toString().trim();
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
        mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(( LoginActivity.this), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            ShowHideProgessDialog.showHideProgessDialog(LoginActivity.this);
                            finish();


                        }else {
                            if(task.getException().toString().contains("formatted")){
                                mContainEamil.setError("Email không đúng định dạng, xin nhập lại!!!");
                               // mEmailEditext.requestFocus();
                            }
                            if(task.getException().toString().contains("no user")){
                                mContainEamil.setError("Email chưa đăng ký, xin nhập đúng emai đã đăng ký hoặc tạo tài khoản mới!!!");
                                //mEmailEditext.requestFocus();
                            }
                            if(task.getException().toString().contains("password is invalid")){
                                mContainPassword.setError("Mật khẩu không chính xác, xin nhập lại!!!");
//                                mPasswordEdittext.setError("Mật khẩu không chính xác, xin nhập lại!!!");
//                                mPasswordEdittext.requestFocus();
                            }


//                            Toast.makeText(LoginActivity.this, "Error!!!" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Log.d("TAG", "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
    }
}
