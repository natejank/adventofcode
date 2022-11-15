package net.sloshy.aoc.common;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Utilities {
    /**
     * Gets a list of strings for each line from the given file
     *
     * @param filename the file
     * @return a list of strings; null if file not found
     */
    public static List<String> getContent(String filename) {
        return getContent(filename, (String s) -> s);
    }

    /**
     * Gets a list for each line of a file. Uses an arbitrary lambda to process
     * each line, to reduce boilerplate at the start of each day.
     * TODO think about the design choice of returning null vs throwing exceptions
     *
     * @param filename      the file
     * @param lineProcessor a function which intakes a string and outputs an object
     * @param <T>           The type to output
     * @return A list of values, or null if the file was not found
     */
    public static <T> List<T>
    getContent(String filename, Function<String, T> lineProcessor) {
        // use arraylists to improve random access
        var record = new ArrayList<T>();
        try (var file = new BufferedReader(new FileReader(filename))) {
            String l;
            while ((l = file.readLine()) != null) {
                record.add(lineProcessor.apply(l));
            }
        } catch (IOException e) {
            return null;
        }
        return record;
    }
}
