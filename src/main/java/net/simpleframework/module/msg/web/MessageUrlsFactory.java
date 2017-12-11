package net.simpleframework.module.msg.web;

import net.simpleframework.module.msg.web.page.MyNoticeMessageTPage;
import net.simpleframework.module.msg.web.page.MyPrivateMessageDraftTPage;
import net.simpleframework.module.msg.web.page.MyPrivateMessageSentTPage;
import net.simpleframework.module.msg.web.page.MyPrivateMessageTPage;
import net.simpleframework.module.msg.web.page.MySystemMessageTPage;
import net.simpleframework.module.msg.web.page.t2.AbstractMyMessagePage.MyNoticeMessagePage;
import net.simpleframework.module.msg.web.page.t2.AbstractMyMessagePage.MyPrivateMessageDraftPage;
import net.simpleframework.module.msg.web.page.t2.AbstractMyMessagePage.MyPrivateMessagePage;
import net.simpleframework.module.msg.web.page.t2.AbstractMyMessagePage.MyPrivateMessageSentPage;
import net.simpleframework.module.msg.web.page.t2.AbstractMyMessagePage.MySystemMessagePage;
import net.simpleframework.mvc.common.UrlsCache;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MessageUrlsFactory extends UrlsCache {

	public MessageUrlsFactory() {
		put(MyNoticeMessageTPage.class, MyNoticeMessagePage.class);
		put(MySystemMessageTPage.class, MySystemMessagePage.class);
		put(MyPrivateMessageTPage.class, MyPrivateMessagePage.class);
		put(MyPrivateMessageSentTPage.class, MyPrivateMessageSentPage.class);
		put(MyPrivateMessageDraftTPage.class, MyPrivateMessageDraftPage.class);
	}
}
