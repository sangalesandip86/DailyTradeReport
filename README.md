# DailyTradeReport

**Requires JDK 8 or above installed**

**Setup Instructions**

This program has been developed and tested on windows platform only

1. Clone/Download DailyTradeReport repository
2. Import DailyTradeReport project as maven project into IDE
3. Build maven project e.g mvn clean install
4. Go to /daily-trade-report/src/main/java/com/trading/DailyTradeReportApplication.java and run the main method.

**Test Data**
1. To set custom Test Data , Please modify /daily-trade-report/tradeInstructions.txt
   Entity|Buy/Sell|AgreedFx|Currency|InstructionDate|SettlementDate|Units|Price
	 
2. Run as Java application ->   com.trading.DailyTradeReportApplication
 
**Running Application**
1. Go to /daily-trade-report/src/main/java/com/trading/DailyTradeReportApplication.java
2. Run as Java application ->   DailyTradeReportApplication.java
 
**Assumptions**
1. Trade Instructions sent by client in text file format (pipe delimited fields).
2. I have implemented solution to read TradeInstruction from given tradeInstruction file in text format, We can make it better by implementing Scheduler/Cron job and monitor incoming file on any specified folder Location.
3. Assumed all fields are in required format, extensive validation is not done on input fields, we can implement validation later on all incoming fields.
4. PricePerUnit, Units and agreedFx should be positive number.

**Solution and Coding**
1. Reader :
	TradeInstructionReader interface to read data provided by client, Right now this application only provides text file specific implementation.
2. Report : 
	TradeReport interface provide public API process records of TradeInstrcution and apply business logic.
3. Writer : 
	TradeReportWriter provide public API to generateReport, this application right now only support Console specific ConsoleTradeReportWriter.
	
**Dependencies Used**
1. Junit
2. SLF4J

**Plugins Used**
1. Maven