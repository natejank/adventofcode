package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Utilities;

import java.util.*;

public class Day7 {
    public static void main(String[] args) {
        var day7 = new Day7(Utilities.getContent(args[0]));

        Utilities.printResult(day7.part1(), day7.part2());
    }

    private List<String> input;
    private File root;
    public Day7(List<String> input) {
        this.input = input;
        root = new File();
        populateFilesystem();
    }

    public int part1() {
        int totalSize = 0;
        // not using recursion lol
        List<File> children = new LinkedList<>();
        children.add(root);
        while (!children.isEmpty()) {
            File directory = children.remove(0);
            if (directory.getSize() <= 100000) {
                totalSize += directory.getSize();
            }
            for (String f : directory.children().keySet()) {
                File file = directory.children().get(f);
                if (file.size() == -1) {
                    children.add(file);
                }
            }
        }
        return totalSize;
    }

    public int part2() {
        int currentFree = 70000000 - root.getSize();
        List<File> meetsRequirements = new LinkedList<>();
        // not using recursion lol
        List<File> children = new LinkedList<>();
        children.add(root);
        while (!children.isEmpty()) {
            File directory = children.remove(0);
            if (currentFree + directory.getSize() >= 30000000) {
                meetsRequirements.add(directory);
            }
            for (String f : directory.children().keySet()) {
                File file = directory.children().get(f);
                if (file.size() == -1) {
                    children.add(file);
                }
            }
        }
        return meetsRequirements.stream().mapToInt(File::getSize).min().orElseThrow();
    }

    public void populateFilesystem() {
        List<String> wd = new ArrayList<>();
        String cmd = "";
        for (String command : input) {
            var operands = command.split(" ");
            if (command.startsWith("$")) {
                cmd = operands[1];
                if (cmd.equals("cd")) {
                    wd = modifyWorkingDirectory(wd, operands[2]);
                }
            } else {
                if (cmd.equals("ls")) {
                    var parent = getPWD(wd);

                    if (operands[0].equals("dir")) {
                        if (!parent.children().containsKey(operands[1])) {
                            parent.children().put(operands[1], new File());
                        }
                    }
                    else {
                        parent.children().put(operands[1], new File(Integer.parseInt(operands[0])));
                    }
                }
            }
        }
    }

    public static List<String> modifyWorkingDirectory(List<String> workingDirectory, String destination) {
        switch (destination) {
            case "/" -> workingDirectory = workingDirectory.subList(0, 0);
            default -> workingDirectory.add(destination);
            case ".." -> {
                // we can't go lower than the root
                if (workingDirectory.size() != 0) {
                    // lop off the last directory
                    return workingDirectory.subList(0, workingDirectory.size() - 1);
                }
            }
            case "." -> {}  // don't do anything; destination is wd
        }
        return workingDirectory;
    }

    public File getPWD(List<String> pwd) {
        File parent = root;

        for (String dir : pwd) {
            if (!parent.children().containsKey(dir))
                // new directory just dropped 😳
                parent.children().put(dir, new File());

            parent = parent.children().get(dir);
        }
        return parent;
    }

    /**
     * EVERYTHING IS A FILE MWAHAHAHAHAHA
     * @param size
     * @param children
     */
    private record File(int size, Map<String, File> children){
        File(int size) {
            this(size, null);
        }
        File() {
            this(-1, new HashMap<>());
        }

        public int getSize() {
            if (size != -1) {
                // base case; not a directory
                return size;
            }

            int size = 0;
            for (String f : children.keySet()) {
                File child = children.get(f);
                size += child.getSize();
            }
            return size;
        }

        public void listContents() {
            listContents("/");
        }

        private void listContents(String path) {
            for (String key : children.keySet()) {
                File child = children.get(key);
                if (child.size == -1) {
                    child.listContents(path + "/" + key);
                } else {
                    System.out.printf("%s/%s : %d%n", path, key, child.size);
                }
            }
        }
    }
}
