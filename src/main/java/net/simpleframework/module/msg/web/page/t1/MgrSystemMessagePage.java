package net.simpleframework.module.msg.web.page.t1;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.module.msg.web.plugin.SystemMessagePlugin;
import net.simpleframework.mvc.PageMapping;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.Icon;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@PageMapping(url = "/msg/system/mgr")
public class MgrSystemMessagePage extends AbstractMgrMessagePage {

	@Override
	protected void onForward(final PageParameter pp) throws Exception {
		super.onForward(pp);

		final TablePagerBean tablePager = addTablePagerBean(pp, MgrMessageTbl.class);
		tablePager
				.addColumn(
						new TablePagerColumn(COL_TOPIC, $m("AbstractMgrMessagePage.0")).setSort(false))
				.addColumn(TablePagerColumn.DATE(COL_CREATEDATE, $m("AbstractMgrMessagePage.1")))
				.addColumn(TablePagerColumn.OPE(80));

		final AjaxRequestBean ajaxRequest = addAjaxRequest(pp, "MgrSystemMessagePage_publishPage",
				SystemMessagePublishPage.class);
		// publish window
		addWindowBean(pp, "MgrSystemMessagePage_publishWin", ajaxRequest)
				.setTitle($m("MgrSystemMessagePage.0")).setWidth(560).setHeight(320);
	}

	@Override
	protected SystemMessagePlugin getMessageMark(final PageParameter pp) {
		return ((IMessageWebContext) messageContext).getSystemMessagePlugin();
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		final ElementList btns = super.getLeftElements(pp);
		btns.add(0, SpanElement.SPACE);
		btns.add(0, new LinkButton($m("MgrSystemMessagePage.0")).setIconClass(Icon.comment)
				.setOnclick("$Actions['MgrSystemMessagePage_publishWin']();"));
		return btns;
	}
}
