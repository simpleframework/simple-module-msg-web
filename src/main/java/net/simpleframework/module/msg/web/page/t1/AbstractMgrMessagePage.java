package net.simpleframework.module.msg.web.page.t1;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.module.common.plugin.IModulePlugin;
import net.simpleframework.module.msg.AbstractMessage;
import net.simpleframework.module.msg.IMessageContextAware;
import net.simpleframework.module.msg.plugin.IMessagePlugin;
import net.simpleframework.module.msg.web.page.AbstractMyMessageTPage;
import net.simpleframework.module.msg.web.page.MessageViewPage;
import net.simpleframework.module.msg.web.plugin.IMessageUI;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.common.element.TabButton;
import net.simpleframework.mvc.common.element.TabButtons;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.EPagerBarLayout;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.db.GroupDbTablePagerHandler;
import net.simpleframework.mvc.template.t1.T1ResizedTemplatePage;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractMgrMessagePage extends T1ResizedTemplatePage implements
		IMessageContextAware {

	@Override
	protected void onForward(final PageParameter pp) throws Exception {
		super.onForward(pp);

		pp.addImportCSS(AbstractMyMessageTPage.class, "/mgr_message.css");

		// delete
		addDeleteAjaxRequest(pp, "AbstractMessageMgrPage_delete");

		// msg win
		addAjaxRequest(pp, "AbstractMessageMgrPage_viewPage", MessageViewPage.class);
		addWindowBean(pp, "AbstractMessageMgrPage_viewWin")
				.setContentRef("AbstractMessageMgrPage_viewPage")
				.setTitle($m("AbstractMyMessageTPage.0")).setHeight(410).setWidth(640);
	}

	@Override
	public String getPageRole(final PageParameter pp) {
		return messageContext.getModule().getManagerRole();
	}

	protected TablePagerColumn createUserColumn(final PageParameter pp, final String columnName,
			final String columnText) {
		return super.createUserColumn(pp, columnName, columnText, "AbstractMessageMgrPage_tbl");
	}

	protected TablePagerBean addTablePagerBean(final PageParameter pp,
			final Class<? extends MgrMessageTbl> tblClass) {
		return (TablePagerBean) addTablePagerBean(pp, "AbstractMessageMgrPage_tbl")
				.setShowLineNo(true).setPagerBarLayout(EPagerBarLayout.bottom)
				.setContainerId("tbl_" + hashId).setHandlerClass(tblClass);
	}

	protected abstract IMessagePlugin getMessageMark(PageParameter pp);

	public IForward doDelete(final ComponentParameter cp) {
		final Object[] ids = StringUtils.split(cp.getParameter("id"));
		getMessageMark(cp).getMessageService().delete(ids);
		return new JavascriptForward("$Actions['AbstractMessageMgrPage_tbl']();");
	}

	@Override
	public TabButtons getTabButtons(final PageParameter pp) {
		final TabButtons tabs = TabButtons.of();
		for (final IModulePlugin messageMark : messageContext.getPluginRegistry().allPlugin()) {
			tabs.append(new TabButton(messageMark.getText(), ((IMessageUI) messageMark)
					.getManagerPageUrl(pp)));
		}
		return tabs;
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		return ElementList.of(createDeleteBtn());
	}

	protected LinkButton createDeleteBtn() {
		return LinkButton.deleteBtn().setOnclick(
				"$Actions['AbstractMessageMgrPage_tbl'].doAct('AbstractMessageMgrPage_delete');");
	}

	@Override
	protected String toHtml(final PageParameter pp, final Map<String, Object> variables,
			final String currentVariable) throws IOException {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='AbstractMgrMessagePage'>");
		sb.append("  <div id='tbl_").append(hashId).append("'></div>");
		sb.append("</div>");
		return sb.toString();
	}

	public static class MgrMessageTbl extends GroupDbTablePagerHandler {
		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			return getMessageMark(cp).getMessageService().queryMessages();
		}

		protected IMessagePlugin getMessageMark(final ComponentParameter cp) {
			return ((AbstractMgrMessagePage) get(cp)).getMessageMark(cp);
		}

		protected LinkElement toTopicElement(final ComponentParameter cp, final AbstractMessage msg) {
			final IMessagePlugin oMark = getMessageMark(cp);
			return new LinkElement(msg.getTopic())
					.setOnclick("$Actions['AbstractMessageMgrPage_viewWin']('msgId=" + msg.getId()
							+ "&messageMark=" + oMark.getMark() + "');");
		}

		protected String toOpeHTML(final ComponentParameter cp, final AbstractMessage msg) {
			return ButtonElement.deleteBtn()
					.setOnclick("$Actions['AbstractMessageMgrPage_delete']('id=" + msg.getId() + "');")
					.toString();
		}

		@Override
		protected Map<String, Object> getRowData(final ComponentParameter cp, final Object dataObject) {
			final AbstractMessage msg = (AbstractMessage) dataObject;
			final KVMap kv = new KVMap();
			kv.add("topic", toTopicElement(cp, msg));
			kv.add("createDate", msg.getCreateDate());
			kv.put(TablePagerColumn.OPE, toOpeHTML(cp, msg));
			return kv;
		}
	}

	static final TablePagerColumn TC_TOPIC() {
		return AbstractMyMessageTPage.TC_TOPIC();
	}

	static final TablePagerColumn TC_CREATEDATE() {
		return AbstractMyMessageTPage.TC_CREATEDATE();
	}

	static final TablePagerColumn TC_CATEGORY() {
		return new TablePagerColumn("category", $m("AbstractMyMessageTPage.7"), 120);
	}
}
