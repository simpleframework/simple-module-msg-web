package net.simpleframework.module.msg.web.plugin;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.ID;
import net.simpleframework.module.msg.IMessageContext;
import net.simpleframework.module.msg.IP2PMessageService;
import net.simpleframework.module.msg.impl.P2PMessageService;
import net.simpleframework.module.msg.plugin.AbstractP2PMessagePlugin;
import net.simpleframework.module.msg.plugin.IMessageCategory.AbstractMessageCategory;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.module.msg.web.page.AbstractMyMessageTPage;
import net.simpleframework.module.msg.web.page.MyPrivateMessageDraftTPage;
import net.simpleframework.module.msg.web.page.MyPrivateMessageSentTPage;
import net.simpleframework.module.msg.web.page.MyPrivateMessageTPage;
import net.simpleframework.module.msg.web.page.t1.MgrPrivateMessagePage;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.PageParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PrivateMessagePlugin extends AbstractP2PMessagePlugin implements IMessageUI {

	public static class PrivateMessageService extends P2PMessageService {
		@Override
		protected int getMark() {
			return IMessageContext.PRIVATEMESSAGE_MARK;
		}
	}

	@Override
	public IP2PMessageService getMessageService() {
		return singleton(PrivateMessageService.class);
	}

	@Override
	public int getMark() {
		return IMessageContext.PRIVATEMESSAGE_MARK;
	}

	@Override
	public String getText() {
		return $m("PrivateMessagePlugin.0");
	}

	@Override
	public String getIconClass(final PageParameter pp) {
		return pp.getCssResourceHomePath(AbstractMyMessageTPage.class) + "/images/private.png";
	}

	@Override
	public String getManagerPageUrl(final PageParameter pp) {
		return AbstractMVCPage.url(MgrPrivateMessagePage.class);
	}

	@Override
	public String getMyPageUrl(final PageParameter pp) {
		return ((IMessageWebContext) messageContext).getUrlsFactory().getUrl(pp,
				MyPrivateMessageTPage.class);
	}

	public static PrivateMessageDraftCategory DRAFT_MODULE = new PrivateMessageDraftCategory();
	public static PrivateMessageSentCategory SENT_MODULE = new PrivateMessageSentCategory();

	{
		registMessageCategory(SENT_MODULE);
		registMessageCategory(DRAFT_MODULE);
	}

	public static class PrivateMessageDraftCategory extends _PrivateMessageCategory {

		@Override
		public String getMyPageUrl(final PageParameter pp) {
			return ((IMessageWebContext) messageContext).getUrlsFactory().getUrl(pp,
					MyPrivateMessageDraftTPage.class);
		}

		@Override
		public String getName() {
			return "PRIVATEMESSAGE_DRAFT";
		}

		@Override
		public String getIconClass(final PageParameter pp) {
			return pp.getCssResourceHomePath(AbstractMyMessageTPage.class) + "/images/draft.png";
		}

		@Override
		public String toString() {
			return $m("PrivateMessagePlugin.2");
		}
	}

	public static class PrivateMessageSentCategory extends _PrivateMessageCategory {
		@Override
		public String getMyPageUrl(final PageParameter pp) {
			return ((IMessageWebContext) messageContext).getUrlsFactory().getUrl(pp,
					MyPrivateMessageSentTPage.class);
		}

		@Override
		public String getName() {
			return "PRIVATEMESSAGE_SENT";
		}

		@Override
		public String getIconClass(final PageParameter pp) {
			return pp.getCssResourceHomePath(AbstractMyMessageTPage.class) + "/images/sent.png";
		}

		@Override
		public String toString() {
			return $m("PrivateMessagePlugin.1");
		}
	}

	private static abstract class _PrivateMessageCategory extends AbstractMessageCategory implements
			IMessageUI {
		@Override
		public String getManagerPageUrl(final PageParameter pp) {
			return null;
		}

		@Override
		public Object getFrom(final ID fromId) {
			return null;
		}
	}
}
