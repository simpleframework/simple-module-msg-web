package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.web.HttpUtils;
import net.simpleframework.module.common.plugin.IModulePlugin;
import net.simpleframework.module.msg.AbstractMessage;
import net.simpleframework.module.msg.IMessageContextAware;
import net.simpleframework.module.msg.IMessageService;
import net.simpleframework.module.msg.plugin.IMessageCategory;
import net.simpleframework.module.msg.plugin.IMessagePlugin;
import net.simpleframework.module.msg.web.IMessageConst;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.module.msg.web.plugin.IMessageUI;
import net.simpleframework.module.msg.web.plugin.PrivateMessagePlugin;
import net.simpleframework.module.msg.web.plugin.PrivateMessagePlugin.PrivateMessageDraftCategory;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.EVerticalAlign;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.Icon;
import net.simpleframework.mvc.common.element.ImageElement;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.common.element.LinkElementEx;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.common.element.SupElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.menu.EMenuEvent;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.db.AbstractDbTablePagerHandler;
import net.simpleframework.mvc.template.lets.Category_ListPage;
import net.simpleframework.mvc.template.struct.CategoryItem;
import net.simpleframework.mvc.template.struct.CategoryItems;
import net.simpleframework.mvc.template.struct.NavigationButtons;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractMyMessageTPage extends Category_ListPage implements IMessageConst,
		IMessageContextAware {

	@Override
	protected void onForward(final PageParameter pp) {
		super.onForward(pp);

		pp.addImportCSS(AbstractMyMessageTPage.class, "/my_message.css");

		// delete
		addDeleteAjaxRequest(pp, "AbstractMyMessageTPage_delete");

		// ajax view
		addAjaxRequest(pp, "AbstractMyMessageTPage_view").setHandlerMethod("doView");

		// msg win
		addAjaxRequest(pp, "AbstractMyMessageTPage_viewPage", MessageViewPage.class);
		addWindowBean(pp, "AbstractMyMessageTPage_viewWin")
				.setContentRef("AbstractMyMessageTPage_viewPage")
				.setTitle($m("AbstractMyMessageTPage.0")).setResizable(false).setHeight(410)
				.setWidth(640);

		addMessageComponents(pp);
	}

	protected void addMessageComponents(final PageParameter pp) {
	}

	protected void createMarkMenuComponent(final PageParameter pp) {
		// 标记菜单
		final MenuBean mb = (MenuBean) addComponentBean(pp, "AbstractMyMessageTPage_markMenu",
				MenuBean.class).setMenuEvent(EMenuEvent.click).setSelector(
				"#idAbstractMyMessageTPage_markMenu");
		mb.addItem(
				MenuItem
						.of($m("MyPrivateMessageTPage.2"))
						.setOnclick(
								"$Actions['AbstractMyMessageTPage_tbl'].doAct('AbstractMyMessageTPage_mark', null, 'mark=read');"))
				.addItem(
						MenuItem
								.of($m("MyPrivateMessageTPage.3"))
								.setOnclick(
										"$Actions['AbstractMyMessageTPage_tbl'].doAct('AbstractMyMessageTPage_mark', null, 'mark=unread');"))
				.addItem(
						MenuItem.of($m("MyPrivateMessageTPage.4")).setOnclick(
								"$Actions['AbstractMyMessageTPage_mark']('mark=all');"));
		// 标记
		addAjaxRequest(pp, "AbstractMyMessageTPage_mark").setHandlerMethod("doMark");
	}

	protected AbstractElement<?> createMarkMenuElement() {
		return LinkButton.menu($m("AbstractMyMessageTPage.6")).setIconClass(Icon.eye_open)
				.setId("idAbstractMyMessageTPage_markMenu");
	}

	protected abstract IMessagePlugin getMessagePlugin(PageParameter pp);

	@SuppressWarnings("unchecked")
	public <T extends AbstractMessage> IForward doMark(final ComponentParameter cp) {
		final IMessagePlugin oMark = getMessagePlugin(cp);
		final IMessageService<T> service = (IMessageService<T>) oMark.getMessageService();
		final String mark = cp.getParameter("mark");

		if ("all".equals(mark)) {
			service.doAllRead(cp.getLoginId());
		} else {
			for (final Object id : StringUtils.split(cp.getParameter("id"))) {
				final T message = service.getBean(id);
				if (message != null) {
					if ("read".equals(mark)) {
						service.doRead(cp.getLoginId(), message);
					} else if ("unread".equals(mark)) {
						service.doUnRead(cp.getLoginId(), message);
					}
				}
			}
		}
		return new JavascriptForward("$Actions['AbstractMyMessageTPage_tbl']();");
	}

	public IForward doDelete(final ComponentParameter cp) {
		final Object[] ids = StringUtils.split(cp.getParameter("id"));
		getMessagePlugin(cp).getMessageService().doDelete(cp.getLoginId(), ids);
		return new JavascriptForward("$Actions['AbstractMyMessageTPage_tbl']();");
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractMessage> IForward doView(final ComponentParameter cp) {
		final IMessagePlugin oMark = getMessagePlugin(cp);
		final IMessageService<T> service = (IMessageService<T>) oMark.getMessageService();
		final T msg = service.getBean(cp.getParameter("msgId"));
		service.doRead(cp.getLoginId(), msg);
		return new JavascriptForward("$Actions['AbstractMyMessageTPage_viewWin']('msgId=")
				.append(msg.getId()).append("&messageMark=").append(oMark.getMark())
				.append("'); $Actions['AbstractMyMessageTPage_tbl']();");
	}

	protected TablePagerBean addTablePagerBean(final PageParameter pp,
			final Class<? extends MyMessageTbl> tblClass) {
		return addTablePagerBean(pp, "AbstractMyMessageTPage_tbl", tblClass, false)
				.setShowFilterBar(true).setShowHead(true).setShowCheckbox(true);
	}

	private CategoryItem createCategoryItem(final PageParameter pp, final IMessageUI oModule) {
		final String href = oModule.getMyPageUrl(pp);
		final CategoryItem block = new CategoryItem(oModule.toString())
				.setHref(href)
				.setIconClass(oModule.getIconClass())
				.setSelected(
						href.equals(((IMessageWebContext) messageContext).getUrlsFactory().getUrl(pp,
								getClass())));
		if (oModule instanceof IMessagePlugin) {
			final IMessagePlugin oMark = (IMessagePlugin) oModule;
			final int num = oMark.getMessageService().getUnreadMessageCount(pp.getLoginId());
			if (num > 0) {
				block.setNum(new SupElement(num).setHighlight(true));
			}
		} else {
			if (oModule instanceof PrivateMessageDraftCategory) {
				final PrivateMessagePlugin oMark = ((IMessageWebContext) messageContext)
						.getPrivateMessagePlugin();
				final int c = oMark
						.getMessageService()
						.queryFromMessages(pp.getLoginId(), null,
								PrivateMessagePlugin.DRAFT_MODULE.getName()).getCount();
				if (c > 0) {
					block.setTitle(oModule.toString() + SupElement.num(c));
				}
			}
		}
		return block;
	}

	@Override
	protected CategoryItems getCategoryList(final PageParameter pp) {
		final CategoryItems titles = CategoryItems.of();
		for (final IModulePlugin oMark : messageContext.getPluginRegistry().allPlugin()) {
			final CategoryItem block = createCategoryItem(pp, (IMessageUI) oMark);
			final Collection<IMessageCategory> coll = ((IMessagePlugin) oMark).allMessageCategory();
			if (coll != null) {
				final List<CategoryItem> children = block.getChildren();
				for (final IMessageCategory child : coll) {
					if (child instanceof IMessageUI) {
						children.add(createCategoryItem(pp, (IMessageUI) child));
					}
				}
			}
			titles.append(block);
		}
		return titles;
	}

	private Boolean getRead(final PageParameter pp) {
		final String s = pp.getParameter("s");
		if ("unread".equals(s)) {
			return Boolean.FALSE;
		}
		if ("read".equals(s)) {
			return Boolean.TRUE;
		}
		return null;
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		final IMessagePlugin oMark = getMessagePlugin(pp);
		final IMessageService<?> service = oMark.getMessageService();
		final int all = service.queryMessages(pp.getLoginId(), null).getCount();
		final int unread = service.getUnreadMessageCount(pp.getLoginId());

		final ElementList eles = ElementList.of();
		final Boolean read = getRead(pp);
		final String url = ((IMessageUI) oMark).getMyPageUrl(pp);
		eles.add(new LinkElementEx($m("AbstractMyMessageTPage.3")).setSelected(read == null).setHref(
				url));
		eles.add(SupElement.num(all));
		eles.add(SpanElement.SPACE);
		eles.add(new LinkElementEx($m("AbstractMyMessageTPage.4")).setSelected(read != null && !read)
				.setHref(HttpUtils.addParameters(url, "s=unread")));
		if (unread > 0) {
			eles.add(SupElement.num(unread));
		}
		eles.add(SpanElement.SPACE);
		final int i = all - unread;
		eles.add(new LinkElementEx($m("AbstractMyMessageTPage.5")).setSelected(read != null && read)
				.setHref(HttpUtils.addParameters(url, "s=read")));
		if (i > 0) {
			eles.add(SupElement.num(i));
		}
		return eles;
	}

	@Override
	public NavigationButtons getNavigationBar(final PageParameter pp) {
		return NavigationButtons.of(new SpanElement($m("MessageWebContext.1")), new SpanElement(
				getMessagePlugin(pp).getText()));
	}

	protected AbstractElement<?> createDeleteElement() {
		return LinkButton.deleteBtn().setOnclick(
				"$Actions['AbstractMyMessageTPage_tbl'].doAct('AbstractMyMessageTPage_delete');");
	}

	public static class MyMessageTbl extends AbstractDbTablePagerHandler {
		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			final IMessagePlugin oMark = getMessageMark(cp);
			final String s = cp.getParameter("s");
			if (StringUtils.hasText(s)) {
				cp.addFormParameter("s", s);
			}
			return oMark.getMessageService().queryMessages(cp.getLoginId(),
					((AbstractMyMessageTPage) get(cp)).getRead(cp), cp.getParameter("category"));
		}

		protected IMessagePlugin getMessageMark(final ComponentParameter cp) {
			return ((AbstractMyMessageTPage) get(cp)).getMessagePlugin(cp);
		}

		protected LinkElement createTopic(final ComponentParameter cp, final AbstractMessage msg) {
			final IMessagePlugin oMark = getMessageMark(cp);
			@SuppressWarnings("unchecked")
			final IMessageService<AbstractMessage> service = (IMessageService<AbstractMessage>) oMark
					.getMessageService();
			final boolean unread = !service.isRead(cp.getLoginId(), msg);
			return new LinkElement(msg.getTopic()).setStrong(unread).setOnclick(
					"$Actions['"
							+ (unread ? "AbstractMyMessageTPage_view" : "AbstractMyMessageTPage_viewWin")
							+ "']('msgId=" + msg.getId() + "&messageMark=" + oMark.getMark() + "');");
		}

		protected ImageElement createImageMark(final ComponentParameter cp, final AbstractMessage msg) {
			return new ImageElement(cp.getCssResourceHomePath(AbstractMyMessageTPage.class)
					+ "/images/unread.png").setVerticalAlign(EVerticalAlign.middle);
		}

		protected AbstractElement<?> createFrom(final ComponentParameter cp, final AbstractMessage msg) {
			return null;
		}

		protected AbstractElement<?> createUser(final ComponentParameter cp, final AbstractMessage msg) {
			return null;
		}

		protected AbstractElement<?> createCategory(final ComponentParameter cp,
				final AbstractMessage msg) {
			return null;
		}

		protected ElementList createOPE(final ComponentParameter cp, final AbstractMessage msg) {
			return ElementList.of(ButtonElement.deleteBtn().setOnclick(
					"$Actions['AbstractMyMessageTPage_delete']('id=" + msg.getId() + "');"));
		}

		@Override
		protected Map<String, Object> getRowData(final ComponentParameter cp, final Object dataObject) {
			final AbstractMessage msg = (AbstractMessage) dataObject;
			final KVMap kv = new KVMap();

			final LinkElement topic = createTopic(cp, msg);
			kv.add(COL_TOPIC, topic);
			if (topic != null && topic.isStrong()) {
				kv.add(TablePagerColumn.ICON, createImageMark(cp, msg));
			}

			kv.add(COL_FROMID, createFrom(cp, msg));
			kv.add(COL_USERID, createUser(cp, msg));
			kv.add(COL_CATEGORY, createCategory(cp, msg));
			kv.add(COL_CREATEDATE, msg.getCreateDate());
			kv.put(TablePagerColumn.OPE, createOPE(cp, msg));
			return kv;
		}
	}
}
