/*
 * CS335 
 * Summer 2024
 * Midterm - Group 1
 * Name: Sophie Steinberger
 * Created: 06/11/2024
 */

package reading_bubby.src;

import java.util.*;

public class ReadBook extends Book {

    private int rating;
    private String username;

    public ReadBook(int id, String t, String a, String uName){
        super(id, t, a);
        username = uName;
        //default set to -1, which will need to code to not display unless rating >= 0
        rating = -1;
    }

    public ReadBook(int id, String t, String a, String uName, int r){
        super(id, t, a);
        username = uName;
        //default set to -1, which will need to code to not display unless rating >= 0
        rating = r;
    }

    public int getRating(){
        return rating;
    }

    public String getUsername(){
        return username;
    }

    //QUESTION: will this update that ReadBook's rating in both the user's readBooks ArrayList AND the allReadBooks ArrayList
    public void rateBook(Scanner myScanner){
        System.out.println("Rating " +  super.getTitle() + " by " + super.getAuthor() + ":\n");
        //0 exits because nothing happens
        int numSelected = validateNumber(0, 10, myScanner);
        if(numSelected != 0){
            rating = numSelected;
        }
    }

    public int validateNumber(int minVal, int maxVal, Scanner myScanner){
        int selection = -1;
        do {
            System.out.print("Enter a number from " + minVal + " to " + maxVal + ": ");
            while (!myScanner.hasNextInt()) {
                myScanner.next();
                System.out.println("Enter a number from " + minVal + " to " + maxVal + ": ");
            }
            selection = myScanner.nextInt();
        } while (selection > maxVal || selection < minVal);
        return selection;
    }
}
