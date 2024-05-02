package ui;

import model.Event;
import model.EventLog;

//This is just a (static) utility class that handles the printing of the logs.
public class Printer {

    //EFFECTS: an empty constructor, literally does nothing
    private Printer() {

    }


    //EFFECTS: prints out each Event in the EventLog singleton.
    public static void printLog() {
        for (Event event : EventLog.getInstance()) {
            System.out.println(event.getDescription());
        }
    }
}
