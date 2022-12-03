package net.sloshy.aoc.common;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Utilities {
    /**
     * Gets a list for each line of a file. Uses an arbitrary lambda to process
     * each line, to reduce boilerplate at the start of each day.
     *
     * @param filename      the file
     * @param lineProcessor a function which intakes a string and outputs an object
     * @param <T>           The type to output
     * @return A list of values
     * @throws RuntimeException if file does not exist
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
            // this is bad API design, but it reduces boilerplate for this
            // specific application.  If IO fails we need to fix the path,
            // so halting execution is almost always correct.
            throw new RuntimeException(e);
        }
        return record;
    }

    /**
     * Gets a list of strings for each line from the given file
     *
     * @param filename the file
     * @return a list of strings
     * @throws RuntimeException if file does not exist
     */
    public static List<String> getContent(String filename) {
        return getContent(filename, (String s) -> s);
    }

    /**
     * Displays the results of each challenge part
     * @param part1 the result of part 1
     * @param part2 the result of part 2
     * @param <P1> part 1 result type
     * @param <P2> part 2 result type
     */
    public static <P1, P2> void printResult(P1 part1, P2 part2) {
        System.out.printf("Part 1: %s%n", part1);
        System.out.printf("Part 2: %s%n", part2);
    }
}
