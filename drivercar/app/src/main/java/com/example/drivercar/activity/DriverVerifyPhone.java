package com.example.drivercar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.drivercar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class DriverVerifyPhone extends AppCompatActivity {

    String verificationId;
    FirebaseAuth FAuth;
    Button verify , Resend ;
    TextView txt;
    PinView entercode;
    String phoneno,fname,lname,password,confpassword,house,statee,cityy,Area,cpAddress,ImageURL,emailid,longitude,latitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_verify_phone);

        phoneno = getIntent().getStringExtra("phonenumber").trim();
        fname = getIntent().getStringExtra("fname").trim();
        lname = getIntent().getStringExtra("lname").trim();
        password = getIntent().getStringExtra("password").trim();
        house = getIntent().getStringExtra("house").trim();
        confpassword = getIntent().getStringExtra("confpassword").trim();
        statee = getIntent().getStringExtra("statee").trim();
        cityy = getIntent().getStringExtra("cityy").trim();
        Area = getIntent().getStringExtra("Area").trim();
        cpAddress = getIntent().getStringExtra("cpAddress").trim();
        ImageURL = getIntent().getStringExtra("ImageURL").trim();
        emailid = getIntent().getStringExtra("emailid").trim();
        latitude = getIntent().getStringExtra("latitude").trim();
        longitude = getIntent().getStringExtra("longitude").trim();

        entercode = (PinView) findViewById(R.id.code);
        txt = (TextView) findViewById(R.id.text);
        Resend = (Button)findViewById(R.id.Resendotp);
        verify = (Button) findViewById(R.id.Verify);
        FAuth = FirebaseAuth.getInstance();

        Resend.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);

        sendverificationcode(phoneno);


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = entercode.getText().toString().trim();
                Resend.setVisibility(View.INVISIBLE);

                if (code.isEmpty() && code.length()<6){
                    entercode.setError("Nh???p v??o code");
                    entercode.requestFocus();
                    return;
                }
                Toasty.success(DriverVerifyPhone.this, "??ang g???i code qua m??y b???n ?????i t??...!", Toast.LENGTH_SHORT, true).show();

//                Toast.makeText(ChefVerifyPhone.this, "??ang g???i code qua m??y b???n ?????i t??...", Toast.LENGTH_SHORT).show();
                verifyCode(code);



            }
        });

        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

                txt.setVisibility(View.VISIBLE);
                txt.setText("G???i l???i m?? trong v??ng"+millisUntilFinished/1000+"gi??y");

            }

            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                Resend.setVisibility(View.VISIBLE);
                txt.setVisibility(View.INVISIBLE);

            }
        }.start();

        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toasty.success(DriverVerifyPhone.this, "??ang g???i code qua m??y c???a b???n ?????i t??...!", Toast.LENGTH_SHORT, true).show();

//                Toast.makeText(ChefVerifyPhone.this, "??ang g???i code qua m??y c???a b???n ?????i t??...", Toast.LENGTH_SHORT).show();

                Resend.setVisibility(View.INVISIBLE);
                Resendotp(phoneno);

                new CountDownTimer(60000,1000){

                    @Override
                    public void onTick(long millisUntilFinished) {

                        txt.setVisibility(View.VISIBLE);
                        txt.setText("G???i l???i m?? trong v??ng"+millisUntilFinished/1000+"gi??y");

                    }

                    /**
                     * Callback fired when the time is up.
                     */
                    @Override
                    public void onFinish() {
                        Resend.setVisibility(View.VISIBLE);
                        txt.setVisibility(View.INVISIBLE);

                    }
                }.start();
            }
        });

    }

    private void Resendotp(String phonenum) {
        sendverificationcode(phonenum);
    }

    private void sendverificationcode(String number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                number,
                60,
                TimeUnit.SECONDS,
                this,
                mcallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                entercode.setText(code);  // Auto Verification // thi???t l???p t??? ????ng nh???p th??nh c??ng

                verifyCode(code);

            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toasty.error(DriverVerifyPhone.this, ""+e.getMessage(), Toast.LENGTH_SHORT, true).show();

//            Toast.makeText(ChefVerifyPhone.this , e.getMessage(),Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(String s , PhoneAuthProvider.ForceResendingToken forceResendingToken){
            super.onCodeSent(s,forceResendingToken);

            verificationId = s;

        }
    };

    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId , code);
        linkCredential(credential);
    }

    private void linkCredential(PhoneAuthCredential credential) {

        FAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(DriverVerifyPhone.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            phoneno = getIntent().getStringExtra("phonenumber").trim();
                            fname = getIntent().getStringExtra("fname").trim();
                            lname = getIntent().getStringExtra("lname").trim();
                            password = getIntent().getStringExtra("password").trim();
                            house = getIntent().getStringExtra("house").trim();
                            confpassword = getIntent().getStringExtra("confpassword").trim();
                            statee = getIntent().getStringExtra("statee").trim();
                            cityy = getIntent().getStringExtra("cityy").trim();
                            Area = getIntent().getStringExtra("Area").trim();
                            cpAddress = getIntent().getStringExtra("cpAddress").trim();
                            ImageURL = getIntent().getStringExtra("ImageURL").trim();
                            emailid = getIntent().getStringExtra("emailid").trim();
                            latitude = getIntent().getStringExtra("latitude").trim();
                            longitude = getIntent().getStringExtra("longitude").trim();

                            Intent intent = new Intent(DriverVerifyPhone.this , DriverInfomationActivity.class);
                            intent.putExtra("phonenumber",phoneno);
                            intent.putExtra("fname",fname);
                            intent.putExtra("lname",lname);
                            intent.putExtra("password",password);
                            intent.putExtra("house",house);
                            intent.putExtra("confpassword",confpassword);
                            intent.putExtra("statee",statee);
                            intent.putExtra("cityy",cityy);
                            intent.putExtra("Area",Area);
                            intent.putExtra("cpAddress",cpAddress);
                            intent.putExtra("ImageURL",ImageURL);
                            intent.putExtra("emailid",emailid);
                            intent.putExtra("latitude",latitude);
                            intent.putExtra("longitude",longitude);

                            startActivity(intent);
                            finish();
                        }else{
                            ReusableCodeForAll.ShowAlert(DriverVerifyPhone.this,"Error",task.getException().getMessage());
                        }
                    }
                });

    }
}