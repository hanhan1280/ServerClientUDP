package Graphics;

public class Animation {

    public int delay;
    private Sprite[] frames;
    private int currentFrame,count;

    public Animation() {
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
