package com.tads.vdlab.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

@Controller
public class FeedbackController {

    @Value("${feedback.video1}")
    private String video1Url;

    @Value("${feedback.video2}")
    private String video2Url;

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

    @GetMapping("/controlador-fpga/{camera}")
    public ModelAndView feedback(@PathVariable("camera") Integer camera) throws InterruptedException, IOException {

        //startStream();

        ModelAndView model = new ModelAndView("feedback");
        if(camera == 1){
            model.addObject("urlStream", video2Url);
        } else if (camera == 2) {
            model.addObject("urlStream", video1Url);
        }
        model.addObject("placa", camera);

        //TimeUnit.SECONDS.sleep(3);

        socket = new Socket(host, port);
        socketOut = new DataOutputStream(socket.getOutputStream());

        return model;
    }

    @MessageMapping("/comando")
    @SendTo("/painel/comando")
    public void fpgaCommand(DadosSocket dados) throws Exception {
        Integer comando = dados.getComandoInputs();
        //Conversão de número inteiro para binário
        String conversao = Integer.toBinaryString(comando);
        conversao = String.format("%22s", conversao).replace(' ', '0');
        conversao += dados.getPlacaDesejada().toString();
        socketOut.write(conversao.getBytes());
        socketOut.writeUTF("\n");

        System.out.println(conversao);
    }



    @GetMapping("/fim-feedback")
    public ModelAndView fimFeedback(){

//        Thread newThread = new Thread(this::stopStream);

//        newThread.start();

        ModelAndView modelAndView = new ModelAndView("carregar");
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DadosSocket{
        Integer comandoInputs;
        Integer placaDesejada;
    }

}
