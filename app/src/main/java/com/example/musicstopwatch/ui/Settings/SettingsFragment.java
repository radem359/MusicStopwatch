package com.example.musicstopwatch.ui.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.musicstopwatch.R;

public class SettingsFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        if(root.findViewById(R.id.handwash_container) != null){
            getFragmentManager().beginTransaction().add(R.id.handwash_container, new HandwashSettingsFragment()).commit();
        }

        return root;
    }




}
