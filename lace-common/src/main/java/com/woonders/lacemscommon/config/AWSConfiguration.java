package com.woonders.lacemscommon.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by emanuele on 28/11/16.
 */
@Configuration
public class AWSConfiguration {

    public static final String BUCKET_NAME = System.getProperty("AWS_S3_BUCKET");
    public static final String BUCKET_NAME_BACKUP = System.getProperty("AWS_S3_BUCKET_BACKUP");
    private final String REGION_NAME;

    public AWSConfiguration() {
        REGION_NAME = System.getProperty("cloud.aws.region.static");
    }

    @Deprecated
    //deprecated in last aws sdk, needs to be changed (look sendmail implementation, it uses the new way)
    @Bean
    public AmazonS3 amazonS3Client() {
        return AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(REGION_NAME)).build();
    }
}
