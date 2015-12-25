package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;

import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.ctx.trans.Transaction;
import net.simpleframework.module.msg.IMessageContext;
import net.simpleframework.module.msg.IMessageContextAware;
import net.simpleframework.module.msg.P2PMessage;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.module.msg.web.plugin.PrivateMessagePlugin;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.Checkbox;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.RowField;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.common.element.TableRow;
import net.simpleframework.mvc.common.element.TableRows;
import net.simpleframework.mvc.component.ComponentHandlerException;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.Validator;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PrivateMessageSentPage extends AbstractSentMessagePage implements IMessageContextAware {

	@Override
	protected void onForward(final PageParameter pp) throws Exception {
		super.onForward(pp);

		addSmileyDictionary(pp);

		addFormValidationBean(pp).addValidators(
				new Validator(EValidatorMethod.required, "#sm_receiver, #sm_topic"));

		addAjaxRequest(pp, "PrivateMessageSentPage_save2").setHandlerMethod("onSave2").setSelector(
				getFormSelector());

		addUserAutocompleteBean(pp, "PrivateMessageSentPage_autocomplete").setInputField(
				"sm_receiver").setSepChar(";");
	}

	@Transaction(context = IMessageContext.class)
	@Override
	public JavascriptForward onSave(final ComponentParameter cp) throws Exception {
		// 发送
		final PrivateMessagePlugin plugin = ((IMessageWebContext) messageContext)
				.getPrivateMessagePlugin();
		String toUsers;
		final ID fromId = cp.getLoginId();
		final String topic = cp.getParameter("sm_topic");
		final String content = getContent(cp);
		for (String r : StringUtils.split(toUsers = cp.getParameter("sm_receiver"), ";")) {
			r = r.trim();
			final PermissionUser user = cp.getUser(r);
			final ID userId = user.getId();
			if (userId == null) {
				throw ComponentHandlerException.of($m("PrivateMessageSentPage.4", r));
			}
			plugin.sentMessage(userId, fromId, topic, content);
		}

		P2PMessage message = getMessage(cp);
		final Date sentDate = new Date();
		if (cp.getBoolParameter(OPT_SENTBOX)) {
			final boolean insert = message == null || "reply".equals(cp.getParameter("t"));
			if (insert) {
				message = new P2PMessage();
				message.setCreateDate(sentDate);
				message.setMessageMark(plugin.getMark());
				message.setFromId(cp.getLoginId());
			}
			message.setSentDate(sentDate);
			message.setCategory(PrivateMessagePlugin.SENT_MODULE.getName());
			message.setToUsers(toUsers);
			message.setTopic(topic);
			message.setContent(content);
			if (insert) {
				plugin.getMessageService().insert(message);
			} else {
				plugin.getMessageService().update(message);
			}
		} else if (message != null) {
			plugin.getMessageService().delete(message.getId());
		}
		final JavascriptForward js = super.onSave(cp);
		js.append("$Actions['AbstractMyMessageTPage_tbl']();");
		return js;
	}

	@Transaction(context = IMessageContext.class)
	public JavascriptForward onSave2(final ComponentParameter cp) {
		final PrivateMessagePlugin mark = ((IMessageWebContext) messageContext)
				.getPrivateMessagePlugin();
		// 暂存
		P2PMessage message = getMessage(cp);
		final boolean insert = message == null || "reply".equals(cp.getParameter("t"));
		if (insert) {
			message = new P2PMessage();
			message.setCategory(PrivateMessagePlugin.DRAFT_MODULE.getName());
			message.setCreateDate(new Date());
			message.setMessageMark(mark.getMark());
			message.setFromId(cp.getLoginId());
		}
		message.setToUsers(cp.getParameter("sm_receiver"));
		message.setTopic(cp.getParameter("sm_topic"));
		message.setContent(getContent(cp));
		if (insert) {
			mark.getMessageService().insert(message);
		} else {
			mark.getMessageService().update(message);
		}
		final JavascriptForward js = new JavascriptForward();
		js.append("$('sm_msgId').value = '").append(message.getId()).append("';");
		js.append("$Actions['AbstractMyMessageTPage_tbl']();");
		js.append("alert('").append($m("PrivateMessageSentPage.8")).append("');");
		return js;
	}

	public static final String OPT_SENTBOX = "opt_sentBox";

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		final P2PMessage message = getMessage(pp);
		if (message != null
				&& PrivateMessagePlugin.SENT_MODULE.getName().equals(message.getCategory())) {
			return null;
		}

		final Checkbox opt_sentBox = new Checkbox(OPT_SENTBOX, $m("PrivateMessageSentPage.6"))
				.setChecked(true);
		return ElementList.of(opt_sentBox);
	}

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		final P2PMessage message = getMessage(pp);
		final ElementList el = ElementList.of();
		if (message == null
				|| !PrivateMessagePlugin.SENT_MODULE.getName().equals(message.getCategory())) {
			final ButtonElement saveBtn = SAVE_BTN();
			final StringBuilder sb = new StringBuilder(
					"if ($F('sm_content').trim() == '' && !confirm('")
					.append($m("PrivateMessageSentPage.9")).append("')) { return; }")
					.append(saveBtn.getOnclick());
			el.append(saveBtn.setOnclick(sb.toString()));
			el.append(SpanElement.SPACE);
			el.append(VALIDATION_BTN($m("PrivateMessageSentPage.7")).setOnclick(
					"$Actions['PrivateMessageSentPage_save2']();"));
			el.append(SpanElement.SPACE);
		}
		el.append(ButtonElement.closeBtn());
		return el;
	}

	@Override
	public String getFocusElement(final PageParameter pp) {
		if ("reply".equals(pp.getParameter("t"))) {
			return "sm_content";
		}
		return "sm_receiver";
	}

	@Override
	protected TableRows getTableRows(final PageParameter pp) {
		final InputElement msgId = InputElement.hidden().setName("msgId").setId("sm_msgId");
		final InputElement sm_receiver = new InputElement("sm_receiver");
		final InputElement sm_topic = new InputElement("sm_topic");
		final InputElement sm_content = InputElement.textarea("sm_content").setRows(12);

		final P2PMessage message = getMessage(pp);
		if (message != null) {
			if ("reply".equals(pp.getParameter("t"))) {
				sm_receiver.setText(pp.getUser(message.getFromId()).getName());
				sm_topic.setText($m("PrivateMessageSentPage.5") + message.getTopic());
				final StringBuilder c = new StringBuilder();
				c.append("\r\r\r==================================\r");
				c.append(message.getContent());
				sm_content.setText(c.toString());
			} else {
				sm_receiver.setText(message.getToUsers());
				sm_topic.setText(message.getTopic());
				sm_content.setText(message.getContent());
			}
			msgId.setText(message.getId());
		}
		// InputElement
		// .hidden("t").setValue(pp),

		final TableRow r1 = new TableRow(new RowField($m("PrivateMessageSentPage.0"), msgId,
				sm_receiver).setStarMark(true));
		final TableRow r2 = new TableRow(
				new RowField($m("PrivateMessageSentPage.1"), sm_topic).setStarMark(true));
		// final TableRow r3 = new TableRow(new
		// RowField($m("PrivateMessageSentPage.2"), sm_content,
		// sm_content_bar));
		final TableRow r3 = new TableRow(new RowField("", sm_content, sm_content_bar));
		return TableRows.of(r1, r2, r3);
	}

	@Override
	public int getLabelWidth(final PageParameter pp) {
		return 85;
	}

	@Override
	public String toTableRowsString(final PageParameter pp) {
		return super.toTableRowsString(pp)
				+ new SpanElement($m("PrivateMessageSentPage.3")).setClassName("sm_atten");
	}

	protected static P2PMessage getMessage(final PageParameter pp) {
		return getCacheBean(pp, ((IMessageWebContext) messageContext).getPrivateMessagePlugin()
				.getMessageService(), "msgId");
	}
}
