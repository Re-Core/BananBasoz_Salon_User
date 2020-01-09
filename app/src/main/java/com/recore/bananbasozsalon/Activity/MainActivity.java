package com.recore.bananbasozsalon.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.recore.bananbasozsalon.Common.Common;
import com.recore.bananbasozsalon.Model.User;
import com.recore.bananbasozsalon.R;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1000;
    //added
    private static final String TAG = "PhoneLogin";
    TextInputEditText edtUsername, edtPhoneNumber, edtVerificationCode;
    TextInputLayout usernameContainer, phoneContainer, codeContainer;
    Button btnVerify;
    AlertDialog waitingDialog;
    private Button btnSignUp;
    private LinearLayout registerText;
    private Intent destination;
    private Button btnContinue;
    private FirebaseDatabase db;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthCredential credential;
    private ConstraintLayout main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        registerText = (LinearLayout) findViewById(R.id.txtSignIn);
        usernameContainer = (TextInputLayout) findViewById(R.id.usernameContainer);
        phoneContainer = (TextInputLayout) findViewById(R.id.phoneLoginContainer);
        codeContainer = (TextInputLayout) findViewById(R.id.codeContainer);


        mAuth = FirebaseAuth.getInstance();

        main_layout = (ConstraintLayout) findViewById(R.id.main_layout);

        edtUsername = (TextInputEditText) findViewById(R.id.edtUserName);
        edtPhoneNumber = (TextInputEditText) findViewById(R.id.edtUserPhone);
        edtVerificationCode = (TextInputEditText) findViewById(R.id.edtValidationCode);


        btnVerify = (Button) findViewById(R.id.btnSignUpValidateCode);
        waitingDialog = new SpotsDialog(this);
        // waitingDialog.setMessage(getResources().getString(R.string.waiting_string));
        waitingDialog.setCancelable(false);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
                Toast.makeText(MainActivity.this, "Verification Complete", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                waitingDialog.dismiss();
                // Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(MainActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                Log.d("FirebaseException", e.getMessage());
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
//                    Toast.makeText(MainActivity.this,"InValid Phone Number",Toast.LENGTH_SHORT).show();
//                    edtPhoneNumber.setError(getResources().getString(R.string.invalid_phone),getDrawable(R.drawable.ic_error_outline_black_24dp));
                    if (main_layout != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(main_layout.getWindowToken(), 0);
                    }
                    Snackbar.make(main_layout, "Invalid phone number", Snackbar.LENGTH_LONG).show();

                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {

                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // Log.d(TAG, "onCodeSent:" + verificationId);
                waitingDialog.dismiss();
                Toast.makeText(MainActivity.this, "Verification code has been send on your number", Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                phoneContainer.setVisibility(View.GONE);
                usernameContainer.setVisibility(View.GONE);
                btnSignUp.setVisibility(View.GONE);
                registerText.setVisibility(View.GONE);

                codeContainer.setVisibility(View.VISIBLE);
                btnVerify.setVisibility(View.VISIBLE);
            }
        };

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edtPhoneNumber.getText().toString())) {
                    waitingDialog.show();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+964" +
                                    edtPhoneNumber.getText().toString(),
                            60,
                            java.util.concurrent.TimeUnit.SECONDS,
                            MainActivity.this,
                            mCallbacks);
                } else {
                    waitingDialog.dismiss();
//                    Toast.makeText(MainActivity.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
//                    edtPhoneNumber.setError(getResources().getString(R.string.no_phone),getDrawable(R.drawable.ic_error_outline_black_24dp));
                    if (main_layout != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    Snackbar.make(main_layout, "Phone number should be of format 07xx", Snackbar.LENGTH_LONG).show();
                }

            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edtVerificationCode.getText().toString())) {
                    waitingDialog.show();
                    credential = PhoneAuthProvider.getCredential(mVerificationId, edtVerificationCode.getText().toString());
                }
                // [END verify_with_code]
                signInWithPhoneAuthCredential(credential);
            }
        });


        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destination = new Intent(MainActivity.this, SignInActivity.class);
                gotoActivity(destination);
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {


        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()) {
                            // Log.d(TAG, "signInWithCredential:success");
                            userRef = FirebaseDatabase.getInstance().getReference(Common.UserInformation);
                            final String userPhone = edtPhoneNumber.getText().toString();
                            //check if number exist in firebase
                            Log.d("TAG", "inside of signInWithCredential / onComplete");

                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (!dataSnapshot.child(userPhone).exists()) {
                                        //if user does not exist create new user and added to database
                                        User user = new User();
                                        user.setUserPhone(userPhone);
                                        user.setUsername(edtUsername.getText().toString());
                                        user.setUserAvatar("");
                                        user.setUserId(userPhone);
                                        user.setUserTimeStamp(ServerValue.TIMESTAMP);

                                        userRef.child(edtPhoneNumber.getText().toString())
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        //login
                                                        userRef.child(edtPhoneNumber.getText().toString())
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        Common.currentUser = dataSnapshot.getValue(User.class);

                                                                        Paper.book().write("currentUser", Common.currentUser);


                                                                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                                                        startActivity(intent);
                                                                        waitingDialog.dismiss();
                                                                        finish();
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                waitingDialog.dismiss();
                                                Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else {
                                        //if user already have an account
//                                        userRef.child(userPhone)
//                                                .addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                        Common.currentUser = dataSnapshot.getValue(User.class);
//
//                                                        Paper.book().write("currentUser", Common.currentUser);
//
//                                                        Intent intent =new Intent(MainActivity.this,HomeActivity.class);
//                                                        startActivity(intent);
//                                                        waitingDialog.dismiss();
//                                                        finish();
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                    }
//                                                });

                                        Snackbar.make(main_layout, "User already registered please sign in", Snackbar.LENGTH_LONG).show();
                                        waitingDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Snackbar.make(main_layout, databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
                                    waitingDialog.dismiss();
                                }
                            });

                            Toast.makeText(MainActivity.this, "Verification Done", Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                waitingDialog.dismiss();
//                                Toast.makeText(MainActivity.this,"Invalid Verification",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    private void gotoActivity(Intent destination) {
        startActivity(destination);
    }

}
