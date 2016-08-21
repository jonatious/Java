package tictactoeconsole;
import java.util.Scanner;

public class TicTacToeConsole {

    static int size = 3;
    static boolean player_first = true;
    static int diff = 1; //1 = easy; 2 = medium; 3 = hard
    static String[][] board = new String[size][size];
    static int win_row,win_col;
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("\nWelcome to Tic-Tac-Toe!!\n\nRules: Get maximum score by putting as many X's as you can in a row/column/diagonal. Good Luck!");

        get_difficulty(sc);
        game_loop(sc);
    }
		
    public static void get_difficulty(Scanner sc){
        while(true){
            System.out.println("\nChoose your difficulty\n1.Easy\n2.Medium\n3.Hard");
            diff = sc.nextInt();
            if(diff > 0 && diff < 4)
                break;
            System.out.println("Invalid difficulty. Try again.");
        }
        StartGame.print_board(board);
    }

    private static void game_loop(Scanner sc) {
        while(true){
            StartGame.start_game(sc);
            System.out.println("\nEnter 1 to play again. Any other key to exit");
            String option = sc.next();
            if(!option.equals("1"))
                return;
            player_first = !player_first;
            for(int i=0; i<size; i++)
                for(int j=0; j<size; j++)
                    board[i][j] = null;
            StartGame.print_board(board);
        }
    }

    
}
