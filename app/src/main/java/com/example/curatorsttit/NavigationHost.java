package com.example.curatorsttit;
import androidx.fragment.app.Fragment;

public interface NavigationHost {
    void navigateTo(Fragment fragment, boolean addToBackstack);
    void openActivivty(Fragment fragment,boolean addToBackstack);
}
