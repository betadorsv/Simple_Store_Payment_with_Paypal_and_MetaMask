package com.shopping.finalproject.global;

import com.shopping.finalproject.model.Products;

import java.util.ArrayList;
import java.util.List;

public class GlobalData {
    public static List<Products> cart;
    static {
        cart=new ArrayList<Products>();
    }
}
