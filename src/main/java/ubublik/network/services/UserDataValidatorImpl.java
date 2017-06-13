package ubublik.network.services;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserDataValidatorImpl implements UserDataValidator {

    private static final String NICKNAME_PATTERN = "^([A-Za-z][0-9a-zA-Z\\._]{4,50})$";
    private static final String NAME_PATTERN = "^([A-ZА-ЯІЇЄҐĄĆĘŁŃÓŚŹŻ]{1}[a-zа-яіїґєąćęłńóśźż]{1,60})$";
    private static final String PASSWORD_PATTERN = "(?=.*?[A-Za-z]).{8,}$";


    @Override
    public boolean validate(DataType dataType, Object object) {
        if (object==null) return false;
        if (!(object instanceof String)) return false;
        String sObject = (String)object;
        switch (dataType){
            case NICKNAME: return checkNickname(sObject);
            case NAME: return checkName(sObject);
            case PASSWORD: return checkPassword(sObject);
            default: return false;
        }
    }


    private boolean checkNickname(String s){
        boolean res = Pattern.compile(NICKNAME_PATTERN).matcher(s).matches();
        return res;
    }

    private boolean checkName(String s){
        return Pattern.compile(NAME_PATTERN).matcher(s).matches();
    }

    private boolean checkPassword(String s){
        return Pattern.compile(PASSWORD_PATTERN).matcher(s).matches();
    }


}
