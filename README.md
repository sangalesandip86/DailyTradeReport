# DailyTradeReport

**Requires JDK 8 or above installed**

**Setup Instructions**
1. Clone/Download DailyTradeReport repository
2. Import as maven project into IDE 
3. Build Project
4. Run as Java application SettlementsReport.java

**Test Data**
1. For Custom test data you can change data in Following method which returns List<TradeInstruction>
 com.sandip.utils.TradeReportUtil.populateTradeInstructions()
2. Run as Java application ->   com.sandip.reports.SettlementsReport.java
 
**Running Application**
1. Go to /daily-trade-report/src/main/java/com/sandip/reports/SettlementsReport.java
2. Run as Java application ->   com.sandip.reports.SettlementsReport.java
 
**Assumptions**
1. Trade Instruction sent by client in format(Text/CSV/JSON) is already converted into java object List<TradeInstruction> format.
2. PricePerUnit, Units and agreedFx should be positive number

**Dependencies Used**
1. Junit

**Plugins Used**
1. Maven