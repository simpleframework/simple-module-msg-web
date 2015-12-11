package net.simpleframework.module.msg.web.plugin;

import net.simpleframework.module.msg.plugin.NoticeMessagePlugin;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.module.msg.web.page.AbstractMyMessageTPage;
import net.simpleframework.module.msg.web.page.MyNoticeMessageTPage;
import net.simpleframework.module.msg.web.page.t1.MgrNoticeMessagePage;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.PageParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NoticeMessageWebPlugin extends NoticeMessagePlugin implements IMessageUI {

	@Override
	public String getIconClass(final PageParameter pp) {
		return pp.getCssResourceHomePath(AbstractMyMessageTPage.class) + "/images/notice.png";
	}

	@Override
	public String getMyPageUrl(final PageParameter pp) {
		return ((IMessageWebContext) messageContext).getUrlsFactory().getUrl(pp,
				MyNoticeMessageTPage.class);
	}

	@Override
	public String getManagerPageUrl(final PageParameter pp) {
		return AbstractMVCPage.url(MgrNoticeMessagePage.class);
	}
}
