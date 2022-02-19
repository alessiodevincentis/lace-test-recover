package com.woonders.lacemscommon.app.viewmodel;

import com.woonders.lacemscommon.db.entity.Pratica;
import com.woonders.lacemscommon.db.entityenum.FeeType;
import com.woonders.lacemscommon.db.entityenum.Impiego;
import com.woonders.lacemscommon.db.entityenum.SimulatorRoundingMode;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ema98 on 8/10/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
public class SimulatorTableViewModel extends AbstractBaseViewModel {

    private long id;
    private String name;
    private FeeType insuranceCostFeeType;
    private BigDecimal over;
    private SimulatorRoundingMode simulatorRoundingMode;
    private FeeType inquiryCostFeeType;
    private FeeType managementFeesFeeType;
    private FeeType stampDutyFeeType;
    private FeeType otherCostsFeeType;
    private SimulatorTableStatus simulatorTableStatus;
    private OperatorViewModel creatorOperatorViewModel;
    private AziendaViewModel aziendaViewModel;
    private Pratica.TipoPratica simulatorTableType;
    private List<Impiego> jobTypeList;
    private List<SimulatorTableDetailsViewModel> simulatorTableDetailsViewModelList;
    private List<OperatorViewModel> grantedOperatorViewModelList;

    @Override
    protected long getIdToCompare() {
        return id;
    }

    @Getter
    public enum SimulatorTableStatus {
        ENABLED(true, "Abilitata"), DISABLED(false, "Disabilitata");

        private final boolean enabled;
        private final String label;

        SimulatorTableStatus(final boolean enabled, final String label) {
            this.enabled = enabled;
            this.label = label;
        }

        public static SimulatorTableStatus fromValue(final boolean value) {
            if (value) {
                return ENABLED;
            } else {
                return DISABLED;
            }
        }
    }
}
