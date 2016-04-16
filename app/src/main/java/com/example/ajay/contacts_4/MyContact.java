package com.example.ajay.contacts_4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajay on 28/9/15.
 */
public class MyContact {

    public String name;
    public String number;
    public static List<String> mNumbersList = new ArrayList<>();
    public String Id;

    public MyContact( String Id,String number){
//        this.name = name;
        this.Id = Id;
        this.number = number;
        mNumbersList.add(number);
    }
}