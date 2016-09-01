package net.simpleframework.module.msg.web.page.t1;

import static net.simpleframework.common.I18n.$m;

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

	@Override
	protected void onForward(final PageParameter pp) throws Exception {
		super.onForward(pp);

		final TablePagerBean tablePager = addTablePagerBean(pp);
		tablePager.addColumn(TC_TOPIC())
				.addColumn(createUserColumn(pp, "userId", $m("AbstractMyMessageTPage.2")).setWidth(80))
				.addColumn(TC_CREATEDATE()).addColumn(TC_CATEGORY())
				.addColumn(TablePagerColumn.OPE(80));

		// 用户选择
		addUserSelectForTbl(pp, "AbstractMessageMgrPage_tbl", "userId");

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
		return ElementList.of(
				new LinkButton($m("MgrNoticeMessagePage.1"))
						.setOnclick("$Actions['MgrNoticeMessagePage_contentWin']();"),
				SpanElement.SPACE, createDeleteBtn());
	}

	private static Option OPTION_CATEGORY = new Option("category", $m("MgrNoticeMessagePage.0"));

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		pp.putParameter(G, "category");
		return ElementList.of(
				createTabsElement(pp, TabButtons.of(
						new TabButton($m("NoticeMessageContentPage.4"), url(MgrNoticeMessagePage.class)),
						new TabButton(EMessageSendTo.email, url(MgrNoticeMessage_EmailPage.class)),
						new TabButton(EMessageSendTo.mobile, url(MgrNoticeMessage_MobilePage.class)))),
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
					columns.get("category").setVisible(!"category".equals(g));
					return columns;
				}

				@Override
				public Map<String, Object> getRowData(final ComponentParameter cp,
						final Object dataObject) {
					final AbstractP2PMessage msg = (AbstractP2PMessage) dataObject;
					final KVMap kv = new KVMap();
					kv.add("topic", toTopicElement(cp, msg));
					kv.add("userId", TemplateUtils.toIconUser(cp, msg.getUserId()));
					kv.add("createDate", msg.getCreateDate());
					final IMessageCategory mCategory = getMessageMark(cp)
							.getMessageCategory(msg.getCategory());
					if (mCategory != null) {
						kv.add("category", mCategory);
					}
					kv.put(TablePagerColumn.OPE, toOpeHTML(cp, msg));
					return kv;
				}
			};
		};
	}
}
