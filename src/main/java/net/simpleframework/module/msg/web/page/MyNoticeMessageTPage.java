package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;

import net.simpleframework.module.common.bean.CategoryStat;
import net.simpleframework.module.msg.AbstractMessage;
import net.simpleframework.module.msg.plugin.IMessageCategory;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.module.msg.web.plugin.NoticeMessageWebPlugin;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.EVerticalAlign;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkElementEx;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.common.element.SupElement;
import net.simpleframework.mvc.common.element.TagElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MyNoticeMessageTPage extends AbstractMyMessageTPage {

	@Override
	protected NoticeMessageWebPlugin getMessagePlugin(final PageParameter pp) {
		return (NoticeMessageWebPlugin) messageContext.getNoticeMessagePlugin();
	}

	@Override
	protected void onForward(final PageParameter pp) {
		super.onForward(pp);

		final TablePagerBean tablePager = addTablePagerBean(pp, NoticeMessageTbl.class);
		tablePager
				.addColumn(TablePagerColumn.ICON().setWidth(16))
				.addColumn(
						TablePagerColumn.col(COL_TOPIC, $m("AbstractMgrMessagePage.0")).setSort(false))
				.addColumn(
						new TablePagerColumn(COL_CATEGORY, $m("AbstractMyMessageTPage.7")).setWidth(115)
								.setFilterSort(false))
				.addColumn(
						new TablePagerColumn(COL_CREATEDATE, $m("AbstractMyMessageTPage.1"), 115)
								.setPropertyClass(Date.class))
				.addColumn(TablePagerColumn.OPE().setWidth(80));

		// 标记菜单
		createMarkMenuComponent(pp);
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		final ElementList el = super.getLeftElements(pp);
		final ElementList el2 = ElementList.of();
		final NoticeMessageWebPlugin plugin = getMessagePlugin(pp);
		final String category = pp.getParameter("category");
		for (final CategoryStat stat : plugin.getMessageService().queryCategoryItems(pp.getLoginId())) {
			final String category2 = stat.getCategoryId();
			final IMessageCategory mCategory = plugin.getMessageCategory(category2);
			final LinkElementEx link = (LinkElementEx) new LinkElementEx(
					mCategory != null ? mCategory.toString() : $m("MyFavoritesTPage.5")).setSelected(
					category2.equals(category)).setHref(
					((IMessageWebContext) messageContext).getUrlsFactory().getUrl(pp,
							MyNoticeMessageTPage.class, "category=" + category2));
			el2.append(
					new SpanElement().setClassName("notice_category_item").addElements(link,
							SupElement.num(stat.getCount())), SpanElement.SPACE);
			if (link.isSelected()) {
				((LinkElementEx) el.get(0)).setSelected(false);
			}
		}

		return ElementList.of(TagElement.table().addElements(
				TagElement.tr().addElements(
						TagElement.td(el).setWidth("140px").setVerticalAlign(EVerticalAlign.top),
						TagElement.td(el2))));
	}

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		return ElementList.of(createMarkMenuElement(), SpanElement.SPACE, createDeleteElement());
	}

	public static class NoticeMessageTbl extends MyMessageTbl {

		@Override
		protected AbstractElement<?> createCategory(final ComponentParameter cp,
				final AbstractMessage msg) {
			final IMessageCategory category = messageContext.getNoticeMessagePlugin()
					.getMessageCategory(msg.getCategory());
			return category != null ? new SpanElement(category) : null;
		}
	}
}
