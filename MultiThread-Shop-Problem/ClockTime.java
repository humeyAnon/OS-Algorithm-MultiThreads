
// ClockTime Class/Thread that keeps overall time for the shop

public class ClockTime implements Runnable{

    private int clockTime, totalCustomers, lastCustomerLeftTime;
    private boolean cleaningFlag = false;
    private Shop shop;

    public ClockTime(){
        clockTime = 0;
    }

    // While their are still customers the clock thread will continue to run, incrementing clock time after every sleep
    // Due to the customer threads running faster then the clock thread it will keep count appropriately.
    // If on a clock tick it triggers that the shopSemaphore is full that indicates all 5 seats have been taken and cleaning needs to take place
    // So if all 5 seats are never taken in 1 sitting the shop never gets cleaned
    @Override
    public void run(){

        while(totalCustomers != 0){

            try {
                Thread.sleep(100);
                if(shop.getShopSem().availablePermits() == 0){
                    clockTime = lastCustomerLeftTime;
                    clockTime += 4;
                }
                clockTime++;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Getters & Setters
    public int getClockTime() {
        return clockTime;
    }

    public void customerLeft(){
        totalCustomers--;
    }

    public void setLastCustomerLeftTime(int lastCustomerLeftTime) {

        if(this.lastCustomerLeftTime < lastCustomerLeftTime || this.lastCustomerLeftTime == 0){
            this.lastCustomerLeftTime = lastCustomerLeftTime;
        }
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }
}