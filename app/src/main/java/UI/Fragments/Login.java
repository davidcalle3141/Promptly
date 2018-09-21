package UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import calle.david.promptly.R;



public class Login extends Fragment {


    private GoogleSignInClient mGoogleSingInClient;
    private FirebaseAuth mAuth;
    int RC_SIGN_IN = 1;
    String TAG = "Login Fragment";
    private View mView;
    private Context mContext;

    @BindView(R.id.sign_in_button)SignInButton mGoogleSingInButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        this.mContext = this.getContext();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleSingInClient = GoogleSignIn.getClient(mContext,gso);

        ButterKnife.bind(this,view);
        mAuth = FirebaseAuth.getInstance();
        this.mView = view;
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser account = mAuth.getCurrentUser();
        updateUi(account);

    }

    @OnClick(R.id.sign_in_button)
    public void signIn(View v){
        Intent signInIntent = mGoogleSingInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }



    private void showSignInButton() {
        mGoogleSingInButton.setVisibility(View.VISIBLE);
        mGoogleSingInButton.setSize(SignInButton.SIZE_WIDE);
    }

    private void updateUi(FirebaseUser account) {
        if(account!=null){

        }else showSignInButton();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }catch (ApiException e){
                Log.w(TAG, "Google sign in failed", e);
                //TODO update if sign in failed
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUi(user);
                        }else{
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(mView, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUi(null);

                        }
                    }
                });

    }
}
