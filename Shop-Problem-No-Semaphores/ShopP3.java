
// Shop thread Q3

import java.util.concurrent.ArrayBlockingQueue;

public class ShopP3 extends Thread {

    private int totalCustomers, lastCustomerTime;
    private int shopTime, count = 0;
    private ArrayBlockingQueue<CustomerP3> seats = new ArrayBlockingQueue<>(5);

    public ShopP3() {
        shopTime = 0;
    }

    // Shop thread will run while there are still customers inside the shop - Sleeps for the customers to run through
    // Their thread calculations, checks if the seats Q is full - If it is the shop needs to be cleaned so do this
    // Check if any customers are needing to leave the restaurant and remove from the seat Q - increase shopTime
    // and notify the shop monitor for customer threads to check if they are ready to enter the shop
    @Override
    public void run() {

        while(totalCustomers != 0) {
            try {
                Thread.sleep(100);

                if(checkFull()) {
                    shopTime = lastCustomerTime;
                    shopTime += 5;
                }

                checkIfReadyToLeave();

                ++shopTime;

                synchronized (this){
                    notifyAll();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkFull(){
        return seats.size() == 5;
    }

    public void checkIfReadyToLeave(){
            for (CustomerP3 c : seats) {
                if (c.getLeaveTime() == shopTime || c.getLeaveTime() <= shopTime) {
                    c.isFinishedEating(true);
                    totalCustomers--;
                    seats.remove(c);

                }
            }
    }

    // Sets the customers seat/leave time for output, figured out the time of when
    // the last customer will leave the shop, Add the thread to the blockingQ - Due to the nature of how this runs
    // All threads inside will ultimately need to wait for the shopTime to catch up to the LeaveTime - As the shop run
    // Checks if its time for the customer to leave.
    public void enterShop(CustomerP3 c) throws InterruptedException {

        c.setSeatTime(shopTime - 1);
        c.setLeaveTime(shopTime + (c.getEatingTime() - 1));

        setLastCustomerTime(c);

        if(shopTime <= c.getLeaveTime() && !seats.contains(c)){
                seats.add(c);
        }
    }

    // Getters and Setters
    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public int getShopTime() {
        return shopTime;
    }

    public void setLastCustomerTime(CustomerP3 c){
        if(c.getLeaveTime() > lastCustomerTime){
            lastCustomerTime = c.getLeaveTime();
        }
    }
}
