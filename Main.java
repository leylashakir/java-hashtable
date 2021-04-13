package main;

import java.io.*;
import java.util.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class Main {

    // Hash function utilized to calculate keys for the bids hashtable
    public static long hashFunction(String bidId, long tableSize) {
        long longBidId = Long.parseLong(bidId);
        return longBidId % tableSize;
    }

    public static Hashtable loadBids (String CSV_PATH) {

        // Initialize new empty hashtable with default size and load factor
        Hashtable<Long, Bid> bids = new Hashtable<>();

        long tableSize = 0;

        try {
            // get bid info from CSV file and load bids into the hashtable, using previously defined hashfunction for the keys
            Reader in = new FileReader(CSV_PATH);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                Bid bid = new Bid();
                bid.title = record.get(0);
                bid.bidId = record.get(1);
                bid.fund = record.get(8);
                bid.amount = record.get(4);
                tableSize++;
                bids.put(hashFunction(bid.bidId, tableSize), bid);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bids;
    }

    public static void printAll(Hashtable bids) {
        Enumeration keys = bids.keys();
        while (keys.hasMoreElements()) {
            long key = (long) keys.nextElement();
            Bid bid = (Bid) bids.get(key);
            System.out.println("Key: " + key + " " + "Bid ID: " + bid.bidId + ", Bid Amount: " + bid.amount + ", Bid Title: " + bid.title + ", Bid Fund: " + bid.fund);
        }
    }

    public static void search(Hashtable bids, String bidId) {
        Enumeration values = bids.elements();
        while (values.hasMoreElements()) {
            Bid bid = (Bid) values.nextElement();
            if (bid.bidId.equals(bidId)) {
                System.out.println("Bid found!");
                System.out.println("Bid ID: " + bid.bidId + ", Bid Amount: " + bid.amount + ", Bid Title: " + bid.title + ", Bid Fund: " + bid.fund);
            }
        }
        System.out.println("Search complete.");
    }

    public static Hashtable delete(Hashtable bids, String bidId) {
        Enumeration keys = bids.keys();
        while (keys.hasMoreElements()) {
            long key = (long) keys.nextElement();
            Bid bid = (Bid) bids.get(key);
            if (bid.bidId.equals(bidId)) {
                System.out.println("Bid found.");
                System.out.println("Bid ID: " + bid.bidId + ", Bid Amount: " + bid.amount + ", Bid Title: " + bid.title + ", Bid Fund: " + bid.fund);
                bids.remove(bid);
                System.out.println("Bid removed successfully.");
            }
        }
        return bids;
    }

    public static void main(String[] args) {
        // Path to CSV file that contains data
        final String CSV_PATH = "eBid_Monthly_Sales.csv";

        Scanner scanner = new Scanner(System.in);

        System.out.println("Please select one of the following options:\n");
        System.out.println("1. Load Bids");
        System.out.println("2. Display all Bids");
        System.out.println("3. Find Bid By Bid ID");
        System.out.println("4. Remove Bid");
        System.out.println("5. Exit");

        int input = scanner.nextInt();

        long loadTimeStart = System.currentTimeMillis();
        Hashtable bidTable = loadBids(CSV_PATH);
        long loadTimeEnd = System.currentTimeMillis();
        long loadTimeComplete = loadTimeEnd - loadTimeStart;

        switch (input) {
            case 1: {
                System.out.println("Bid table loaded. Time elapsed: " + loadTimeComplete + " ms\n");
                main(args);
                break;
            }
            case 2: {
                long timeStart = System.currentTimeMillis();
                printAll(bidTable);
                long timeEnd = System.currentTimeMillis();
                long timeElapsed = timeEnd - timeStart;
                System.out.println("Bids printed. Time elapsed: " + timeElapsed + " ms\n");
                main(args);
                break;
            }
            case 3: {
                System.out.println("Please enter a valid 5-digit Auction ID:");
                try {
                    int bidToFind = scanner.nextInt();
                    String bidToFindString = String.valueOf(bidToFind);
                    long timeStart = System.currentTimeMillis();
                    search(bidTable, bidToFindString);
                    long timeEnd = System.currentTimeMillis();
                    long timeElapsed = timeEnd - timeStart;
                    System.out.println("Time elapsed: " + timeElapsed + " ms\n");
                } catch (InputMismatchException e) {
                    System.out.println("Incorrect input format. 5 digit integer needed.\n");
                }
                main(args);
                break;
            }
            case 4: {
                System.out.println("Please enter a valid 5-digit Auction ID:");
                try {
                    int bidToFind = scanner.nextInt();
                    String bidToFindString = String.valueOf(bidToFind);
                    long timeStart = System.currentTimeMillis();
                    delete(bidTable, bidToFindString);
                    long timeEnd = System.currentTimeMillis();
                    long timeElapsed = timeEnd - timeStart;
                    System.out.println("Time elapsed: " + timeElapsed + " ms\n");
                } catch (InputMismatchException e) {
                    System.out.println("Incorrect input format. 5 digit integer needed.\n");
                }
                main(args);
                break;
            }
            case 5: {
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            }
            default:
                System.out.println("Please enter an integer\n");
                main(args);
                break;
        }
    }
}

