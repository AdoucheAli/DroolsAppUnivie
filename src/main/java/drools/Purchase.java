package drools;

public class Purchase {

    private Client client;
    private Product product;
    private double discount;
    private double shippingCosts;
    private PaymentType paymentType;
    private Destination destination;

    public Purchase(Client client, Product product, PaymentType paymentType, Destination destination) {
        this.client = client;
        this.product = product;
        this.paymentType = paymentType;
        this.destination = destination;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
        return product.getPrice() + shippingCosts - product.getPrice()*discount;
    }
}
