package com.example.curatorsttit.common;
public class Validator {
    void validateFields(){

    }

    public static boolean validateEmail(String email){
        //final String pattern = Patterns.EMAIL_ADDRESS.pattern();

        //Перед тем, как отправить данные на сервер для получения токена, необходимо проверить
        //заполненность полей, а также наличие символа @ в поле для Email.
        if(email.isEmpty())
            return false;
        else if(!email.matches("^[A-Za-z.]+?\\@[A-Za-z]+\\.[A-Za-z]{2,3}$")){
            return false;
        }
        return true;
    }
    public static boolean validatePassword(String password){
        if(password.isEmpty())
            return false;
        /*else if(!password.matches("\\[A-z]{8,}")){
            return false;
        }*/
        return true;
    }
}
