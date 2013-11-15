package net.simpleframework.module.msg.web.page.t2;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.ctx.permission.IPermissionConst;
import net.simpleframework.module.msg.web.page.AbstractMyMessageTPage;
import net.simpleframework.module.msg.web.page.MyNoticeMessageTPage;
import net.simpleframework.module.msg.web.page.MyPrivateMessageDraftTPage;
import net.simpleframework.module.msg.web.page.MyPrivateMessageSentTPage;
import net.simpleframework.module.msg.web.page.MyPrivateMessageTPage;
import net.simpleframework.module.msg.web.page.MySystemMessageTPage;
import net.simpleframework.mvc.PageMapping;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.template.struct.NavigationButtons;
import net.simpleframework.mvc.template.t2.T2TemplatePage;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractMyMessagePage extends T2TemplatePage {

	@Override
	public String getRole(final PageParameter pp) {
		return IPermissionConst.ROLE_ALL_ACCOUNT;
	}

	protected abstract Class<? extends AbstractMyMessageTPage> getMessageTPageClass();

	@Override
	protected String toHtml(final PageParameter pp, final Map<String, Object> variables,
			final String currentVariable) throws IOException {
		return pp.includeUrl(getMessageTPageClass());
	}

	@Override
	public NavigationButtons getNavigationBar(final PageParameter pp) {
		return super.getNavigationBar(pp).appendAll(
				singleton(getMessageTPageClass()).getNavigationBar(pp));
	}

	@PageMapping(url = "/msg/notice/my")
	public static class MyNoticeMessagePage extends AbstractMyMessagePage {

		@Override
		protected Class<? extends AbstractMyMessageTPage> getMessageTPageClass() {
			return MyNoticeMessageTPage.class;
		}
	}

	@PageMapping(url = "/msg/private/my/draft")
	public static class MyPrivateMessageDraftPage extends AbstractMyMessagePage {

		@Override
		protected Class<? extends AbstractMyMessageTPage> getMessageTPageClass() {
			return MyPrivateMessageDraftTPage.class;
		}
	}

	@PageMapping(url = "/msg/private/my")
	public static class MyPrivateMessagePage extends AbstractMyMessagePage {

		@Override
		protected Class<? extends AbstractMyMessageTPage> getMessageTPageClass() {
			return MyPrivateMessageTPage.class;
		}
	}

	@PageMapping(url = "/msg/private/my/sent")
	public static class MyPrivateMessageSentPage extends AbstractMyMessagePage {

		@Override
		protected Class<? extends AbstractMyMessageTPage> getMessageTPageClass() {
			return MyPrivateMessageSentTPage.class;
		}
	}

	@PageMapping(url = "/msg/system/my")
	public static class MySystemMessagePage extends AbstractMyMessagePage {

		@Override
		protected Class<? extends AbstractMyMessageTPage> getMessageTPageClass() {
			return MySystemMessageTPage.class;
		}
	}
}
