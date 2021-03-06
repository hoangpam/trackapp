package com.example.drivercar.activity;

import static android.graphics.Typeface.BOLD_ITALIC;
import static com.example.drivercar.R.string.ban;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drivercar.R;
import com.example.drivercar.bottomnavigation.DriverPanel_BottomNavigation;
import com.example.drivercar.databinding.ActivityMainBinding;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;


public class MainMenu extends AppCompatActivity {

    Button signinemail,signinphone,signup,googleSinginBtn,facebookSinginBtn;
    ImageView bgimage;
    private LoginButton loginButton;
    //view binding
    private ActivityMainBinding binding;
    private static final int RC_SIGN_IN = 100;

    private GoogleSignInClient googleSignInClient;

    private CallbackManager mCallbackManager;

    FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private  static final String TAG = "GOOGLE_SIGN_IN_TAG";
    private  static final String TAG1 = "Facebook_Authencation_TAG";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Users");
    private Uri image_uri;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference ref;
    private AccessTokenTracker accessTokenTracker;

    double latitude = 0.0, longitude = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        final Animation zoomin = AnimationUtils.loadAnimation(this,R.anim.zoomin);
        final Animation zoomout = AnimationUtils.loadAnimation(this,R.anim.zoomout);

        bgimage=findViewById(R.id.back2);
        bgimage.setAnimation(zoomin);
        bgimage.setAnimation(zoomout);

        zoomout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bgimage.startAnimation(zoomin);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        zoomin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bgimage.startAnimation(zoomout);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        signinemail=(Button)findViewById(R.id.SignwithEmail);
        signinphone=(Button)findViewById(R.id.SignwithPhone);
        signup=(Button)findViewById(R.id.Signup);

//        facebookSinginBtn=(Button) findViewById(R.id.facebookSinginBtn);
//        loginButton = findViewById(R.id.login_button);
//        facebookSinginBtn.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View v) {
//                Toasty.Config.getInstance()
//                        .setToastTypeface(Typeface.createFromAsset(getAssets(), "font-times-new-roman.ttf"))
//                        .allowQueue(false)
//                        .apply();
//                Toasty.custom(MainMenu.this, ban, getResources().getDrawable(R.drawable.ic_facebook),
//                        android.R.color.black, android.R.color.holo_green_light, Toasty.LENGTH_SHORT, true, true).show();
////                Toasty.info(MainMenu.this, getFormattedMessage()).show();
////                Toasty.error(MainMenu.this, "??ang ho??n thi???n ch???c n??ng", Toast.LENGTH_SHORT,true).show();
////                LoginManager.getInstance().logInWithReadPermissions(MainMenu.this, Arrays.asList("user_photos", "email", "public_profile", "user_posts"));
////                LoginManager.getInstance().logInWithPublishPermissions(MainMenu.this, Arrays.asList("publish_actions"));
////                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
////                    @Override
////                    public void onSuccess(LoginResult loginResult) {
////                        Log.d(TAG1,"onSuccess"+ loginResult);
////                        handlerFacebookToken(loginResult.getAccessToken());
////                    }
////
////                    @Override
////                    public void onCancel() {
////                        Log.d(TAG1,"onCancel");
////                    }
////
////                    @Override
////                    public void onError(FacebookException error) {
////                        Log.d(TAG1,"onError"+error);
////                    }
////                });
////
////                authStateListener = new FirebaseAuth.AuthStateListener() {
////                    @Override
////                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
////                        FirebaseUser user = firebaseAuth.getCurrentUser();
////                        if(user != null){
////                            updateUI(user);
////                        }else {
////                            updateUI(null);
////                        }
////                    }
////                };
////
////                accessTokenTracker = new AccessTokenTracker() {
////                    @Override
////                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
////                        if(currentAccessToken == null){
////                            firebaseAuth.signOut();
////                        }
////                    }
////                };
////                LoginManager.getInstance().retrieveLoginStatus(this, new LoginStatusCallback() {
////                    @Override
////                    public void onCompleted(AccessToken accessToken) {
////                        Log.d(TAG1,"onSuccess"+ accessToken);
////                        handlerFacebookToken(accessToken);
////                    }
////
////                    @Override
////                    public void onFailure() {
////                        Log.d(TAG1,"onFailure");
////                    }
////
////                    @Override
////                    public void onError(Exception exception) {
////                        Log.d(TAG1,"onError"+exception);
////                    }
////                });
//            }
//        });
//        Handler mHandler = new Handler();
//        FacebookSdk.InitializeCallback initializeCallback = new FacebookSdk.InitializeCallback() {
//            @Override
//            public void onInitialized() {
//
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //UI Code Here
//                    }
//                });
//            }
//        };
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
//
//        mCallbackManager = CallbackManager.Factory.create();







        //configure the Google Signin
//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

//        googleSinginBtn = findViewById(R.id.googleSinginBtn);

        //Google SignInButton Click to begin Google SignIn
//        binding.getRoot().findViewById(R.id.googleSinginBtn)
//        googleSinginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //begin google sign in
//                final ProgressDialog mDialog = new ProgressDialog(MainMenu.this);
//                mDialog.setCancelable(false);
//                mDialog.setCanceledOnTouchOutside(false);
//                mDialog.setTitle("\uD83D\uDC37 \n T??nh h??nh m??y y???u vui l??ng ch???");
//                mDialog.setMessage("??ang m??? th??ng b??o ......");
//                mDialog.show();
//                Log.d(TAG, "onClick: begin Google SignIn && { B???t ?????u ????ng nh???p Google }");
//                Toasty.warning(MainMenu.this, "\uD83D\uDC37 \nT??nh h??nh m??y y???u vui l??ng ch???", Toast.LENGTH_LONG,true).show();
//                Intent intent = googleSignInClient.getSignInIntent();
//                startActivityForResult(intent, RC_SIGN_IN);
//            }
//        });


        signinemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signemail = new Intent(MainMenu.this,Driverlogin.class);
                signemail.putExtra("Home","Email");
                startActivity(signemail);
                finish();
            }
        });

        signinphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signphone = new Intent(MainMenu.this,Driverloginphone.class);
                signphone.putExtra("Home","Phone");
                startActivity(signphone);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(MainMenu.this,DriverRegistration.class);
                signup.putExtra("Home","SignUp");
                startActivity(signup);
                finish();
            }
        });
    }

//    @SuppressLint("LongLogTag")
//    private void handlerFacebookToken(AccessToken accessToken) {
//        Log.d(TAG1,"handleFacebookToken"+accessToken);
//
//        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
//        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @SuppressLint("LongLogTag")
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(MainMenu.this, "???? ??ang nh???p th??nh c??ng", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG1,"sign in with credential: successful");
//                    FirebaseUser user = firebaseAuth.getCurrentUser();
//                    updateUI(user);
//                }else {
//                    Log.d(TAG1,"sign in with credential: failure\n",task.getException());
//                    Toasty.error(MainMenu.this, "Authencation Failed", Toast.LENGTH_SHORT,true).show();
//                    updateUI(null);
//                }
//            }
//        });
//    }

//    @SuppressLint("LongLogTag")
//    private void updateUI(FirebaseUser user) {
//        if(user != null){
//            Log.d(TAG1,"sign in whith user");
//            Toasty.success(this, "tt", Toast.LENGTH_SHORT,true).show();
//        }else {
//            Log.d(TAG1,"errorf");
//            Toasty.error(this, "L???i k??a", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    protected void onStart() {

        super.onStart();

//        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected  void onStop(){
        super.onStop();

//        if(authStateListener != null){
//            firebaseAuth.removeAuthStateListener(authStateListener);
//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
////        mCallbackManager.onActivityResult(requestCode,resultCode,data);
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        //Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(....)
//        if(requestCode == RC_SIGN_IN){
//            Log.d(TAG,"onActivityResult: Google Signin intent result && { K???t qu??? ?? ?????nh c???a Google Signin }");
//            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                //google sign in success, now auth with firebase
//                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
//                firebaseAuthWithGoogleAccount(account);
//            }catch (Exception e){
//                //failed google sign in
//                Log.e(TAG,"onActivityResult: "+e.getMessage());
//                ReusableCodeForAll.ShowAlert(this,"??ang l???i",""+e.getMessage());
//            }
//        }
//
//    }

    private CharSequence getFormattedMessage() {
        final String prefix = "Formatted ";
        final String highlight = "bold italic";
        final String suffix = " text";
        SpannableStringBuilder ssb = new SpannableStringBuilder(prefix).append(highlight).append(suffix);
        int prefixLen = prefix.length();
        ssb.setSpan(new StyleSpan(BOLD_ITALIC),
                prefixLen, prefixLen + highlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    private void Erro() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("\uD83D\uDC37 \n ?? k??a l???i");
        alerta.setMessage("???????ng d???n th???t b???i ki???m tra l???i m??y c???a b???n");
        alerta.setPositiveButton("OK", null);
        alerta.show();
    }
    public void UpdateDM(){

    }
//    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
//        Log.d(TAG,"firebaseAuthWithGoogleAccount: begin firebase auth with google acccount && { B???t ?????u x??c th???c firebase v???i t??i kho???n google }");
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
//
//        final ProgressDialog mDialog = new ProgressDialog(MainMenu.this);
//        mDialog.setCancelable(false);
//        mDialog.setCanceledOnTouchOutside(false);
//        mDialog.setTitle("T??nh h??nh m???ng y???u");
//        mDialog.setMessage("??ang ????ng k?? v?? t???i h??nh ?????i di???n l??n, vui l??ng ?????i......");
//        mDialog.show();
//
//        firebaseAuth.signInWithCredential(credential)
//                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult authResult) {
//                        //login success
//                        Log.d(TAG,"onSuccess: Logged In");
//
//                        if(image_uri == null ){
//                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//                            String uid = firebaseUser.getUid();
//                            String fname = firebaseUser.getDisplayName();
//                            String emailid = firebaseUser.getEmail();
//                            String phone =firebaseUser.getPhoneNumber();
//                            String timestamp = ""+System.currentTimeMillis();
//                            Uri photo = firebaseUser.getPhotoUrl();
//
//
//                            HashMap<String, String> hashMap1 = new HashMap<>();
//                            hashMap1.put("UID", ""+uid);
//                            hashMap1.put("MobileNo",""+phone);
//                            hashMap1.put("LastName", "");
//                            hashMap1.put("City", "H??? Ch?? Minh");
//                            hashMap1.put("Area", "T??n " +
//                                    "");//ph?????ng x??
//                            hashMap1.put("Password", "123456789");
//                            hashMap1.put("CompleteAddress", "123 L?? Qu?? Ph?????ng T??n Qu?? Qu???n T??n Ph?? Th??nh ph??? H??? Ch?? Minh");
//                            hashMap1.put("State", "T??n Ph??");//Qu???n huy???n
//                            hashMap1.put("ConfirmPassword", "123456789");
//                            hashMap1.put("House", "123");//S??? ???????ng
//                            hashMap1.put("ImageURL", "");//url of uploadted image
//                            hashMap1.put("ImageURL1", "");//url of uploadted image
//                            hashMap1.put("ImageURL2", "");//url of uploadted image
//                            hashMap1.put("ImageURL3", "");//url of uploadted image
//                            hashMap1.put("Latitude", "0.0" );
//                            hashMap1.put("Longitude", "0.0" );
//                            hashMap1.put("Online", "true");
//                            hashMap1.put("DriverOpen", "true");
//                            hashMap1.put("AccountType","Driver");
//                            hashMap1.put("FirstName", fname);
//                            hashMap1.put("EmailId", emailid);
//                            hashMap1.put("Timestamp", ""+timestamp);//+DateFormat.getTimeInstance().format(new Date())
//
//                            databaseReference
//                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                    .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    mDialog.dismiss();
//                                    if(task.isSuccessful()){
//
//                                        Toasty.success(MainMenu.this, "????ng nh???p th??nh c??ng", Toast.LENGTH_SHORT,true).show();
//                                        Intent Z = new Intent(MainMenu.this, DriverInfomationActivity.class);
//
//                                        startActivity(Z);
//                                        finish();
//                                    }
//                                    else{
//                                        mDialog.dismiss();
//                                        ReusableCodeForAll.ShowAlert(MainMenu.this,"K???t n???i c???a b???n ??ang b??? l???i",task.getException().getMessage());
//                                    }
//
//                                }
//                            });
//
//                        }else{
//                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//
//                            String filePathAndName = "profile_image/" + "" + firebaseAuth.getUid();
//
//                            image_uri = firebaseUser.getPhotoUrl();
//
//                            final ProgressDialog progressDialog = new ProgressDialog(MainMenu.this);
//                            progressDialog.setCancelable(false);
//                            progressDialog.setCanceledOnTouchOutside(false);
//                            progressDialog.setIndeterminate(false);
//                            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                            progressDialog.setProgress(0);
//                            progressDialog.setTitle("T??nh h??nh m???ng y???u");
//                            progressDialog.setMessage("??ang ????ng k?? v?? t???i h??nh ???nh ?????i di???n l??n, vui l??ng ?????i t??....");
//                            progressDialog.show();
//
//                            //d??nh cho truy c???p firebase
//                            storage = FirebaseStorage.getInstance();
//                            storageReference = storage.getReference();
//
//                            ref = FirebaseStorage.getInstance().getReference(filePathAndName);
//
//                            ref.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                                    while (!uriTask.isSuccessful()) ;
//                                    Uri downloadImageUri = uriTask.getResult();
//
//                                    String uid = firebaseUser.getUid();
//                                    String fname = firebaseUser.getDisplayName();
//                                    String emailid = firebaseUser.getEmail();
//                                    String phone =firebaseUser.getPhoneNumber();
//                                    String timestamp = ""+System.currentTimeMillis();
//
//                                    HashMap<String, String> hashMap1 = new HashMap<>();
//                                    hashMap1.put("UID", "" + uid);
//                                    hashMap1.put("MobileNo", ""+phone);
//                                    hashMap1.put("LastName", "");
//                                    hashMap1.put("City", "H??? Ch?? Minh");
//                                    hashMap1.put("Area", "T??n Qu??");//ph?????ng x??
//                                    hashMap1.put("Password", "123456789");
//                                    hashMap1.put("CompleteAddress", "123 L?? Qu?? Ph?????ng T??n Qu?? Qu???n T??n Ph?? Th??nh ph??? H??? Ch?? Minh");
//                                    hashMap1.put("State", "T??n Ph??");//Qu???n huy???n
//                                    hashMap1.put("ConfirmPassword", "123456789");
//                                    hashMap1.put("House", "123");//S??? ???????ng
//                                    hashMap1.put("Latitude", "0.0" );
//                                    hashMap1.put("Longitude", "0.0" );
//                                    hashMap1.put("Online", "true");
//                                    hashMap1.put("DriverOpen", "true");
//                                    hashMap1.put("AccountType","Driver");
//                                    hashMap1.put("FirstName", fname);
//                                    hashMap1.put("EmailId", emailid);
//                                    hashMap1.put("ImageURL", ""+downloadImageUri);
//                                    hashMap1.put("ImageURL1", ""+downloadImageUri);
//                                    hashMap1.put("ImageURL2", ""+downloadImageUri);
//                                    hashMap1.put("ImageURL3", ""+downloadImageUri);
//                                    hashMap1.put("Timestamp", "" + timestamp);//+DateFormat.getTimeInstance().format(new Date())
//
//
//
//                                    databaseReference
//                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                            .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if(task.isSuccessful()){
//                                                progressDialog.dismiss();
//                                                mDialog.dismiss();
//
//                                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                                                while (!uriTask.isSuccessful()) ;
//                                                Uri downloadImageUri = uriTask.getResult();
//                                                String image = ""+downloadImageUri;
//
//                                                Toasty.success(MainMenu.this, "????ng nh???p th??nh c??ng", Toast.LENGTH_SHORT,true).show();
//                                                Intent Z = new Intent(MainMenu.this, DriverInfomationActivity.class);
//                                                startActivity(Z);
//                                                finish();
//                                            }
//                                            else{
//                                                progressDialog.dismiss();
//                                                mDialog.dismiss();
//                                                ReusableCodeForAll.ShowAlert(MainMenu.this,"K???t n???i c???a b???n ??ang b??? l???i",task.getException().getMessage());
//                                            }
//
//                                        }
//                                    });
//
//                                }
//                            })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//
//                                }
//                            })
//                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                                        @Override
//                                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
//                                            progressDialog.setMessage("??ang t???i " + (int) progress + "%");
//                                            progressDialog.setCanceledOnTouchOutside(false);
//                                        }
//                                    });
//
//                        }
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        //login failed
//                        Erro();
//                        Log.e(TAG,"onFailure: Logged failed &&{ Ghi nh???t k?? kh??ng th??nh c??ng }\n"+e.getMessage());
//                        Toasty.error(MainMenu.this, "????ng nh???p kh??ng th??nh c??ng\n"+e.getMessage(), Toast.LENGTH_SHORT,true).show();
//                    }
//                });
//
//
//
//    }
}