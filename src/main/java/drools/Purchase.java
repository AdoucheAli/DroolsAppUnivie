package drools;

public class Purchase {

    private Client client;
    private double basePrice;
    private double discount;
    private double shippingCosts;
    private PaymentType paymentType;
    private Destination destination;

    public Purchase(Client client, double basePrice, PaymentType paymentType, Destination destination) {
        this.client = client;
        this.basePrice = basePrice;
        this.paymentType = paymentType;
        this.destination = destination;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getShippingCosts() {
        return shippingCosts;
    }

    public void setShippingCosts(double shippingCosts) {
        this.shippingCosts = shippingCosts;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public double toPay() {
        return basePrice + shippingCosts - basePrice*discount;
    }
}
