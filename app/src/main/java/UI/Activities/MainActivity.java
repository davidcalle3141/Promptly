package UI.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import UI.Fragments.Login;
import Utils.FragmentNavUtils;
import calle.david.promptly.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            if(savedInstanceState==null) FragmentNavUtils.startActivityFragment(getSupportFragmentManager(), new Login(), R.id.fragment_container);






    }
}
