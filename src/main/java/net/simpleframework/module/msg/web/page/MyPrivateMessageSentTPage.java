package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.module.msg.AbstractMessage;
import net.simpleframework.module.msg.IP2PMessageService;
import net.simpleframework.module.msg.P2PMessage;
import net.simpleframework.module.msg.plugin.IMessagePlugin;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.ETextAlign;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.window.WindowBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MyPrivateMessageSentTPage extends MyPrivateMessageTPage {

	@Override
	protected void addComponents(final PageParameter pp) {
		final TablePagerBean tablePager = addTablePagerBean(pp, PrivateMessageSentTbl.class);
		tablePager
				.addColumn(
						TablePagerColumn.col(COL_TOPIC, $m("AbstractMgrMessagePage.0"))
								.setTextAlign(ETextAlign.left).setSort(false))
				.addColumn(
						new TablePagerColumn(COL_USERID, $m("MyPrivateMessageSentTPage.0"), 115)
								.setTextAlign(ETextAlign.left).setFilter(false))
				.addColumn(
						new TablePagerColumn(COL_CREATEDATE, $m("AbstractMyMessageTPage.1"), 115)
								.setPropertyClass(Date.class))
				.addColumn(TablePagerColumn.OPE().setWidth(80));

		addAjaxRequest(pp, "MyPrivateMessageSentTPage_viewPage", PrivateMessageSentViewPage.class);
		// sent window
		addComponentBean(pp, "MyPrivateMessageSentTPage_viewWin", WindowBean.class)
				.setContentRef("MyPrivateMessageSentTPage_viewPage")
				.setTitle($m("MyPrivateMessageSentTPage.1")).setWidth(540).setHeight(360);
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
			return ((IP2PMessageService) oMark.getMessageService()).querySentMessages(oMark.getMark(),
					cp.getLoginId());
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
