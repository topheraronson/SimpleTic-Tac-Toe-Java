package tictactoe;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String PLAYER1 = "X";
    private static final String PLAYER2 = "O";

    private static final String[][] spaces = new String[5][5];

    public static void main(String[] args) {

        initializeSpaces();
        printGrid();
        gameLoop();
    }

    private static void initializeSpaces() {

        for (int column = 0; column < 5; column++) {
            spaces[0][column] = column < 4 ? "--" : "-";
            spaces[4][column] = column < 4 ? "--" : "-";
        }

        for (int row = 1; row < 4; row++) {
            spaces[row][0] = "| ";
            spaces[row][4] = "| ";
        }

        for (int row = 1; row < 4; row++) {
            for (int column = 1; column < 4; column++) {
                spaces[row][column] = "_ ";
            }
        }
    }

    private static void printGrid() {

        for (String[] row : spaces) {
            for (String space : row) {
                System.out.print(space);
            }

            System.out.println();
        }
    }

    private static void gameLoop() {
        boolean isPlayerOne = true;
        String currentState;

        while (true) {

            int[] input = getInput();
            int rowChoice;
            int columnChoice;

            if (input[0] == 0 || input[1] == 0) {
                continue;
            } else {
                rowChoice = input[0];
                columnChoice = input[1];
            }

            if (isOutOfBounds(rowChoice, columnChoice)){
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }

            if (isSpaceTaken(rowChoice, columnChoice)) {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }

            spaces[rowChoice][columnChoice] = isPlayerOne ? "X " : "O ";
            printGrid();

            currentState = createSpaceString();
            if (didPlayerWin(isPlayerOne ? PLAYER1 : PLAYER2, currentState)) {
                System.out.printf("%s wins",isPlayerOne ? PLAYER1 : PLAYER2 );
                break;
            }


            if (isGameFinished(currentState)) {
                System.out.println("Draw");
                break;
            }

            isPlayerOne = !isPlayerOne;
        }
    }

    private static int[] getInput() {
        Scanner scanner = new Scanner(System.in);
        int[] playersChoice = new int[2];

        try {
            playersChoice[0] = Integer.parseInt(scanner.next());
            playersChoice[1] = Integer.parseInt(scanner.next());

        } catch (Exception e) {
            System.out.println("You should enter numbers!");
        }

        return playersChoice;
    }

    private static boolean isOutOfBounds(int row, int column) {
        return (column < 1 || column > 3) || (row < 1 || row > 3);
    }

    private static boolean isSpaceTaken(int row, int column) {

        return !spaces[row][column].equals("_ ");
    }

    private static String createSpaceString() {
        StringBuilder sb = new StringBuilder();

        for (int row = 1; row < 4; row++) {
            for (int column = 1; column < 4; column++) {
                sb.append(spaces[row][column].trim());
            }
        }

        return sb.toString();
    }

    private static boolean didPlayerWin(String player, String input) {

        /*****************************************************
         *  Win cases:
         *          XXX______
         *          ___XXX___
         *          ______XXX
         *          X__X__X__
         *          _X__X__X_
         *          __X__X__X
         *          X___X___X
         *          __X_X_X__
         ******************************************************/

        String[] winCases = new String[] {"\\b%1$s%1$s%1$s.{6}\\b", "\\b.{3}%1$s%1$s%1$s.{3}\\b",
                "\\b.{6}%1$s%1$s%1$s\\b", "\\b(%1$s.{2}){3}\\b", "\\b(.{1}%1$s.){3}\\b", "\\b(.{2}%1$s){3}\\b",
                "\\b%1$s.{3}%1$s.{3}%1$s\\b", "\\b.{2}%1$s.%1$s.%1$s.{2}\\b"};

        for (String winCase : winCases) {

            Pattern pattern = Pattern.compile(winCase.formatted(player), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            boolean matchFound = matcher.find();

            if (matchFound) {
                return true;
            }
        }

        return false;
    }

    private static boolean isGameFinished(String input) {

        if (getPlayerSpotsCount(input, PLAYER1.charAt(0)) + getPlayerSpotsCount(input, PLAYER2.charAt(0)) <= 8) {
            return false;
        }

        return true;
    }

    private static int getPlayerSpotsCount(String input, char player) {
        int count = 0;

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == player) {
                count++;
            }
        }

        return count;
    }
}
