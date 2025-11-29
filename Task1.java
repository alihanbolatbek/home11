import java.util.*;
import java.time.LocalDateTime;

// =================== КЛАССЫ ===================

class User {
    protected int ID;
    protected String name;
    protected String email;
    protected String address;
    protected String phone;
    protected String role;

    public User(int user_id, String name, String email, String address, String phone, String role) {
        this.ID = user_id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.role = role;
    }

    public void register() {
        System.out.println(name + " зарегистрирован(а).");
    }

    public void login() {
        System.out.println(name + " вошёл(ла) в систему.");
    }

    public void updateData(Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            try {
                var field = this.getClass().getSuperclass().getDeclaredField(key);
                field.setAccessible(true);
                field.set(this, value);
            } catch (Exception e) {
                try {
                    var field = this.getClass().getDeclaredField(key);
                    field.setAccessible(true);
                    field.set(this, value);
                } catch (Exception ignored) {}
            }
        });
        System.out.println("Данные пользователя " + name + " обновлены.");
    }
}


class Client extends User {
    private int loyaltyPoints;
    private List<Order> orders = new ArrayList<>();

    public Client(int user_id, String name, String email, String address, String phone) {
        super(user_id, name, email, address, phone, "Client");
        this.loyaltyPoints = 0;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }
}


class Admin extends User {
    public Admin(int user_id, String name, String email, String address, String phone) {
        super(user_id, name, email, address, phone, "Admin");
    }

    public void logAction(String action) {
        System.out.println("Admin " + name + " действие: " + action);
    }
}


class Category {
    int ID;
    String name;
    String description;

    public Category(int id, String name, String description) {
        this.ID = id;
        this.name = name;
        this.description = description;
    }
}


class Product {
    int ID;
    String name;
    String description;
    double price;
    int stock;
    Category category;

    public Product(int id, String name, String description, double price, int stock, Category category) {
        this.ID = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public void create() {
        System.out.println("Товар " + name + " создан.");
    }

    public void update(Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            try {
                var field = this.getClass().getDeclaredField(key);
                field.setAccessible(true);
                field.set(this, value);
            } catch (Exception ignored) {}
        });
        System.out.println("Товар " + name + " обновлён.");
    }

    public void delete() {
        System.out.println("Товар " + name + " удалён.");
    }
}


class PromoCode {
    String code;
    int discountPercent;

    public PromoCode(String code, int discount) {
        this.code = code;
        this.discountPercent = discount;
    }

    public boolean isValid() {
        return true;
    }
}


abstract class PaymentMethod {
    int ID;
    double amount;
    LocalDateTime date;

    public PaymentMethod(int id, double amount, LocalDateTime date) {
        this.ID = id;
        this.amount = amount;
        this.date = date;
    }

    abstract void process();
}


class CardPayment extends PaymentMethod {
    String cardNumber;

    public CardPayment(int id, double amount, LocalDateTime date, String cardNumber) {
        super(id, amount, date);
        this.cardNumber = cardNumber;
    }

    public void process() {
        System.out.println("Обработка платежа по карте на сумму " + amount);
    }
}


class WalletPayment extends PaymentMethod {
    String walletId;

    public WalletPayment(int id, double amount, LocalDateTime date, String walletId) {
        super(id, amount, date);
        this.walletId = walletId;
    }

    public void process() {
        System.out.println("Обработка платежа через кошелёк на сумму " + amount);
    }
}


class Payment {
    int ID;
    String type;
    double amount;
    String status;
    LocalDateTime date;

    public Payment(int id, String type, double amount, String status, LocalDateTime date) {
        this.ID = id;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.date = date;
    }

    public void process() {
        System.out.println("Обработка платежа " + ID);
    }

    public void refund() {
        System.out.println("Возврат платежа " + ID);
    }
}


class Delivery {
    int ID;
    String address;
    String status;
    String courier;

    public Delivery(int id, String address, String courier) {
        this.ID = id;
        this.address = address;
        this.courier = courier;
        this.status = "ожидает";
    }

    public void send() {
        status = "отправлен";
        System.out.println("Доставка " + ID + " отправлена");
    }

    public void track() {
        System.out.println("Статус доставки " + ID + ": " + status);
    }

    public void complete() {
        status = "завершена";
        System.out.println("Доставка " + ID + " завершена");
    }
}


class Order {
    int ID;
    Client client;
    List<Product> products;
    PromoCode promo;
    String status;
    double total;
    Payment payment;
    Delivery delivery;

    public Order(int id, Client client, List<Product> products, PromoCode promo) {
        this.ID = id;
        this.client = client;
        this.products = products;
        this.promo = promo;

        this.total = products.stream().mapToDouble(p -> p.price).sum();
        if (promo != null) total *= (1 - promo.discountPercent / 100.0);

        this.status = "создан";
    }

    public void placeOrder() {
        status = "оформлен";
        System.out.println("Заказ " + ID + " оформлен. Сумма: " + total);
    }

    public void cancel() {
        status = "отменён";
        System.out.println("Заказ " + ID + " отменён");
    }

    public void pay(Payment payment) {
        this.payment = payment;
        this.status = "оплачен";
        System.out.println("Заказ " + ID + " оплачен. Сумма: " + payment.amount);
    }
}


class Review {
    int ID;
    Client client;
    Product product;
    int rating;
    String comment;

    public Review(int id, Client client, Product product, int rating, String comment) {
        this.ID = id;
        this.client = client;
        this.product = product;
        this.rating = rating;
        this.comment = comment;
    }
}


// =================== MAIN ===================
public class Task1 {
    public static void main(String[] args) {

        Client client = new Client(1, "Ereke", "ereke@mail.com", "Алматы", "+77712345678");
        Admin admin = new Admin(100, "Admin1", "admin@mail.com", "Алматы", "+77700011122");

        client.register();
        admin.logAction("Система запущена");

        Category category1 = new Category(10, "Электроника", "Смартфоны и аксессуары");
        Product product1 = new Product(101, "Смартфон X", "Новый смартфон", 120000, 10, category1);
        Product product2 = new Product(102, "Наушники Y", "Беспроводные наушники", 20000, 15, category1);

        product1.create();
        product2.create();

        PromoCode promo = new PromoCode("SALE10", 10);

        Order order = new Order(5001, client, Arrays.asList(product1, product2), promo);
        client.addOrder(order);
        order.placeOrder();

        Payment payment = new Payment(3001, "Card", order.total, "pending", LocalDateTime.now());
        order.pay(payment);

        Delivery delivery = new Delivery(9001, client.address, "Курьер 1");
        order.delivery = delivery;
        delivery.send();
        delivery.track();
        delivery.complete();

        Review review = new Review(7001, client, product1, 5, "Отличный смартфон!");
        System.out.println("Отзыв от " + review.client.name + " для " + review.product.name + ": " +
                review.rating + " звезд - " + review.comment);

        System.out.println("\n=== СТАТУСЫ ===");
        System.out.println("Клиент: " + client.name + ", Лояльные очки: " + client.getLoyaltyPoints());
        System.out.println("Заказ " + order.ID + ": Статус - " + order.status + ", Сумма - " + order.total);
        System.out.println("Платеж " + payment.ID + ": Статус - " + payment.status + ", Сумма - " + payment.amount);
        System.out.println("Доставка " + delivery.ID + ": Статус - " + delivery.status);
    }
}
