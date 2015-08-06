package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.module.msg.web.plugin.SystemMessagePlugin;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MySystemMessageTPage extends AbstractMyMessageTPage {

	@Override
	protected SystemMessagePlugin getMessagePlugin(final PageParameter pp) {
		return ((IMessageWebContext) messageContext).getSystemMessagePlugin();
	}

	@Override
	protected void onForward(final PageParameter pp) throws Exception {
		super.onForward(pp);

		final TablePagerBean tablePager = addTablePagerBean(pp, MyMessageTbl.class);
		tablePager
				.addColumn(TablePagerColumn.ICON().setWidth(16))
				.addColumn(
						new TablePagerColumn(COL_TOPIC, $m("AbstractMgrMessagePage.0")).setSort(false))
				.addColumn(TablePagerColumn.DATE(COL_CREATEDATE, $m("AbstractMyMessageTPage.1")))
				.addColumn(TablePagerColumn.OPE().setWidth(80));

		// 标记菜单
		createMarkMenuComponent(pp);
	}

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		return ElementList.of(createMarkMenuElement(), SpanElement.SPACE, createDeleteElement());
	}
}
