# DailyTradeReport

**Requires JDK 8 or above installed**

**Setup Instructions**
1. Clone/Download DailyTradeReport repository
2. Import as maven project into IDE 
3. Build Project
4. Run as Java application /daily-trade-report/src/main/java/com/sandip/DailyTradeReportApplication.java

**Test Data**
1. To set custom Test Data , Please modify /daily-trade-report/tradeInstructions.txt
   Entity|Buy/Sell|AgreedFx|Currency|InstructionDate|SettlementDate|Units|Price
	 
2. Run as Java application ->   com.sandip.DailyTradeReportApplication
 
**Running Application**
1. Go to /daily-trade-report/src/main/java/com/sandip/DailyTradeReportApplication.java
2. Run as Java application ->   DailyTradeReportApplication.java
 
**Assumptions**
1. Trade Instructions sent by client in text file format (pipe delimited fields).
2. PricePerUnit, Units and agreedFx should be positive number
3. Assumed all fields are in required format, validation is not done, we can implement validation later on all incoming fields.

**Dependencies Used**
1. Junit

**Plugins Used**
1. Maven