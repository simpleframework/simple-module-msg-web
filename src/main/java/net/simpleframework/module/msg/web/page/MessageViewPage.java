package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.ID;
import net.simpleframework.common.web.html.HtmlUtils;
import net.simpleframework.module.msg.AbstractMessage;
import net.simpleframework.module.msg.IMessageContextAware;
import net.simpleframework.module.msg.P2PMessage;
import net.simpleframework.module.msg.plugin.IMessageCategory;
import net.simpleframework.module.msg.plugin.IMessagePlugin;
import net.simpleframework.module.msg.plugin.IP2PMessagePlugin;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.BlockElement;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.RowField;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.common.element.TableRow;
import net.simpleframework.mvc.common.element.TableRows;
import net.simpleframework.mvc.component.ui.dictionary.SmileyUtils;
import net.simpleframework.mvc.template.lets.FormTableRowTemplatePage;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MessageViewPage extends FormTableRowTemplatePage implements IMessageContextAware {

	@Override
	public int getLabelWidth(final PageParameter pp) {
		return 80;
	}

	@Override
	protected String getPageCSS(final PageParameter pp) {
		return "MessageViewPage";
	}

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		return ElementList.of(ButtonElement.closeBtn());
	}

	@Override
	public boolean isButtonsOnTop(final PageParameter pp) {
		return true;
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		final IMessagePlugin plugin = messageContext.getPluginRegistry().getPlugin(
				pp.getIntParameter("messageMark"));
		final AbstractMessage msg = getMessage(pp, plugin);
		return ElementList.of(SpanElement.strongText(msg.getTopic()));
	}

	@Override
	protected TableRows getTableRows(final PageParameter pp) {
		final IMessagePlugin plugin = messageContext.getPluginRegistry().getPlugin(
				pp.getIntParameter("messageMark"));
		final AbstractMessage msg = getMessage(pp, plugin);

		final ID fromId = msg.getFromId();
		Object from = plugin.getFrom(fromId);
		final IMessageCategory mCategory = plugin.getMessageCategory(msg.getCategory());
		if (mCategory != null && from == null) {
			from = mCategory.getFrom(fromId);
		}

		final TableRow r1 = new TableRow(new RowField($m("MessageViewPage.0"), new InputElement()
				.setText("m_fromId").setText(from)), new RowField($m("MessageViewPage.1"),
				new InputElement().setText(mCategory)));

		final TableRow r2 = new TableRow(new RowField($m("MessageViewPage.2"),
				new InputElement().setText(msg.getCreateDate())), new RowField($m("MessageViewPage.3"),
				new InputElement().setText(msg instanceof P2PMessage ? ((P2PMessage) msg).getReadDate()
						: null)));

		final TableRow r3 = new TableRow(new RowField($m("MessageViewPage.4"), new BlockElement()
				.setText(HtmlUtils.convertHtmlLines(SmileyUtils.replaceSmiley(msg.getContent())))
				.setClassName("mv_content")));
		return TableRows.of(r1, r2, r3).setReadonly(true);
	}

	private AbstractMessage getMessage(final PageParameter pp, final IMessagePlugin plugin) {
		AbstractMessage msg = null;
		if (plugin instanceof IP2PMessagePlugin) {
			msg = ((IP2PMessagePlugin) plugin).getMessageLogService()
					.getBean(pp.getParameter("logId"));
		}
		if (msg == null) {
			msg = plugin.getMessageService().getBean(pp.getParameter("msgId"));
		}
		return msg;
	}
}
