package tictactoeconsole;

import java.util.Scanner;
import static tictactoeconsole.TicTacToeConsole.player_first;
import static tictactoeconsole.TicTacToeConsole.board;
import static tictactoeconsole.TicTacToeConsole.size;

public class StartGame {

    public static void start_game(Scanner sc) {
        int row, col;
        while(true){
            
            if(!player_first)
                if(!Controls.board_full()){
                    ComputerMove.computer_play();
                    print_board(board);
                }
            
            if(Controls.check_winner())
                return;
            
            while(true){
                row = Controls.get_row(sc);
                col = Controls.get_col(sc);
                if(board[row-1][col-1] == null)
                    break;
                else
                    System.out.println("\nBox has been taken. Please try again!!\n");
            }
            board[row-1][col-1] = "X";
            print_board(board);
            
            if(Controls.check_winner())
                return;
            
            if(player_first)
                if(!Controls.board_full())
                    ComputerMove.computer_play();
                    print_board(board);
        }
    }
    
    public static void print_board(String[][] board) {
        System.out.print("  ");
        for(int i=1; i<=size; i++)
            System.out.print(i + " ");
                    
        for(int i=1; i<=size; i++){
            print_pattern();
            print_row(i,board[i-1]);
        }
        print_pattern();
        print_NL();
    }
    
    public static void print_pattern(){
        print_NL();
        System.out.print(" +");
        for(int i=1; i<=size; i++)
            System.out.print("-+");
    }

    public static void print_row(int n, String[] row) {
        print_NL();
        System.out.print(n + "|");
        for(int i=1; i<=size; i++){
            if(row[i-1] != null)
                System.out.print(row[i-1] + "|");
            else {
                System.out.print(" |");
            }
        }
    }

    public static void print_NL(){
        System.out.println("");
    }
    
}
