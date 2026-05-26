# AP_CS_SEM1_INVESTERING — Stock Portfolio System

A console-based stock portfolio management system. Built in Java with no external dependencies, using CSV files for persistence.

Developed as part of the 1st semester cross-disciplinary project at KEA.

## Group

| Name                 | GitHub              |
|----------------------|---------------------|
| [Isak Pais]          | [Ispa070]           |
| [Nicklas Viftrup]    | [Nickichandk]       |
| [Villa Christensen]  | [Villa123]          |
| [Patrick Clemmensen] | [PatrickClemmensen] |


---

## About the Project

A console-based investment platform where users can manage a personal stock portfolio. The system allows users to log in, browse the Nasdaq Copenhagen stock market, buy and sell stocks, and track the performance of their holdings — all with data saved to and restored from CSV files.

## Roles

The system has one role, selected by entering a user ID on startup.

### Member (Investor)
- Log in with a user ID
- Browse all available stocks on the market
- Buy stocks (validated against cash balance)
- Sell stocks (validated against current holdings)
- View portfolio with current value, cost basis, and unrealized gain
- View portfolio value converted to a preferred currency

---

## Features

- **Portfolio tracking** — positions are tracked with quantity and weighted average buy price, updated on every purchase
- **Weighted average cost** — buying more of an existing stock recalculates the average buy price automatically
- **Unrealized gain** — each position shows current profit or loss vs. original cost
- **Currency conversion** — portfolio totals can be displayed in EUR, USD, GBP, and more
- **Input validation** — invalid tickers, negative quantities, and insufficient funds all throw specific exceptions
- **File persistence** — transactions are saved to CSV on every trade

---

## Project Structure

```
project-root/
├── data/
│   ├── stockMarket.csv          — 25 Nasdaq Copenhagen stocks with price, sector, rating
│   ├── bondMarket.csv           — Danish government bonds
│   ├── currency.csv             — exchange rates relative to DKK
│   ├── users.csv                — 10 users with 100,000 DKK starting balance
│   └── transactions.csv         — historical buy/sell orders
├── src/
│   ├── app/
│   │   └── Main.java            — entry point, service instantiation
│   ├── interfaces/
│   │   ├── Tradeable.java       — contract for buyable/sellable assets
│   │   ├── Rankable.java        — contract for sortable holdings
│   │   ├── CurrencyConverter.java
│   │   └── CSVSerializeable.java
│   ├── model/
│   │   ├── asset/
│   │   │   ├── Asset.java       — abstract base for stocks and bonds
│   │   │   └── Stock.java       — sector, price, dividend yield, rating
│   │   ├── portfolio/
│   │   │   ├── Portfolio.java   — collection of positions
│   │   │   ├── Position.java    — quantity, average buy price, unrealized gain
│   │   │   └── User.java        — cash balance, personal details, portfolio
│   │   └── transaction/
│   │       ├── Transaction.java — records a buy/sell event
│   │       └── OrderType.java   — BUY / SELL enum
│   ├── service/
│   │   ├── StockMarketService.java   — loads and queries market data
│   │   ├── UserService.java          — loads and looks up users
│   │   ├── PortfolioService.java     — buy/sell business logic
│   │   └── CurrencyService.java      — currency conversion from CSV rates
│   ├── ui/
│   │   ├── MainMenu.java        — login and role selection
│   │   └── MemberMenu.java      — portfolio, market, buy, sell, currency view
│   └── util/
│       ├── comparator/
│       │   ├── ByPercentReturn.java  — sort positions by return (Comparator)
│       │   └── ByTickerName.java     — sort positions alphabetically (Comparator)
│       ├── constants/
│       │   └── AppConstants.java     — file paths, delimiters, base currency
│       ├── csv/
│       │   ├── CSVReader.java        — generic semicolon-delimited file reader
│       │   └── CSVWriter.java        — generic CSV file writer and appender
│       ├── exception/
│       │   ├── InsufficientFundsException.java
│       │   ├── InsufficientQuantityException.java
│       │   ├── InvalidAssetException.java
│       │   └── UnsupportedCurrencyException.java
│       └── validation/
│           ├── CashBalanceValidator.java
│           ├── CurrencyCodeValidator.java
│           ├── QuantityValidator.java
│           └── TickerValidator.java
└── test/
    ├── resources/
    │   ├── test_users.csv
    │   ├── test_stocks.csv
    │   └── test_currency.csv
    ├── model/
    │   ├── asset/
    │   │   └── StockTest.java
    │   ├── portfolio/
    │   │   ├── PortfolioTest.java
    │   │   └── PositionTest.java
    │   └── transaction/
    │       ├── TransactionTest.java
    │       └── OrderTypeTest.java
    ├── service/
    │   ├── StockMarketServiceTest.java
    │   ├── UserServiceTest.java
    │   └── PortfolioServiceTest.java
    └── util/
        └── validation/
            ├── CashBalanceValidatorTest.java
            ├── QuantityValidatorTest.java
            └── TickerValidatorTest.java
```

---

## Development Process

Built using **Scrum** with two one-week sprints.

| | Dates | Sprint Goal |
|---|---|---|
| Sprint 1 | TBD | A user can log in, browse the market, buy and sell stocks, and view their portfolio — with all data saved to file |
| Sprint 2 | TBD | Portfolio sorting, currency conversion, bond support, and accountant-style reporting |

---

## Technical Requirements

- Java (OOP — encapsulation, inheritance, abstraction, polymorphism)
- Console-based, single-user
- File persistence — data saved on every trade, reloaded on startup
- Packages: `model` / `service` / `ui` / `util`
- Sorting via `Comparable` and `Comparator`
- Input validation with custom exception handling
- Enums for order types
- Interfaces for `Tradeable`, `Rankable`, `CurrencyConverter`, `CSVSerializeable`

---

## Submission

**Deadline:** TBD
**Presentation:** TBD