package net.simpleframework.module.msg.web.page.t1;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.module.msg.EMessageSendTo;
import net.simpleframework.module.msg.IMessageContextAware;
import net.simpleframework.module.msg.MessageException;
import net.simpleframework.module.msg.plugin.IMessageCategory;
import net.simpleframework.module.msg.plugin.NoticeMessageCategory;
import net.simpleframework.module.msg.plugin.NoticeMessagePlugin;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.BlockElement;
import net.simpleframework.mvc.common.element.Checkbox;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.tree.AbstractTreeHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;
import net.simpleframework.mvc.template.lets.LCTemplateWinPage;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126 .com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NoticeMessageContentPage extends LCTemplateWinPage implements IMessageContextAware {

	@Override
	protected void onForward(final PageParameter pp) {
		super.onForward(pp);

		addComponentBean(pp, "NoticeMessageContentPage_tree", TreeBean.class).setCookies(false)
				.setContainerId("idNoticeMessageContentPage_tree")
				.setHandlerClass(MessageTypeTree.class);

		addAjaxRequest(pp, "NoticeMessageContentPage_nav").setHandlerMethod("doNav");

		addAjaxRequest(pp, "NoticeMessageContentPage_save").setConfirmMessage($m("Confirm.Post"))
				.setHandlerMethod("doSave").setSelector(".NoticeMessageContentPage_Center");
	}

	@Override
	protected String getWindowAction(final PageParameter pp) {
		return "MgrNoticeMessagePage_contentWin";
	}

	private static NoticeMessagePlugin getNoticeMessagePlugin() {
		return ((IMessageWebContext) messageContext).getNoticeMessagePlugin();
	}

	public IForward doSave(final ComponentParameter cp) throws Exception {
		final NoticeMessageCategory mCategory = (NoticeMessageCategory) getNoticeMessagePlugin()
				.getMessageCategory(cp.getParameter("nmc_mark"));
		if (mCategory == null) {
			throw MessageException.of($m("NoticeMessageContentPage.3"));
		}
		final Map<String, Object> props = new HashMap<String, Object>();
		props.put("sendto-normal", cp.getBoolParameter("EMessageSendTo_normal"));
		props.put("sendto-email", cp.getBoolParameter("EMessageSendTo_email"));
		props.put("sendto-mobile", cp.getBoolParameter("EMessageSendTo_mobile"));
		props.put("topic", cp.getParameter("nmc_topic"));
		props.put("content", cp.getParameter("nmc_content"));
		messageContext.getContextSettings().saveNoticeMessageCategoryProps(mCategory.getName(), props);
		return null;
	}

	public IForward doNav(final ComponentParameter cp) {
		final NoticeMessageCategory mCategory = (NoticeMessageCategory) getNoticeMessagePlugin()
				.getMessageCategory(cp.getParameter("mark"));
		if (mCategory != null) {
			final JavascriptForward js = new JavascriptForward();
			js.append("$('nmc_text').innerHTML = '")
					.append(JavascriptUtils.escape(mCategory.toString())).append("';");
			js.append("$('nmc_mark').value = '").append(mCategory.getName()).append("';");
			js.append("$('nmc_topic').value = '")
					.append(JavascriptUtils.escape(mCategory.getTopic(null))).append("';");
			js.append("$('nmc_content').value = \"")
					.append(JavascriptUtils.escape(mCategory.getContent(null))).append("\";");
			js.append("$('EMessageSendTo_normal').checked = ").append(mCategory.isSendTo_normal())
					.append(";");
			js.append("$('EMessageSendTo_email').checked = ").append(mCategory.isSendTo_email())
					.append(";");
			js.append("$('EMessageSendTo_mobile').checked = ").append(mCategory.isSendTo_mobile())
					.append(";");
			return js;
		}
		return null;
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		final NoticeMessageCategory mCategory = (NoticeMessageCategory) getNoticeMessagePlugin()
				.allMessageCategory().iterator().next();
		return ElementList.of(SpanElement.strongText(mCategory == null ? null : mCategory).setId(
				"nmc_text"));
	}

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		return ElementList.of(
				LinkButton.saveBtn().setOnclick("$Actions['NoticeMessageContentPage_save']();"),
				SpanElement.SPACE, LinkButton.closeBtn());
	}

	@Override
	protected String toHtml(final PageParameter pp, final Map<String, Object> variables,
			final String currentVariable) throws IOException {
		if ("content_left".equals(currentVariable)) {
			return "<div id='idNoticeMessageContentPage_tree'></div>";
		} else if ("content_center".equals(currentVariable)) {
			final StringBuilder sb = new StringBuilder();
			final InputElement nmc_mark = InputElement.hidden("nmc_mark");
			final InputElement nmc_topic = new InputElement("nmc_topic");
			final InputElement nmc_content = InputElement.textarea("nmc_content").setRows(10);
			final Checkbox box_normal = new Checkbox("EMessageSendTo_normal",
					$m("NoticeMessageContentPage.4"));
			final Checkbox box_email = new Checkbox("EMessageSendTo_email", EMessageSendTo.email);
			final Checkbox box_mobile = new Checkbox("EMessageSendTo_mobile", EMessageSendTo.mobile);
			final NoticeMessageCategory mCategory = (NoticeMessageCategory) getNoticeMessagePlugin()
					.allMessageCategory().iterator().next();
			if (mCategory != null) {
				nmc_mark.setText(mCategory.getName());
				nmc_topic.setText(mCategory.getTopic(null));
				nmc_content.setText(mCategory.getContent(null));
				box_normal.setChecked(mCategory.isSendTo_normal());
				box_email.setChecked(mCategory.isSendTo_email());
				box_mobile.setChecked(mCategory.isSendTo_mobile());
			}
			sb.append("<div class='NoticeMessageContentPage_Center'>");
			sb.append(nmc_mark);
			sb.append(new SpanElement($m("NoticeMessageContentPage.0")).setClassName("lbl"));
			sb.append(new BlockElement().addElements(nmc_topic));
			sb.append(new SpanElement($m("NoticeMessageContentPage.1")).setClassName("lbl"));
			sb.append(new BlockElement().addElements(nmc_content));
			sb.append(new SpanElement($m("MgrNoticeMessagePage.0")).setClassName("lbl"));
			sb.append(new BlockElement().addElements(box_normal, SpanElement.SPACE, box_email,
					SpanElement.SPACE, box_mobile));
			sb.append("</div>");
			return sb.toString();
		}
		return super.toHtml(pp, variables, currentVariable);
	}

	public static class MessageTypeTree extends AbstractTreeHandler {

		@Override
		public TreeNodes getTreenodes(final ComponentParameter cp, final TreeNode parent) {
			final TreeBean treeBean = (TreeBean) cp.componentBean;
			final String rootText = $m("NoticeMessageContentPage.2");
			if (parent == null) {
				final TreeNode root = new TreeNode(treeBean, rootText);
				root.setOpened(true);
				return TreeNodes.of(root);
			} else {
				final Object dataObject = parent.getDataObject();
				if (rootText.equals(dataObject)) {
					final TreeNodes nodes = TreeNodes.of();
					final Collection<IMessageCategory> coll = getNoticeMessagePlugin()
							.allMessageCategory();
					final Map<String, TreeNode> cache = new HashMap<String, TreeNode>();
					int i = 0;
					for (final IMessageCategory c : coll) {
						final TreeNode tn = new TreeNode(treeBean, c);
						final NoticeMessageCategory mCategory = (NoticeMessageCategory) c;
						final String groupText = mCategory.getGroupText();
						if (StringUtils.hasText(groupText)) {
							TreeNode pTn = cache.get(groupText);
							if (pTn == null) {
								cache.put(groupText, pTn = new TreeNode(treeBean, groupText));
								pTn.setOpened(true);
								nodes.add(pTn);
							}
							pTn.children().add(tn);
						} else {
							nodes.add(tn);
						}
						if (i++ == 0) {
							tn.setSelect(true);
						}
						tn.setJsClickCallback("$Actions['NoticeMessageContentPage_nav']('mark="
								+ c.getName() + "');");
					}
					return nodes;
				}
			}
			return super.getTreenodes(cp, parent);
		}
	}
}