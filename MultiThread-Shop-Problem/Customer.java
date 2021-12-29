
// Customer thread Q1

import java.util.concurrent.CountDownLatch;

public class Customer extends Thread{

    private int arrivalTime, eatingTime, seatTime, leaveTime, customerID;
    private final Shop shop;
    private boolean finished;
    private ClockTime ct;
    private CountDownLatch latch;

    public Customer(int arriveTime, int customerID, int eatingTime, Shop shop, ClockTime ct) {

        this.arrivalTime = arriveTime;
        this.customerID = customerID;
        this.eatingTime = eatingTime;
        this.shop = shop;
        this.finished = false;
        this.ct = ct;

    }

    // Customers run method - sleeps for 1ms to stop threads bottlenecking into the Semaphore
    // Constantly loops while they have no finished eating - Once the clock time hits their arrival time
    // They try to acquire the shopSem, if its full they are put in the Semaphore wait queue, else they enter the shop
    // Once they are finished they notify the latch that 1 thread has completed.
    @Override
    public void run() {

        while(!finished) {
                try {
                   Thread.sleep(1);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

            if(this.arrivalTime <= ct.getClockTime()) {

                try {
                    shop.getShopSem().acquire();
                    shop.enterShop(this);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    latch.countDown();
                }
            }
        }
    }

    //Getters && Setters
    public int getEatingTime() {
        return eatingTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setSeatTime(int seatTime) {
        this.seatTime = seatTime;
    }

    public void setLeaveTime(int leaveTime) {
        this.leaveTime = leaveTime;
    }

    public int getSeatTime() {
        return seatTime;
    }

    public int getLeaveTime() {
        return leaveTime;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }
}
