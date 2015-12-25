package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.module.msg.AbstractMessage;
import net.simpleframework.module.msg.IP2PMessageService;
import net.simpleframework.module.msg.plugin.IMessagePlugin;
import net.simpleframework.module.msg.web.plugin.PrivateMessagePlugin;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MyPrivateMessageDraftTPage extends MyPrivateMessageTPage {

	@Override
	protected void addMessageComponents(final PageParameter pp) {
		final TablePagerBean tablePager = addTablePagerBean(pp, PrivateMessageDraftTbl.class);
		tablePager.addColumn(TC_TOPIC()).addColumn(TC_USERID())
				.addColumn(TC_CREATEDATE().setColumnText($m("MyPrivateMessageDraftTPage.0")))
				.addColumn(TablePagerColumn.OPE(70));
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
			return ((IP2PMessageService) oMark.getMessageService()).queryFromMessages(cp.getLoginId(),
					null, PrivateMessagePlugin.DRAFT_MODULE.getName());
		}

		@Override
		protected LinkElement toTopicElement(final ComponentParameter cp, final AbstractMessage msg) {
			return new LinkElement(msg.getTopic())
					.setOnclick("$Actions['MyPrivateMessageTPage_sentWin']('msgId=" + msg.getId()
							+ "');");
		}
	}
}
