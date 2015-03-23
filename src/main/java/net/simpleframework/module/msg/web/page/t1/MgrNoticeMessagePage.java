package net.simpleframework.module.msg.web.page.t1;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;
import java.util.Map;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.module.msg.AbstractP2PMessage;
import net.simpleframework.module.msg.EMessageSendTo;
import net.simpleframework.module.msg.plugin.IMessageCategory;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.module.msg.web.plugin.NoticeMessageWebPlugin;
import net.simpleframework.mvc.PageMapping;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.Option;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.common.element.TabButton;
import net.simpleframework.mvc.common.element.TabButtons;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumns;
import net.simpleframework.mvc.template.TemplateUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@PageMapping(url = "/msg/notice/mgr")
public class MgrNoticeMessagePage extends AbstractMgrMessagePage {
	public static final String COL_CATEGORY = "category";

	public static final String COL_USERID = "userId";

	@Override
	protected void onForward(final PageParameter pp) {
		super.onForward(pp);

		final TablePagerBean tablePager = addTablePagerBean(pp);
		tablePager
				.addColumn(
						TablePagerColumn.col(COL_TOPIC, $m("AbstractMgrMessagePage.0")).setSort(false))
				.addColumn(
						createUserColumn(pp, COL_USERID, $m("AbstractMyMessageTPage.2"),
								"AbstractMessageMgrPage_tbl").setWidth(80))
				.addColumn(
						new TablePagerColumn(COL_CREATEDATE, $m("AbstractMyMessageTPage.1"), 120)
								.setPropertyClass(Date.class))
				.addColumn(new TablePagerColumn(COL_CATEGORY, $m("AbstractMyMessageTPage.7"), 120))
				.addColumn(TablePagerColumn.OPE().setWidth(80));

		// 用户选择
		addUserSelectForTbl(pp, "AbstractMessageMgrPage_tbl");

		addNoticeComponents(pp);
	}

	protected TablePagerBean addTablePagerBean(final PageParameter pp) {
		return addTablePagerBean(pp, NoticeMessageTbl.class);
	}

	protected void addNoticeComponents(final PageParameter pp) {
		// 通知内容
		addAjaxRequest(pp, "MgrNoticeMessagePage_contentPage", NoticeMessageContentPage.class);
		addWindowBean(pp, "MgrNoticeMessagePage_contentWin")
				.setContentRef("MgrNoticeMessagePage_contentPage")
				.setTitle($m("MgrNoticeMessagePage.1")).setHeight(450).setWidth(710);
	}

	@Override
	protected NoticeMessageWebPlugin getMessageMark(final PageParameter pp) {
		return (NoticeMessageWebPlugin) ((IMessageWebContext) messageContext)
				.getNoticeMessagePlugin();
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		return ElementList.of(new LinkButton($m("MgrNoticeMessagePage.1"))
				.setOnclick("$Actions['MgrNoticeMessagePage_contentWin']();"), SpanElement.SPACE,
				createDeleteBtn());
	}

	private static Option OPTION_CATEGORY = new Option(COL_CATEGORY, $m("MgrNoticeMessagePage.0"));

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		pp.putParameter(G, COL_CATEGORY);
		return ElementList.of(
				createTabsElement(pp, TabButtons.of(new TabButton($m("NoticeMessageContentPage.4"),
						url(MgrNoticeMessagePage.class)), new TabButton(EMessageSendTo.email,
						url(MgrNoticeMessage_EmailPage.class)), new TabButton(EMessageSendTo.mobile,
						url(MgrNoticeMessage_MobilePage.class)))),
				createGroupElement(pp, "AbstractMessageMgrPage_tbl", OPTION_CATEGORY));
	}

	public static class NoticeMessageTbl extends MgrMessageTbl {

		@Override
		public Object getGroupValue(final ComponentParameter cp, final Object bean,
				final String groupColumn) {
			final AbstractP2PMessage msg = (AbstractP2PMessage) bean;
			final IMessageCategory mCategory = getMessageMark(cp)
					.getMessageCategory(msg.getCategory());
			if (mCategory != null) {
				return mCategory.toString();
			}
			return super.getGroupValue(cp, bean, groupColumn);
		}

		@Override
		public AbstractTablePagerSchema createTablePagerSchema() {
			return new DefaultDbTablePagerSchema() {
				@Override
				public TablePagerColumns getTablePagerColumns(final ComponentParameter cp) {
					final TablePagerColumns columns = super.getTablePagerColumns(cp);
					final String g = cp.getParameter(G);
					columns.get(COL_CATEGORY).setVisible(!COL_CATEGORY.equals(g));
					return columns;
				}

				@Override
				public Map<String, Object> getRowData(final ComponentParameter cp,
						final Object dataObject) {
					final AbstractP2PMessage msg = (AbstractP2PMessage) dataObject;
					final KVMap kv = new KVMap();
					kv.add(COL_TOPIC, createTopic(cp, msg));
					kv.add(COL_USERID, TemplateUtils.toIconUser(cp, msg.getUserId()));
					kv.add(COL_CREATEDATE, msg.getCreateDate());
					final IMessageCategory mCategory = getMessageMark(cp).getMessageCategory(
							msg.getCategory());
					if (mCategory != null) {
						kv.add(COL_CATEGORY, mCategory.toString());
					}
					kv.put(TablePagerColumn.OPE, createOPE(cp, msg));
					return kv;
				}
			};
		};
	}
}
