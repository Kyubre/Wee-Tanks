public class FpsLimiter {
    private final long nanosPerFrame;
    private long lastFrameTime;
    private int fps;

    public FpsLimiter(int fps) {
        this.nanosPerFrame = 1_000_000_000 / fps;
        this.lastFrameTime = 0;
        this.fps = fps;
    }

    public boolean canRender(long now) {
        if (now - lastFrameTime >= nanosPerFrame) {
            lastFrameTime = now;
            return true;
        }
        return false;
    }

    public int getFps() {
        return fps;
    }
}
