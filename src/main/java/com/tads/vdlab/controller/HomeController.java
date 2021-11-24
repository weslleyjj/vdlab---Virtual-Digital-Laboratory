package com.tads.vdlab.controller;

import com.tads.vdlab.util.ScriptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    public static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Value("${arquivos.vdlab}")
    private String arquivosPath;

    @Value("${comandos.scripts.programmer}")
    private String scriptProgrammer;

    @Value("${comandos.scripts.getPlacas}")
    private String scriptGetPlacas;

    private List<String> placasConectadas = new ArrayList<>();

    public HomeController() throws IOException {
        getPlacasNoSistema();
    }

    @PostMapping(value = "/upload")
    public String uploadArquivoSof(@RequestParam("file") MultipartFile file, Integer placaEscolhida ) throws IOException {
        if (file.isEmpty()) {
            return "index";
        }
        try {
            String fileName = "carregar-"+file.getOriginalFilename();

            Path path = Paths.get(arquivosPath).toAbsolutePath().normalize();
            Files.createDirectories(path);

            Path targetLocation = path.resolve(fileName); // Caminho absoluto para acesso ao arquivo
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String[] comandoProgramar = {scriptProgrammer, placasConectadas.get(placaEscolhida), "p;" + targetLocation};

            ScriptUtil.executaComandoShellArray(comandoProgramar);

            return "redirect:/controlador-fpga/"+ placaEscolhida;

        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @GetMapping("/tutorial")
    public String paginaTutorial(){
        return "tutorial";
    }

    private void getPlacasNoSistema() throws IOException {
        List<String> resultShell = ScriptUtil.executaComandoShellLine("/home/weslley/Documentos/TADS/TCC/testes-tcl/get_fpga_order.sh");
        for (String s : resultShell) {
            if(s.contains("USB")){
                placasConectadas.add(s);
            }
        }
    }

}

