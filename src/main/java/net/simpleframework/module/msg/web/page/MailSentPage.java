package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Iterator;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.mail.Email;
import net.simpleframework.common.web.html.HtmlUtils;
import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.ctx.trans.Transaction;
import net.simpleframework.module.msg.IEmailService;
import net.simpleframework.module.msg.IMessageContext;
import net.simpleframework.module.msg.web.page.AbstractMessagePage.AbstractSentMessagePage;
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
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.ctx.permission.IPagePermissionHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MailSentPage extends AbstractSentMessagePage {

	@Override
	protected void onForward(final PageParameter pp) throws Exception {
		super.onForward(pp);

		addFormValidationBean(pp).addValidators(
				new Validator(EValidatorMethod.required, "#sm_senter, #sm_topic, #sm_content"));

		addUserRoleAutocompleteBean(pp, "MailSentPage_autocomplete").setInputField("sm_senter")
				.setSepChar(";");
	}

	@Override
	protected AjaxRequestBean addAjaxRequest_onSave(final PageParameter pp) {
		return super.addAjaxRequest_onSave(pp).setConfirmMessage($m("MailSentPage.4"));
	}

	@Transaction(context = IMessageContext.class)
	@Override
	public JavascriptForward onSave(final ComponentParameter cp) throws Exception {
		final IEmailService eService = messageContext.getEmailService();
		final IPagePermissionHandler pHandler = cp.getPermission();
		final String topic = cp.getParameter("sm_topic");
		String content = cp.getParameter("sm_content");
		content = HtmlUtils.convertHtmlLines(content);
		if (cp.getBoolParameter("sm_autolink")) {
			content = HtmlUtils.autoLink(content);
		}
		for (final String s : StringUtils.split(cp.getParameter("sm_senter"), ";")) {
			if (s.startsWith("#")) {
				final Iterator<PermissionUser> it = pHandler.users(s.substring(1), null);
				while (it.hasNext()) {
					final PermissionUser user = it.next();
					final String email = user.getEmail();
					if (StringUtils.hasText(email)) {
						eService.sentMail(Email.of(email).subject(topic).addHtml(content));
					}
				}
			} else {
				final PermissionUser user = pHandler.getUser(s);
				final String email = user.getEmail();
				if (StringUtils.hasText(email)) {
					eService.sentMail(Email.of(email).subject(topic).addHtml(content));
				}
			}
		}

		return JavascriptForward.alert($m("MailSentPage.5")).append(super.onSave(cp));
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
