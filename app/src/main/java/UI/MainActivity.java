package UI;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.ads.MobileAds;

import java.util.Objects;

import UI.Fragments.FilePicker;
import UI.Fragments.Login;
import UI.Fragments.SavedPrompts;
import UI.Fragments.Settings;
import Utils.FragmentNavUtils;
import Utils.InjectorUtils;
import ViewModel.PromptsViewModel;
import ViewModel.PromptsViewModelFactory;
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
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/1033173712");





                mBottomNavigation.setAccentColor(Color.parseColor("#da0000"));

                AHBottomNavigationItem item0 =
                        new AHBottomNavigationItem(getResources().getString(R.string.bottom_nav_saved_files), R.drawable.ic_noun_save_1203125);

                AHBottomNavigationItem item1 =
                new AHBottomNavigationItem(getResources().getString(R.string.bottom_nav_file_picker), R.drawable.ic_noun_file_1956530);

                AHBottomNavigationItem item2 =
                        new AHBottomNavigationItem(getResources().getString(R.string.bottom_nav_file_settings), R.drawable.ic_noun_settings_1187459);

                mBottomNavigation.addItem(item0);
                mBottomNavigation.addItem(item1);
                mBottomNavigation.addItem(item2);



                mBottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
                    switch (position){
                        case 0:
                            FragmentNavUtils.pop(getSupportFragmentManager());
                            FragmentNavUtils.replaceFragment(getSupportFragmentManager(),R.id.fragment_container,new SavedPrompts(),"SAVED_PROMPTS");
                            break;
                        case 1:
                            FragmentNavUtils.pop(getSupportFragmentManager());
                            FragmentNavUtils.replaceFragment(getSupportFragmentManager(),R.id.fragment_container,new FilePicker(),"FILE_PICKER");
                            break;

                        case 2:
                            FragmentNavUtils.pop(getSupportFragmentManager());
                            FragmentNavUtils.replaceFragment(getSupportFragmentManager(),R.id.fragment_container,new Settings(),"SETTINGS");
                            break;


                    }

                    return true;
                });


        if(savedInstanceState==null){
            mBottomNavigation.setCurrentItem(1);
            FragmentNavUtils.startActivityFragment(getSupportFragmentManager(), new Login(), R.id.fragment_container);
            PromptsViewModelFactory factory = InjectorUtils.provideSavedPromptsFactory(Objects.requireNonNull(this));
            ViewModelProviders.of(this, factory).get(PromptsViewModel.class);
        }

    }


}
