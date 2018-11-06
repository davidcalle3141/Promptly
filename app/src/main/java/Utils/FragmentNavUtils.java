package Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentNavUtils {
    public static void startActivityFragment(FragmentManager fragmentManager, Fragment entryFragment, int container){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(container,entryFragment,"ENTRY_FRAGMENT");
        fragmentTransaction.commit();
    }
    public static void navigateToFragment(FragmentManager fragmentManager, Fragment destination, int container,String FRAGMENT_TAG){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(container,destination);
        fragmentTransaction.addToBackStack(FRAGMENT_TAG);
        fragmentTransaction.commit();
    }
    public static void replaceFragment(FragmentManager fragmentManager, int container, Fragment fragment, String FRAGMENT_TAG){

        fragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(container, fragment,FRAGMENT_TAG)
                .commit();
    }
    public static void popInclusiveTrue(FragmentManager fragmentManager, String DESTINATION_TAG){
        fragmentManager.popBackStack(DESTINATION_TAG,FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    public static void popInclusiveFalse(FragmentManager fragmentManager, String DESTINATION_TAG){
        fragmentManager.popBackStack(DESTINATION_TAG,0);
    }
    public static void pop(FragmentManager fragmentManager){

        fragmentManager.popBackStack();
    }

}
