package com.example.ringermodesilenttest.issuecode;

import android.content.Context;
import android.media.AudioManager;

import com.example.ringermodesilenttest.MainActivity;

/**
 * This code demonstrates an example of the issue,
 * when setting the phone to "RINGER_MODE_SILENT" - makes it switch to
 * Do not Disturb mode
 */
public class VolumeController {

    public void setToRingerModeNormal(){
        final AudioManager audioManager = getAudioManager();

        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }



    public void setToRingerModeSilent(){
        final AudioManager audioManager = getAudioManager();
        /*
            UNEXPECTED Effect.

            sets the sytem into "DONT_DISTURB",
            which, near setting phone to silent - has many side-effects,
            like hiding also notification icons
         */
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    private AudioManager getAudioManager() {
        Context context = MainActivity.getMainActivity();
        final AudioManager audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        return audioManager;
    }
}
