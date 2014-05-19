package at.meinedomain.CheckIt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.badlogic.androidgames.framework.FileIO;

public class Settings {
    public static boolean soundEnabled = true;
    
    public static void load(FileIO files) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    files.readFile(".chesscalate")));
            soundEnabled = Boolean.parseBoolean(in.readLine());
        } catch (IOException e) {
            // :( It's ok we have defaults TODO check if everything's really fine.
        } catch (NumberFormatException e) {
            // :/ It's ok, defaults save our day TODO check if everything's really fine.
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }
    
    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    files.writeFile(".chesscalate")));
            out.write(Boolean.toString(soundEnabled));            
        } catch (IOException e) {
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }
    }
}