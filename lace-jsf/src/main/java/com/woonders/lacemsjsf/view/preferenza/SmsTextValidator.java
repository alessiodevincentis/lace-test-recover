package com.woonders.lacemsjsf.view.preferenza;

import com.woonders.lacemsjsf.config.PropertiesUtil;
import com.woonders.lacemsjsf.util.BaseValidator;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.text.MessageFormat;

/**
 * Created by Emanuele on 11/02/2017.
 */
@Slf4j
public abstract class SmsTextValidator extends BaseValidator {

	/*-
	* ^[A-Za-z0-9 \r\n@£$¥èéùìòÇØøÅå\u0394_\u03A6\u0393\u039B\u03A9\u03A0\u03A8\u03A3\u0398\u039EÆæßÉ!"#$%&amp;'()*+,\-./:;&lt;=&gt;?¡ÄÖÑÜ§¿äöñüà^{}\\\[~\]|\u20AC]*$
	*
	* Assert position at the beginning of the string «^»
	* Match a single character present in the list below «[A-Za-z0-9 \r\n@£$¥èéùìòÇØøÅå\u0394_\u03A6\u0393\u039B\u03A9\u03A0\u03A8\u03A3\u0398\u039EÆæßÉ!"#$%&amp;'()*+,\-./:;&lt;=&gt;?¡ÄÖÑÜ§¿äöñüà^{}\\\[~\]|\u20AC]*»
	*    Between zero and unlimited times, as many times as possible, giving back as needed (greedy) «*»
	*    A character in the range between "A" and "Z" «A-Z»
	*    A character in the range between "a" and "z" «a-z»
	*    A character in the range between "0" and "9" «0-9»
	*    The character " " « »
	*    A carriage return character «\r»
	*    A line feed character «\n»
	*    One of the characters "@£$¥èéùìòÇØøÅå" «@£$¥èéùìòÇØøÅå»
	*    Unicode character U+0394 «\u0394», Greek capital Delta
	*    The character "_" «_»
	*    Unicode character U+03A6 «\u03A6», Greek capital Phi
	*    Unicode character U+0393 «\u0393», Greek capital Gamma
	*    Unicode character U+039B «\u039B», Greek capital Lambda
	*    Unicode character U+03A9 «\u03A9», Greek capital Omega
	*    Unicode character U+03A0 «\u03A0», Greek capital Pi
	*    Unicode character U+03A8 «\u03A8», Greek capital Psi
	*    Unicode character U+03A3 «\u03A3», Greek capital Sigma
	*    Unicode character U+0398 «\u0398», Greek capital Theta
	*    Unicode character U+039E «\u039E», Greek capital Xi
	*    One of the characters "ÆæßÉ!"#$%&amp;'()*+," «ÆæßÉ!"#$%&amp;'()*+,»
	*    A - character «\-»
	*    One of the characters "./:;&lt;=&gt;?¡ÄÖÑÜ§¿äöñüà^{}" «./:;&lt;=&gt;?¡ÄÖÑÜ§¿äöñüà^{}»
	*    A \ character «\\»
	*    A [ character «\[»
	*    The character "~" «~»
	*    A ] character «\]»
	*    The character "|" «|»
	*    Unicode character U+20AC «\u20AC», Euro sign
	* Assert position at the end of the string (or before the line break at the end of the string, if any) «$»
	* http://frightanic.com/software-development/regex-for-gsm-03-38-7bit-character-set/
	*/

	// public static final String GSM_CHARACTERS_REGEX = "^[A-Za-z0-9
	// \\r\\n@£$¥èéùìòÇØøÅå\u0394_\u03A6\u0393\u039B\u03A9\u03A0\u03A8\u03A3\u0398\u039EÆæßÉ!\"#$%&amp;'()*+,\\-./:;&lt;=&gt;?¡ÄÖÑÜ§¿äöñüà^{}\\\\\\[~\\]|\u20AC]*$";
	// mi sono basato su quella e l'ho modificata eliminando cose inutili e
	// pericolose come suggerito qui da clicksend
	// https://en.wikipedia.org/wiki/GSM_03.38#GSM_7-bit_default_alphabet_and_extension_table_of_3GPP_TS_23.038_.2F_GSM_03.38
	// http://help.clicksend.com/13993-SMS/how-many-characters-can-i-send-in-an-sms
	// per non sbagliarci, ho anche disabilitato i caratteri non standard,
	// verranno scartati da clicksend
	// controllare sempre SMSSendValidator
	protected static final String GSM_BASIC_CHARACTERS_REGEX = "^[A-Za-z0-9 \\-@£$àèéùìò_É!\"#$%&amp;'()*+,.:;&lt;=&gt;?]*$";
	private static final int MIN_SMS_LENGTH = 20;
	private static final int MAX_SMS_LENGTH = 160;
	protected String smsString;
	protected PropertiesUtil propertiesUtil;

	@PostConstruct
	public void init() {
		propertiesUtil = findBean(PropertiesUtil.JSF_NAME);
	}

	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object object) throws ValidatorException {
		smsString = (String) object;
		validateSms();
	}

	public void validateSms() {
		if (smsString != null) {
			if (!smsString.matches(GSM_BASIC_CHARACTERS_REGEX)) {
				final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						propertiesUtil.getMsgSmsValidationError(), null);
				throw new ValidatorException(msg);
			}
			if (calcSmsLength() < MIN_SMS_LENGTH) {
				final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						MessageFormat.format(propertiesUtil.getMsgSmsMinLengthError(), MIN_SMS_LENGTH), null);
				throw new ValidatorException(msg);
			}
			if (calcSmsLength() > MAX_SMS_LENGTH) {
				final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						MessageFormat.format(propertiesUtil.getMsgSmsMaxLengthError(), MAX_SMS_LENGTH), null);
				throw new ValidatorException(msg);
			}
		}
	}

	public abstract int calcSmsLength();
}
