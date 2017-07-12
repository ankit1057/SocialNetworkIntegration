package com.socialnetworkintegration.javabasic.google;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.socialnetworkintegration.R;

/**
 * Created by tasol on 11/7/17.
 */

public class GooglePlusDash extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{
    TextView tvName,tvEmail;
    ImageView ivProfilePic;
    private GoogleApiClient googleApiClient;
    private ProgressDialog mProgressDialog;
    SignInButton btnSignIn;
    Button btnSignOut,btnRevokeAccess;
    LinearLayout lnrProfileDetails;
    private static final int RC_SIGN_IN = 007;
    private static final String TAG=GooglePlusDash.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_plus_dash);
        tvName=(TextView)findViewById(R.id.tvName);
        tvEmail=(TextView)findViewById(R.id.tvEmail);
        ivProfilePic=(ImageView)findViewById(R.id.ivProfilePic);
        lnrProfileDetails=(LinearLayout)findViewById(R.id.lnrProfileDetails);
        btnSignIn=(SignInButton)findViewById(R.id.btnSignIn);
        btnSignOut=(Button)findViewById(R.id.btn_sign_out);
        btnRevokeAccess=(Button)findViewById(R.id.btn_revoke_access);

        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());
    }

    private void signIn(){
        Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,RC_SIGN_IN);
    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUi(false);
            }
        });
    }
    private void revokeAccess(){
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUi(false);
            }
        });
    }
    private void handleSignResult(GoogleSignInResult result){
        Log.v("@@@WWe",TAG+" "+result.isSuccess());
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            Log.v("@@@WWe",TAG+ "Name : "+account.getDisplayName());
            String name = account.getDisplayName();
            String email = account.getEmail();
            String image=account.getPhotoUrl().toString();
            tvName.setText(name);
            tvEmail.setText(email);
            Glide.with(GooglePlusDash.this).load(image).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(ivProfilePic);
            updateUi(true);
        }else {
            updateUi(false);
        }

    }
    private void updateUi(boolean isSignedIn){
        if(isSignedIn){
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnRevokeAccess.setVisibility(View.VISIBLE);
            lnrProfileDetails.setVisibility(View.VISIBLE);
        }else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
            lnrProfileDetails.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btnSignIn:
                signIn();
                break;

            case R.id.btn_sign_out:
                signOut();
                break;

            case R.id.btn_revoke_access:
                revokeAccess();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignResult(result);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult>opr=Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            //It is used to check the chache and sighin instantly to valid fast result
            Log.v("@@@WWe",TAG+ " Chached Signned In");
            GoogleSignInResult result=opr.get();
            handleSignResult(result);
        }else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    hideProgressDialog();
                    handleSignResult(result);
                }
            });
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading ...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v("@@@WWE",TAG+" Connection Failed  "+connectionResult);
    }
}
