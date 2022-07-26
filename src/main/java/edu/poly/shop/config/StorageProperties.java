package edu.poly.shop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;


@Configuration
@ConfigurationProperties("storage")// anh xa storage
@Data
public class StorageProperties {
	private String location;//anh xa khai bao truong location 
}
