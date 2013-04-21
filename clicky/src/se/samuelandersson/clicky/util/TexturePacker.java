package se.samuelandersson.clicky.util;

import java.io.File;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

/**
 * Packs single images into image atlases.
 */
public class TexturePacker extends TexturePacker2
{
    public TexturePacker(File rootDir, Settings settings)
    {
        super(rootDir, settings);
    }

    private static final String INPUT_DIR = "images";
    private static final String OUTPUT_DIR = "../clicky-android/assets/images/atlas";
    private static final String PACK_FILE = "pages"; // .json

    private static final String SKIN_INPUT_DIR = "skin";
    private static final String SKIN_OUTPUT_DIR = "../clicky-android/assets/skin";
    private static final String SKIN_PACK_FILE = "uiskin"; // .json

    public static void main(String[] args)
    {
        try {
            TexturePacker2.process(INPUT_DIR, OUTPUT_DIR, PACK_FILE);
            TexturePacker2.process(SKIN_INPUT_DIR, SKIN_OUTPUT_DIR,
                    SKIN_PACK_FILE);
        } catch (RuntimeException e) {
            e.printStackTrace(System.err);
        }
    }
}
