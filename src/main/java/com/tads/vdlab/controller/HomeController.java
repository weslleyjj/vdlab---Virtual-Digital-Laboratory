package com.tads.vdlab.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;

@Controller
public class HomeController {

    @Value("${arquivos.vdlab}")
    private String arquivosPath;

    @PostMapping(value = "/upload")
    public String uploadArquivoSof(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "index";
        }
        try {
            String fileName = "carregar-"+file.getOriginalFilename();

            Path path = Paths.get(arquivosPath).toAbsolutePath().normalize();
            Files.createDirectories(path);

            Path targetLocation = path.resolve(fileName); // Caminho absoluto para acesso ao arquivo
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String comando = "sh /home/weslley/Proograms/altera/quartus/bin/quartus_pgm -c usb-blaster -m JTAG -o " +
                    "p;" +
                    targetLocation.toString();

            executaComandoShell(comando);

            return "redirect:/controlador-fpga";

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void executaComandoShell(String comando) throws IOException {
        System.out.println(comando);
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(comando);

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        // Read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

        // Read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }

    }
}

