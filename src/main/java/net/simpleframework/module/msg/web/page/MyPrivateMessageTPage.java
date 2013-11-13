package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;

import net.simpleframework.module.msg.AbstractMessage;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.module.msg.web.plugin.PrivateMessagePlugin;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.ETextAlign;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.Icon;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.menu.MenuItems;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.window.WindowBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyPrivateMessageTPage extends AbstractMyMessageTPage {

	@Override
	protected PrivateMessagePlugin getMessagePlugin(final PageParameter pp) {
		return ((IMessageWebContext) context).getPrivateMessagePlugin();
	}

	@Override
	protected void addComponents(final PageParameter pp) {
		final TablePagerBean tablePager = addTablePagerBean(pp, PrivateMessageTbl.class);
		tablePager
				.addColumn(TablePagerColumn.ICON().setWidth(16))
				.addColumn(
						TablePagerColumn.col(COL_TOPIC, $m("AbstractMgrMessagePage.0"))
								.setTextAlign(ETextAlign.left).setSort(false))
				.addColumn(
						new TablePagerColumn(COL_FROMID, $m("MyPrivateMessageTPage.0"), 115)
								.setTextAlign(ETextAlign.left).setFilter(false))
				.addColumn(
						new TablePagerColumn(COL_CREATEDATE, $m("AbstractMyMessageTPage.1"), 115)
								.setPropertyClass(Date.class))
				.addColumn(TablePagerColumn.OPE().setWidth(80));
		addSentWindowComponent(pp);
		// 标记菜单
		createMarkMenuComponent(pp);
	}

	protected void addSentWindowComponent(final PageParameter pp) {
		addAjaxRequest(pp, "MyPrivateMessageTPage_sentPage", PrivateMessageSentPage.class);
		// sent window
		addComponentBean(pp, "MyPrivateMessageTPage_sentWin", WindowBean.class)
				.setContentRef("MyPrivateMessageTPage_sentPage")
				.setTitle($m("MyPrivateMessageTPage.1")).setWidth(640).setHeight(480);
	}

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		return ElementList.of(
				new LinkButton($m("MyPrivateMessageTPage.1")).setIconClass(Icon.envelope).setOnclick(
						"$Actions['MyPrivateMessageTPage_sentWin']();"), SpanElement.SPACE,
				createMarkMenuElement(), SpanElement.SPACE, createDeleteElement());
	}

	private static final MenuItems CONTEXT_MENUS = MenuItems
			.of()
			.append(
					MenuItem.of($m("MyPrivateMessageTPage.5")).setOnclick_act(
							"MyPrivateMessageTPage_sentWin", "msgId", "t=reply")).append(MenuItem.sep())
			.append(MenuItem.itemDelete().setOnclick_act("AbstractMyMessageTPage_delete", "id"));

	public static class PrivateMessageTbl extends MyMessageTbl {

		@Override
		public MenuItems getContextMenu(final ComponentParameter cp, final MenuBean menuBean,
				final MenuItem menuItem) {
			return menuItem == null ? CONTEXT_MENUS : null;
		}

		@Override
		protected ElementList createOPE(final ComponentParameter cp, final AbstractMessage msg) {
			return ElementList.of(new ButtonElement($m("MyPrivateMessageTPage.5"))
					.setOnclick("$Actions['MyPrivateMessageTPage_sentWin']('t=reply&msgId="
							+ msg.getId() + "');"), AbstractTablePagerSchema.IMG_DOWNMENU);
		}

		@Override
		protected AbstractElement<?> createFrom(final ComponentParameter cp, final AbstractMessage msg) {
			return new SpanElement(toIconUser(cp, msg.getFromId()));
		}
	}
}
