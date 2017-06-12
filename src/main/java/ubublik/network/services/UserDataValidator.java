package ubublik.network.services;


public interface UserDataValidator {

    enum DataType {NICKNAME};

    boolean validate(DataType dataType, Object object);
}
