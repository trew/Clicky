package se.samuelandersson.clicky.util;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.WindowedMean;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * <p>
 * Utility class to measure time of operations. It utilizes {@link WindowedMean}
 * in order to get a mean value for the operation.
 * </p>
 * Usage:<br>
 * <code>
 * logger.start("rendering");<br>
 * render();<br>
 * logger.stop("rendering");<br>
 * logger.log(); // write to console<br>
 * </code>
 * 
 * @author Samuel Andersson
 * @since 2013-03-30
 */
public class StatusLogger
{
    private Map<String, WindowedMean> means;
    private Map<String, Long> loggingTimes;
    private long time;
    private long loggingDelay;
    private DecimalFormat timeFormat;
    private boolean enabled;
    static public StatusLogger logger;

    private PrintStream ps;

    public StatusLogger()
    {
        enabled = true;
        time = 0;
        means = new HashMap<String, WindowedMean>();
        loggingTimes = new HashMap<String, Long>();
        setPrintStream(System.out);
        setLoggingDelay(1f);
        timeFormat = new DecimalFormat("#.#########");
        logger = this;
    }

    public void setPrintStream(PrintStream pstream)
    {
        if (pstream == null)
            throw new NullPointerException("printstream");
        ps = pstream;
    }

    public void setLoggingDelay(float secs)
    {
        if (secs < 0)
            throw new IllegalArgumentException("logging delay must be positive");
        loggingDelay = (long) (secs * 1000f);
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    /**
     * Output all values to console. Will be done once per second if called
     * every frame, much like {@link FPSLogger}. FPS is always logged.
     */
    public void log()
    {
        if (!enabled)
            return;
        if (TimeUtils.millis() - time > loggingDelay) {
            ps.print("StatusLogger: fps: " + Gdx.graphics.getFramesPerSecond());
            for (String name : means.keySet()) {
                ps.print(", " + name + ": ");
                ps.print(timeFormat.format(means.get(name).getMean()));
            }
            ps.println("");
            ps.flush();
            time = TimeUtils.millis();
        }
    }

    public void log(String name, float value)
    {
        if (!enabled)
            return;
        if (!means.containsKey(name))
            means.put(name, new WindowedMean(10));
        means.get(name).addValue(value);
    }

    /**
     * Called right before the measuring of an operation. A call to stop() must
     * follow.
     */
    public void start(String name)
    {
        if (!enabled)
            return;
        if (!means.containsKey(name)) {
            means.put(name, new WindowedMean(10));
        }
        if (loggingTimes.get(name) == null) {
            loggingTimes.put(name, new Long(TimeUtils.nanoTime()));
        } else {
            throw new RuntimeException("Do not call start twice!");
        }
    }

    /**
     * Called when the operation has completed. Must be preceded by a call to
     * start(). The time taken between start() and stop() is measured and added
     * to a {@link WindowedMean} instance.
     */
    public void stop(String name)
    {
        if (!enabled)
            return;
        if (!means.containsKey(name) || loggingTimes.get(name) == null) {
            throw new RuntimeException("Must call start() before stop()");
        }
        // divide by 1 billion to get time in seconds
        long time = TimeUtils.nanoTime() - loggingTimes.get(name);
        means.get(name).addValue(time / 1000000000.0f);
        loggingTimes.put(name, null);
    }

    /** Get the average time for this operation */
    public float getMean(String name)
    {
        if (means.containsKey(name)) {
            return means.get(name).getMean();
        } else {
            return 0f;
        }
    }

    /** Get a map containing the average times for all logged operations */
    public Map<String, Float> getMeans()
    {
        HashMap<String, Float> all = new HashMap<String, Float>();
        for (String key : means.keySet()) {
            all.put(key, means.get(key).getMean());
        }
        return all;
    }
}
