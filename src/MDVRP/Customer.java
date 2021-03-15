package MDVRP;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Customer {
    private int id;
    private int x;
    private int y;
    private int duration;
    private int demand;
    private boolean onBorderline;
    private List<Integer> possibleDepotsIDs = new ArrayList<>();


    public Customer(Integer id, Integer x, Integer y, Integer duration, Integer demand) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.duration = duration;
        this.demand = demand;
    }

    public void addPossibleDepot(int possibleDepotsID) {
        if (! this.onBorderline) {
            this.onBorderline = true;
        }
        this.possibleDepotsIDs.add(possibleDepotsID);
    }

    public int getId()                          { return id; }
    public int getX()                           { return x; }
    public int getY()                           { return y; }
    public int getDuration()                    { return duration; }
    public int getDemand()                      { return demand; }
    public boolean getOnBorder()                { return onBorderline; }
    public List<Integer> getPossibleDepots()    { return possibleDepotsIDs; }

    @Override
    public String toString() {
        // return customer id
        return "" + this.id;
    }

}
