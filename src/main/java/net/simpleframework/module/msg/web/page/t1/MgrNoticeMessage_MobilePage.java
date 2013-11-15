package net.simpleframework.module.msg.web.page.t1;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.module.msg.EMessageSendTo;
import net.simpleframework.mvc.PageMapping;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@PageMapping(url = "/msg/notice/mobile")
public class MgrNoticeMessage_MobilePage extends MgrNoticeMessagePage {

	@Override
	protected TablePagerBean addTablePagerBean(final PageParameter pp) {
		return addTablePagerBean(pp, NoticeMessage_MobileTbl.class);
	}

	public static class NoticeMessage_MobileTbl extends NoticeMessageTbl {

		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			return context.getP2PMessageLogService().queryMessages(EMessageSendTo.mobile);
		}
	}
}
