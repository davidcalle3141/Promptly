package UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import Utils.FragmentNavUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import calle.david.promptly.R;



public class Login extends android.support.v4.app.Fragment {


    private GoogleSignInClient mGoogleSingInClient;
    private FirebaseAuth mAuth;
    int RC_SIGN_IN = 1;
    String TAG = "Login Fragment";
    private View mView;
    private Context mContext;
    FragmentManager mFragmentManager;

    @BindView(R.id.sign_in_button)SignInButton mGoogleSingInButton;
    @BindView(R.id.login_background_image)
    ImageView mBackgroundImage;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity()!=null){
        getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_login, container, false);
        this.mContext = this.getContext();
        mFragmentManager = getFragmentManager();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleSingInClient = GoogleSignIn.getClient(mContext,gso);

        ButterKnife.bind(this,mView);
        mAuth = FirebaseAuth.getInstance();

        LoadImageAsync loadImage = new LoadImageAsync();
        loadImage.execute();

        return mView;
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
            if(getActivity()!=null){
                FragmentNavUtils.replaceFragment(mFragmentManager, R.id.fragment_container, new FilePicker(), "FILE_PICKER");

                getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);

            }

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
                e.printStackTrace();
                Log.w(TAG, "Google sign in failed", e);
                //TODO update if sign in failed
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
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

    public class LoadImageAsync extends AsyncTask<String, Void, Bitmap> {
        private final String DEFAULT_IMAGE = getString(R.string.default_login_background_image);


        @Override
        protected Bitmap doInBackground(String... urls) {
            URL url;
            Bitmap bmp = null;
            try {


                HttpURLConnection urlConnection = null;
                url = new URL(DEFAULT_IMAGE);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                bmp = BitmapFactory.decodeStream(in);

            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mBackgroundImage.setImageBitmap(bitmap);

        }
    }





}
