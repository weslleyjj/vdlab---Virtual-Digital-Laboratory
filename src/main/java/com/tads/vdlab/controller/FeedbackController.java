package com.tads.vdlab.controller;

import com.tads.vdlab.model.Agendamento;
import com.tads.vdlab.model.Usuario;
import com.tads.vdlab.repository.UsuarioRepository;
import com.tads.vdlab.service.AgendamentoService;
import com.tads.vdlab.util.ScriptUtil;
import com.tads.vdlab.util.UsuarioUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Base64;
import java.util.stream.Collectors;

@Controller
public class FeedbackController {
    public static Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    @Value("${arquivos.vdlab}")
    private String arquivosPath;

    @Value("${comandos.scripts.programmer}")
    private String scriptProgrammer;

    @Value("${comandos.scripts.getPlacas}")
    private String scriptGetPlacas;

    @Value("#{'${feedback.urlsMonitoramento}'.split(',')}")
    private List<String> camerasSistema;

    @Value("${feedback.iniciarVideo}")
    private String startStream;

    @Value("${feedback.pararVideo}")
    private String stopStream;

    @Value("${fpga.host}")
    private String host;

    @Value("${fpga.port}")
    private Integer port;

    @Value("${fpga.quantidadePlacas}")
    private Integer quantidadePlacas;

    private HashMap<String, String> placasConectadas = new HashMap<>();
    private HashMap<String, Boolean> disponibilidadePlacas = new HashMap<>();
    private HashMap<String, Integer> camerasMonitoramento = new HashMap<>();
    private HashMap<String, Integer> ordemPlacas = new HashMap<>();

    private Socket socket;
    private DataOutputStream socketOut;
    private BCryptPasswordEncoder encoder;

    private UsuarioRepository usuarioRepository;
    private AgendamentoService agendamentoService;

    @Autowired
    public FeedbackController(UsuarioRepository usuarioRepository, AgendamentoService agendamentoService) throws IOException {
        this.agendamentoService = agendamentoService;
        this.usuarioRepository = usuarioRepository;
        encoder = new BCryptPasswordEncoder();
        getPlacasNoSistema();
    }

    @GetMapping("/controlador-fpga")
    public ModelAndView feedback(@RequestParam("p") String codigoPlaca) throws InterruptedException, IOException {

        //startStream();

        Integer camera = getCameraDisponivel(codigoPlaca);

        ModelAndView model = new ModelAndView("feedback");
        model.addObject("urlStream", camerasSistema.get(camera));
        model.addObject("placa", codigoPlaca);

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

        conversao += (ordemPlacas.get(dados.getPlacaDesejada()) + 1);

        socketOut.write(conversao.getBytes());
        socketOut.writeUTF("\n");

        System.out.println(conversao);
    }

    @PostMapping(value = "/upload")
    public String uploadArquivoSof(@RequestParam("file") MultipartFile file, Principal principal, RedirectAttributes redirectAttributes) throws IOException {
        if(!isHorarioUsuarioValido(UsuarioUtil.getUsuarioLogado(principal, usuarioRepository))){
            redirectAttributes.addFlashAttribute("message", "Você não possui agendamento neste horário.");
            return "redirect:/carregar";
        }
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Envie um arquivo válido.");
            return "redirect:/carregar";
        }

        String codigoPlaca = getPlacaDisponivel();

        if(codigoPlaca == null){
            redirectAttributes.addFlashAttribute("message", "Todas as placas estão em uso no momento");
            return "redirect:/carregar";
        }
        try {
            String fileName = "carregar-"+file.getOriginalFilename();

            Path path = Paths.get(arquivosPath).toAbsolutePath().normalize();
            Files.createDirectories(path);

            Path targetLocation = path.resolve(fileName); // Caminho absoluto para acesso ao arquivo
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);



            String[] comandoProgramar = {scriptProgrammer, placasConectadas.get(codigoPlaca), "p;" + targetLocation};

            ScriptUtil.executaComandoShellArray(comandoProgramar);

            disponibilidadePlacas.put(codigoPlaca, false);
            return "redirect:/controlador-fpga?p="+ codigoPlaca;

        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
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

    private String getPlacaDisponivel(){
        List<String> keys;
        keys = disponibilidadePlacas.keySet().stream().collect(Collectors.toList());

        for (String key : keys) {
            if(disponibilidadePlacas.get(key)){
                return key;
            }
        }

        return null;
    }

    /**
     * Função para obter o indice da List em que se encontra a url da camera especifica da placa
     * */
    private Integer getCameraDisponivel(String chave){
        return camerasMonitoramento.get(chave);
    }

    /**
     * Função para inicialização das FPGAs no servidor, gerando suas chaves de acesso e controle de disponibilidade
     * */
    private void getPlacasNoSistema() throws IOException {
        List<String> resultShell = ScriptUtil.executaComandoShellLine("/home/weslley/Documentos/TADS/TCC/testes-tcl/get_fpga_order.sh");
        Integer index = 0;
        for (String s : resultShell) {
            if(s.contains("USB")){
                String chaveAcessoPlaca = encoder.encode(s);
                chaveAcessoPlaca = Base64.getEncoder().withoutPadding().encodeToString(chaveAcessoPlaca.getBytes());

                placasConectadas.put(chaveAcessoPlaca, s);
                disponibilidadePlacas.put(chaveAcessoPlaca, true);
                camerasMonitoramento.put(chaveAcessoPlaca, index);
                ordemPlacas.put(chaveAcessoPlaca, index);
                index++;
//                placasConectadas.add(s);
            }
        }
    }

    private boolean isHorarioUsuarioValido(Usuario usuario){
        LocalDateTime dataAtual = LocalDateTime.now();
        List<Agendamento> agendamentos = agendamentoService.buscarAgendamentoByIdUsuario(usuario.getId());

        for (Agendamento agendamento : agendamentos) {
            LocalDateTime dataExpirado = agendamento.getDataAgendada();
            boolean isIndefinido = agendamento.getTempoSessao() == 0;
            if(!isIndefinido){
                dataExpirado.plusMinutes(agendamento.getTempoSessao());
                if(agendamento.getDataAgendada().isBefore(dataAtual) && dataExpirado.isBefore(dataAtual)){
                    return true;
                }
            }else{
                if(agendamento.getDataAgendada().isBefore(dataAtual)){
                    return true;
                }
            }
        }
        return false;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DadosSocket{
        Integer comandoInputs;
        String placaDesejada;
    }

}
