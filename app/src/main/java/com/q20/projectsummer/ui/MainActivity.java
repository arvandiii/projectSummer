package com.q20.projectsummer.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityOptionsCompat;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.non_android_programmers.responsivegui.ResponsiveImageView;
import com.q20.projectsummer.Custom.CustomActivity;
import com.q20.projectsummer.R;
import com.q20.projectsummer.utilities.Settings;

import java.io.IOException;
import java.io.InputStream;

import Game.Letter;
import Game.Player;
import Game.Game;
import QAPack.V1.Pack;
import QAPack.V1.Word;
import Utility.PrimitiveSerializer;

public class MainActivity extends CustomActivity {

    final static int packIDs[] = {R.raw.pack0,R.raw.pack1};
    public static Pack offlinePack[] = new Pack[packIDs.length];

    public static Player player;

    private Boolean isFabOpen = false;
    private ResponsiveImageView settingsFab, trophyFab, soundFab;
    private Animation fab_open, fab_close, rotate_backward, rotate_forward;

    Slide transition;
    ImageView profileImageView;
    TextView profileName;


    public static void passRandomWordToGameActivity(int packId) {
        Word currentWord =  getRandomWord(packId);
        Letter[] letters = new Letter[currentWord.word.replace(" ", "").length()];
        for (int i =0 ; i<letters.length;i++){
            letters[i] = new Letter(null,false);
        }
        GameActivity.currentGame = new Game(null,100000,0, currentWord, packId,letters);
    }

    public static void initializeGame() {
        passRandomWordToGameActivity(1);
    }

    public static Word getRandomWord(int packNumber) {
        int random = (int) (Math.random() * MainActivity.offlinePack[packNumber].words.size());
        return MainActivity.offlinePack[packNumber].words.get(random);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < packIDs.length; i++) {
            InputStream inStream = getResources().openRawResource(packIDs[i]);
            try {
                byte[] packData = new byte[inStream.available()];
                inStream.read(packData);
                offlinePack[i] = new Pack();
                offlinePack[i].deserialize(PrimitiveSerializer.bytesToLS(packData));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        profileImageView = (ImageView) findViewById(R.id.main_activity_profile_image);
        //profileImageView.setTag(R.drawable.char_m_40);
        profileName = (TextView) findViewById(R.id.main_activity_profile_name);
        settingsFab = (ResponsiveImageView)findViewById(R.id.main_activity_settings_fab);
        soundFab = (ResponsiveImageView)findViewById(R.id.main_activity_sound_fab);
        trophyFab = (ResponsiveImageView)findViewById(R.id.main_activity_trophy_fab);

        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.close_fab);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.open_fab);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward_fab);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward_fab);

        Intent intent = getIntent();
        if (intent != null){
            profileImageView.setImageResource(intent.getIntExtra("ID", R.drawable.char_m_40));
            profileImageView.setTag(intent.getIntExtra("ID", R.drawable.char_m_40));
            profileName.setText(intent.getStringExtra("USER_NAME"));
        }

        isFabOpen = false;
        setupWindowAnimations();
        initializePlayer();
    }

    private void initializePlayer(){
        player = new Player();
        player.username = getIntent().getStringExtra("USER_NAME");
    }

    @TargetApi(21)
    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        transition = new Slide(Gravity.END);
        //transition.setSlideEdge(Gravity.END);
        transition.setDuration(300);
        getWindow().setEnterTransition(transition);
        //getWindow().setSharedElementExitTransition(slideTransition);
        //getWindow().setExitTransition(slideTransition);
    }

    //when user click on new game local btn
    public void onNewGame(View view) {
        Intent intent = new Intent(this, NewGameDialog.class);
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this, null).toBundle());
    }

    //when user click on information btn
    public void onInformation(View view) {
        Intent intent = new Intent(this, RegisterPageActivity.class);
        startActivity(intent);
    }

    //when user click on ranking page btn
    public void onRankingMenu(View view) {
        Intent intent = new Intent(this, RankingPageActivity.class);
        startActivity(intent);
    }

    //when user click on about us btn
    public void onAboutUs(View view) {
        Intent intent = new Intent(this, AboutUsActivity.class);
        startActivity(intent);
    }

    public void onCoins(View view) {
        Intent intent = new Intent(this, BuyCoinDialog.class);
        startActivity(intent);
    }

    public void onSettings(View view) {
        if (isFabOpen){
            animateFab(rotate_forward, fab_close, false, View.INVISIBLE);
        }else {
            animateFab(rotate_backward, fab_open, true, View.VISIBLE);
        }
    }

    public void onSound(View view) {
        if (Settings.isMuted){
            Settings.isMuted = false;
            soundFab.setImageResource(R.drawable.mute);
        }else {
            Settings.isMuted = true;
            soundFab.setImageResource(R.drawable.speaker);
        }
    }

    public void onTrophy(View view) {
        Intent intent = new Intent(this, TrophyPageActivity.class);
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this, null).toBundle());
    }

    private void animateFab(Animation rotation, Animation openORclose, Boolean fabFlag, int visibility){
        settingsFab.startAnimation(rotation);
        soundFab.startAnimation(openORclose);
        trophyFab.startAnimation(openORclose);
        soundFab.setClickable(fabFlag);
        trophyFab.setClickable(fabFlag);
        soundFab.setVisibility(visibility);
        trophyFab.setVisibility(visibility);
        isFabOpen = fabFlag;
    }

    public void onProfileImage(View view) {
        Intent intent = new Intent(this, ProfileImageActivity.class);
        intent.putExtra("ID", (Integer) profileImageView.getTag());
        startActivityForResult(intent, 1, ActivityOptionsCompat.makeSceneTransitionAnimation(this, null).toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                int profileImageId = data.getIntExtra("ID", R.drawable.char_m_40);

                //Initialize profile image
                profileImageView.setImageResource(profileImageId);
                profileImageView.setTag(profileImageId);
            }
        }
    }
}