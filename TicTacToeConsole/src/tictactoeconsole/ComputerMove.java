package tictactoeconsole;

import static tictactoeconsole.TicTacToeConsole.board;
import static tictactoeconsole.TicTacToeConsole.diff;
import static tictactoeconsole.TicTacToeConsole.size;
import java.util.ArrayList;
import java.util.Random;

public class ComputerMove {
    
    static int[][] preferred_moves ;
    
    public static void computer_play(){
        switch (diff) {
            case 1:
                make_rand_move();
                break;
            case 2:
                if(!try_win_move())
                    make_rand_move();
                break;
            case 3:
                initialize_moves();
                hard_move();
                break;
        }
    }
    
    public static void make_rand_move(){
        int row,col;
        while(true){
                row = get_rand();
                col = get_rand();
                if(board[row-1][col-1] == null){
                    board[row-1][col-1] = "O";
                    System.out.println("My move is " + row + ", " + col);
                    return;
                }
            }
    }
    
    public static boolean try_win_move(){
        for(int i=0; i<size; i++)
            for(int j=0;j<size; j++){
                if(board[i][j] == null){
                    String temp = board[i][j];
                    board[i][j] = "O";
                    if(Controls.winner(Controls.getScores()).equals("O")){
                        System.out.println("My move is " + i+1 + ", " + j+1);
                        return true;
                    }
                    else
                        board[i][j] = temp;
                }
            }
        return false;
    }
    
    public static int get_rand(){
        return (int)Math.round(Math.random()*(size-1) + 1);
    }
    
    public static void initialize_moves() {
	int loop_length = (size*size)+5 ;
        double mid_point = (long)size/2;
        preferred_moves = new int[loop_length][2]; 

        int[] move0 = {(int)mid_point,(int)mid_point};
        int[] move1 = {0,0};
        int[] move2 = {0,size-1};
        int[] move3 = {size-1,0};
        int[] move4 = {size-1,size-1};

        preferred_moves[0]= move0;
        preferred_moves[1]= move1;
        preferred_moves[2]= move2;
        preferred_moves[3]= move3;
        preferred_moves[4]= move4;
        int currItem=5;

        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                int[] move = {i,j};
                if(currItem!= (loop_length-1)){
                    preferred_moves[currItem] = move;
                    currItem = currItem+1;
                }
            }
        }
    }
    
    public static void hard_move() {
        Random rand = new Random();
        ArrayList<Integer> number = new ArrayList<Integer>();
        int n = size, row, col, i, Select, loc,k=0;
        int size=n*n;
        int[] moves = new int[2];
        for(i=0;i<size;i++){
            number.add(i);
        }
        for(i=0;i<size;i++){
            Select = rand.nextInt(number.size());
            loc = number.get(Select);
            number.remove(Select);   
            row = (int)loc/n;
            col = (int)loc%n;
            if(board[row][col] == null){
                board[row][col] = "O";
                if(row >= 0 && row < n && col >= 0 && col < n && Controls.winner(Controls.getScores()).equals("O")){
                    System.out.println("My move is " + (row+1) + ", " + (col+1));
                    return;
                }
                else
                    board[row][col] = null;
            }
        }
        
        for (int[] row_col : preferred_moves) {
            if (board[row_col[0]][row_col[1]] == null) {
                board[row_col[0]][row_col[1]] = "O";
                System.out.println("My move is " + (row_col[0]+1) + ", " + (row_col[1]+1));
                return;
            }
        }
        make_rand_move();
    }
}
