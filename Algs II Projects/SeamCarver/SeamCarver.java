/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {

    private final Picture picture;
    private Picture picture2;
    private Picture transposedPicture;
    private int[][] rgbArray;
    private double[][] currentEnergyArray, transposedEnergyArray;
    private static final int INFINITY = Integer.MAX_VALUE;
    private int[][] transposedPicturePixels;
    private boolean isTransposed, initialEnergyArrayCreated, newPictureSize,
            changedDimensions;
    private int currentHeight, currentWidth;

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.picture = new Picture(picture);
        this.picture2 = new Picture(picture);
        setRGB();
        createInitialEnergyArray();
        currentHeight = height();
        currentWidth = width();
    }

    public Picture picture() {
        if (changedDimensions) {
            if (newPictureSize) {
                if (isTransposed) {
                    // StdOut.println("Within picture: " + currentHeight + " : " + currentWidth);
                    picture2 = new Picture(currentWidth, currentHeight);
                    // StdOut.println(picture2.toString());
                    for (int col = 0; col < currentWidth; col++) {
                        for (int row = 0; row < currentHeight; row++) {
                            // StdOut.println(col + " " + row);
                            picture2.setRGB(col, row, rgbArray[col][row]);
                        }
                    }
                    newPictureSize = false;
                    // StdOut.println("first test");
                    // StdOut.println(picture2.toString());
                    return new Picture(picture2);
                }
                else {
                    picture2 = new Picture(currentWidth, currentHeight);
                    for (int col = 0; col < currentHeight; col++) {
                        for (int row = 0; row < currentWidth; row++) {
                            picture2.setRGB(row, col, rgbArray[col][row]);
                        }
                    }
                    newPictureSize = false;
                    // StdOut.println("second test");
                    // StdOut.println(picture2.toString());
                    return new Picture(picture2);
                }
            }
            return picture2;
        }
        else {
            return picture;
        }
    }

    private void transposeArray() {
        if (!isTransposed) {
            // currentWidth = height();
            // currentHeight = width();
            transposedEnergyArray = new double[width()][height()];
            transposedPicturePixels = new int[width()][height()];
            for (int row = 0; row < height(); row++) {
                for (int col = 0; col < width(); col++) {
                    transposedEnergyArray[col][row] = currentEnergyArray[row][col];
                    transposedPicturePixels[col][row] = rgbArray[row][col];
                }
            }

        }

        else {
            // currentWidth = width();
            // currentHeight = height();
            transposedEnergyArray = new double[height()][width()];
            transposedPicturePixels = new int[height()][width()];
            for (int row = 0; row < width(); row++) {
                for (int col = 0; col < height(); col++) {
                    transposedEnergyArray[col][row] = currentEnergyArray[row][col];
                    transposedPicturePixels[col][row] = rgbArray[row][col];
                }
            }
        }
        // if (!isTransposed) {
        //     currentWidth = height();
        //     currentHeight = width();
        //     transposedEnergyArray = new double[currentHeight][currentWidth];
        //     transposedPicturePixels = new int[currentHeight][currentWidth];
        //     for (int row = 0; row < currentWidth; row++) {
        //         for (int col = 0; col < currentHeight; col++) {
        //             transposedEnergyArray[col][row] = currentEnergyArray[row][col];
        //             transposedPicturePixels[col][row] = rgbArray[row][col];
        //         }
        //     }
        //
        // }
        //
        // else {
        //     currentWidth = width();
        //     currentHeight = height();
        //     transposedEnergyArray = new double[currentHeight][currentWidth];
        //     transposedPicturePixels = new int[currentHeight][currentWidth];
        //     for (int row = 0; row < currentWidth; row++) {
        //         for (int col = 0; col < currentHeight; col++) {
        //             transposedEnergyArray[col][row] = currentEnergyArray[row][col];
        //             transposedPicturePixels[col][row] = rgbArray[row][col];
        //         }
        //     }
        // }
        currentEnergyArray = transposedEnergyArray;
        rgbArray = transposedPicturePixels;
        isTransposed = !isTransposed;
    }

    public int width() {
        return picture().width();
    }

    public int height() {
        return picture().height();
    }

    public double energy(int x, int y) {
        int height = height();
        int width = width();

        if (x < 0 || y < 0 || x > width - 1 || y > height - 1) {
            throw new IllegalArgumentException();
        }
        if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
            return 1000.00;
        }
        if (initialEnergyArrayCreated) {
            if (isTransposed) {
                return currentEnergyArray[x][y];
            }
            else {
                return currentEnergyArray[y][x];
            }
        }
        else {
            double energy = Math.sqrt(calcEnergyX(x, y) + calcEnergyY(x, y));
            return energy;
        }
    }

    public int[] findVerticalSeam() {
        if (isTransposed) {
            transposeArray();
        }
        int[] verticalSeam = findSeam();
        return verticalSeam;
    }

    public int[] findHorizontalSeam() {
        if (!isTransposed) {
            transposeArray();
        }
        int[] horizontalSeam = findSeam();
        return horizontalSeam;
    }

    public void removeHorizontalSeam(int[] seam) {


        if (seam == null) {
            throw new IllegalArgumentException();
        }

        if (!isTransposed) {
            transposeArray();
        }

        // print2DArray(currentEnergyArray);

        int width = height();
        int height = width();

        // StdOut.println("cols: " + height + " rows: " + width);
        checkSeam(seam, height);

        if (width <= 1) {
            throw new IllegalArgumentException();
        }

        // double[][] newEnergyArray = new double[height][width - 1];
        int[][] newPicturePixels = new int[height][width - 1];

        try {
            for (int row = 0; row < height; row++) {
                int destPos = 0;
                for (int col = 0; col < width; col++) {
                    if (col != seam[row]) {
                        // System.arraycopy(currentEnergyArray[row], col, newEnergyArray[row], destPos,
                        //                  1);
                        System.arraycopy(rgbArray[row], col, newPicturePixels[row], destPos++, 1);
                    }
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }

        // currentEnergyArray = newEnergyArray;
        rgbArray = newPicturePixels;
        updateEnergyArray(rgbArray[0].length, rgbArray.length);
        // printSeam(seam);
        // StdOut.println("testing print within horizontal:");
        // print2DArray(currentEnergyArray);
        // updateEnergySeam(seam, width, height);
        // StdOut.println("Post-update:");
        // print2DArray(currentEnergyArray);
        newPictureSize = true;
        changedDimensions = true;
        currentHeight--;
        // picture();
    }

    public void removeVerticalSeam(int[] seam) {

        if (seam == null) {
            throw new IllegalArgumentException();
        }

        if (isTransposed) {
            transposeArray();
        }

        int height = height();
        int width = width();

        checkSeam(seam, height);

        if (width() <= 1) {
            throw new IllegalArgumentException();
        }

        // double[][] newEnergyArray = new double[height][width - 1];
        int[][] newPicturePixels = new int[height][width - 1];

        try {
            for (int row = 0; row < height; row++) {
                int destPos = 0;
                for (int col = 0; col < width; col++) {
                    if (col != seam[row]) {
                        // System.arraycopy(currentEnergyArray[row], col, newEnergyArray[row], destPos,
                        //                  1);
                        System.arraycopy(rgbArray[row], col, newPicturePixels[row], destPos++, 1);
                    }
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }

        // currentEnergyArray = newEnergyArray;
        rgbArray = newPicturePixels;
        updateEnergyArray(rgbArray[0].length, rgbArray.length);
        // printSeam(seam);
        // StdOut.println("testing print within vertical:");
        // print2DArray(currentEnergyArray);

        // updateEnergySeam(seam, height, width);
        // StdOut.println("Post-update:");
        // print2DArray(currentEnergyArray);
        newPictureSize = true;
        changedDimensions = true;
        currentWidth--;
        // picture();
    }

    private double energy2(int x, int y, int height, int width) {
        if (x < 0 || y < 0 || x > width - 1 || y > height - 1) {
            throw new IllegalArgumentException();
        }
        if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
            return 1000.00;
        }
        // if (initialEnergyArrayCreated) {
        //     if (isTransposed) {
        //         return currentEnergyArray[x][y];
        //     }
        //     else {
        //         return currentEnergyArray[y][x];
        //     }
        // }
        else {
            double energy = Math.sqrt(calcEnergyX(x, y) + calcEnergyY(x, y));
            return energy;
        }
    }

    private void updateEnergySeam(int[] seam, int height, int width) {
        try {
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    if (col == seam[row]) {
                        // StdOut.println("currentEnergyArrayCell: " + currentEnergyArray[row][col]);
                        try {
                            currentEnergyArray[row][col] = revisedEnergySeam(row, col);
                        }
                        catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                            // 1
                        }
                        try {
                            currentEnergyArray[row - 1][col] = revisedEnergySeam(row - 1, col);
                        }
                        catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                            // 2
                        }
                        try {
                            currentEnergyArray[row + 1][col] = revisedEnergySeam(row + 1, col);
                        }
                        catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                            // 3
                        }
                        try {
                            currentEnergyArray[row][col + 1] = revisedEnergySeam(row, col + 1);
                        }
                        catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                            // 4
                        }
                        try {
                            currentEnergyArray[row][col - 1] = revisedEnergySeam(row, col - 1);
                        }
                        catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                            // 5
                        }


                    }

                }
            }
        }
        catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            //
        }

    }

    private void updateEnergyArray(int width, int height) {
        currentEnergyArray = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // StdOut.println(i + " " + j);
                currentEnergyArray[i][j] = energy2(j, i, height,
                                                   width); // (inverted col/row compared to API)
                // StdOut.printf("%9.2f ", energyArray[i][j]);
            }
            // StdOut.println();
        }
        // initialEnergyArrayCreated = true;
    }

    private double revisedEnergySeam(int x, int y) {
        int height = currentWidth;
        int width = currentHeight;

        // StdOut.println("height: " + height + " width: " + width);
        // StdOut.println("y: " + y + " x: " + x);

        if (x < 0 || y < 0 || x > height - 1 || y > width - 1) {
            throw new IllegalArgumentException();
        }
        if (x == 0 || y == 0 || y == width || x == height) {
            return 1000.00;
        }
        else {
            double energy = Math.sqrt(calcEnergyX(y, x) + calcEnergyY(y, x));
            return energy;
        }
    }


    private void checkSeam(int[] seam, int dimension) {
        // StdOut.println(seam.length + " " + dimension);
        if (seam.length != dimension) {
            throw new IllegalArgumentException();
        }
        for (int j = 0; j < seam.length - 1; j++) {
            int difference = Math.abs(seam[j + 1] - seam[j]);
            if (difference > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void createInitialEnergyArray() {
        int height = height();
        int width = width();
        currentEnergyArray = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // StdOut.println(i + " " + j);
                currentEnergyArray[i][j] = energy(j, i); // (inverted col/row compared to API)
                // StdOut.printf("%9.2f ", energyArray[i][j]);
            }
            // StdOut.println();
        }
        initialEnergyArrayCreated = true;
    }

    private void setRGB() {
        int height = height();
        int width = width();
        rgbArray = new int[height][width];
        // println(width() + " " + height());
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                rgbArray[row][col] = picture().getRGB(col, row);
            }
        }
    }

    private int[] findSeam() {

        int height;
        int width;

        if (!isTransposed) {
            // StdOut.println("not transposed");
            height = height();
            width = width();
        }
        else {
            // StdOut.println("transposed");
            width = height();
            height = width();
        }

        // StdOut.println("rows: " + height + " cols: " + width);

        int[] seam = new int[height];
        double[][] distTo = new double[height][width];
        Integer[][] edgeTo = new Integer[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                distTo[i][j] = INFINITY;
            }
        }

        for (int i = 0; i < height; i++) { // sets the Row
            for (int j = 0; j < width; j++) { // sets the Col

                if (i == 0) {
                    distTo[i][j] = currentEnergyArray[i][j];
                }
                try {
                    if (distTo[i + 1][j - 1] > distTo[i][j] + currentEnergyArray[i + 1][j
                            - 1]) {
                        distTo[i + 1][j - 1] = distTo[i][j] + currentEnergyArray[i + 1][j - 1];
                        edgeTo[i + 1][j - 1] = j;
                    }
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    //
                }
                try {
                    if (distTo[i + 1][j] > distTo[i][j] + currentEnergyArray[i + 1][j]) {
                        distTo[i + 1][j] = distTo[i][j] + currentEnergyArray[i + 1][j];
                        edgeTo[i + 1][j] = j;
                    }
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    //
                }
                try {
                    if (distTo[i + 1][j + 1] > distTo[i][j] + currentEnergyArray[i + 1][j]) {
                        distTo[i + 1][j + 1] = distTo[i][j] + currentEnergyArray[i + 1][j + 1];
                        edgeTo[i + 1][j + 1] = j;
                    }
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    //
                }
            }
        }
        double min = INFINITY;
        int lastSeam = INFINITY;
        for (int i = 0; i < width; i++) {
            if (distTo[height - 1][i] < min) {
                min = distTo[height - 1][i];
                lastSeam = i;
            }
        }
        seam[height - 1] = lastSeam;
        for (int i = height - 1; i > 0; i--) {
            seam[i - 1] = edgeTo[i][lastSeam];
            lastSeam = edgeTo[i][lastSeam];
        }
        // StdOut.printf("{ ");
        // for (int x : seam)
        //     StdOut.print(x + " ");
        // StdOut.println("}");
        return seam;
    }

    private void printSeam(int[] seam) {
        StdOut.printf("{");
        for (int i : seam)
            StdOut.print(i + " ");
        StdOut.println("}");
    }


    private double calcEnergyY(int col, int row) {
        // int rgb1 = picture.getRGB(col, row - 1);
        // int rgb2 = picture.getRGB(col, row + 1);

        int rgb1 = rgbArray[row][col - 1];
        int rgb2 = rgbArray[row][col + 1];

        // int a = (rgb >> 24) & 0xFF;
        int r11 = (rgb1 >> 16) & 0xFF;
        int g11 = (rgb1 >> 8) & 0xFF;
        int b11 = (rgb1 >> 0) & 0xFF;

        int r22 = (rgb2 >> 16) & 0xFF;
        int g22 = (rgb2 >> 8) & 0xFF;
        int b22 = (rgb2 >> 0) & 0xFF;

        int rDif = r22 - r11;
        int gDif = g22 - g11;
        int bDif = b22 - b11;

        double energyY = rDif * rDif + gDif * gDif + bDif * bDif;
        return energyY;
    }

    private double calcEnergyX(int col, int row) {
        // int rgb1 = picture.getRGB(col - 1, row);
        // int rgb2 = picture.getRGB(col + 1, row);

        int rgb1 = rgbArray[row - 1][col];
        int rgb2 = rgbArray[row + 1][col];

        // int a = (rgb >> 24) & 0xFF;
        int r11 = (rgb1 >> 16) & 0xFF;
        int g11 = (rgb1 >> 8) & 0xFF;
        int b11 = (rgb1 >> 0) & 0xFF;

        int r22 = (rgb2 >> 16) & 0xFF;
        int g22 = (rgb2 >> 8) & 0xFF;
        int b22 = (rgb2 >> 0) & 0xFF;

        int rDif = r22 - r11;
        int gDif = g22 - g11;
        int bDif = b22 - b11;

        double energyX = rDif * rDif + gDif * gDif + bDif * bDif;
        return energyX;
    }


    private static void print2DArray(double[][] array) {
        StdOut.println("width: " + array[0].length + " height: " + array.length);
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[0].length; col++) {
                StdOut.printf("%9.2f ", array[row][col]);
            }
            StdOut.println();
        }
    }

    private static void print2DArray(int[][] array) {
        StdOut.println("width: " + array[0].length + " height: " + array.length);
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[0].length; col++) {
                StdOut.printf("%9d ", array[row][col]);
            }
            StdOut.println();
        }
    }


    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);
        print2DArray(sc.currentEnergyArray);
        StdOut.println(sc.picture().toString());
        sc.transposeArray();
        print2DArray(sc.currentEnergyArray);
        // sc.transposeArray();


        int[] seam = sc.findHorizontalSeam();
        print2DArray(sc.currentEnergyArray);

        sc.removeHorizontalSeam(seam);
        StdOut.println(sc.picture().toString());
        StdOut.println("currentEnergyArray below:");
        print2DArray(sc.currentEnergyArray);
        // StdOut.println(sc.picture().toString());
        StdOut.println(sc.height() + " a " + sc.width());
        StdOut.println(sc.currentHeight + " b " + sc.currentWidth);

        print2DArray(sc.currentEnergyArray);
        int[] seam2 = sc.findVerticalSeam();
        sc.removeVerticalSeam(seam2);
        // StdOut.println(sc.picture().toString());
        int[] seam3 = sc.findHorizontalSeam();
        sc.removeHorizontalSeam(seam3);
        // StdOut.println(sc.picture().toString());
    }
}

