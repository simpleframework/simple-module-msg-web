package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.module.msg.AbstractMessage;
import net.simpleframework.module.msg.IP2PMessageService;
import net.simpleframework.module.msg.plugin.IMessagePlugin;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ETextAlign;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyPrivateMessageDraftTPage extends MyPrivateMessageTPage {

	@Override
	protected void addComponents(final PageParameter pp) {
		final TablePagerBean tablePager = addTablePagerBean(pp, PrivateMessageDraftTbl.class);
		tablePager
				.addColumn(
						TablePagerColumn.col(COL_TOPIC, $m("AbstractMgrMessagePage.0"))
								.setTextAlign(ETextAlign.left).setSort(false))
				.addColumn(
						new TablePagerColumn(COL_USERID, $m("MyPrivateMessageSentTPage.0"), 115)
								.setTextAlign(ETextAlign.left).setFilter(false))
				.addColumn(
						new TablePagerColumn(COL_CREATEDATE, $m("MyPrivateMessageDraftTPage.0"), 115)
								.setPropertyClass(Date.class))
				.addColumn(TablePagerColumn.OPE().setWidth(80));
		addSentWindowComponent(pp);
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		return null;
	}

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		return ElementList.of(createDeleteElement());
	}

	public static class PrivateMessageDraftTbl extends MyMessageTbl {
		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			final IMessagePlugin oMark = getMessageMark(cp);
			return ((IP2PMessageService) oMark.getMessageService()).queryDraftMessages(
					oMark.getMark(), cp.getLoginId());
		}

		@Override
		protected LinkElement createTopic(final ComponentParameter cp, final AbstractMessage msg) {
			return new LinkElement(msg.getTopic())
					.setOnclick("$Actions['MyPrivateMessageTPage_sentWin']('msgId=" + msg.getId()
							+ "');");
		}
	}
}
