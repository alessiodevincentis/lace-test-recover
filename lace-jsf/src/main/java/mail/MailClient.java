package mail;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.woonders.lacemscommon.app.viewmodel.AziendaViewModel;
import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.app.viewmodel.PraticaViewModel;
import com.woonders.lacemscommon.app.viewmodel.PreventivoViewModel;
import com.woonders.lacemscommon.app.viewmodel.PrivacyTemplateViewModel;
import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entityenum.Tipo;
import com.woonders.lacemscommon.util.DateToCalendar;
import com.woonders.lacemsjsf.config.PropertiesUtil;

import enumPdf.NameFieldsPdfCliente;
import lombok.Getter;
import lombok.Setter;

@ManagedBean(name = MailClient.NAME)
@SessionScoped
@Getter
@Setter
public class MailClient implements Serializable {

	public static final String NAME = "mailClient";
	public static final String JSF_NAME = "#{" + NAME + "}";

	private String subject;
	private String cc;
	private String ccn;
	private String body;
	@ManagedProperty(PropertiesUtil.JSF_NAME)
	private PropertiesUtil propertiesUtil;
    private boolean mailPrivacy = false;
    

	//TODO tutti questi metodi che creano le stringhe andrebbero unificati per PraticaViewModel e PreventivoViewModel
	// Trasforma i preventivi dei nominativi in una stringa
	public String textPreventiviNominativo(final ClienteViewModel nominativo,
			final List<PraticaViewModel> listPreventivi, final AziendaViewModel aziendaViewModel) {
		final StringBuilder builder = new StringBuilder();

		if (listPreventivi != null) {
			builder.append(propertiesUtil.getDatiFornitiMsgValue());
			builderDatiAnagrafici(nominativo, builder);
			builder.append(propertiesUtil.getPreventivoMsgValue());
			for (final PraticaViewModel p : listPreventivi) {
				builder.append("Tipo: ");
				builder.append(Objects.toString(p.getTipoPratica(), ""));
				builder.append(" Rata: ");
				builder.append(Objects.toString(p.getRata(), ""));
				builder.append(" Durata: ");
				builder.append(Objects.toString(p.getDurata(), ""));
				builder.append(" Importo: ");
				builder.append(Objects.toString(p.getImportoErogato(), ""));
				builder.append(" Tan: ");
				builder.append(Objects.toString(p.getTan(), ""));
				builder.append(" Taeg: ");
				builder.append(Objects.toString(p.getTaeg(), ""));
				builder.append(" Teg: ");
				builder.append(Objects.toString(p.getTeg(), ""));
				builder.append(System.getProperty("line.separator"));
				builder.append(System.getProperty("line.separator"));
			}
		}
		builder.append(propertiesUtil.getInformativaPreventivoMsgValue());
		builder.append(propertiesUtil.getInfoDocMsgValue());
		builderDatiAzienda(aziendaViewModel, builder);
		return builder.toString();
	}

	// Trasforma i preventivi dei nominativi in una stringa
	public String textPreventiviNominativoSMS(final ClienteViewModel nominativo,
			final List<PraticaViewModel> listPreventivi) {
		final StringBuilder builder = new StringBuilder();

		if (listPreventivi != null) {
			builder.append(propertiesUtil.getDatiClienteMsgValue());
			builderCognomeNome(nominativo, builder);
			builder.append(propertiesUtil.getPreventivoMsgSMSValue());
			int i = 1;
			for (final PraticaViewModel p : listPreventivi) {
				builder.append(" Nr" + i + ".");
				builder.append(" Tipo: ");
				builder.append(Objects.toString(p.getTipoPratica(), ""));
				builder.append(" Rata: ");
				builder.append(Objects.toString(p.getRata(), ""));
				builder.append(" Durata: ");
				builder.append(Objects.toString(p.getDurata(), ""));
				builder.append(" Importo: ");
				builder.append(Objects.toString(p.getImportoErogato(), ""));
				builder.append(" Tan: ");
				builder.append(Objects.toString(p.getTan(), ""));
				builder.append(" Taeg: ");
				builder.append(Objects.toString(p.getTaeg(), ""));
				builder.append(" Teg: ");
				builder.append(Objects.toString(p.getTeg(), ""));
				i++;
			}
		}
		return builder.toString();
	}
	
	//*** Mod By Cristian 16-11-2021
	public Map<Long, StringBuilder> textPrivacyTemplate(final ClienteViewModel nominativo,
			final List<PrivacyTemplateViewModel> listPrivacyTemplte) {
		Map<Long, StringBuilder> privacyTemplateMap = new HashMap<>();
		final StringBuilder builder = new StringBuilder();

		String provenienza = nominativo.getProvenienza();
		String fornitorelead = (nominativo.getFornitoreLead() == null) ? "" : nominativo.getFornitoreLead();
		long policyTemplateId;
		
		Collections.sort(listPrivacyTemplte);
		Collections.reverse(listPrivacyTemplte);
		boolean okMap = false;
		
		//*** Inserisco dati del cliente
		builder.append("Gentile Cliente,\nsulla base dei dati forniti:\n\n");
		builderDatiAnagrafici(nominativo, builder);
		builder.append("\n\n");
		
		for(final PrivacyTemplateViewModel p : listPrivacyTemplte)
		{
			if ((fornitorelead == null || fornitorelead == "") 
					&& (provenienza != null && provenienza.length() > 0))
			{
				if (p.getProvenienza() == null)
					p.setProvenienza("");
				if (p.getProvenienza().equals(provenienza))
				{
					builder.append(p.getPrivacyText());
					policyTemplateId = p.getId();
					privacyTemplateMap.put(policyTemplateId, builder);
					okMap = true;
					break;
				}
			}
			else 
			{
				if (p.getFornitoreLead().equals(fornitorelead))
				{
					builder.append(p.getPrivacyText());
					policyTemplateId = p.getId();
					privacyTemplateMap.put(policyTemplateId, builder);
					okMap = true;
					break;
				}
			}
			
		}
		if (!okMap)
		{
			privacyTemplateMap.put((long)0, builder);
		}
		
		return privacyTemplateMap;
	}
	
	public String getOggettoMailPrivacy(final ClienteViewModel clienteViewModel) {
		final StringBuilder builder = new StringBuilder();
		builder.append(propertiesUtil.getOggettoMailBustapagaValue());
		builderCognomeNome(clienteViewModel, builder);
		return builder.toString();
	}
	

	private void builderCognomeNome(final ClienteViewModel persona, final StringBuilder builder) {
		builder.append(" " + persona.getCognome());
		builder.append(" " + persona.getNome());
	}

	public String getOggettoMailPreventivo(final ClienteViewModel clienteViewModel) {
		final StringBuilder builder = new StringBuilder();
		builder.append(propertiesUtil.getOggettoMailPreventivoValue());
		builderCognomeNome(clienteViewModel, builder);
		return builder.toString();
	}

	public String getOggettoMailBustapaga(final ClienteViewModel clienteViewModel) {
		final StringBuilder builder = new StringBuilder();
		builder.append(propertiesUtil.getOggettoMailBustapagaValue());
		builderCognomeNome(clienteViewModel, builder);
		return builder.toString();
	}

	public void setPropertiesUtil(final PropertiesUtil propertiesUtil) {
		this.propertiesUtil = propertiesUtil;
	}

	public void reset() {
		setSubject("");
		setBody("");
	}

	private void builderDatiAnagrafici(final ClienteViewModel clienteViewModel, final StringBuilder builder) {
		builder.append("Cognome: ");
		builder.append(clienteViewModel.getCognome());
		builder.append("\nNome: ");
		builder.append(clienteViewModel.getNome());
		builder.append("\nData di Nascita: ");
		builder.append(DateToCalendar.patternData(clienteViewModel.getDataNascita()));
		builder.append("\nImpiego: ");
		builder.append(clienteViewModel.getImpiego());
		builder.append("\nData Assunzione/Pensionamento: ");
		builder.append(DateToCalendar.patternData(clienteViewModel.getDataInizio()));
		if (clienteViewModel.getTipo().equalsIgnoreCase(Tipo.NOMINATIVO.getValue())) {
			builder.append("\nNetto Stipendio: ");
			builder.append(clienteViewModel.getNettoMensileNominativo());
		}
	}

	private void builderDatiAzienda(final AziendaViewModel aziendaViewModel, final StringBuilder builder) {
		builder.append("\n\n-Email: ");
		builder.append(aziendaViewModel.getEmail());
		builder.append("\n-Telefono: ");
		builder.append(aziendaViewModel.getTelefono());
		builder.append("\n-Fax: ");
		builder.append(aziendaViewModel.getFax());
		builder.append("\n-Sito Web: ");
		builder.append(aziendaViewModel.getSitoWeb());
	}

	private void builderDatiAziendaSMS(final AziendaViewModel aziendaViewModel, final StringBuilder builder) {
		builder.append(" Email: ");
		builder.append(aziendaViewModel.getEmail());
		builder.append(" Fax: ");
		builder.append(aziendaViewModel.getFax());
	}

	// Trasforma i preventivi del cliente in una stringa
	public String textPreventivi(final ClienteViewModel clienteViewModel,
			final List<PreventivoViewModel> listPreventivi, final AziendaViewModel aziendaViewModel) {
		final StringBuilder builder = new StringBuilder();

		if (listPreventivi != null) {
			builder.append(propertiesUtil.getDatiClienteMsgValue());
			builderCognomeNome(clienteViewModel, builder);
			builder.append(propertiesUtil.getPreventivoMsgValue());
			for (final PreventivoViewModel p : listPreventivi) {
				builder.append("- *Tipo: ");
				builder.append(Objects.toString(p.getTipoPratica(), ""));
				builder.append(" Rata: ");
				builder.append(Objects.toString(p.getRata(), ""));
				builder.append(" Durata: ");
				builder.append(Objects.toString(p.getDurata(), ""));
				builder.append(" Importo: ");
				builder.append(Objects.toString(p.getImporto(), ""));
				builder.append(" Tan: ");
				builder.append(Objects.toString(p.getTan(), ""));
				builder.append(" Taeg: ");
				builder.append(Objects.toString(p.getTaeg(), ""));
				builder.append(" Teg: ");
				builder.append(Objects.toString(p.getTeg(), ""));
				builder.append(System.getProperty("line.separator"));
				builder.append(System.getProperty("line.separator"));
			}
			builder.append(propertiesUtil.getInfoUlteriori1MsgValue());
			builderDatiAzienda(aziendaViewModel, builder);
		}
		return builder.toString();
	}

	// Trasforma i preventivi del cliente in una stringa per un sms
	public String textPreventiviSMS(final ClienteViewModel clienteViewModel,
			final List<PreventivoViewModel> listPreventivi) {
		final StringBuilder builder = new StringBuilder();

		if (listPreventivi != null) {
			builder.append(propertiesUtil.getDatiClienteMsgValue());
			builderCognomeNome(clienteViewModel, builder);
			builder.append(propertiesUtil.getPreventivoMsgSMSValue());
			int i = 1;
			for (final PreventivoViewModel p : listPreventivi) {
				builder.append(" Nr" + i + ".");
				builder.append(" Tipo: ");
				builder.append(Objects.toString(p.getTipoPratica(), ""));
				builder.append(" Rata: ");
				builder.append(Objects.toString(p.getRata(), ""));
				builder.append(" Durata: ");
				builder.append(Objects.toString(p.getDurata(), ""));
				builder.append(" Importo: ");
				builder.append(Objects.toString(p.getImporto(), ""));
				builder.append(" Tan: ");
				builder.append(Objects.toString(p.getTan(), ""));
				builder.append(" Taeg: ");
				builder.append(Objects.toString(p.getTaeg(), ""));
				builder.append(" Teg: ");
				builder.append(Objects.toString(p.getTeg(), ""));
				i++;
			}
		}
		return builder.toString();
	}

	public String textRichiestaBusta(final ClienteViewModel clienteViewModel, final String tipoPratica,
			final AziendaViewModel aziendaViewModel) {
		final StringBuilder builder = new StringBuilder();
		builder.append(propertiesUtil.getDatiClienteMsgValue());
		builderCognomeNome(clienteViewModel, builder);
		builder.append(propertiesUtil.getInfoBenestareMsgValue());
		builder.append(" " + convertTipoPratica(tipoPratica));
		builder.append(propertiesUtil.getBustapagaMsgValue());
		builderDatiAzienda(aziendaViewModel, builder);
		return builder.toString();
	}

	public String textRichiestaBustaSMS(final ClienteViewModel clienteViewModel, final String tipoPratica,
			final AziendaViewModel aziendaViewModel) {
		final StringBuilder builder = new StringBuilder();
		builder.append(propertiesUtil.getDatiClienteMsgValue());
		builderCognomeNome(clienteViewModel, builder);
		builder.append(propertiesUtil.getInfoBenestareSMSMsgValue());
		builder.append(" " + convertTipoPratica(tipoPratica));
		builder.append(propertiesUtil.getBustapagaSMSMsgValue());
		builderDatiAziendaSMS(aziendaViewModel, builder);
		return builder.toString();
	}

	public String convertTipoPratica(String tipoPratica) {
		if (tipoPratica.equalsIgnoreCase(Pratica.TipoPratica.CESSIONE_P.getValue())) {
			tipoPratica = NameFieldsPdfCliente.CQP_LETTERE.getFieldName().toLowerCase();
		} else if (tipoPratica.equalsIgnoreCase(Pratica.TipoPratica.CESSIONE_S.getValue())) {
			tipoPratica = NameFieldsPdfCliente.CQS_LETTERE.getFieldName().toLowerCase();
		} else if (tipoPratica.equalsIgnoreCase(Pratica.TipoPratica.DELEGA.getValue())) {
			tipoPratica = NameFieldsPdfCliente.DLG_LETTERE.getFieldName().toLowerCase();
		} else {
			tipoPratica = NameFieldsPdfCliente.PP_LETTERE.getFieldName().toLowerCase();
		}

		return tipoPratica;
	}

	// TODO da eliminare nel refactor e lasciarlo gestire a spring
	public void reinitialize() {
		subject = "";
		cc = "";
		ccn = "";
		body = "";
	}
}
