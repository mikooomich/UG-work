package Recursion.Maze;


import java.io.*;



public class Maze {
    /**
     * This class aims to find a solution to a maze.
     *
     * @author Michael Zhou
     * @course COSC 1P03
     * @assignment #4 (Part 2)
     * @version 1.0
     * @date 2022-03-23
     */

//    private ASCIIDataFile dataFile;
//    private ASCIIOutputFile outputFile;


    private PrintWriter outputFile;
    private BufferedReader dataFile;


    private char[][] field; // maze
    private char[][] solution; // solution to the maze

    int startX; // starting coordinates
    int startY;



    public Maze() throws IOException {
        // read from data file to array

            // clear the solution file first
            File f = new File("./src/Recursion/Maze/Solution.txt");
            f.delete();

            outputFile = new PrintWriter(new FileOutputStream("./src/Recursion/Maze/Solution.txt", true));
//            dataFile = new BufferedReader(new FileReader("./src/Recursion/Maze/smol Maze.txt"));
            dataFile = new BufferedReader(new FileReader("./src/Recursion/Maze/big chungus maze.txt"));
//        dataFile = new BufferedReader(new FileReader("./src/Recursion/Maze/no solution maze.txt"));







        String[] thing = dataFile.readLine().split("\t");

        field = new char[Integer.parseInt(thing[0])][Integer.parseInt(thing[1])];

        for (int i = 0; i < field.length; i++ ) {
            char[] readValue = dataFile.readLine().toCharArray();
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = readValue[j];
            }
        }

        // generate a random, valid (ignoring walls) start position
        for (;;) {
            startX = (int) Math.floor(Math.random() * (field.length - 1) + 1); // assume outer wall of maze will be solid, ignore outer border when generating numbers
            startY = (int) Math.floor(Math.random() * (field.length - 1) + 1);
            if (field[startX][startY] == ' ') {
                field[startX][startY] = 'S';
                break;
            }
        }

        display(field); // print original map to console

        /** Manual start pos override */
//            startX = 34;
//            startY = 50;
//            field[startX][startY] = 'S';

        System.out.println("\n\n\n-----------------\nProcessing...\n-----------------");
        findPath(startX, startY, field);
        findPath(startX, startY, copyArray(field));
        System.out.println("\n\n\n-----------------\nPrinting solution\n-----------------");
//        outputFile = new ASCIIOutputFile();

        try { // differentiate between no solution and valid solution
            display(solution);
            save(solution);
        }
        catch (Exception error) {
            System.out.println("No solution");
            outputFile.println("No solution");
        }


        dataFile.close();
        outputFile.close();
    }


    private void findPath (int row, int column, char[][] maze) {
        /**
         * This method is the pathfinding of the maze.
         *
         * @param row position in the row
         * @param column position in the column
         * @param maze The current maze with the most progress in finding the end.
         */

        if (maze[row][column] == 'E') {
            maze[startX][startY] = 'S'; // compensate for destruction of start marker during pathfinding
            solution = copyArray(maze); // This is my way to return a maze once a solution has reached without forcefully stopping the recursion
//            throw new RuntimeException(); // prevent unnecessary recursion
            return;
            /** The solution is found. Let's ignore the fact that solutions can be potentially extremely inefficient. The guidelines say it's ok ¯\_(ツ)_/¯ */
        }

        if (maze[row][column] == '#' || maze[row][column] == '.' || maze[row][column] == '>'|| maze[row][column] == 'v'|| maze[row][column] == '<'|| maze[row][column] == '^') {
            // treat directional markers as walls or "clockwise loop of death"
            return;
        }

        // try all 4 directions
        maze[row][column] = '>'; findPath(row,column +1, maze);
        maze[row][column] = 'v'; findPath(row+1,column, maze);
        maze[row][column] = '<'; findPath(row,column -1, maze);
        maze[row][column] = '^'; findPath(row-1,column, maze);

        maze[row][column] = '.'; // all possibilities exhausted
    }



    private char[][] copyArray(char[][] array) {
        /**
         * Create a copy of a char array
         *
         * @param array Char array to be copied
         * @return copy of char[][] array
         */
        char[][] newArray = new char[array.length][array[0]. length]; // use static value 0 because we wil only work with non-raggad
        for (int i = 0; i < field.length; i++ ) {
            for (int j = 0; j < field[i].length; j++) {
                newArray[i][j] = array[i][j];
            }
        }

        return newArray;
    }


    private void display(char[][]array) {
        /**
         * This method displays the field array to console. Assume array is valid with no holes
         *
         * @param array Char array to be displayed
         */

        for (int i = 0; i < array.length; i++ ) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j]);
            }
            System.out.print("\n");
        }
    }


    private void save(char[][]array) {
        /**
         * This method writes field to file. Assume array is valid with no holes
         *
         * @param array Char array to be written
         */

        for (int i = 0; i < array.length; i++ ) {
            for (int j = 0; j < array[i].length; j++) {
                outputFile.print(array[i][j]);
            }
            outputFile.println();
        }
    }





    private char[][] focusOnRoute(char[][] array) {
        /**
         * Eliminate the '.' for better viewing of solution. This isn't really part of the assignment
         *
         * @param array Char array to be processed
         * @return copy of processed array
         */

        char[][] newArray = new char[array.length][array[0]. length]; // use static value 0 because we wil only work with non-ragged
        for (int i = 0; i < array.length; i++ ) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == '.') {
                    newArray[i][j] = ' ';
                }
                else {
                    newArray[i][j] = array[i][j];
                }
            }
        }

        return array;
    }



    public static void main ( String[] args ) throws IOException { Maze i = new Maze(); };

}
