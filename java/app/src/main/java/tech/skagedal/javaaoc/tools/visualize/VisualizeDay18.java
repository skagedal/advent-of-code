package tech.skagedal.javaaoc.tools.visualize;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import tech.skagedal.javaaoc.tools.linear.Point3D;

/**
 * Writes to an .obj file that can be visualized with e.g. https://3dviewer.net/
 */
public class VisualizeDay18 {
    public static void writeObjFile(List<Point3D> points) {
        final var path = Paths.get(System.getProperty("user.home")).resolve("Desktop").resolve("out.obj");
        try (var writer = Files.newBufferedWriter(path)) {
            writeToWriter(points, writer);
        } catch (IOException exception) {
            System.err.println("ERROR: " + exception);
        }
    }

    private static void writeToWriter(List<Point3D> points, BufferedWriter writer) {
        final var printWriter = new PrintWriter(writer);
        for (var point : points) {
            printWriter.printf("v %d %d %d\n", point.x() + 0, point.y()+ 1, point.z() + 1);
            printWriter.printf("v %d %d %d\n", point.x() + 0, point.y()+ 0, point.z() + 1);
            printWriter.printf("v %d %d %d\n", point.x() + 1, point.y()+ 0, point.z() + 1);
            printWriter.printf("v %d %d %d\n", point.x() + 1, point.y()+ 1, point.z() + 1);
            printWriter.printf("v %d %d %d\n", point.x() + 0, point.y()+ 1, point.z() + 0);
            printWriter.printf("v %d %d %d\n", point.x() + 0, point.y()+ 0, point.z() + 0);
            printWriter.printf("v %d %d %d\n", point.x() + 1, point.y()+ 0, point.z() + 0);
            printWriter.printf("v %d %d %d\n", point.x() + 1, point.y()+ 1, point.z() + 0);
        }
        for (var i = 0; i < points.size(); i++) {
            final var m = i * 8;
            printWriter.printf("f %d %d %d %d\n", m+1, m+2, m+3, m+4);
            printWriter.printf("f %d %d %d %d\n", m+8, m+7, m+6, m+5);
            printWriter.printf("f %d %d %d %d\n", m+4, m+3, m+7, m+8);
            printWriter.printf("f %d %d %d %d\n", m+5, m+1, m+4, m+8);
            printWriter.printf("f %d %d %d %d\n", m+5, m+6, m+2, m+1);
            printWriter.printf("f %d %d %d %d\n", m+2, m+6, m+7, m+3);
        }
    }
}
