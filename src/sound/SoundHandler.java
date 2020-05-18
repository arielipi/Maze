package sound;

import java.io.*;
import java.util.Random;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;


/**
 * This enum encapsulates all the sound effects of a game, so as to separate the sound playing
 * codes from the game codes.
 * 1. Define all your sound effect names and the associated wave file.
 * 2. To play a specific sound, simply invoke SoundEffect.SOUND_NAME.play().
 * 3. You might optionally invoke the static method SoundEffect.init() to pre-load all the
 *    sound files, so that the play is not paused while loading the file for the first time.
 * 4. You can use the static variable SoundEffect.volume to mute the sound.
 */
public enum SoundHandler {
   WALK("src/sound/footsteps.ogg"),   // walking 
   WALKONGRAVEL("src/sound/footsteps/footstepsOnGravel.ogg"),   // walking on gravel
   WALKONWOOD("src/sound/footsteps/footstepsOnWoodenBridge.ogg"),   // walking on wood
   
   TORCH("src/sound/fire.ogg"),         // torch sound effect
   LEVEL1AMBIENCE("src/sound/backgroundhorror.ogg"),
   
   GASPING("src/sound/onInit/manGasping.ogg"),
   HEARTBEAT("src/sound/onInit/heartBeat.ogg"),
   LAUGHTER("src/sound/onInit/ladyLaughsCrazy.ogg"),
   BREATHING("src/sound/onInit/breathing.ogg"),
   
   DOOR_BALLON_POP("src/sound/door/ballonPop.ogg"),
   DOOR_BANG("src/sound/door/doorBang.ogg"),
   DOOR_CLICK_OPEN_AND_CREAK("src/sound/door/doorClickOpenAndCreak.ogg"),
   DOOR_LOCK_OPEN("src/sound/door/doorLockOpen.ogg"),
   DOOR_SHUT("src/sound/door/doorShut.ogg"),
   DOOR_SHUT_GENTLY("src/sound/door/doorShutGently.ogg"),
   DOOR_SLAM("src/sound/door/doorSlam.ogg");
   
   // Nested class for specifying volume
   public static enum Volume {
      MUTE, LOW, MEDIUM, HIGH
   }
   
   public static Volume volume = Volume.LOW;
   
   // Each sound effect has its own clip, loaded with its own sound file.
   private Music clip;
   
   // Constructor to construct each element of the enum with its own sound file.
   SoundHandler(String soundFileName) {
	   if(!TinySound.isInitialized()) {
		   TinySound.init();
	   }
	   clip = TinySound.loadMusic(new File(soundFileName));
   }
   
   // Play or Re-play the sound effect from the beginning, by rewinding.
   public void play() {
      if (volume != Volume.MUTE) {

           if (clip.playing())
               clip.stop();   // Stop the player if it is still running
            clip.setLoopPositionByFrame(0); // rewind to the beginning
            clip.play(false);     // Start playing

      }
   }

   // Play or Re-play the sound effect from the beginning, by rewinding.
   public void loop() {
      if (volume != Volume.MUTE) {
         if (!clip.playing()) {
        	 clip.setLoopPositionByFrame(0); // rewind to the beginning
        	 clip.play(true);     // Start playing again
         }
      }
   }

   // Play or Re-play the sound effect from the beginning, by rewinding.
   public void stop() {
      if (volume != Volume.MUTE) {
         if (clip.playing()) {        	 
        	 clip.stop();     // Start playing again
         }
      }
   }
   
   // Wood - givenWalk = 1
   // Gravel - givenWalk = 2
   // else - givenWalk = 0
   public static void playWalkAccordingToGivenInt(int givenWalk) {
	   if(givenWalk == 0) {
		   SoundHandler.WALK.loop();
	   } else if(givenWalk == 1) {
		   SoundHandler.WALKONWOOD.loop();
	   } else if(givenWalk == 2) {
		   SoundHandler.WALKONGRAVEL.loop();
	   }
   }
   
   // Wood - givenWalk = 1
   // Gravel - givenWalk = 2
   // else - givenWalk = 0
   public static void stopWalkAccordingToGivenInt(int givenWalk) {
	   if(givenWalk == 0) {
		   SoundHandler.WALK.stop();
	   } else if(givenWalk == 1) {
		   SoundHandler.WALKONWOOD.stop();
	   } else if(givenWalk == 2) {
		   SoundHandler.WALKONGRAVEL.stop();
	   }
   }
   
   public static void levelFinishedDoorSound() {
	   Random rand = new Random();
	   int doorNumbers = 7;
	   int i = rand.nextInt(doorNumbers);
	   if(i == 0) {
		   SoundHandler.DOOR_BALLON_POP.play();
	   } else if(i == 1) {
		   SoundHandler.DOOR_BANG.play();
	   } else if(i == 2) {
		   SoundHandler.DOOR_CLICK_OPEN_AND_CREAK.play();
	   } else if(i == 3) {
		   SoundHandler.DOOR_LOCK_OPEN.play();
	   } else if(i == 4) {
		   SoundHandler.DOOR_SHUT.play();
	   } else if(i == 5) {
		   SoundHandler.DOOR_SHUT_GENTLY.play();
	   } else if(i == 6) {
		   SoundHandler.DOOR_SLAM.play();
	   }
   }
   
   public static void initGameManGasping() {
	   SoundHandler.GASPING.play();
   }
   
   public static void initGameHeartbeat() {
	   SoundHandler.HEARTBEAT.play();
   }
   
   public static void initGameCrazyLaughter() {
	   SoundHandler.LAUGHTER.play();
   }

	public static void initGameBreathing() {
		SoundHandler.BREATHING.play();
	}
   
   // Optional static method to pre-load all the sound files.
   public static void init() {
      //values(); // calls the constructor for all the elements
	   TinySound.init();
   }
}