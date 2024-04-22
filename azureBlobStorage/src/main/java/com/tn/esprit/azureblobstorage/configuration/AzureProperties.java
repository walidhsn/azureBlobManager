package com.tn.esprit.azureblobstorage.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("azure.blob")
public class AzureProperties {
    private String connectionstring;
    private String container;
}
