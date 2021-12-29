
// Shop class - Calculates the logic for each customers inside the shop - then holds the customer if its not their 
//				leave time, once its their leave time, set finished and release the semaphore

import java.util.concurrent.Semaphore;

public class Shop {

    private Semaphore shopSem;
    public ClockTime ct;

    public Shop(ClockTime ct){

        shopSem = new Semaphore(5, true);
        this.ct = ct;

    }

    public Semaphore getShopSem() {
        return shopSem;
    }

    // Customer enters the shop - Sets the customers seat/leave timings and sets the clocks last customer time
    // Holds the thread until the clock gets to the customers leave time, note this could be done better I think
    // perhaps with a semaphore? But I did this question wrong and had to re-do it and spend to long on it.
    // Sets the customer as finished and releases the shop Semaphore
    public void enterShop(Customer c) {
        try {

            c.setSeatTime(ct.getClockTime());

            c.setLeaveTime(ct.getClockTime() + c.getEatingTime());
            ct.setLastCustomerLeftTime(c.getLeaveTime());

            while(ct.getClockTime() < c.getLeaveTime()) {
                Thread.sleep(1);
            }

            c.setFinished(true);

            ct.customerLeft();

            shopSem.release();

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
