package Graphics;

import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Animation {

    Sprite[] frames;
    public int currentFrame;
    public int totalFrames;
    public int count;
    public int delay;
    public int timesPlayed;

    public Animation(){
        timesPlayed = 0;
    }

    public Animation(Sprite[] frames){
        timesPlayed = 0;
        setFrames(frames);
    }

    public void setFrames(Sprite[] frames) {
        this.frames = frames;
        currentFrame = 0;
        totalFrames = frames.length;
        count = 0;
        delay = 2;
    }

    public Sprite getImage() {
        return frames[currentFrame];
    }

    public void update(){
        if (delay == -1) return;
        count++;
        if(count == delay){
            currentFrame++;
            count = 0;
        }
        if(currentFrame == totalFrames){
            currentFrame = 0;
            timesPlayed++;
        }
    }

}
