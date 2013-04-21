package se.samuelandersson.clicky.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.OrderedMap;

public class Highscores implements Serializable
{
    static public final String HIGHSCORES_PATH = "clicky-highscores.json";

    private Array<Score> scores;

    static public Highscores instance;

    private Highscores()
    {
        scores = new Array<Score>();
    }

    static public void load()
    {
        if (!Gdx.files.external(HIGHSCORES_PATH).exists()) {
            instance = new Highscores();
            for (int i = 0; i < 10; i++)
                instance.scores.add(new Score());
            save();
        }
        instance = new Json().fromJson(Highscores.class,
                Gdx.files.external(HIGHSCORES_PATH));
    }

    static public void save()
    {
        if (instance == null)
            load();
        Gdx.files.external(HIGHSCORES_PATH).writeString(
                new Json().toJson(instance), false);
    }

    static public Array<Score> getHighscores()
    {
        if (instance == null)
            load();
        return instance.scores;
    }

    /**
     * Returns the position for which the score enters the highscore. -1 if it
     * doesn't qualify
     */
    static public int qualifiesForHighscore(Integer s)
    {
        if (instance == null)
            load();
        int i = 0;
        for (Score score : instance.scores) {
            if (s > score.score)
                return i;
            i++;
        }
        return -1;
    }

    static public void addHighscore(String name, Integer score)
    {
        int pos = qualifiesForHighscore(score);
        if (pos >= 0) {
            instance.scores.insert(pos, new Score(name, score));
            instance.scores.removeIndex(instance.scores.size - 1);
            save();
        }
    }

    static public final class Score
    {
        String name;
        Integer score;

        public Score()
        {
            this("---", 0);
        }

        public Score(String name, int score)
        {
            this.name = name;
            this.score = score;
        }

        public String getName()
        {
            return name;
        }

        public Integer getScore()
        {
            return score;
        }

        @Override
        public String toString()
        {
            return name + "-" + score;
        }
    }

    @Override
    public void write(Json json)
    {
        json.writeArrayStart("highscores");
        for (Score score : scores) {
            json.writeValue(score);
        }
        json.writeArrayEnd();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Json json, OrderedMap<String, Object> jsonData)
    {
        scores = json.readValue("highscores", Array.class, Score.class,
                jsonData);
    }
}
