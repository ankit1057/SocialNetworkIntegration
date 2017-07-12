package com.socialnetworkintegration.javabasic.facebook;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.socialnetworkintegration.R;

import org.json.JSONObject;

/**
 * Created by tasol on 11/7/17.
 */

public class FacebookDash extends Activity {
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private CallbackManager callbackManager;
    LoginButton login_button;

    private FacebookCallback<LoginResult> callBack=new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            AccessToken accessToken=loginResult.getAccessToken();
            Profile profile=Profile.getCurrentProfile();
            displayMessage(profile);
            getUserProfile(accessToken);
        }

        @Override
        public void onCancel() {
            Log.v("@@@WW"," onCancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log.v("@@@WW"," "+error);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_dash);
        login_button=(LoginButton)findViewById(R.id.login_button);
        callbackManager=CallbackManager.Factory.create();
        accessTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        profileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        login_button.setReadPermissions("user_friends");
        login_button.registerCallback(callbackManager,callBack);
    }
    private void displayMessage(Profile profile){
        if(profile != null){
            Log.v("@@@WWE"," "+profile);
        }
    }

    private void getUserProfile(final AccessToken accessToken) {

        GraphRequest request=GraphRequest.newMeRequest(
                accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        String name="",email="",password="",image="";
                        try{
                            Log.v("@@@WWe","AccessToken:"+accessToken.toString());
                            if(object.has("picture")){
                                image=object.getJSONObject("picture").getJSONObject("data").getString("url");
                            }
                            if(object.has("name")){
                                name=object.getString("name").toString();
                            }

                            if(object.has("id")){
                                password=object.getString("id").toString();
                            }
                            if(object.has("email")){
                                email=object.getString("email").toString();
                            }
                            Handler handler=new Handler();
                            final String finalImage = image;
                            final String finalImage1 = image;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences preferences=getSharedPreferences("userPref",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=preferences.edit();
                                    editor.putString("isLoggedIn","1");
                                    editor.putString("userImage", finalImage1);
                                    editor.commit();
//
//                                    Intent intent=new Intent(FacebookDash.this,MyChildActivity.class);
//                                    intent.putExtra("profile_image", finalImage);
//                                    startActivity(intent);

                                }
                            },5000);

                        }catch (Exception je){

                        }

                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields","id,name,email,picture.width(200)");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
