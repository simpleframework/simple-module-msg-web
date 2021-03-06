package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;

import net.simpleframework.module.msg.AbstractMessage;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.module.msg.web.plugin.PrivateMessagePlugin;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.Icon;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.menu.MenuItems;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.template.TemplateUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MyPrivateMessageTPage extends AbstractMyMessageTPage {

	@Override
	protected PrivateMessagePlugin getMessagePlugin(final PageParameter pp) {
		return ((IMessageWebContext) messageContext).getPrivateMessagePlugin();
	}

	@Override
	protected void addMessageComponents(final PageParameter pp) {
		final TablePagerBean tablePager = addTablePagerBean(pp, PrivateMessageTbl.class);
		tablePager.addColumn(TablePagerColumn.ICON().setWidth(16)).addColumn(TC_TOPIC())
				.addColumn(TC_FROMID()).addColumn(TC_CREATEDATE()).addColumn(TablePagerColumn.OPE(70));
		addSentWindowComponent(pp);
		// 标记菜单
		createMarkMenuComponent(pp);
	}

	protected void addSentWindowComponent(final PageParameter pp) {
		final AjaxRequestBean ajaxRequest = addAjaxRequest(pp, "MyPrivateMessageTPage_sentPage",
				PrivateMessageSentPage.class);
		// sent window
		addWindowBean(pp, "MyPrivateMessageTPage_sentWin", ajaxRequest)
				.setTitle($m("MyPrivateMessageTPage.1")).setWidth(500).setHeight(540);
	}

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		return ElementList.of(
				new LinkButton($m("MyPrivateMessageTPage.1")).setIconClass(Icon.envelope)
						.setOnclick("$Actions['MyPrivateMessageTPage_sentWin']();"),
				SpanElement.SPACE, createMarkMenuElement(), SpanElement.SPACE, createDeleteElement());
	}

	private static final MenuItems CONTEXT_MENUS = MenuItems.of()
			.append(MenuItem.of($m("MyPrivateMessageTPage.5"))
					.setOnclick_act("MyPrivateMessageTPage_sentWin", "msgId", "t=reply"))
			.append(MenuItem.sep())
			.append(MenuItem.itemDelete().setOnclick_act("AbstractMyMessageTPage_delete", "id"));

	public static class PrivateMessageTbl extends MyMessageTbl {

		@Override
		public MenuItems getContextMenu(final ComponentParameter cp, final MenuBean menuBean,
				final MenuItem menuItem) {
			return menuItem == null ? CONTEXT_MENUS : null;
		}

		@Override
		protected String toFromHTML(final ComponentParameter cp, final AbstractMessage msg) {
			return TemplateUtils.toIconUser(cp, msg.getFromId());
		}

		@Override
		protected String toOpeHTML(final ComponentParameter cp, final AbstractMessage msg) {
			final StringBuilder sb = new StringBuilder();
			sb.append(new ButtonElement($m("MyPrivateMessageTPage.5")).setOnclick(
					"$Actions['MyPrivateMessageTPage_sentWin']('t=reply&msgId=" + msg.getId() + "');"));
			sb.append(AbstractTablePagerSchema.IMG_DOWNMENU);
			return sb.toString();
		}
	}
}
