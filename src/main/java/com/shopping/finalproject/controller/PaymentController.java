package com.shopping.finalproject.controller;

import com.shopping.finalproject.Hash.AESHash;
import com.shopping.finalproject.dto.PaymentDTO;
import com.shopping.finalproject.global.GlobalData;
import com.shopping.finalproject.model.Products;
import com.shopping.finalproject.model.User;
import org.apache.tomcat.jni.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PaymentController {


    private static String currentUserName;
    static RestTemplate restTemplate = new RestTemplate();
    static String baseUrl = "http://localhost:8080/wallet";
    private boolean paymentDone=false;


    public String checkWallet(String cardNumber,String name,String cvv,Double price, IvParameterSpec iv, SecretKey key){


        HashMap<String, Object> request = new HashMap<>();
        request.put("cardHolderName", name);
        request.put("price", price);
        request.put("cardNumber", cardNumber);
        request.put("cvvNumber",cvv);
        restTemplate.put(baseUrl, request);
        paymentDone = true;
        return "BE";
    }

    @RequestMapping("/pay-wallet")
    public String paymentWithWallet(@ModelAttribute("paymentInfo")PaymentDTO paymentDTO) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        AESHash aesHash=new AESHash();
        String cardNumber= aesHash.encrypt(paymentDTO.getCardNumber());
        String cvvNumber=aesHash.encrypt(paymentDTO.getCvvNumber());
        String nameHolder=aesHash.encrypt(paymentDTO.getCardHolderName());
        IvParameterSpec iv=aesHash.getIv();
        SecretKey key=aesHash.getKey();
        System.out.println(iv);
        System.out.println(currentUserName);

        Double totalPrice=GlobalData.cart.stream().mapToDouble(Products::getPrice).sum();

       if(checkWallet(cardNumber,nameHolder,cvvNumber,totalPrice, iv, key)=="BE") {
           GlobalData.cart.clear();
           return "paymentSuccess";
       }
       return "404";
    }


}
