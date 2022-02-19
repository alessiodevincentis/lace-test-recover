package com.woonders.lacemsjsf.view.storagespaceinfo;

import com.woonders.lacemscommon.service.FileService;
import com.woonders.lacemscommon.service.impl.AmazonS3FileServiceImpl;
import com.woonders.lacemsjsf.config.PropertiesUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.chart.MeterGaugeChartModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static com.woonders.lacemsjsf.view.storagespaceinfo.StorageSpaceInfoView.NAME;

/**
 * Created by ema98 on 9/18/2017.
 */
@ManagedBean(name = NAME)
@ViewScoped
@Getter
@Setter
public class StorageSpaceInfoView {

    public static final String NAME = "storageSpaceInfoView";
    public static final String JSF_NAME = "#{" + NAME + "}";

    @ManagedProperty(AmazonS3FileServiceImpl.JSF_NAME)
    private FileService fileService;

    @ManagedProperty(PropertiesUtil.JSF_NAME)
    private PropertiesUtil propertiesUtil;

    private MeterGaugeChartModel storageSpaceMeterGaugeChartModel;

    @PostConstruct
    public void init() {
        List<Number> intervals = new ArrayList<Number>(){{
            add(StorageSpaceChartInfo.LOW_USAGE.value);
            add(StorageSpaceChartInfo.MEDIUM_USAGE.value);
            add(StorageSpaceChartInfo.HIGH_USAGE.value);
            add(StorageSpaceChartInfo.MAX.value);
        }};

        storageSpaceMeterGaugeChartModel = new MeterGaugeChartModel(((fileService.getUsedStorageSpace() / 1024) / 1024) / 1024, intervals);

        storageSpaceMeterGaugeChartModel.setSeriesColors(new StringJoiner(",").add(StorageSpaceChartInfo.LOW_USAGE.chartColor).add(StorageSpaceChartInfo.MEDIUM_USAGE.chartColor)
                .add(StorageSpaceChartInfo.HIGH_USAGE.chartColor).add(StorageSpaceChartInfo.MAX.chartColor).toString());
        storageSpaceMeterGaugeChartModel.setTitle(propertiesUtil.getStorageSpaceChartTitle());
        storageSpaceMeterGaugeChartModel.setGaugeLabel("GB");

    }

    @AllArgsConstructor
    private enum StorageSpaceChartInfo {
        LOW_USAGE(10, "66cc66"), MEDIUM_USAGE(50, "93b75f"), HIGH_USAGE (90, "e7e658"), MAX( 100, "cc6666");

        private int value;
        private String chartColor;
    }

}
