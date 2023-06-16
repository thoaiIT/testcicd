package com.madive.bigcommerce.madiveone.admin.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Profiles {

    @Value("${spring.profiles.active:loc}")
    private String profile;
    private static String PROFILE;

    @PostConstruct
    public void postConstruct() {
    	PROFILE = this.profile;
    }

    public static boolean isLoc() {
    	return Type.LOC.name().equalsIgnoreCase(PROFILE);
    }

    public static boolean isDev() {
    	return Type.DEV.name().equalsIgnoreCase(PROFILE);
    }

    public static boolean isPrd() {
    	return Type.PRD.name().equalsIgnoreCase(PROFILE);
    }

    enum Type {
    	LOC, DEV, PRD;
    }
}