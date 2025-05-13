import java.util.Scanner;

// Factory Method Pattern
interface FriesFactory {
    Fries createFries();
}

class SmallFriesFactory implements FriesFactory {
    @Override
    public Fries createFries() {
        return new SmallFries();
    }
}

class MediumFriesFactory implements FriesFactory {
    @Override
    public Fries createFries() {
        return new MediumFries();
    }
}

class LargeFriesFactory implements FriesFactory {
    @Override
    public Fries createFries() {
        return new LargeFries();
    }
}

// Fries Interface with Decorator 
interface Fries {
    void prepareFries();
    void setSize(int packet);
    double price();
    String getSauces();
}

// Abstract Base Fries Class
abstract class BaseFries implements Fries {
    protected int packet;
    protected String sauces = "";

    @Override
    public void setSize(int packet) {
        this.packet = packet;
        System.out.println("Size set to: " + packet);
    }

    @Override
    public String getSauces() {
        return sauces.isEmpty() ? "" : sauces;  
    }
}

// Concrete Fries Classes
class SmallFries extends BaseFries {
    @Override
    public void prepareFries() {
        System.out.println("Preparing Small Fries...");
    }

    @Override
    public double price() {
        return 3.0;
    }
}

class MediumFries extends BaseFries {
    @Override
    public void prepareFries() {
        System.out.println("Preparing Medium Fries...");
    }

    @Override
    public double price() {
        return 4.0;
    }
}

class LargeFries extends BaseFries {
    @Override
    public void prepareFries() {
        System.out.println("Preparing Large Fries...");
    }

    @Override
    public double price() {
        return 5.0;
    }
}

// Decorator Pattern for Sauces
abstract class FriesDecorator implements Fries {
    protected Fries wrappedFries;

    public FriesDecorator(Fries fries) {
        this.wrappedFries = fries;
    }

    @Override
    public void prepareFries() {
        wrappedFries.prepareFries();
    }

    @Override
    public void setSize(int packet) {
        wrappedFries.setSize(packet);
    }

    @Override
    public double price() {
        return wrappedFries.price();
    }

    @Override
    public String getSauces() {
        return wrappedFries.getSauces();
    }
}

class KetchupSauceDecorator extends FriesDecorator {
    public KetchupSauceDecorator(Fries fries) {
        super(fries);
    }

    @Override
    public double price() {
        return wrappedFries.price() + 0.50;
    }

    @Override
    public String getSauces() {
        return wrappedFries.getSauces() + (wrappedFries.getSauces().isEmpty() ? "" : ", ") + "Ketchup";
    }
}

class ChiliSauceDecorator extends FriesDecorator {
    public ChiliSauceDecorator(Fries fries) {
        super(fries);
    }

    @Override
    public double price() {
        return wrappedFries.price() + 0.70;
    }

    @Override
    public String getSauces() {
        return wrappedFries.getSauces() + (wrappedFries.getSauces().isEmpty() ? "" : ", ") + "Chili";
    }
}

// Strategy Pattern for Payment
interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardPaymentStrategy implements PaymentStrategy {
    private String cardNumber;

    public CreditCardPaymentStrategy(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paying RM" + String.format("%.2f", amount) + " using Credit Card: " + cardNumber);
    }
}

class OnlineBankingPaymentStrategy implements PaymentStrategy {
    private String bankAccount;

    public OnlineBankingPaymentStrategy(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paying RM" + String.format("%.2f", amount) + " using Online Banking: " + bankAccount);
    }
}

class Payment {
    private PaymentStrategy paymentStrategy;

    public Payment(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void processPayment(double amount) {
        paymentStrategy.pay(amount);
    }
}

// Client Class with Interactive Menu
public class RefinedSolution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

      
        System.out.println("Welcome to French King Fries!");
        System.out.println("Select Fries Size:");
        System.out.println("1. Small");
        System.out.println("2. Medium");
        System.out.println("3. Large");
        
        FriesFactory friesFactory = null;
        int sizeChoice = scanner.nextInt();

        switch (sizeChoice) {
            case 1:
                friesFactory = new SmallFriesFactory();
                break;
            case 2:
                friesFactory = new MediumFriesFactory();
                break;
            case 3:
                friesFactory = new LargeFriesFactory();
                break;
            default:
                System.out.println("Invalid size. Defaulting to Small.");
                friesFactory = new SmallFriesFactory();
        }

        // Create Fries using Factory Method
        Fries selectedFries = friesFactory.createFries();
        selectedFries.prepareFries();
        selectedFries.setSize(1);
        
      
        System.out.println("\nSelect Sauce Options:");
        System.out.println("1. No Sauce");
        System.out.println("2. Ketchup");
        System.out.println("3. Chili");
        System.out.println("4. Both Sauces");
        
        int sauceChoice = scanner.nextInt();

        Fries finalFries = selectedFries;

        switch (sauceChoice) {
            case 2:
                finalFries = new KetchupSauceDecorator(selectedFries); // Decorator handles adding sauce
                break;
            case 3:
                finalFries = new ChiliSauceDecorator(selectedFries); // Decorator handles adding sauce
                break;
            case 4:
                finalFries = new ChiliSauceDecorator(
                    new KetchupSauceDecorator(selectedFries)); // Apply both decorators
                break;
        }

        // Display Sauces and Price
        System.out.println("\nSauce(s): " + finalFries.getSauces());
        double totalPrice = finalFries.price();
        System.out.println("Total Price: RM" + String.format("%.2f", totalPrice));

     
        System.out.println("\nSelect Payment Method:");
        System.out.println("1. Credit Card");
        System.out.println("2. Online Banking");
        
        int paymentChoice = scanner.nextInt();

        //Strategy Pattern
        PaymentStrategy paymentStrategy;

        switch (paymentChoice) {
            case 1:
                System.out.println("Enter Credit Card Number:");
                String cardNumber = scanner.nextLine();
                paymentStrategy = new CreditCardPaymentStrategy(cardNumber);
                break;
            case 2:
                System.out.println("Enter Bank Account Number:");
                String bankAccount = scanner.nextLine();
                paymentStrategy = new OnlineBankingPaymentStrategy(bankAccount);
                break;
            default:
                System.out.println("Invalid payment method. Using Credit Card as default.");
                paymentStrategy = new CreditCardPaymentStrategy("0000-0000-0000-0000");
        }

        // Process Payment
        Payment payment = new Payment(paymentStrategy);
        payment.processPayment(totalPrice);

        scanner.close();
    }
}