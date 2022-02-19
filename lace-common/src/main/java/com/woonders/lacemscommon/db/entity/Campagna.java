package com.woonders.lacemscommon.db.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import com.woonders.lacemscommon.app.viewmodel.FiltroCampagnaMarketingViewModel;
import com.woonders.lacemscommon.db.converter.FiltroCampagnaMarketingConverter;

import lombok.*;

/**
 * Created by Emanuele on 27/03/2017.
 */
@Entity
@Table(name = "campagna")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Campagna {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "creationDateTime")
	private LocalDateTime creationDateTime;
	@Column(name = "sentDateTime")
	private LocalDateTime sentDateTime;
	@Column(name = "text")
	private String text;
	@Column(name = "campagnaType")
	@Enumerated(EnumType.STRING)
	private CampagnaType campagnaType;
	@Column(name = "campagnaImport")
	private boolean campagnaImport;
	@OneToOne
	private Operator creatorOperator;
	@OneToOne
	private Operator senderOperator;
	@Column(name = "filtri")
	@Convert(converter = FiltroCampagnaMarketingConverter.class)
	private FiltroCampagnaMarketingViewModel filtroCampagnaMarketingViewModel;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "campagna")
	private List<CampagnaSms> campagnaSmsList;
	@Column(name = "totaleDestinatari")
	private int totaleDestinatari;
	@Column(name = "totaleMessaggi")
	private int totaleMessaggi;

	public enum CampagnaType {
		SMS("SMS"), EMAIL("E-mail");

		private final String value;

		CampagnaType(final String value) {
			this.value = value;
		}

		public static CampagnaType fromValue(final String value) {
			if (value == null) {
				return null;
			}
			for (final CampagnaType campagnaType : CampagnaType.values()) {
				if (campagnaType.toString().equalsIgnoreCase(value)) {
					return campagnaType;
				}
			}
			throw new IllegalArgumentException("No constant with value " + value);
		}

		public String getValue() {
			return value;
		}

	}
}
