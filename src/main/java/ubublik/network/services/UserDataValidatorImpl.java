package ubublik.network.services;

import org.springframework.stereotype.Service;

@Service
public class UserDataValidatorImpl implements UserDataValidator {


    @Override
    public boolean validate(DataType dataType, Object object) {
        switch (dataType){
            case NICKNAME: return checkNickname(object);
            default: return false;
        }
    }


    // TODO: 12-Jun-17 add validation methods
    private boolean checkNickname(Object object){
        return false;
    }
}
