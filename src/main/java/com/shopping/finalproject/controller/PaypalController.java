package com.shopping.finalproject.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.shopping.finalproject.global.GlobalData;
import com.shopping.finalproject.model.BillingDetails;
import com.shopping.finalproject.model.Products;
import com.shopping.finalproject.service.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PaypalController {
    @Autowired
    PaypalService paypalService;

    public static final String SUCCESS_URL="/pay/success";
    public static final String CANCEL_URL="/pay/cancel";

    @PostMapping("/pay")
    public String payment(HttpServletRequest request){
        try {
            Payment payment = paypalService.createPayment(
                    10.0,
                    "USD",
                    "paypal" ,
                    "sale",
                    "nothing",
                    "http://localhost:8080" + CANCEL_URL,
                    "http://localhost:8080" + SUCCESS_URL
            );
            for(Links link:payment.getLinks()){
                if(link.getRel().equals("approval_url")){
                    System.out.println("THANH TOAN OK");
                    return "redirect:"+link.getHref();
                }
            }
        }
        catch (PayPalRESTException e){
            e.printStackTrace();
            System.out.println("THANH TOAN that bai");
        }
        System.out.println("THANH TOAN khong biet");

        return "redirect:/";
    }
    @GetMapping(value = CANCEL_URL)
    public String cancelUrl(){
        return "orderPlaced";
    }
    @GetMapping("/pay/success")
    public String paySuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
        System.out.println(paymentId);
        System.out.println(payerId);

        try{
            Payment payment= paypalService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if(payment.getState().equals("approved")){
                return "paymentSuccess";
            }
        }
        catch (PayPalRESTException e){
            System.out.println(e.getMessage());
        }
        return "redirect:/";
    }

}
