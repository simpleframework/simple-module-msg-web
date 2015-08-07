package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.module.msg.AbstractMessage;
import net.simpleframework.module.msg.IP2PMessageService;
import net.simpleframework.module.msg.P2PMessage;
import net.simpleframework.module.msg.plugin.IMessagePlugin;
import net.simpleframework.module.msg.web.plugin.PrivateMessagePlugin;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MyPrivateMessageSentTPage extends MyPrivateMessageTPage {

	@Override
	protected void addMessageComponents(final PageParameter pp) {
		final TablePagerBean tablePager = addTablePagerBean(pp, PrivateMessageSentTbl.class);
		tablePager
				.addColumn(
						new TablePagerColumn(COL_TOPIC, $m("AbstractMgrMessagePage.0")).setSort(false))
				.addColumn(
						new TablePagerColumn(COL_USERID, $m("MyPrivateMessageSentTPage.0"), 115)
								.setFilter(false))
				.addColumn(TablePagerColumn.DATE(COL_CREATEDATE, $m("AbstractMyMessageTPage.1")))
				.addColumn(TablePagerColumn.OPE(80));

		final AjaxRequestBean ajaxRequest = addAjaxRequest(pp, "MyPrivateMessageSentTPage_viewPage",
				PrivateMessageSentViewPage.class);
		// sent window
		addWindowBean(pp, "MyPrivateMessageSentTPage_viewWin", ajaxRequest)
				.setTitle($m("MyPrivateMessageSentTPage.1")).setWidth(600).setHeight(360);
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		return null;
	}

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		return ElementList.of(createDeleteElement());
	}

	public static class PrivateMessageSentTbl extends MyMessageTbl {
		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			final IMessagePlugin oMark = getMessageMark(cp);
			return ((IP2PMessageService) oMark.getMessageService()).queryFromMessages(cp.getLoginId(),
					null, PrivateMessagePlugin.SENT_MODULE.getName());
		}

		@Override
		protected AbstractElement<?> createUser(final ComponentParameter cp, final AbstractMessage msg) {
			return new SpanElement(((P2PMessage) msg).getToUsers());
		}

		@Override
		protected LinkElement createTopic(final ComponentParameter cp, final AbstractMessage msg) {
			return new LinkElement(msg.getTopic())
					.setOnclick("$Actions['MyPrivateMessageSentTPage_viewWin']('msgId=" + msg.getId()
							+ "');");
		}
	}
}
