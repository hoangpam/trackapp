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
import com.example.drivercar.bottomnavigation.DriverPanel_BottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class Driversendotp extends AppCompatActivity {

    String verificationId;
    FirebaseAuth FAuth;
    Button verify , Resend ;
    TextView txt;
//    EditText entercode;
    String phoneno;
    PinView entercode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driversendotp);

        phoneno = getIntent().getStringExtra("phonenumber").trim();

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
                    entercode.setError("Nhập vào code");
                    entercode.requestFocus();
                    return;
                }
                Toasty.success(Driversendotp.this, "Đang gửi code qua máy bạn đợi tí...!", Toast.LENGTH_SHORT, true).show();

//                Toast.makeText(Chefsendotp.this, "Đang gửi code qua máy bạn đợi tí...", Toast.LENGTH_SHORT).show();
                verifyCode(code);



            }
        });

        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

                txt.setVisibility(View.VISIBLE);
                txt.setText("Gửi lại mã trong vòng "+millisUntilFinished/1000+" giây");

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
                Toasty.success(Driversendotp.this, "Đang gửi code qua máy bạn đợi tí...!", Toast.LENGTH_SHORT, true).show();

//                Toast.makeText(Chefsendotp.this, "Đang gửi code qua máy bạn đợi tí...", Toast.LENGTH_SHORT).show();

                Resend.setVisibility(View.INVISIBLE);
                Resendotp(phoneno);

                new CountDownTimer(60000,1000){

                    @Override
                    public void onTick(long millisUntilFinished) {

                        txt.setVisibility(View.VISIBLE);
                        txt.setText("Gửi lại mã trong vòng "+millisUntilFinished/1000+" giây");

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

                    entercode.setText(code);  // Auto Verification
                    verifyCode(code);

            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toasty.error(Driversendotp.this, ""+e.getMessage(), Toast.LENGTH_SHORT, true).show();

//            Toast.makeText(Chefsendotp.this , e.getMessage(),Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(String s , PhoneAuthProvider.ForceResendingToken forceResendingToken){
            super.onCodeSent(s,forceResendingToken);

            verificationId = s;

        }
    };

    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId , code);
        signInWithPhone(credential);
    }

    private void signInWithPhone(PhoneAuthCredential credential) {


            FAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                startActivity(new Intent(Driversendotp.this, DriverPanel_BottomNavigation.class));
                                finish();

                            }else{

                                Toasty.warning(Driversendotp.this, "Vui lòng đăng ký tài khoản để đăng nhập bằng số điện thoại", Toast.LENGTH_SHORT, true).show();
                                Intent b = new Intent(Driversendotp.this, MainMenu.class);
                                startActivity(b);
                                ReusableCodeForAll.ShowAlert(Driversendotp.this,"Error",task.getException().getMessage());
                                finish();
                            }

                        }
                    });
        }


    }
