# TransactionProject

Maven Project : Use maven to build the project. Load the dependencies in pom.xml

For Testing : Provide the arguments through input prompt.

Added Logic to calculate Relative Balance of a bank account for a given time period.

RelativeBalanceCalc.java -> Java file which contains core logic. 
                            txnIntitializer function for converting csv input to a list of Transaction objects.
                            We can use functionalities of 3rd party api opencsv to directly convert csv details to Transaction objects. 
                            relativeBalanceCalculator function contains logic to calculate relative balance and number of valid transactions.
                            
Transaction.java -> Class for Transaction objects. 
                    Setter/Getter/Constructor methods for Transaction class.

RelativeBalanceCalcTest.java -> Test cases for RelativeBalanceCalc class. 
                                Junit test cases to verify the calculation logic for relative balance and number of valid transactions. 


