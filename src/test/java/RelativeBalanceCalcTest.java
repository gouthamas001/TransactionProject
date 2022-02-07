import org.junit.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RelativeBalanceCalcTest {

    @Test
    public void functionalityTest() {
        //Mock Data
        String path = System.getProperty("user.dir");
        File input = new File(path,"sample.csv");
        String from = "20/10/2018 12:00:00";
        String to = "20/10/2018 19:00:00";
        String accId = "ACC334455";
        LocalDateTime fromTime = RelativeBalanceCalc.timeSplitter(from);
        LocalDateTime toTime = RelativeBalanceCalc.timeSplitter(to);


        List<List<String>> data = RelativeBalanceCalc.readTxnsfromCSV(input);
        List<Transaction> txnList = RelativeBalanceCalc.txnIntitializer(data);
        Map<String, Object> respMap = RelativeBalanceCalc.relativeBalanceCalculator(accId, fromTime, toTime, txnList);

        assertEquals(respMap.get("relativeBal").toString(), "-25.0");
        assertEquals(respMap.get("totalValidTxns"), 1);


    }

}
