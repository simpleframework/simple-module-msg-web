package net.simpleframework.module.msg.web.page;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.RowField;
import net.simpleframework.mvc.common.element.TableRow;
import net.simpleframework.mvc.common.element.TableRows;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MailSentPage extends AbstractSentMessagePage {

	@Override
	protected TableRows getTableRows(final PageParameter pp) {
		final InputElement ms_content = InputElement.textarea("ms_content").setRows(10);
		final TableRow r1 = new TableRow(new RowField("发送人", new InputElement()));
		final TableRow r2 = new TableRow(new RowField("主题", new InputElement()));
		final TableRow r3 = new TableRow(new RowField("内容", ms_content, sm_content_bar));
		return TableRows.of(r1, r2, r3);
	}
}
