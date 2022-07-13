import java.util.Random;
import java.util.Scanner;

public class Game {
    static char empty = ' ';
    static char rowBorder = '-';
    static char colBorder = '|';
    static char exit = 'E';
    static char people = 'P';
    static char deadPeople = 'D';
    static char rock = 'R';
    static char bush = '#';

    public static void main(String[] args) {
        //да оправя случая когато храстите запушват изхода
        //fix difficult percents
        //да оправя търсенето в рекурцията
        //да видя дали може move switch-a да се направи в метод...
        Scanner sc = new Scanner(System.in);
        System.out.println("Please select difficult from 1 to 3");
        char difficult;
        int matrixLength = 0;
        int difficultPercent = 0;
        boolean gameOver = false;
        boolean success = false;
        int rightBorderBushPosition = -1;
        int downBorderBushPosition = -1;

        int rockRow;
        int rockCol;

        do {
            difficult = sc.nextLine().charAt(0);
            if (difficult > 51 || difficult < 49) {
                System.out.println("Please select difficult from 1 to 3");
            }

            switch (difficult) {
                case '1' -> {
                    matrixLength = 12;
                    //12.5 percents bushes
                    difficultPercent = 8;
                }
                case '2' -> {
                    matrixLength = 17;
                    //20 percents bushes
                    difficultPercent = 5;
                }
                case '3' -> {
                    matrixLength = 27;
                    //33.33 percents bushes
                    difficultPercent = 3;
                }
            }
        }
        while (difficult < 49 || difficult > 51);

        //initializing matrix
        char[][] matrix = new char[matrixLength][matrixLength];
        //add borders and replace char 0 with ' '
        fillMatrix(matrix);
        //add the exit
        matrix[matrix.length - 1][matrix.length - 1] = exit;
        //add the people
        int peopleRow = giveMeRandomNum(matrixLength - 1);
        int peopleCol = giveMeRandomNum(matrixLength - 1);
        matrix[peopleRow][peopleCol] = people;

        //adds the bushes
        int count = (matrixLength * matrixLength) / difficultPercent;
        while (count > 0) {
            int bushRow = giveMeRandomNum(matrixLength - 1);
            int bushCol = giveMeRandomNum(matrixLength - 1);
            if (canSpawnBush(matrix, bushRow, bushCol)) {
                matrix[bushRow][bushCol] = bush;
                count--;
                //set bushCol position if bush spawn near down border
                if (bushRow == matrixLength - 1 && bushCol > downBorderBushPosition) {
                    downBorderBushPosition = bushCol;
                }
                //set bushRow position if bush spawn near right border
                if (bushCol == matrixLength - 1 && bushRow > rightBorderBushPosition) {
                    rightBorderBushPosition = bushRow;
                }
            }
        }

        //add the rock
        while (true) {
            rockRow = giveMeRandomNum(1, matrixLength / 2);
            rockCol = giveMeRandomNum(1, matrixLength / 2);

            if (canSpawnRock(matrix, rockRow, rockCol) &&
                    isRockStuck(matrix, rockRow, rockCol, rightBorderBushPosition, downBorderBushPosition)) {
                matrix[rockRow][rockCol] = rock;
                break;
            }
        }
        System.out.println("You can move with w = forward, a = left, s = backward and d = right");

//        System.out.println(findPathToTheRockRecursively(matrix, rockRow, rockCol,
//                rightBorderBushPosition,downBorderBushPosition));

        printMatrix(matrix);

        while (!gameOver) {

            char directions = sc.next().charAt(0);

            switch (directions) {
                //up
                case 'w' -> {
                    //game over
                    if (isItOutOfBounds(matrix, peopleRow - 1)) {
                        gameOver = true;
                        break;
                    }
                    //bush logic
                    if (matrix[peopleRow - 1][peopleCol] == bush) {
                        break;
                    }
                    //rock logic
                    if (matrix[peopleRow - 1][peopleCol] == rock) {
                        rockRow = peopleRow - 1;
                        //game over
                        if (isItOutOfBounds(matrix, rockRow - 1)) {
                            gameOver = true;
                            break;
                        }
                        //bush logic
                        if (matrix[rockRow - 1][rockCol] == bush) {
                            break;
                        }
                        matrix[peopleRow][peopleCol] = empty;
                        matrix[--peopleRow][peopleCol] = people;
                        matrix[--rockRow][rockCol] = rock;
                        break;
                    }
                    matrix[peopleRow][peopleCol] = empty;
                    matrix[--peopleRow][peopleCol] = people;
                }
                //left
                case 'a' -> {
                    //game over
                    if (isItOutOfBounds(matrix, peopleCol - 1)) {
                        gameOver = true;
                        break;
                    }
                    //bush logic
                    if (matrix[peopleRow][peopleCol - 1] == bush) {
                        break;
                    }
                    //rock logic
                    if (matrix[peopleRow][peopleCol - 1] == rock) {
                        rockCol = peopleCol - 1;
                        //game over
                        if (isItOutOfBounds(matrix, rockCol - 1)) {
                            gameOver = true;
                            break;
                        }
                        //bush logic
                        if (matrix[rockRow][rockCol - 1] == bush) {
                            break;
                        }
                        matrix[peopleRow][peopleCol] = empty;
                        matrix[peopleRow][--peopleCol] = people;
                        matrix[rockRow][--rockCol] = rock;
                        break;

                    }
                    matrix[peopleRow][peopleCol] = empty;
                    matrix[peopleRow][--peopleCol] = people;
                }
                //down
                case 's' -> {
                    //game over
                    if (isItOutOfBounds(matrix, peopleRow + 1)) {
                        gameOver = true;
                        break;
                    }
                    if (matrix[peopleRow + 1][peopleCol] == exit) {
                        break;
                    }
                    //bush logic
                    if (matrix[peopleRow + 1][peopleCol] == bush) {
                        break;
                    }

                    //rock logic
                    if (matrix[peopleRow + 1][peopleCol] == rock) {
                        rockRow = peopleRow + 1;
                        //game over
                        if (isItOutOfBounds(matrix, rockRow + 1)) {
                            gameOver = true;
                            continue;
                        }
                        //bush logic
                        if (matrix[rockRow + 1][rockCol] == bush) {
                            break;
                        }
                        matrix[peopleRow][peopleCol] = empty;
                        matrix[++peopleRow][peopleCol] = people;
                        matrix[++rockRow][rockCol] = rock;
                        break;

                    }
                    matrix[peopleRow][peopleCol] = empty;
                    matrix[++peopleRow][peopleCol] = people;
                }
                //right
                case 'd' -> {
                    //game over
                    if (isItOutOfBounds(matrix, peopleCol + 1)) {
                        gameOver = true;
                        break;
                    }
                    if (matrix[peopleRow][peopleCol + 1] == exit) {
                        break;
                    }
                    //bush logic
                    if (matrix[peopleRow][peopleCol + 1] == bush) {
                        break;
                    }
                    //rock logic
                    if (matrix[peopleRow][peopleCol + 1] == rock) {
                        rockCol = peopleCol + 1;
                        //game over
                        if (isItOutOfBounds(matrix, rockCol + 1)) {
                            gameOver = true;
                            break;
                        }
                        //bush logic
                        if (matrix[rockRow][rockCol + 1] == bush) {
                            break;
                        }
                        matrix[peopleRow][peopleCol] = empty;
                        matrix[peopleRow][++peopleCol] = people;
                        matrix[rockRow][++rockCol] = rock;
                        break;

                    }
                    matrix[peopleRow][peopleCol] = empty;
                    matrix[peopleRow][++peopleCol] = people;
                }
            }

            //success condition
            if (rockRow == matrixLength - 1 && rockCol == matrixLength - 1) {
                success = true;
                break;
            }

            //Rock game over logic
            if (!isRockStuck(matrix, rockRow, rockCol
                    , rightBorderBushPosition, downBorderBushPosition)) {
                System.out.println("The rock can`t move :=/");
                gameOver = true;
                break;
            }
            printMatrix(matrix);
        }

        if (success) {
            printMatrix(matrix);
            System.out.println("Congrats you win the game");

        }
        if (gameOver) {
            matrix[peopleRow][peopleCol] = deadPeople;
            printMatrix(matrix);
            System.out.println("Game over :(");
        }

    }

    static int giveMeRandomNum(int from, int to) {
        Random random = new Random();
        return random.nextInt(from, to);
    }

    static int giveMeRandomNum(int to) {
        Random random = new Random();
        return random.nextInt(to);
    }

    static boolean canSpawnBush(char[][] matrix, int row, int col) {
        if (row == matrix.length - 1 && col < matrix.length - 1) {
            col++;
        }
        if (col == matrix.length - 1 && row < matrix.length - 1) {
            row++;
        }
        return (matrix[row][col] != people && matrix[row][col] != rowBorder
                && matrix[row][col] != colBorder && matrix[row][col] != exit);
    }

    static boolean canSpawnRock(char[][] matrix, int row, int col) {
        return (matrix[row][col] != people && matrix[row][col] != exit && matrix[row][col] != bush);
    }

    static boolean isRockStuck(char[][] matrix, int rockRow, int rockCol,
                               int rightBorderBushPosition, int downBorderBushPosition) {
        boolean upBorder = rockRow < 1;
        boolean leftBorder = rockCol < 1;
        boolean downBorder = rockCol <= matrix.length - 1 && rightBorderBushPosition > rockRow;
        boolean rightBorder = rockRow <= matrix.length - 1 && downBorderBushPosition > rockCol;
        boolean leftBush = rockCol > 0 && matrix[rockRow][rockCol - 1] == bush;
        boolean rightBush = rockCol < matrix.length - 1 && matrix[rockRow][rockCol + 1] == bush;
        boolean upBush = rockRow > 0 && matrix[rockRow - 1][rockCol] == bush;
        boolean downBush = rockRow < matrix.length - 1 && matrix[rockRow + 1][rockCol] == bush;


        //if the rock is stuck return false
        return !(upBorder || leftBorder || downBorder || rightBorder || (leftBush && upBush) || (leftBush && downBush) ||
                (rightBush && upBush) || (rightBush && downBush));
    }

    static void printMatrix(char[][] matrix) {
        for (int row = 0; row < matrix.length; row++) {

            if (row == 0) {
                for (int i = 0; i < matrix.length + 2; i++) {
                    System.out.print(rowBorder);
                    if (i == matrix.length + 1) {
                        System.out.println();
                    }
                }

            }
            System.out.print(colBorder);
            for (int col = 0; col < matrix[row].length; col++) {
                System.out.print(matrix[row][col]);
            }

            System.out.print(colBorder);

            System.out.println();
            if (row == matrix.length - 1) {
                for (int i = 0; i < matrix.length + 2; i++) {
                    System.out.print(rowBorder);
                }
            }

        }
    }

    static void fillMatrix(char[][] matrix) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix.length; col++) {
                matrix[row][col] = empty;
            }
        }
    }

    static boolean isItOutOfBounds(char[][] matrix, int position) {
        return (position < 0 || position > matrix.length - 1);
    }

    static boolean findPathToTheRockRecursively(char[][] matrix, int row, int col,
                                                int right, int down) {
        if (matrix[row][col] == exit) {
            return true;
        }

        if (!isRockStuck(matrix, row, col, right, down)) {
            return false;
        }
        matrix[row][col] = 'r';

        return findPathToTheRockRecursively(matrix, row + 2, col, right, down) ||
                findPathToTheRockRecursively(matrix, row, col + 2, right, down) ||
                findPathToTheRockRecursively(matrix, row, col - 2, right, down) ||
                findPathToTheRockRecursively(matrix, row - 2, col, right, down);

    }
}
