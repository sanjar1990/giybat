package api.giybat.uz;

import api.giybat.uz.enums.Language;
import api.giybat.uz.enums.SmsType;
import api.giybat.uz.service.SmsSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {
@Autowired
private SmsSenderService smsSenderService;
	@Test
	void contextLoads() {
//		System.out.println(smsSenderService.sendSms(
//				"998908070176",
//				"Bu Eskiz dan test",
//				"9990", SmsType.REGISTRATION,
//				Language.uz));
	}

}
