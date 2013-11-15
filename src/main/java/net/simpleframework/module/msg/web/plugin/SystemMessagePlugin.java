package net.simpleframework.module.msg.web.plugin;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.module.msg.IMessageContext;
import net.simpleframework.module.msg.ISubscribeMessageService;
import net.simpleframework.module.msg.SubscribeMessage;
import net.simpleframework.module.msg.impl.SubscribeMessageService;
import net.simpleframework.module.msg.plugin.AbstractSubscribeMessagePlugin;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.module.msg.web.page.MySystemMessageTPage;
import net.simpleframework.module.msg.web.page.t1.MgrSystemMessagePage;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.PageParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class SystemMessagePlugin extends AbstractSubscribeMessagePlugin implements
		IMessageCategoryUI {

	@Override
	public ISubscribeMessageService getMessageService() {
		return singleton(SystemMessageService.class);
	}

	public static class SystemMessageService extends SubscribeMessageService {
		@Override
		protected int getMark() {
			return IMessageContext.SYSTEMMESSAGE_MARK;
		}
	}

	@Override
	public int getMark() {
		return IMessageContext.SYSTEMMESSAGE_MARK;
	}

	/**
	 * 发送系统消息
	 * 
	 * @param topic
	 * @param content
	 * @return
	 */
	public SubscribeMessage sentSystemMessage(final String topic, final String content) {
		return sentMessage(null, topic, content, 0);
	}

	@Override
	public String getText() {
		return $m("SystemMessagePlugin.0");
	}

	@Override
	public String getIconClass() {
		return "img_system";
	}

	@Override
	public String getMyPageUrl(final PageParameter pp) {
		return ((IMessageWebContext) context).getUrlsFactory().getMyMessageUrl(
				MySystemMessageTPage.class);
	}

	@Override
	public String getManagerPageUrl(final PageParameter pp) {
		return AbstractMVCPage.url(MgrSystemMessagePage.class);
	}
}
