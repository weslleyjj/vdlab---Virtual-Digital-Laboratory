package com.tads.vdlab.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

@Controller
public class FeedbackController {

    @Value("${feedback.video}")
    private String videoUrl;

    @Value("${feedback.iniciarVideo}")
    private String startStream;

    @Value("${feedback.pararVideo}")
    private String stopStream;

    @Value("${fpga.host}")
    private String host;

    @Value("${fpga.port}")
    private Integer port;

    private Socket socket;
    private DataOutputStream socketOut;

    @GetMapping("/controlador-fpga")
    public ModelAndView feedback() throws InterruptedException, IOException {

        //startStream();

        ModelAndView model = new ModelAndView("feedback");
        model.addObject("urlStream", videoUrl);

        //TimeUnit.SECONDS.sleep(3);

        socket = new Socket(host, port);
        socketOut = new DataOutputStream(socket.getOutputStream());

        return model;
    }

    @MessageMapping("/comando")
    @SendTo("/painel/comando")
    public void fpgaCommand(Integer comando) throws Exception {

        //Conversão de número inteiro para binário
        String conversao = Integer.toBinaryString(comando);
        conversao = String.format("%22s", conversao).replace(' ', '0');

        socketOut.write(conversao.getBytes());
        socketOut.writeUTF("\n");

        System.out.println(conversao);
    }



    @GetMapping("/fim-feedback")
    public ModelAndView fimFeedback(){

//        Thread newThread = new Thread(this::stopStream);

//        newThread.start();

        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    private void stopStream() {
        final String uri = stopStream;
        try {
            RestTemplate restTemplate = new RestTemplate();
            Object result = restTemplate.getForObject(uri, Object.class);
        }catch (Exception e){
            System.out.println("Streaming encerrado");
        }
    }

    private void startStream() {
        final String uri = startStream;
        try {
            RestTemplate restTemplate = new RestTemplate();
            Object result = restTemplate.getForObject(uri, Object.class);
        }catch (Exception e){
            System.out.println("Streaming iniciado");
        }
    }

}
