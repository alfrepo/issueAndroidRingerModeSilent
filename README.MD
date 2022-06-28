This project demonstrates the problem with RINGER_MODE_SILENT



**Steps to reproduce the problem (including sample code if appropriate).**

```
final AudioManager audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
/*
    UNEXPECTED Effect.

    sets the sytem into "DONT_DISTURB",
    which, near setting phone to silent - has many side-effects,
    like hiding also notification icons
    */
audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
```		
			
			
**What happened:**

Trying to put the phone into silent mode, so that when a notification arrives - the phone does not ring or vibrate when a voice-call arrives - the phone does not ring or vibrate by setting the ringmode to `"android.media.AudioManager.RINGER_MODE_SILENT"`.

Unexpectedly this puts `"DoNotDisturb"` on, triggering many side-effects near just silencing the phone.


**What you think the correct behavior should be.**

 - When putting the phone into `RINGER_MODE_SILENT` 
 - then the phone should remain silent (not vibrate, not ring) 
   - when a notification arrives remain silent (not vibrate, not ring) 
   - when a call arrives AND the DoNotDisturb ode should NOT be switched on.


**Is this a security related issue? Yes/No**

No


**Don't forget to mention which version of Android you're using, and/or which device the problem appears on (model and Android version).**

- Google Pixel 4, Api level 30
- Google Pixel 5, Android Version 12, Api level 31