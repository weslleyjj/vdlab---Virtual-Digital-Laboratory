package com.tads.vdlab.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ScriptUtil {

    public static Logger logger = LoggerFactory.getLogger(ScriptUtil.class);

    public static List<String> executaComandoShellLine(String comando) throws IOException {
        logger.info("Executando: " + comando);
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(comando);

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));


        List<String> outputs = new ArrayList<>();

        // Salva o retorno proveniente dos scripts bem executados
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            outputs.add(s);
        }

        // Mostra os erros na execução do script
        while ((s = stdError.readLine()) != null) {
            logger.error(s);
        }

        return outputs;
    }

    public static List<String> executaComandoShellArray(String[] comando) throws IOException {
        logger.info("Executando: " + comando.toString());
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(comando);

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));


        List<String> outputs = new ArrayList<>();

        // Salva o retorno proveniente dos scripts bem executados
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            outputs.add(s);
        }

        // Mostra os erros na execução do script
        while ((s = stdError.readLine()) != null) {
            logger.error(s);
        }

        return outputs;
    }
}
