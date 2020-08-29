package com.example.dairyhelp;

public class OrderClass {


    String Order , Id , Uid , Name ;


    public OrderClass(String order, String id, String uid, String name) {
        Order = order;
        Id = id;
        Uid = uid;
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOrder() {
        return Order;
    }

    public void setOrder(String order) {
        Order = order;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
