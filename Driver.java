import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class Point {
    double x = 0.0;
    double y = 0.0;
    int index = 0;
    int label = 0;
}

public class Driver {
    private static Point[] points = new Point[100];

    private static double dist(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    private static double getSSE(Point[] centers) {
        double sse = 0.0;
        for (int j = 0; j < centers.length; j++) {
            for (int m = 0; m < 100; m++) {
                if (points[m].label == centers[j].label) {
                    sse += dist(points[m], centers[j]);
                }
            }
        }
        return sse;
    }

    public static void main(String[] args) {
        try {
            for (int j = 0; j < 100; j++) points[j] = new Point();
            // Open the file that is the first
            // command line parameter
            FileInputStream fstream = new FileInputStream(args[1]);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            br.readLine();
            //Read File Line By Line
            int i = 0;
            while ((strLine = br.readLine()) != null) {
                String[] nums = strLine.split("\t");
                points[i].index = Integer.parseInt(nums[0]);
                points[i].x = Double.parseDouble(nums[1]);
                points[i].y = Double.parseDouble(nums[2]);
                i++;
            }
            //Close the input stream
            in.close();
            int k = Integer.parseInt(args[0]);
            Point[] centers = new Point[k];
            for (int j = 0; j < k; j++) {
                centers[j] = new Point();
                centers[j].x = Math.random();
                centers[j].y = Math.random();
                centers[j].label = -j - 1;
            }

            int round = 0;
            int stable_num = 0;
            while (round < 25 && stable_num != k) {
                for (int j = 0; j < 100; j++) {
                    double min = Double.POSITIVE_INFINITY;
                    for (int m = 0; m < k; m++) {
                        if (dist(points[j], centers[m]) < min) {
                            min = dist(points[j], centers[m]);
                            points[j].label = centers[m].label;
                        }
                    }
                }

                for (int j = 0; j < k; j++) {
                    double sum_x = 0.0;
                    double sum_y = 0.0;
                    int cnt = 0;
                    for (int m = 0; m < 100; m++) {
                        if (points[m].label == centers[j].label) {
                            sum_x += points[m].x;
                            sum_y += points[m].y;
                            cnt++;
                        }
                    }
                    if ((centers[j].x == sum_x / cnt) && (centers[j].y == sum_y / cnt)) {
                        stable_num++;
                    }
                    else {
                        centers[j].x = sum_x / cnt;
                        centers[j].y = sum_y / cnt;
                    }
                }
                round++;
            }

            PrintWriter writer = new PrintWriter(args[2], "UTF-8");
            for (int j = 0; j < k; j++) {
                writer.print("Label " + (j + 1) + ": ");
                for (int m = 0; m < 100; m++) {
                    if (points[m].label == centers[j].label) {
                        writer.print(points[m].index + " ");
                    }
                }
                writer.println();
            }
            writer.println("SSE: " + getSSE(centers));
            writer.close();
        }
        catch (Exception e) { //Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
}
