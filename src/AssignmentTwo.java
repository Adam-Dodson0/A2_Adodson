import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *- Main class for Assessment 2 -
 * Theme Park Management System
 * @author Adam Dodson
 * @version 1
 */

public class AssignmentTwo {

    /**
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws InterruptedException {

    // =======================================
    // PART 1 Modelling the Theme Parks People 
    // =======================================

        //Staff and Visitors to the Theme park
        // Staff staffA = new staff("","", , "");
        // Visitor visA = new Visitor("", "", ,"");
        Staff staffA = new Staff("15","Tim Berners Lee", 51, "Ride Operator");
        Staff staffB = new Staff("245","Alan Turing", 32, "Maintenance");
        Staff staffC = new Staff("193","Ada Lovelace", 28, "Cleaner");
        Staff staffD = new Staff("562","Margaret Hamilton", 41, "Manager");
        Staff staffE = new Staff("349","Edsger Dijkstra", 39, "Handler");
        Staff staffF = new Staff("85","Robert Kahn",21);
        Visitor visA = new Visitor("7","Linus Torvalds", 25, "VIP");
        Visitor visB = new Visitor("284", "Ken Thompson", 31, "ADULT");
        Visitor visC = new Visitor("916", "Grace Hooper", 19, "STUDENT");
        Visitor visD = new Visitor("405", "Vint Cerf", 45, "CONCESSION");

        System.out.println(staffA);
        System.out.println(staffB);
        System.out.println(staffC);
        System.out.println(staffD);
        System.out.println(staffE);
        System.out.println(staffF);
        System.out.println(visA);
        System.out.println(visB);
        System.out.println(visC);
        System.out.println(visD);



    // =============================================
    // PART 2 Modelling the Parks People Attractions
    // =============================================

        Ride coaster = new Ride("R728", "Thunder Coaster", 23, 47);
        Show animal = new Show("S391", "Animal Feeding", 27, 83, List.of("10:15", "13:40", "15:25"));
        Toilet blockA = new Toilet("TB614", "Near the main entrance");

        System.out.println(coaster);
        System.out.println(animal);
        System.out.println(blockA);

        coaster.assignOperator(staffA);
        animal.assignOperator(staffE);
        animal.removeOperator();
        animal.assignOperator(staffE);

    // ==================================
    // PART 3 The Attractions Waitingline
    // ==================================
    


    // ========================
    // PART 4 The visit History
    // ========================



    // ==============================
    // PART 5 Operating an attraction
    // ==============================



    // ========================
    // PART 6 Managing the park
    // ========================



    // ========================================
    // PART 7 Backing up and restoring the park
    // ========================================



    // =====================================
    // PART 8 Running the park (concurrency)
    // =====================================

    }
}
