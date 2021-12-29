
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.regex.*;

public class P1 {

    public static String inputFile;
                public static int northFarmers, southFarmers, count = 1;
                public static SkinnyBridge bridge = new SkinnyBridge();

                public static void main(String[] args) throws IOException {

                    if(args.length < 1){
                        System.out.println("No input file provided");
                        System.exit(0);
                    }
                    else {
                        // Reading the file for the amount of North and South farmers
                        inputFile = readFile(args[0]);
                        startBridgeSimulator(inputFile);

                        // Making the north farmers and add to the array
                        for(int i = 0; i < northFarmers; i++){
                            Farmer n = new Farmer(0, count, bridge);
                            n.start();
                            count++;
                        }

                        // Resetting the count for south farmers
                        count = 1;

                        // Making the south farmers and adding to the array
                        for(int i = 0; i < southFarmers; i++){
                            Farmer s = new Farmer( 1, count, bridge);
                            s.start();
                            count++;
            }
        }
    }

    private static void startBridgeSimulator(String inputFile) {
        // Pattern and matcher to create the array of farmers

        String regexPattern = "N=(?<N>[\\d]+)+[,][\\s]+S=(?<S>[\\d]+)";
        Pattern inputPattern = Pattern.compile(regexPattern);
        Matcher inputMatcher = inputPattern.matcher(inputFile);

        while(inputMatcher.find()){

            northFarmers = Integer.parseInt(inputMatcher.group("N"));
            southFarmers = Integer.parseInt(inputMatcher.group("S"));

        }
    }

    static String readFile(String path)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

}