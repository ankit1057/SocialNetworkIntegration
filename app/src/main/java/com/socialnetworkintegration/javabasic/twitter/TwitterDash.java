package com.socialnetworkintegration.javabasic.twitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.socialnetworkintegration.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * Created by tasol on 11/7/17.
 */

public class TwitterDash extends Activity {
    static String TWITTER_CONSUMER_KEY = "Z2odVbT6qQLucHWMUb3i9nc7D";
    static String TWITTER_CONSUMER_SECRET = "xK3JGkPFqiivbsfBiG3EyEMCRf2VaqaA1elJlI42eYJnRakrlp";
    TwitterLoginButton login_button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_dash);
        login_button=(TwitterLoginButton)findViewById(R.id.login_button);
        Twitter.initialize(TwitterDash.this);


        login_button.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.v("@@@WWe","Session ");
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        login_button.onActivityResult(requestCode,resultCode,data);
    }
}
