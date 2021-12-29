
import java.util.concurrent.CountDownLatch;

public class CustomerP3 extends Thread {

    private int arrivalTime, eatingTime, seatTime, leaveTime, customerID;
    private boolean finishedEating;
    private final ShopP3 shop;
    private CountDownLatch latch;

    public CustomerP3(int arrivalTime, int customerID, int eatingTime, ShopP3 shop) {

        this.arrivalTime = arrivalTime;
        this.customerID = customerID;
        this.eatingTime = eatingTime;
        this.finishedEating = false;
        this.shop = shop;

    }

    // Run for customer - If the thread is not ready to enter the shop it triggers wait to sleep
    // every shop tick triggers the shop monitor and wakes up the threads to check the condition again
    // If its time to enter the shop they enter - afterwards they countdown the latch for main thread
    @Override
    public void run(){

        while(arrivalTime >= shop.getShopTime()) {
            synchronized (shop) {
                try {
                    shop.wait();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            shop.enterShop(this);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            latch.countDown();
        }

    }

    // Getters & Setters
    public int getCustomerID() {
        return customerID;
    }

    public void isFinishedEating(boolean finished) {
        this.finishedEating = finished;
    }

    public int getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(int leaveTime) {
        this.leaveTime = leaveTime;
    }

    public int getEatingTime() {
        return eatingTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public void setSeatTime(int seatTime) {
        this.seatTime = seatTime;
    }

    public int getSeatTime() {
        return seatTime;
    }
}
