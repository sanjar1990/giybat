package api.giybat.uz.service;

import api.giybat.uz.enums.EmailType;
import api.giybat.uz.enums.Language;
import api.giybat.uz.exceptions.AppBadException;
import api.giybat.uz.util.JwtUtil;
import api.giybat.uz.util.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Value("${server.domain.name}")
    private String serverUrl;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Autowired
    private EmailHistoryService emailHistoryService;
    private final Integer emailLimit=3;
    public void sendEmailVerification(String email, String profileId, String name, Language language) {
        String subject = "Giybat verification email";
        String jwt= JwtUtil.encode(profileId);
        String url=serverUrl+"/api/v1/auth/verification/email/%s?lang=%s";
       url= String.format(url,jwt,language.name());
        String message = String.format("<h1 style=\"color:#ff0000\"> Hello%s </h1>  " +
                " <p> Click the link to verify your account! </p> ", name) + url;
        sendMimeEmail(email,message,subject);
    }
    public void sendResetPasswordEmail(String username, Language lang) {
        String subject = "Giybat reset password Code";
        String code= RandomUtil.getRandomSmsCode();
        String message="Code for reset password:%s";
        message=String.format(message,code);
        checkAndSave(username,message,subject,code,lang,EmailType.RESET_PASSWORD);
    }
    public void sendChangeUsernameConfirmMail(@NotBlank(message = "username is required") String username, Language language) {
        String subject = "Giybat change username confirmation";
        String code= RandomUtil.getRandomSmsCode();
        String message="Code for Changing username:%s";
        message=String.format(message,code);
        checkAndSave(username,message,subject,code,language,EmailType.CHANGE_USERNAME);
    }
    private void checkAndSave(String email,String message,String subject,String code,Language language,EmailType emailType) {
        Long count=emailHistoryService.getEmailCount(email);
        if(count>=emailLimit){
            throw new AppBadException(resourceBundleService.getMessage("email.count.reached",language));
        }
        emailHistoryService.create(email,message,code,emailType );
        sendMimeEmail(email,message,subject);
    }
    private void sendMimeEmail(String toEmail,String text,String subject) {
        ExecutorService executorService= Executors.newSingleThreadExecutor();
executorService.execute(()->{
    try{
        MimeMessage message=mailSender.createMimeMessage();
        message.setFrom(fromEmail);
        MimeMessageHelper helper=new MimeMessageHelper(message,true);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(text, true);
        mailSender.send(message);
    }catch (MessagingException e){
        e.printStackTrace();
throw  new RuntimeException(e);
    }
});
executorService.shutdown();
    }
    private void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }



}
