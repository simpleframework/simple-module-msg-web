package net.simpleframework.module.msg.web.page.t1;

import static net.simpleframework.common.I18n.$m;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.module.msg.AbstractMessage;
import net.simpleframework.module.msg.EMessageSendTo;
import net.simpleframework.module.msg.plugin.IMessagePlugin;
import net.simpleframework.module.msg.web.page.MailSentPage;
import net.simpleframework.mvc.PageMapping;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@PageMapping(url = "/msg/notice/email")
public class MgrNoticeMessage_EmailPage extends MgrNoticeMessagePage {

	@Override
	protected void addNoticeComponents(final PageParameter pp) {
		// 邮件发送
		addAjaxRequest(pp, "MgrNoticeMessage_EmailPage_sentPage", MailSentPage.class);
		addWindowBean(pp, "MgrNoticeMessage_EmailPage_sentWin")
				.setContentRef("MgrNoticeMessage_EmailPage_sentPage")
				.setTitle($m("MgrNoticeMessage_EmailPage.0")).setWidth(680).setHeight(420);
	}

	@Override
	protected TablePagerBean addTablePagerBean(final PageParameter pp) {
		return addTablePagerBean(pp, NoticeMessage_EmailTbl.class);
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		return ElementList.of(
				new LinkButton($m("MgrNoticeMessage_EmailPage.0"))
						.setOnclick("$Actions['MgrNoticeMessage_EmailPage_sentWin']();"),
				SpanElement.SPACE, createDeleteBtn());
	}

	public static class NoticeMessage_EmailTbl extends NoticeMessageTbl {

		@Override
		protected LinkElement toTopicElement(final ComponentParameter cp, final AbstractMessage msg) {
			final IMessagePlugin oMark = getMessageMark(cp);
			return new LinkElement(msg.getTopic())
					.setOnclick("$Actions['AbstractMessageMgrPage_viewWin']('logId=" + msg.getId()
							+ "&messageMark=" + oMark.getMark() + "');");
		}

		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			return messageContext.getP2PMessageLogService().queryMessages(EMessageSendTo.email);
		}
	}
}
