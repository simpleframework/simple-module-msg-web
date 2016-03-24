package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.web.html.HtmlUtils;
import net.simpleframework.module.msg.P2PMessage;
import net.simpleframework.module.msg.web.page.AbstractMessagePage.AbstractSentMessagePage;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.BlockElement;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.RowField;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.common.element.TableRow;
import net.simpleframework.mvc.common.element.TableRows;
import net.simpleframework.mvc.component.ui.dictionary.SmileyUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PrivateMessageSentViewPage extends AbstractSentMessagePage {

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		final P2PMessage message = PrivateMessageSentPage.getMessage(pp);
		return ElementList.of(new SpanElement(message.getTopic()).setClassName("topic"));
	}

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		return ElementList.of(ButtonElement.closeBtn());
	}

	@Override
	protected TableRows getTableRows(final PageParameter pp) {
		final P2PMessage message = PrivateMessageSentPage.getMessage(pp);

		String c = SmileyUtils.replaceSmiley(message.getContent());
		c = HtmlUtils.convertHtmlLines(c);

		final TableRow r1 = new TableRow(new RowField($m("PrivateMessageSentPage.0"),
				new BlockElement().setClassName("sv_receiver").setText(
						MessageUtils.toRevString(pp, message.getToUsers(), true))));
		final TableRow r2 = new TableRow(new RowField($m("AbstractMyMessageTPage.8"),
				new BlockElement().setText(message.getSentDate())));
		final TableRow r3 = new TableRow(new RowField("", new BlockElement().setClassName(
				"sv_content").setText(c)));
		return TableRows.of(r1, r2, r3);
	}
}