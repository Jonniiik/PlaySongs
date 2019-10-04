package com.example.player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.squareup.picasso.Picasso;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PlayMusicActivity extends AppCompatActivity {

    ImageView imageViewAlbum;
    Button buttonPlaySounds;
    SeekBar seekBarSounds;

    MediaPlayer mediaPlayer;
    AudioManager audioManager;

    String soundURL;
    private Handler mHandler = new Handler();
    private int mediaFileLenght;
    private int realTimeSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(null);
        setContentView(R.layout.activity_play_music);
        initComponents();
        getInformationAboutArtist();
    }

    private void initComponents() {
        imageViewAlbum = (ImageView) findViewById(R.id.imageViewAlbum);
        buttonPlaySounds = (Button) findViewById(R.id.buttonPlaySounds);
        seekBarSounds = (SeekBar) findViewById(R.id.seekBarSounds);
        mediaPlayer = new MediaPlayer();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        buttonPlaySounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //playMusicRx();
                new PlayerTask().execute(soundURL);
            }
        });
    }

    private void playMusicRx() {
        Observable.just(soundURL)
                .map(new Function<String, Object>() {
                    @Override
                    public Object apply(String s) throws Exception {
                        mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(soundURL));
                        mediaPlayer.prepare();

                        mediaFileLenght = mediaPlayer.getDuration();
                        realTimeSound = mediaFileLenght;
                        return "";
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                            buttonPlaySounds.setBackgroundResource(R.drawable.ic_pause);
                        } else {
                            mediaPlayer.pause();
                            buttonPlaySounds.setBackgroundResource(R.drawable.ic_play);
                        }
                        updateSeekBar();
                    }
                });
    }

    private void getInformationAboutArtist() {
        if (getIntent().hasExtra("imageURL") && getIntent().hasExtra("artistURL")) {
            String imageURL = getIntent().getStringExtra("imageURL");
            String nameURL = getIntent().getStringExtra("artistURL");
            setInformation(imageURL, nameURL);
        }
    }

    private void setInformation(String imageURL, String nameURL) {
        Picasso.get().load(imageURL).into(imageViewAlbum);
        soundURL = nameURL;
        seekBarSounds.setMax(100);
    }

    private class PlayerTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                mediaPlayer.setDataSource(strings[0]);
                //mediaPlayer.seekTo(10);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            mediaFileLenght = mediaPlayer.getDuration();
            realTimeSound = mediaFileLenght;

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                buttonPlaySounds.setBackgroundResource(R.drawable.ic_pause);
            } else {
                mediaPlayer.pause();
                buttonPlaySounds.setBackgroundResource(R.drawable.ic_play);
            }
            updateSeekBar();
        }
    }

    private void updateSeekBar() {
        seekBarSounds.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLenght) * 100));
        if (mediaPlayer.isPlaying()) {
            Runnable updater = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                    realTimeSound -= 1000;
                }
            };
            mHandler.postDelayed(updater, 1000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
}
