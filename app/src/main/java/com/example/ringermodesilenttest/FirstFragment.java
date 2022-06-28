package com.example.ringermodesilenttest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ringermodesilenttest.databinding.FragmentFirstBinding;
import com.example.ringermodesilenttest.issuecode.VolumeController;

public class FirstFragment extends Fragment {

    public static final String TAG = FirstFragment.class.toString();

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final VolumeController volumeController = new VolumeController();

        binding.buttonRINGERMODENORMAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "RINGER_MODE_NORMAL" );
                volumeController.setToRingerModeNormal();
            }
        });

        binding.buttonRINGERMODESILENT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "RINGER_MODE_SILENT" );
                volumeController.setToRingerModeSilent();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}