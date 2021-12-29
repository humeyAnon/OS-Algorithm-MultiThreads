
// Farmer thread class - that runs its fun function constantly
// 				   		 switching which side it is trying to cross from
public class Farmer extends Thread {

    private final String farmerName;
    private String direction;
    private final int farmerNo;
    private final SkinnyBridge bridge;

    public Farmer(int typeOfFarmer, int farmerNo, SkinnyBridge bridge){

        this.farmerNo = farmerNo;
        if(typeOfFarmer == 0){
            this.farmerName = "N_Farmer" + farmerNo;
            this.direction = "South";
        }
        else {
            this.farmerName = "S_Farmer" + farmerNo;
            this.direction = "North";
        }
        this.bridge = bridge;
    }

    @Override
    public void run(){
        // Runs until 100 farmers have crossed the bridge

        while(true) {

            System.out.println(farmerName + ": Waiting for bridge. Going towards " + direction);

            try {
                // Acquiring access to the big ol single lane bridge
                bridge.bridgeCross();

                System.out.println(farmerName + ": Crossing bridge Step 5.");
                Thread.sleep(500);
                System.out.println(farmerName + ": Crossing bridge Step 10.");
                Thread.sleep(500);
                System.out.println(farmerName + ": Crossing bridge Step 15.");

                System.out.println(farmerName + ": Across the bridge.");

                bridge.incrementNeonSign();

                System.out.println("NEON = " + bridge.getNeonSign());

                // Has crossed, release lock
                bridge.bridgeCrossed();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (direction == "North") {
                this.direction = "South";
            }
            else {
                this.direction = "North";
            }
        }
    }

    public int getFarmerNo() {
        return farmerNo;
    }

    public String getFarmerName() {
        return farmerName;
    }
}
