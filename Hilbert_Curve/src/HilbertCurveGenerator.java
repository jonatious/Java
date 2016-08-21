import java.util.ArrayList;

public class HilbertCurveGenerator {

    static int x,y;
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

    static ArrayList<Integer> d2xy(int orderOfCurve, int locationInCurve) {
        int isDivisibleBy4, isNotDivBy4ButBy2, dimensionNumber, tempLocation = locationInCurve;
        boolean locIsDivBy4,locIsNotDivBy4ButBy2;
        x = 0;
        y = 0;

        for (dimensionNumber = 1; dimensionNumber < orderOfCurve; dimensionNumber *= 2) {
            isDivisibleBy4 = 1 & (tempLocation / 2);
            isNotDivBy4ButBy2 = 1 & (tempLocation ^ isDivisibleBy4);
            locIsDivBy4 = checkIfEven(tempLocation / 2);
            rot(dimensionNumber, locIsDivBy4, convertIntToBoolean(isNotDivBy4ButBy2));
            if(!locIsDivBy4)
                x+=dimensionNumber;
            y+= dimensionNumber * isNotDivBy4ButBy2;
            tempLocation /= 4;
        }
        ArrayList<Integer> returnList = new ArrayList<Integer>();
        returnList.add(x);
        returnList.add(y);
        return returnList;
    }

    private static boolean convertIntToBoolean(int number){
        if(number == 1)
            return false;
        else
            return true;
    }

    private static boolean checkIfEven(int number){
        if((number % 2) == 0)
            return true;
        else
            return false;
    }

}
