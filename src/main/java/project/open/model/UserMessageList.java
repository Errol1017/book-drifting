package project.open.model;

import common.Util.Base64Util;
import common.Util.DateUtil;
import project.operation.entity.Message;
import project.operation.model.ClientCache;

/**
 * Created by Errol on 17/6/11.
 */
public class UserMessageList extends ClientListParent {

    private String content;
    private String time;

    public UserMessageList(Message message, ClientCache clientCache) {
        super(clientCache);
        this.content = message.getContent();
        this.time = DateUtil.date2String(message.getCreateTime(), DateUtil.PATTERN_D);
        if (message.getSenderId() == -1) {
            this.setAvatar(Base64Util.img2String("avatar/avatar.png"));
            this.setNickname("系统消息");
        }
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}
