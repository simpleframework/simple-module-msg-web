package net.simpleframework.module.msg.web.page.t1;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ctx.trans.Transaction;
import net.simpleframework.module.msg.IMessageContext;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.module.msg.web.page.AbstractMessagePage.AbstractSentMessagePage;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.RowField;
import net.simpleframework.mvc.common.element.TableRow;
import net.simpleframework.mvc.common.element.TableRows;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.Validator;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class SystemMessagePublishPage extends AbstractSentMessagePage {

	@Override
	protected void onForward(final PageParameter pp) throws Exception {
		super.onForward(pp);

		addSmileyDictionary(pp);

		addFormValidationBean(pp).addValidators(
				new Validator(EValidatorMethod.required, "#sm_topic, #sm_content"));
	}

	@Override
	public boolean isButtonsOnTop(final PageParameter pp) {
		return false;
	}

	@Transaction(context = IMessageContext.class)
	@Override
	public JavascriptForward onSave(final ComponentParameter cp) throws Exception {
		((IMessageWebContext) messageContext).getSystemMessagePlugin().sentSystemMessage(
				cp.getParameter("sm_topic"), getContent(cp));
		final JavascriptForward js = super.onSave(cp);
		js.append("$Actions['AbstractMessageMgrPage_tbl']();");
		return js;
	}

	@Override
	protected TableRows getTableRows(final PageParameter pp) {
		final InputElement sm_topic = new InputElement("sm_topic");
		final InputElement sm_content = InputElement.textarea("sm_content").setRows(6);

		final TableRow r1 = new TableRow(
				new RowField($m("AbstractMgrMessagePage.0"), sm_topic).setStarMark(true));
		final TableRow r2 = new TableRow(new RowField($m("MessageViewPage.4"), sm_content,
				sm_content_bar).setStarMark(true));
		return TableRows.of(r1, r2);
	}
}
