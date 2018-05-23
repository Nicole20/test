package com.example.nicole.test;

import android.media.MediaPlayer;
import android.view.View;

/**
 * Created by Nicole on 12/8/2017.
 */

public class Word {
    private static final int NO_IMAGE = -1;
    private String english;
    private String french;
    private int imageID = NO_IMAGE;
    private final MediaPlayer mediaPlayer;

    public Word(String english, String french, MediaPlayer media) {
        this.english = english;
        this.french = french;
        this.mediaPlayer = media;
    }

    public Word(String english, String french, MediaPlayer media, int imageID) {
        this.english = english;
        this.french = french;
        this.mediaPlayer = media;
        this.imageID = imageID;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getFrench() {
        return french;
    }

    public void setFrench(String french) {
        this.french = french;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public boolean isImage()
    {
        return imageID != NO_IMAGE;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

}
