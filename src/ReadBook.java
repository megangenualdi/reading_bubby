/*
 * CS335 
 * Summer 2024
 * Group 1
 * Name: Sophie Steinberger
 * Created: 06/11/2024
 * Last Updated: 07/23/2024
 */

package reading_bubby.src;

import java.util.*;

public class ReadBook extends Book {

    private int rating;
    private String username;

    public ReadBook(int id, String t, String a, String uName){
        super(id, t, a);
        username = uName;
        //default set to -1, but there is code to not display unless rating >= 0
        rating = -1;
    }

    public ReadBook(int id, String t, String a, String uName, int r){
        super(id, t, a);
        username = uName;
        //default set to -1, there is code to not display unless rating >= 0
        rating = r;
    }

    public int getRating(){
        return rating;
    }

    public String getUsername(){
        return username;
    }

    public void rateBook(Scanner myScanner){
        System.out.println("Rating " +  super.getTitle() + " by " + super.getAuthor() + "\n");
        System.out.print("Rate the book from 1 to 10, or enter 0 to exit without rating: ");
        //0 exits because nothing happens
        int numSelected = validateNumber(0, 10, myScanner);
        if(numSelected != 0){
            rating = numSelected;
        }
    }

    public int validateNumber(int minVal, int maxVal, Scanner myScanner){
        int selection = -1;
        do {
            while (!myScanner.hasNextInt()) {
                myScanner.next();
                if (myScanner.hasNextLine()){
                    System.out.print("Enter a number from " + minVal + " to " + maxVal + ": ");
                }
            }
            selection = myScanner.nextInt();
            if (selection > maxVal || selection < minVal){
                System.out.print("Enter a number from " + minVal + " to " + maxVal + ": ");
            }
        } while (selection > maxVal || selection < minVal);
        return selection;
    }
}
