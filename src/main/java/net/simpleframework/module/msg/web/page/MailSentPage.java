package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Enumeration;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.BlockElement;
import net.simpleframework.mvc.common.element.Checkbox;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.RowField;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.common.element.TableRow;
import net.simpleframework.mvc.common.element.TableRows;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.autocomplete.AutocompleteBean;
import net.simpleframework.mvc.ctx.permission.IPagePermissionHandler;

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
					.setInputField("sm_senter")
					.setSepChar(";")
					.setHandleClass(
							ClassUtils
									.forName("net.simpleframework.organization.web.component.autocomplete.UserRoleAutocompleteHandler"));
		} catch (final ClassNotFoundException e) {
			log.warn(e);
		}
	}

	@Override
	public JavascriptForward onSave(final ComponentParameter cp) {
		final String[] arr = StringUtils.split(cp.getParameter("sm_senter"), ";");
		if (arr != null) {
			final IPagePermissionHandler pHandler = cp.getPermission();
			for (final String s : arr) {
				if (s.startsWith("#")) {
					final Enumeration<ID> enumeration = pHandler.users(s.substring(1), null);
					int i = 0;
					while (enumeration.hasMoreElements()) {
						System.out.println(i++ + " : " + enumeration.nextElement());
					}
				} else {
				}
			}
			// .users(role, null);
			// cp.getUser("").getEmail()
		}
		return super.onSave(cp);
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		return ElementList.of(SpanElement.strongText($m("MailSentPage.0")));
	}

	@Override
	protected TableRows getTableRows(final PageParameter pp) {
		final InputElement sm_senter = new InputElement("sm_senter");
		final InputElement sm_topic = new InputElement("sm_topic");
		final InputElement sm_content = InputElement.textarea("sm_content").setRows(8);

		final Checkbox sm_autolink = new Checkbox("sm_autolink", $m("AbstractSentMessagePage.1"))
				.setChecked(true);

		final TableRow r1 = new TableRow(new RowField($m("MailSentPage.1"), sm_senter));
		final TableRow r2 = new TableRow(new RowField($m("MailSentPage.2"), sm_topic));
		final TableRow r3 = new TableRow(new RowField($m("MailSentPage.3"), sm_content,
				new BlockElement().setClassName("sm_content_bar").addElements(sm_autolink)));

		return TableRows.of(r1, r2, r3);
	}
}
