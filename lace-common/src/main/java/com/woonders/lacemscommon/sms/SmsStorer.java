package com.woonders.lacemscommon.sms;

import com.woonders.lacemscommon.app.clicksend.model.request.SmsRequest;
import com.woonders.lacemscommon.db.entity.CampagnaSms;
import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entityenum.EsitoSmsNotificaLead;
import com.woonders.lacemscommon.db.repository.CampagnaSmsRepository;
import com.woonders.lacemscommon.db.repository.OperatorRepository;
import com.woonders.lacemscommon.db.tenantrepository.SmsTenant;
import com.woonders.lacemscommon.db.tenantrepository.SmsTenantRepository;
import com.woonders.lacemscommon.exception.SmsNotSentException;
import com.woonders.lacemscommon.exception.UnableToSendSmsException;
import com.woonders.lacemscommon.service.SmsService;
import com.woonders.lacemscommon.service.impl.SmsServiceImpl;
import com.woonders.lacemscommon.sms.aws.AWSSmsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emanuele on 04/04/2017.
 */
@Slf4j
@Component
public class SmsStorer {

	@Autowired
	private CampagnaSmsRepository campagnaSmsRepository;

	@Autowired
	private SmsTenantRepository smsTenantRepository;

	@Autowired
	private OperatorRepository operatorRepository;


	@Transactional
	public void setStatusOnSms(List<CampagnaSms> partitionedCampagnaSmsDaSpedireList, EsitoSmsNotificaLead nonSpedito) {
		for (CampagnaSms campagnaSms : partitionedCampagnaSmsDaSpedireList) {
			campagnaSms.setEsito(nonSpedito);
			campagnaSmsRepository.save(campagnaSms);
		}
	}

	public void sendStoreAndRetry(long campagnaSmsId, String tenantName, String mittente, String testo, String destinatario) throws UnableToSendSmsException, SmsNotSentException {
		try {
			String messageId = AWSSmsManager.shared().sendSMSMessage(mittente, testo, destinatario);
			SmsTenant smsTenant = new SmsTenant();
			smsTenant.setIdSms(messageId);
			smsTenant.setTenantName(tenantName);
			smsTenantRepository.save(smsTenant);
			CampagnaSms campagnaSms = campagnaSmsRepository.findOne(campagnaSmsId);
			if (campagnaSms != null) {
				campagnaSms.setDataEsito(LocalDateTime.now());
				campagnaSms.setEsito(EsitoSmsNotificaLead.SPEDITO);
				campagnaSms.setMessageId(messageId);
				campagnaSmsRepository.save(campagnaSms);
			} else {
				log.error("inviato ma poi non trovato nel DB ");
			}
		}
		catch (Exception e){
			log.error("campagna sms exception ");
			throw new UnableToSendSmsException(e);
		}
	}
}
