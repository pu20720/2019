package com.example.qrfoodproject.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Firebase_viaGoogle extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth auth;
    private GoogleSignInClient client;
    private final String token = "586856969924-lj2e17vki39aajrg2hciu7rjrioeq9n8.apps.googleusercontent.com";
    //出現IOException的時候就是這裡的Token失效了，要去Firebase的Authentication撿

    public void buildUpViewByGoogle(Activity activity){

        GoogleSignInOptions option = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail().build();

        client = GoogleSignIn.getClient(activity, option);
        auth = FirebaseAuth.getInstance();

        signIn(activity);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount accountHere = task.getResult(ApiException.class);
                assert accountHere != null;
                checkIfAuthSuccess(accountHere);

            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkIfAuthSuccess(GoogleSignInAccount account){

        Log.d("Authentication", account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) getApplicationContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            Log.d("Authentication", "Success!");
                            FirebaseUser currentUser = auth.getCurrentUser();
                            letMeKnowUserState(currentUser);

                        }else{
                            Log.e("Authentication", task.getException().getMessage());
                            //update null?
                        }
                    }
                });
    }

    public void signIn(Activity activity){
        Intent signInIntent = client.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut(Activity activity){
        auth.signOut();
        client.signOut().addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                letMeKnowUserState(null);
                activity.startActivity(new Intent(activity, MainActivity.class));
            }
        });
    }

    public void letMeKnowUserState(FirebaseUser user){

        if (user != null){
            Log.d("User statement", "NOT NULL");
        }else{
            Log.d("User statement", "NULL");
        }
    }

}
