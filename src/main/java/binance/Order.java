package binance;

import java.util.Objects;

public class Order {
    private String advNo;
    private String price;
    private String nickName;
    private String total;

    public Order(String advNo, String price, String nickName) {
        this.advNo = advNo;
        this.price = price;
        this.nickName = nickName;
    }

    public Order() {
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAdvNo() {
        return advNo;
    }

    public void setAdvNo(String advNo) {
        this.advNo = advNo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String userNo) {
        this.nickName = userNo;
    }

    @Override
    public String toString() {
        return "binance.Order{" + "advNo='" + advNo + '\'' + ", price=" + price + ", nickName='" + nickName + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(price, order.price) && Objects.equals(advNo, order.advNo) && Objects.equals(nickName, order.nickName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(advNo, price, nickName);
    }
}
