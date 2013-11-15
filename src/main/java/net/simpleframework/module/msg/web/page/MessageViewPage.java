package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.ID;
import net.simpleframework.common.web.html.HtmlUtils;
import net.simpleframework.module.msg.AbstractMessage;
import net.simpleframework.module.msg.IMessageContextAware;
import net.simpleframework.module.msg.P2PMessage;
import net.simpleframework.module.msg.plugin.IMessageCategoryPlugin;
import net.simpleframework.module.msg.plugin.IMessagePlugin;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.BlockElement;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.ElementList;
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
		return ElementList.of(ButtonElement.WINDOW_CLOSE);
	}

	@Override
	public boolean isButtonsOnTop(final PageParameter pp) {
		return true;
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		final IMessagePlugin messageMark = context.getPluginRegistry().getPlugin(
				pp.getIntParameter("messageMark"));
		final AbstractMessage msg = messageMark.getMessageService().getBean(pp.getParameter("msgId"));

		return ElementList.of(SpanElement.strongText(msg.getTopic()));
	}

	@Override
	protected TableRows getTableRows(final PageParameter pp) {
		final TableRows rows = TableRows.of();

		final IMessagePlugin messageMark = context.getPluginRegistry().getPlugin(
				pp.getIntParameter("messageMark"));
		final AbstractMessage msg = messageMark.getMessageService().getBean(pp.getParameter("msgId"));

		final IMessageCategoryPlugin mCategory = messageMark.getMessageCategoryPlugin(msg
				.getCategory());
		final ID fromId = msg.getFromId();
		Object from = ((IMessageCategoryPlugin) messageMark).getFrom(fromId);
		if (from == null && mCategory != null) {
			from = mCategory.getFrom(fromId);
		}
		rows.append(new TableRow(new RowField($m("MessageViewPage.0"), new SpanElement("m_fromId")
				.setText(from)), new RowField($m("MessageViewPage.1"), new SpanElement(mCategory))));
		final SpanElement readDate = new SpanElement();
		if (msg instanceof P2PMessage) {
			readDate.setText(((P2PMessage) msg).getReadDate());
		}
		rows.append(new TableRow(new RowField($m("MessageViewPage.2"), new SpanElement(msg
				.getCreateDate())), new RowField($m("MessageViewPage.3"), readDate)));

		String c = SmileyUtils.replaceSmiley(msg.getContent());
		c = HtmlUtils.convertHtmlLines(c);
		rows.append(new TableRow(new RowField($m("MessageViewPage.4"), new BlockElement().setText(c)
				.setClassName("mv_content"))));
		return rows;
	}
}
