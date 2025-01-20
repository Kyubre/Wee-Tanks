public class FpsLimiter {
    private final long nanosPerFrame;
    private long lastFrameTime;

    public FpsLimiter(int fps) {
        this.nanosPerFrame = 1_000_000_000 / fps;
        this.lastFrameTime = 0;
    }

    public boolean canRender(long now) {
        if (now - lastFrameTime >= nanosPerFrame) {
            lastFrameTime = now;
            return true;
        }
        return false;
    }
}
