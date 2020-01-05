package Graphics;

public class Animation {

    public int currentFrame;
    public int count;
    public int delay;
    Sprite[] frames;

    public Animation() {
    }

    public Animation(Sprite[] frames) {
        setFrames(frames);
    }

    public void setFrames(Sprite[] frames) {
        this.frames = frames;
        currentFrame = 0;
        count = 0;
        delay = 2;
    }

    public Sprite getImage() {
        return frames[currentFrame];
    }

    public void update() {
        if (delay == -1) return;
        count++;
        if (count == delay) {
            currentFrame++;
            count = 0;
        }
        if (currentFrame == frames.length) {
            currentFrame = 0;
        }
    }

}
