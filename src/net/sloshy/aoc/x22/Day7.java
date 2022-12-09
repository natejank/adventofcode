package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Utilities;

import java.util.*;

public class Day7 {
    public static void main(String[] args) {
        var day7 = new Day7(Utilities.getContent(args[0]));

        Utilities.printResult(day7.part1(), day7.part2());
    }

    private final File root;

    public Day7(List<String> input) {
        root = new File("/");
        populateFilesystem(root, input);
    }

    public int part1() {
        int totalSize = 0;
        List<File> directories = new LinkedList<>(List.of(root));
        while (!directories.isEmpty()) {
            File directory = directories.remove(0);
            if (directory.getSize() <= 100000) {
                totalSize += directory.getSize();
            }
            for (String f : directory.getChildren()) {
                File file = directory.getChild(f);
                if (file.isDirectory()) {
                    directories.add(file);
                }
            }
        }
        return totalSize;
    }

    public int part2() {
        int currentFree = 70000000 - root.getSize();
        List<File> meetsRequirements = new LinkedList<>();
        List<File> directories = new LinkedList<>(List.of(root));

        while (!directories.isEmpty()) {
            File directory = directories.remove(0);
            if (currentFree + directory.getSize() >= 30000000) {
                meetsRequirements.add(directory);
            }

            for (String f : directory.getChildren()) {
                File file = directory.getChild(f);
                if (file.isDirectory()) {
                    directories.add(file);
                }
            }
        }
        return meetsRequirements.stream().mapToInt(File::getSize).min().orElseThrow();
    }

    /**
     * Reads the input; fills in files from the root.
     */
    private static void populateFilesystem(File root, List<String> input) {
        List<String> wd = new ArrayList<>();
        String cmd = "";
        for (String command : input) {
            String[] operands = command.split(" ");
            if (command.startsWith("$")) {
                cmd = operands[1];
                if (cmd.equals("cd")) {
                    String destination = operands[2];
                    switch (destination) {
                        // remove all items without changing structure
                        case "/" -> wd = wd.subList(0, 0);
                        default -> wd.add(destination);
                        case ".." -> {
                            // we can't go lower than the root
                            if (wd.size() != 0) {
                                // lop off the last directory
                                wd = wd.subList(0, wd.size() - 1);
                            }
                        }
                        case "." -> {}  // don't do anything; destination is wd
                    }
                }
            } else {
                if (cmd.equals("ls")) {
                    String name = operands[1];
                    String size = operands[0];

                    // get the current directory as a file
                    File parent = root;
                    for (String dir : wd) {
                        if (!parent.hasChild(dir)) {
                            // new directory just dropped 😳
                            parent.addChild(dir, new File(dir));
                        }
                        parent = parent.getChild(dir);
                    }

                    if (size.equals("dir")) {
                        // Don't overwrite directories!  It will destroy the internal contents.
                        if (!parent.hasChild(name)) {
                            parent.addChild(name, new File(name));
                        }
                    }
                    else {
                        // overwriting files is safe because they have no children.
                        parent.addChild(name, new File(name, Integer.parseInt(size)));
                    }
                }
            }
        }
    }


    /**
     * EVERYTHING IS A FILE
     */
    private static class File {
        String name;
        int size;
        Map<String, File> children;

        /**
         * Private file constructor
         *
         * @param name name of file
         * @param size size of file; -1 for directories
         * @param children map of children; null for files
         */
        private File(String name, int size, Map<String, File> children) {
            this.name = name;
            this.size = size;
            this.children = children;
        }

        /**
         * File constructor
         *
         * @param name name of file
         * @param size size of file
         */
        public File(String name, int size) {
            this(name, size, null);
        }

        /**
         * Directory constructor
         *
         * @param name name of directory
         */
        public File(String name) {
            this(name, -1, new HashMap<>());
        }

        public String getName() {
            return name;
        }

        public boolean hasChild(String name) {
            return children.containsKey(name);
        }

        public File getChild(String name) {
            return children.get(name);
        }

        public Set<String> getChildren() {
            return children.keySet();
        }

        public void addChild(String name, File child) {
            children.put(name, child);
        }

        public boolean isDirectory() {
            return size == -1;
        }

        public int getSize() {
            if (size != -1) {
                // base case; not a directory
                return size;
            }

            int size = 0;
            for (String f : getChildren()) {
                File child = getChild(f);
                size += child.getSize();
            }
            return size;
        }

        @Override
        public int hashCode() {
            return size + name.hashCode() + (children == null ? 0 : children.hashCode());
        }

        @Override
        public boolean equals(Object obj) {
            boolean result = true;
            if (obj == this)
                return true;
            if (obj instanceof File objFile) {
                result = this.size == objFile.size;
                result &= this.name.equals(objFile.name);
                // null-safe .equals
                result &= Objects.equals(this.children, objFile.children);
            }
            return result;
        }
    }
}
