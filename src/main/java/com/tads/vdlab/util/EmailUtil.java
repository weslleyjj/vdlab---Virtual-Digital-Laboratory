package com.tads.vdlab.util;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

@Component
public class EmailUtil {

    private JavaMailSender mailSender;

    public EmailUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String nomeUsuario,String novaSenha, String emailParaEnvio) {
        try {
            MimeMessage mail = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper( mail );
            helper.setTo(emailParaEnvio);
            helper.setSubject( "QueijoSoft Troca de Senha" );
            helper.setText(createMessage(novaSenha, nomeUsuario), true);
            mailSender.send(mail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void newUserSendMail(String login, String nomePessoa, String emailParaEnvio) {
        try {
            MimeMessage mail = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper( mail );
            helper.setTo(emailParaEnvio);
            helper.setSubject( "Virtual Digital Laboratory - Novo Usuário" );
            helper.setText(newUserMessage(login, nomePessoa), true);
            mailSender.send(mail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void newAgendamentoSendMail(LocalDateTime dataAgendamento,String nomePessoa, String emailParaEnvio) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String dataFormatada = dataAgendamento.format(formatter);
            MimeMessage mail = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper( mail );
            helper.setTo(emailParaEnvio);
            helper.setSubject( "VDLAB - Novo Agendamento" );
            helper.setText(newAgendamentoMessage(dataFormatada, nomePessoa), true);
            mailSender.send(mail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createMessage(String novaSenha, String nomeUsuario){
        return "<!DOCTYPE html>\n" +
                "  <body>\n" +
                "    <h3>Olá "+nomeUsuario+" =D</h3>\n" +
                "    <h4>Foi gerada uma nova senha aleatória para o seu usuário:</h4>\n" +
                "    <p>"+novaSenha+"</p>\n" +
                "    <h4>Atualize a senha assim que fizer o login!</h4>\n" +
                "    <h4>A equipe QueijoSoft agradece!</h4>\n" +
                "  </body>\n" +
                "</html>";
    }

    private String newUserMessage(String login, String nomePessoa){
        return "<!DOCTYPE html>\n" +
                "  <body>\n" +
                "    <h3>Olá, "+nomePessoa+" seja bem vindo ao VDLAB!</h3>\n" +
                "    <h4>O seu usuário foi cadastrado com sucesso!</h4>\n" +
                "    <h4>LOGIN: "+login+"</h4>\n" +
                "    <h4>A equipe VDLAB agradece!</h4>\n" +
                "  </body>\n" +
                "</html>";
    }

    private String newAgendamentoMessage(String data, String nomePessoa){
        return "<!DOCTYPE html>\n" +
                "  <body>\n" +
                "    <h3>Olá, "+nomePessoa+" você possui um novo agendamento no VDLAB!</h3>\n" +
                "    <h4>Seu horário de uso permitido está programado para:</h4>\n" +
                "    <h4>"+data+"</h4>\n" +
                "    <h4>Acesse a plataforma para mais detalhes.</h4>\n" +
                "    <h4>A equipe VDLAB agradece!</h4>\n" +
                "  </body>\n" +
                "</html>";
    }
}
