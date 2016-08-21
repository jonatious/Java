package tictactoeconsole;

import java.util.Scanner;
import static tictactoeconsole.TicTacToeConsole.size;
import static tictactoeconsole.TicTacToeConsole.board;

public class Controls {
    public static boolean board_full() {
        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++)
                if(board[i][j] == null)
                    return false;
        return true;
    }
    
    public static int get_row(Scanner sc){
        int row;
        while(true){
            System.out.print("What is your move? \nPlease type a row number: ");
            row = sc.nextInt();
            if(row > size || row < 1){
                System.out.println("Invalid Row");
            }
            else
                break;
        }
        return row;
    }
    
    public static int get_col(Scanner sc){
        int col;
        while(true){
            System.out.print("Please type a column number: ");
            col = sc.nextInt();
            if(col > size || col < 1){
                System.out.println("Invalid Column");
            }
            else
                break;
        }
        return col;
    }

    public static boolean check_winner() {
        
        Object[] scores = getScores();
        
        int score_X = (int)scores[0];
        int score_O = (int)scores[1];
        
        if(board_full()){
            if(score_X > score_O)
                System.out.println("Hurray! You won!!");
            else if(score_O > score_X)
                System.out.println("Computer wins");
            else
                System.out.println("Its a tie.");
            return true;
        }
        
        if(score_X == size){
            System.out.println("Hurray! You won!!");
            return true;
        }
        if(score_O == size){
            System.out.println("Computer wins");
            return true;
        }
        return false;
    }
    
    public static Object[] getScores(){
        int score_X = 0, score_O = 0;
        
        //checking row_wise
        for(int i=0; i<size; i++){
            int temp_X = 0, temp_O = 0;
            for(int j=0; j<size; j++){
                if(board[i][j] != null)
                    if(board[i][j].equals("X"))
                        temp_X++;
                    else if(board[i][j].equals("O"))
                        temp_O++;
            }
            if(score_X < temp_X)
                score_X = temp_X;
            if(score_O < temp_O)
                score_O = temp_O;
        }
        
        //checking column_wise
        for(int i=0; i<size; i++){
            int temp_X = 0, temp_O = 0;
            for(int j=0; j<size; j++){
                if(board[j][i] != null)
                    if(board[j][i].equals("X"))
                        temp_X++;
                    else if(board[j][i].equals("O"))
                        temp_O++;
            }
            if(score_X < temp_X)
                score_X = temp_X;
            if(score_O < temp_O)
                score_O = temp_O;
        }
        
        //checking diagonals
        int temp_X = 0, temp_O = 0;
        for(int i=0; i<size; i++){
            if(board[i][i] != null)
                if(board[i][i].equals("X"))
                    temp_X++;
                else if(board[i][i].equals("O"))
                    temp_O++;
        }
        if(score_X < temp_X)
            score_X = temp_X;
        if(score_O < temp_O)
            score_O = temp_O;
        
       temp_X = 0;
       temp_O = 0;
        for(int i=0; i<size; i++){
            if(board[i][size-1-i] != null)
                if(board[i][size-1-i].equals("X"))
                    temp_X++;
                else if(board[i][size-1-i].equals("O"))
                    temp_O++;
        }
        if(score_X < temp_X)
            score_X = temp_X;
        if(score_O < temp_O)
            score_O = temp_O;
        return new Object[] {score_X, score_O};
    }
    
    public static String winner (Object[] scores){
        int score_X = (int)scores[0];
        int score_O = (int)scores[1];

        if ((score_O > score_X && board_full()) || score_O == size)
            return "O";
        else if ((score_X > score_O && board_full()) || score_X == size)
            return "X";
        else
            return "";
    }
    
}
