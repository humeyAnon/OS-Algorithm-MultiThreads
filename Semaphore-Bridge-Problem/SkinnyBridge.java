
import java.util.concurrent.Semaphore;

// Simple class that controls the semaphore which simulates the 1 lane bridge
// updates the neon sign as farmers cross back and forth

public class SkinnyBridge {

    public Semaphore skinnyBridgeSem;
    public int neonSign = 0;

    public SkinnyBridge(){
        skinnyBridgeSem = new Semaphore(1,true);
    }

    public void bridgeCross() throws InterruptedException {

        skinnyBridgeSem.acquire();

    }

    public void bridgeCrossed(){

        skinnyBridgeSem.release();

    }

    public void incrementNeonSign() {
        this.neonSign++;
    }

    public int getNeonSign() {
        return neonSign;
    }
}
