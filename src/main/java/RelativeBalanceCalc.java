import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class RelativeBalanceCalc {
    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        File input = new File(path,"sample.csv");
        List<List<String>> data = readTxnsfromCSV(input);
        List<Transaction> txnList = txnIntitializer(data);

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter account id : ");
        String accId = "ACC334455";
        //String accId = scan.nextLine();
        System.out.println("Enter from date : ");
        String from = "20/10/2018 12:00:00";
        //String from = scan.nextLine();
        LocalDateTime fromTime = timeSplitter(from);
        System.out.println("Enter to date : ");
        //String to = scan.nextLine();
        String to = "21/10/2018 19:00:00";
        LocalDateTime toTime = timeSplitter(to);

        Map<String, Object> resp = relativeBalanceCalculator(accId, fromTime, toTime, txnList);
        System.out.println("Relative balance for the period is: "+ resp.get("txnSign") +"$" +
                Math.abs((Double) resp.get("relativeBal")));
        System.out.println("Number of transactions included is: "+ resp.get("totalValidTxns"));

    }

    //CSV Reader function
    public static List<List<String>> readTxnsfromCSV(File txn_file) {
        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(txn_file)) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
                //System.out.println(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return records;
    }

    private static List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    //Mapper function to map csv values to transaction objects
    public static List<Transaction> txnIntitializer(List<List<String>> data) {
        List<Transaction> txnList = new ArrayList<>();
        data.remove(0);
        data.forEach(ele -> {
            ele.replaceAll(t -> Objects.isNull(t) ? "NA-" : t);
            Transaction txn = new Transaction();
            txn.setTransactionId(ele.get(0));
            txn.setFromAccountId(ele.get(1));
            txn.setToAccountId(ele.get(2));
            txn.setCreatedAt(timeSplitter(ele.get(3)));
            txn.setAmount(Float.valueOf(ele.get(4)));
            txn.setTransactionType(ele.get(5));
            if (ele.size() > 6) {
                txn.setRelatedTransaction(ele.get(6));
            }
            txnList.add(txn);
        });
        return txnList;
    }

    public static Map<String, Object> relativeBalanceCalculator(String accId, LocalDateTime fromTime, LocalDateTime toTime,
                                                                List<Transaction> txnList){
        double negAmount;
        double posAmount;
        String ind = "";
        List<Transaction> validNegTxns = new ArrayList<>();
        List<Transaction> validPosTxns = new ArrayList<>();

        //List of valid Negative Transactions
        List<Transaction> negTxns = txnList.stream().filter(txn -> txn.getFromAccountId().contains(accId) &&
                txn.getCreatedAt().isBefore(toTime) && txn.getCreatedAt().isAfter(fromTime) &&
                txn.getTransactionType().contains("PAYMENT"))
                .collect(Collectors.toList());

        //List of valid Negative Transaction IDs

        //List of valid Positive Transactions
        List<Transaction> posTxns = txnList.stream().filter(txn -> txn.getToAccountId().contains(accId) &&
                txn.getCreatedAt().isBefore(toTime) && txn.getCreatedAt().isAfter(fromTime) &&
                txn.getTransactionType().contains("PAYMENT"))
                .collect(Collectors.toList());

        //List of valid Positive Transaction IDs
        //List<String> posTxnIds = posTxns.stream().map(Transaction::getTransactionId)
       //           .collect(Collectors.toList());


        //Predicate<Transaction> isTransactionReversal = e -> e.getTransactionType() == "REVERSAL";
        List<String> reversalTransactionIds = txnList.stream().filter(ele -> ele.getTransactionType().contains("REVERSAL"))
                .map(Transaction::getRelatedTransaction).collect(Collectors.toList());


        negTxns.forEach(txn -> {
            if (!reversalTransactionIds.contains(txn.getTransactionId())) {
                validNegTxns.add(txn);
            }
        });

        posTxns.forEach(txn -> {
            if (!reversalTransactionIds.contains(txn.getTransactionId())) {
                validPosTxns.add(txn);
            }
        });



        negAmount = validNegTxns.stream().mapToDouble(Transaction::getAmount).sum();
        posAmount = validPosTxns.stream().mapToDouble(Transaction::getAmount).sum();
        double relativeBal = posAmount - negAmount;
        if (relativeBal < 0) {
            ind = "-";
        } else if (relativeBal > 0) {
            ind = "+";
        }

        Map<String, Object> respMap = new HashMap<>();
        respMap.put("txnSign", ind);
        respMap.put("totalValidTxns", validNegTxns.size()+validPosTxns.size());
        respMap.put("relativeBal", relativeBal);
        return respMap;

    }

    public static LocalDateTime timeSplitter(String time) {
        String[] dateTimeSegments = time.trim().split(" ");
        String[] dateSegments = dateTimeSegments[0].split("/");
        String[] timeSegments = dateTimeSegments[1].split(":");
        int year = Integer.parseInt(dateSegments[2]);
        int month = Integer.parseInt(dateSegments[1]);
        int day = Integer.parseInt(dateSegments[0]);
        int hr = Integer.parseInt(timeSegments[0]);
        int min = Integer.parseInt(timeSegments[1]);
        int sec = Integer.parseInt(timeSegments[2]);
        return LocalDateTime.of(year, month, day, hr, min, sec);

    }
}
