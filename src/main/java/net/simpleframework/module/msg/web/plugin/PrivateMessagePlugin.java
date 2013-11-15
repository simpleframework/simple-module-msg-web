package net.simpleframework.module.msg.web.plugin;

import static net.simpleframework.common.I18n.$m;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.common.ID;
import net.simpleframework.module.common.plugin.AbstractModulePlugin;
import net.simpleframework.module.msg.IMessageContext;
import net.simpleframework.module.msg.IP2PMessageService;
import net.simpleframework.module.msg.impl.P2PMessageService;
import net.simpleframework.module.msg.plugin.AbstractP2PMessagePlugin;
import net.simpleframework.module.msg.plugin.IMessageCategoryPlugin;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.module.msg.web.page.MyPrivateMessageDraftTPage;
import net.simpleframework.module.msg.web.page.MyPrivateMessageSentTPage;
import net.simpleframework.module.msg.web.page.MyPrivateMessageTPage;
import net.simpleframework.module.msg.web.page.t1.MgrPrivateMessagePage;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.PageParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PrivateMessagePlugin extends AbstractP2PMessagePlugin implements IMessageCategoryUI {

	@Override
	public IP2PMessageService getMessageService() {
		return singleton(PrivateMessageService.class);
	}

	public static class PrivateMessageService extends P2PMessageService {
		@Override
		protected int getMark() {
			return IMessageContext.PRIVATEMESSAGE_MARK;
		}
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
	public String getIconClass() {
		return "img_private";
	}

	@Override
	public String getManagerPageUrl(final PageParameter pp) {
		return AbstractMVCPage.url(MgrPrivateMessagePage.class);
	}

	@Override
	public String getMyPageUrl(final PageParameter pp) {
		return ((IMessageWebContext) context).getUrlsFactory().getMyMessageUrl(
				MyPrivateMessageTPage.class);
	}

	public static PrivateMessageDraftCategory DRAFT_MODULE = new PrivateMessageDraftCategory();
	public static PrivateMessageSentCategory SENT_MODULE = new PrivateMessageSentCategory();

	private static Collection<IMessageCategoryPlugin> children;
	static {
		children = new ArrayList<IMessageCategoryPlugin>();
		children.add(SENT_MODULE);
		children.add(DRAFT_MODULE);
	}

	@Override
	public Collection<IMessageCategoryPlugin> getChildren() {
		return children;
	}

	public static class PrivateMessageDraftCategory extends _PrivateMessageCategory {

		@Override
		public String getMyPageUrl(final PageParameter pp) {
			return ((IMessageWebContext) context).getUrlsFactory().getMyMessageUrl(
					MyPrivateMessageDraftTPage.class);
		}

		@Override
		public String getIconClass() {
			return "img_private_draft";
		}

		@Override
		public String getText() {
			return $m("PrivateMessagePlugin.2");
		}
	}

	public static class PrivateMessageSentCategory extends _PrivateMessageCategory {
		@Override
		public String getMyPageUrl(final PageParameter pp) {
			return ((IMessageWebContext) context).getUrlsFactory().getMyMessageUrl(
					MyPrivateMessageSentTPage.class);
		}

		@Override
		public String getIconClass() {
			return "img_private_sent";
		}

		@Override
		public String getText() {
			return $m("PrivateMessagePlugin.1");
		}
	}

	private static abstract class _PrivateMessageCategory extends AbstractModulePlugin implements
			IMessageCategoryPlugin, IMessageCategoryUI {
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
