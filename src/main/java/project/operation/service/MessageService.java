package project.operation.service;

import common.CRUD.service.ComService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.navigator.service.CacheManager;
import project.operation.entity.Message;
import project.operation.model.ClientCache;

/**
 * Created by Errol on 17/6/6.
 */
@Service
public class MessageService {

    @Autowired
    private ComService comService;
    @Autowired
    private CacheManager cacheManager;

//    private static MessageService instance = new MessageService();
//
//    private MessageService() {}
//
//    public static MessageService getInstance() {
//        return instance;
//    }

    public void send(long senderId, long clientId, String content) {
        Message message = new Message(senderId, clientId, content);
        comService.saveDetail(message);
        ClientCache cc = cacheManager.getClientCache(clientId);
        cc.getNews().add(message.getId());
    }

}
