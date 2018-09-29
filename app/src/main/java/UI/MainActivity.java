package UI;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import UI.Fragments.FilePicker;
import UI.Fragments.Login;
import UI.Fragments.SavedPrompts;
import UI.Fragments.Settings;
import Utils.FragmentNavUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import calle.david.promptly.R;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.bottom_navigation_view)AHBottomNavigation mBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

                AHBottomNavigationItem item1 =
                        new AHBottomNavigationItem(getResources().getString(R.string.bottom_nav_file_picker), R.drawable.ic_noun_file_1956530);

                AHBottomNavigationItem item2 =
                        new AHBottomNavigationItem(getResources().getString(R.string.bottom_nav_saved_files), R.drawable.ic_noun_save_1203125);

                AHBottomNavigationItem item3 =
                        new AHBottomNavigationItem(getResources().getString(R.string.bottom_nav_file_settings), R.drawable.ic_noun_settings_1187459);

                mBottomNavigation.addItem(item1);
                mBottomNavigation.addItem(item2);
                mBottomNavigation.addItem(item3);

                mBottomNavigation.setCurrentItem(1);


                mBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener(){
                    @Override
                    public boolean onTabSelected(int position, boolean wasSelected) {
                        switch (position){
                            case 0:
                                FragmentNavUtils.replaceFragment(getSupportFragmentManager(),R.id.fragment_container,new FilePicker(),"FILE_PICKER");
                                break;
                            case 1:
                                FragmentNavUtils.replaceFragment(getSupportFragmentManager(),R.id.fragment_container,new SavedPrompts(),"SAVED_PROMPTS");
                                break;

                            case 2:
                                FragmentNavUtils.replaceFragment(getSupportFragmentManager(),R.id.fragment_container,new Settings(),"SETTINGS");
                                break;

                        }

                        return true;
                    }
                });

        if(savedInstanceState==null){
            mBottomNavigation.setCurrentItem(1);
            FragmentNavUtils.startActivityFragment(getSupportFragmentManager(), new Login(), R.id.fragment_container);}




    }
}