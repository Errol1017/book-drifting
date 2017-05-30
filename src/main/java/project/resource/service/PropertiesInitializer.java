package project.resource.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import project.resource.properties.ServerProperties;
import project.resource.properties.WeChatProperties;

/**
 * Created by Errol on 17/5/29.
 */
@Component
public class PropertiesInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${project.file.path}")
    private String fileBasePath;
    @Value("${route.host}")
    private String host;
    @Value("${route.redirect}")
    private String redirect;
    @Value("${route.remote}")
    private String remote;
    
    @Value("${wx.app.id}")
    private String wxAppId;
    @Value("${wx.app.secret}")
    private String wxAppSecret;
    @Value("${wx.redirect.uri}")
    private String wxRedirectUri;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ServerProperties.getInstance().init(fileBasePath, host, redirect, remote);
        WeChatProperties.getInstance().init(wxAppId, wxAppSecret, wxRedirectUri);
    }
}
