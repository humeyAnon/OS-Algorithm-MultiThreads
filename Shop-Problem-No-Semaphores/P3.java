

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class P3 {

    public static String inputFile;
    private static ArrayList<CustomerP3> list = new ArrayList<>();
    private static ShopP3 shopP3 = new ShopP3();
    private static CountDownLatch latch;
    private static String output = "Customer      Arrives \t  Seats\t     Leaves\n";
	
	// Creates the shopThread, read in customers and set up the latch
	// Start all customers, main thread is then paused and awaits for all customers to finish
	// then prints output
    public static void main(String[] args) throws IOException, InterruptedException {

        if(args.length < 1) {
            System.out.println("No input file provided.");
            System.exit(0);
        }
        else {

            inputFile = readFile(args[0]);
            Thread shopThread = new Thread(shopP3);
            readCustomers(inputFile);

            shopP3.setTotalCustomers(list.size());

            latch = new CountDownLatch(list.size());
            sendLatch();

            for(CustomerP3 c : list){
                c.start();
            }

            shopThread.start();

            latch.await();
            sortCustomerList();
            printOutput();

        }
    }

    private static void printOutput(){
        for(CustomerP3 c : P3.list){
            output = output.concat("C" + c.getCustomerID() + "\t\t" + c.getArrivalTime() + "\t   " + c.getSeatTime() + "\t\t" + c.getLeaveTime() + "\n");
        }

        System.out.println(output);
    }

    private static void sortCustomerList() {
        Collections.sort(P3.list, (o1, o2) -> {
            // Arrival times are the same, so sort by the customerId
            if(o1.getArrivalTime() == o2.getArrivalTime()){
                return Integer.compare(o1.getCustomerID(), o2.getCustomerID());
            }
            return Integer.compare(o1.getArrivalTime(), o2.getArrivalTime());
        });
    }
	
	// Sends latch to all customers
    private static void sendLatch() {
        for(CustomerP3 c : list){
            c.setLatch(latch);
        }
    }

    // Function to utilise regex to extract the relevant information from the input files
    // Creates a customer and adds it to the arrayList
    private static void readCustomers(String inputFile) {

        String regexPattern = "(?<ARRIVE>[\\d]+)[\\s][C](?<CUSTOMER>[\\d]+)[\\s](?<EATING>[\\d]+)";
        Pattern inputPattern = Pattern.compile(regexPattern);
        Matcher inputMatcher = inputPattern.matcher(inputFile);

        while(inputMatcher.find()){

            list.add(new CustomerP3(Integer.parseInt(inputMatcher.group("ARRIVE")),
                    Integer.parseInt(inputMatcher.group("CUSTOMER")),
                    Integer.parseInt(inputMatcher.group("EATING")),
                    shopP3));
        }
    }

    // Helper function to read in the input file
    static String readFile(String path)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

}


