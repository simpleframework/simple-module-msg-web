package net.simpleframework.module.msg.web.page;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.RowField;
import net.simpleframework.mvc.common.element.TableRow;
import net.simpleframework.mvc.common.element.TableRows;
import net.simpleframework.mvc.component.ui.autocomplete.AutocompleteBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MailSentPage extends AbstractSentMessagePage {

	@Override
	protected void onForward(final PageParameter pp) {
		super.onForward(pp);

		try {
			addComponentBean(pp, "MailSentPage_autocomplete", AutocompleteBean.class)
					.setInputField("ms_senter")
					.setSepChar(";")
					.setHandleClass(
							ClassUtils
									.forName("net.simpleframework.organization.web.component.autocomplete.UserAutocompleteHandler"));
		} catch (final ClassNotFoundException e) {
			log.warn(e);
		}
	}

	@Override
	protected TableRows getTableRows(final PageParameter pp) {
		final InputElement ms_senter = new InputElement("ms_senter");
		final InputElement ms_content = InputElement.textarea("ms_content").setRows(10);
		final TableRow r1 = new TableRow(new RowField("发送人", ms_senter));
		final TableRow r2 = new TableRow(new RowField("主题", new InputElement()));
		final TableRow r3 = new TableRow(new RowField("内容", ms_content, sm_content_bar));
		return TableRows.of(r1, r2, r3);
	}
}
