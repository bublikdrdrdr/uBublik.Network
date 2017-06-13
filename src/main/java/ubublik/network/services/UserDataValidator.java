package ubublik.network.services;


public interface UserDataValidator {

    enum DataType {NICKNAME, NAME, PASSWORD};

    boolean validate(DataType dataType, Object object);
}
