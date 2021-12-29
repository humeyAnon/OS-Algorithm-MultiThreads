
// Main class - Reads input, creates all needed threads and latch to pause main thread until needed
// 				Sorts the array of threads for the needed output order

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class P2 {

    private static String inputFile;
    private static Shop shop;
    private final static ArrayList<Customer> list = new ArrayList<>();
    private static ClockTime timer = new ClockTime();
    private static CountDownLatch latch;
    private static String output = "Customer      Arrives \t  Seats\t     Leaves\n";

    // Main reads in text file - Creates the timer thread, shop, all customers and a countdownlatch
    // Timer starts, give the latch to all customers and start all the customer threads
    // Main thread then gets paused until all customers have finished due to the countdownlatch - then prints output string
    public static void main(String[] args) throws IOException, InterruptedException {

        if(args.length < 1) {
            System.out.println("No input file provided.");
            System.exit(0);
        }
        else {

            inputFile = readFile(args[0]);

            Thread threadTime = new Thread(timer);
            shop = new Shop(timer);

            timer.setShop(shop);

            readCustomers(inputFile);

            timer.setTotalCustomers(list.size());
            threadTime.start();

            latch = new CountDownLatch(list.size());

            sendLatch();

            for(Customer c : list){
                c.start();
            }

            latch.await();

            sortCustomerList();
            printOutput();

        }
    }

        private static void printOutput(){
            for(Customer c : P2.list){
                output = output.concat("C" + c.getCustomerID() + "\t\t" + c.getArrivalTime() + "\t   " + c.getSeatTime() + "\t\t" + c.getLeaveTime() + "\n");
            }

            System.out.println(output);
        }

        // Gives the latch to all the customer threads
        private static void sendLatch() {
            for(Customer c : list){
                c.setLatch(latch);
            }
        }

        // Function to utilise regex to extract the relevant information from the input files
        // Creates a customer and adds it to the arrayList
        private static void readCustomers(String inputFile) {

            String regexPattern = "(?<ARRIVE>[\\d]+)[\\s][C](?<CUSTOMER>[\\d]+)[\\s](?<EATING>[\\d]+)";
            Pattern inputPattern = Pattern.compile(regexPattern);
            Matcher inputMatcher = inputPattern.matcher(inputFile);

            while (inputMatcher.find()) {
                        list.add(new Customer(Integer.parseInt(inputMatcher.group("ARRIVE")),
                            Integer.parseInt(inputMatcher.group("CUSTOMER")),
                            Integer.parseInt(inputMatcher.group("EATING")),
                            shop, timer));
            }

        }

        // Method to sort the array via ArriveTime and CustomerID if needed
        // For inorder output
        private static void sortCustomerList() {
            Collections.sort(P2.list, (o1, o2) -> {
                // Arrival times are the same, so sort by the customerId
                if(o1.getArrivalTime() == o2.getArrivalTime()){
                    return Integer.compare(o1.getCustomerID(), o2.getCustomerID());
                }

                return Integer.compare(o1.getArrivalTime(), o2.getArrivalTime());
            });
        }

        // Helper method to read in the input file
        static String readFile(String path)
                throws IOException
        {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, StandardCharsets.UTF_8);
        }


}



