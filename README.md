# checkout-component

### Overview
This project implements a market checkout system that calculates the total price of a list of items in a shopping cart, applying any multi-price promotions where applicable.

The system is designed to:
- Scan individual items
- Identify and apply promotional discounts for eligible items if specific conditions are met (e.g., buy N items for a special price Y).
- Handle both promotional and non-promotional items in the checkout process.
- Provide flexibility for easily configuring new promotions.

The calculation ensures the correct application of discounts, whether the cart items fully qualify for the promotion or not, leaving unmatched items at regular prices.

### Design Overview

Key Components:

`Item` \
Represents an individual product with the following fields:
- id: Unique identifier for the product.
- name: A descriptive name for the product.
- price: The normal price (per unit) of the product.

`CartItem` \
Represents an item added to the shopping cart, with:
- item: An Item object.
- quantity: Number of units of the Item present in the cart.

`Promotion` \
Defines the contract for promotion implementations:
- Method: apply(List<CartItem> cartItems) \
Applies the promotion to the specified list of CartItem objects, returning the total discount that applies for the promotion.

`MultiPricePromotion` (Implementation of Promotion)\
Applies multi-price promotional rules (e.g., buy N items for a special price Y).

Fields:
- itemId: The id of the item to which the promotion applies.
- requiredQuantity: The number of items required to qualify for the promotional price.
- specialPrice: The total promotional price for the specified quantity.

Logic:
- Calculates the number of qualified promotional sets.
- Calculates the appropriate discount only for qualified sets.
- Ensures unmatched items are handled separately and charged at their standard price.

`Checkout` Combines items in a shopping cart and applies all active promotions to calculate:
- Total price after applying discounts.
- Total discount applied.

Responsibilities:
- Handles multiple promotions simultaneously.
- Determines the final price by considering both promotional sets and remaining non-promotional items.

### Dependencies
- Java 21: Project uses modern Java features and follows Java SDK version 21.
- JUnit 5: For writing and executing unit test cases.

### Building and Running the Project

###### Prerequisites

Ensure you have the following installed:
- Java Development Kit (JDK) 21 or higher.
- Maven build tool (if not installed, download it from Maven Download).

###### Steps to Build and Run

1. Clone the repository:\
`git clone <repository_url>`\
`cd <project_directory>`
2. Build the project: Use Maven to compile the code and run tests.
`mvn clean install`
3. Run the application: You can run it using any IDE (e.g., IntelliJ IDEA, Eclipse) or via the command line:\
`java -cp target/<generated-jar-file>.jar <main-class-path>`
4. Run Tests:\
`mvn test`