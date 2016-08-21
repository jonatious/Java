import java.util.ArrayList;

public class HibertGeneratorSpike {
    static int x,y;
    //rotate/flip a quadrant appropriately
    static void rot(int dimensionNumber, boolean locIsDivBy4, boolean locIsNotDivBy4ButBy2) {
        if (locIsNotDivBy4ButBy2) {
            if (!locIsDivBy4) {
                x = dimensionNumber - 1 - x;
                y = dimensionNumber - 1 - y;
            }
            swapxy();
        }
    }

    static void rot(int dimensionNumber, int locIsDivBy4, int locIsNotDivBy4ButBy2) {
        if (locIsNotDivBy4ButBy2 == 0) {
            if (locIsDivBy4 == 1) {
                x = dimensionNumber - 1 - x;
                y = dimensionNumber - 1 - y;
            }
            swapxy();
        }
    }

    public static void swapxy(){
        int t=x;
        x=y;
        y=t;
    }

    //convert d to (x,y)
    static ArrayList<Integer> d2xy(int orderOfCurve, int locationInCurve) {
        int isDivisibleBy4, isNotDivBy4ButBy2, dimensionNumber, tempLocation = locationInCurve;
        boolean locIsDivBy4,locIsNotDivBy4ButBy2;
        x = 0;
        y = 0;

        for (dimensionNumber = 1; dimensionNumber < orderOfCurve; dimensionNumber *= 2) {

            isDivisibleBy4 = 1 & (tempLocation / 2);  // This operation checks if the number is odd or even
            isNotDivBy4ButBy2 = 1 & (tempLocation ^ isDivisibleBy4);
//
            locIsDivBy4 = checkIfEven(tempLocation / 2);
//            if(!locIsDivBy4){
//                locIsNotDivBy4ButBy2 = checkIfEven(tempLocation);
//            }else {
//                locIsNotDivBy4ButBy2 = false;
//            }

            rot(dimensionNumber, locIsDivBy4, convertIntToBoolean(isNotDivBy4ButBy2));
//            if(!locIsDivBy4)
//                x+=dimensionNumber;
//            if(!locIsNotDivBy4ButBy2)
//                y+=dimensionNumber;

            if(!locIsDivBy4)
                x+=dimensionNumber;
            y+= dimensionNumber * isNotDivBy4ButBy2;

//            x += dimensionNumber * isDivisibleBy4;
//            y += dimensionNumber * isNotDivBy4ButBy2;

            tempLocation /= 4;

//            isDivisibleBy4 = 1 & (tempLocation / 2);  // This operation checks if the number is odd or even
//            isNotDivBy4ButBy2 = 1 & (tempLocation ^ isDivisibleBy4); // These two operations together finds the quadrant in which the location is present
//            rot(dimensionNumber, convertIntToBoolean(isDivisibleBy4), convertIntToBoolean(isNotDivBy4ButBy2));
//            x += dimensionNumber * isDivisibleBy4;
//            y += dimensionNumber * isNotDivBy4ButBy2;
//            tempLocation /= 4;
        }

        ArrayList<Integer> returnList = new ArrayList<Integer>();
        returnList.add(x);
        returnList.add(y);
        return returnList;
    }

    static boolean convertIntToBoolean(int number){
        if(number == 1)
            return false;
        else
            return true;
    }

    static boolean checkIfEven(int number){
        if((number % 2) == 0)
            return true;
        else
            return false;
    }

    public static void main(String[] args) throws InterruptedException {
        double x0 = 0.01;
        double y0 = 0.99;
        double a0 = 0.0;
        //double step = Math.sqrt(3)/2;
        double step = 0.0078;
        int x1 = d2xy(128,0).get(0);
        int y1 = d2xy(128,0).get(1);
        System.out.println(x1+","+y1);
        int x2 = d2xy(128,1).get(0);
        int y2 = d2xy(128,1).get(1);
        if(x2>x1)
            a0 = 270.0;
        x1=x2;
        y1=y2;
       // System.out.println(x1+","+y1);
        TurtleSpike turtleSpike = new TurtleSpike(x0, y0, a0);
        turtleSpike.goForward(step);
        for (int i = 2; i < 16384; i++) {
            x2 = d2xy(128,i).get(0);
            y2 = d2xy(128,i).get(1);
            if(x1>x2){
                if(a0 == 0){
                    turtleSpike.turnLeft(90.0);
                }
                else if(a0==180){
                    turtleSpike.turnLeft(270.0);
                }
                turtleSpike.goForward(step);
                a0 = 90.0;
            }
            else if(x2>x1){
                if(a0 == 180){
                    turtleSpike.turnLeft(90.0);
                }
                else if(a0 == 0){
                    turtleSpike.turnLeft(270.0);
                }
                turtleSpike.goForward(step);
                a0 = 270.0;
            }

            if(y1>y2){
                if(a0 == 90){
                    turtleSpike.turnLeft(90.0);
                }
                else if(a0 == 270){
                    turtleSpike.turnLeft(270.0);
                }
                turtleSpike.goForward(step);
                a0 = 180.0;
            }
            else if(y2>y1){
                if(a0 == 270){
                    turtleSpike.turnLeft(90.0);
                }
                else if(a0 == 90){
                    turtleSpike.turnLeft(270.0);
                }
                turtleSpike.goForward(step);
                a0 = 0.0;
            }
            x1=x2;
            y1=y2;
//            Thread.sleep(10);
         //   System.out.println(x1+","+y1);
        }


    }
}
