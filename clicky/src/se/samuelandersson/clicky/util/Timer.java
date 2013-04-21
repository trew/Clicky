package se.samuelandersson.clicky.util;

import com.badlogic.gdx.utils.Array;

public class Timer
{
    static public final Timer instance = new Timer();
    private Array<Task> tasks;

    public Timer()
    {
        tasks = new Array<Task>();
    }

    public static void update(float delta)
    {
        synchronized (instance.tasks) {
            for (int i = 0; i < instance.tasks.size; i++) {
                instance.tasks.get(i).update(delta);
                if (instance.tasks.get(i).canceled) {
                    instance.tasks.removeIndex(i--);
                }
            }
        }
    }

    public static void schedule(Task task, float delaySeconds,
            float intervalSeconds)
    {
        task.intervalSeconds = intervalSeconds;
        task.delaySeconds = delaySeconds;
        instance.tasks.add(task);
    }

    static public abstract class Task
    {
        private float intervalSeconds;
        private float delaySeconds;

        private float time;
        private boolean canceled;

        public Task()
        {
            time = 0;
            canceled = false;
        }

        private final void update(float delta)
        {
            time += delta;
            while (time > delaySeconds) {
                run();
                time -= intervalSeconds;
            }

        }

        abstract public void run();

        public void cancel()
        {
            canceled = true;
        }
    }
}
