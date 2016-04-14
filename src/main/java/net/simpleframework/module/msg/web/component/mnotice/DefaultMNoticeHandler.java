package net.simpleframework.module.msg.web.component.mnotice;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;
import java.util.Set;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ado.query.IteratorDataQuery;
import net.simpleframework.common.ID;
import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.module.msg.P2PMessage;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.module.msg.web.page.MessageUtils;
import net.simpleframework.module.msg.web.plugin.PrivateMessagePlugin;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.component.AbstractComponentHandler;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DefaultMNoticeHandler extends AbstractComponentHandler implements IMNoticeHandler {

	@Override
	public IDataQuery<PermissionUser> allUsers(final ComponentParameter cp) {
		// 缺省取当前部门的用户
		return new IteratorDataQuery<PermissionUser>(cp.getLdept().users());
	}

	@Override
	public String getTopic(final ComponentParameter cp) {
		return null;
	}

	@Override
	public String getContent(final ComponentParameter cp) {
		return null;
	}

	@Override
	public LinkElement getOpenUrl(final ComponentParameter cp) {
		return null;
	}

	protected String toSentContent(final ComponentParameter cp, final String content) {
		final StringBuilder sb = new StringBuilder();
		sb.append(content);
		final LinkElement le = getOpenUrl(cp);
		if (le != null) {
			sb.append("<p>").append(le.setTarget("_blank")).append("</p>");
		}
		return sb.toString();
	}

	protected final PrivateMessagePlugin plugin = ((IMessageWebContext) messageContext)
			.getPrivateMessagePlugin();

	@Override
	public JavascriptForward onSent(final ComponentParameter cp, final Set<ID> users,
			final String topic, final String content) {
		for (final ID userId : users) {
			plugin.sentMessage(userId, cp.getLoginId(), topic, toSentContent(cp, content));
		}

		// 保存发件箱
		if (cp.getBoolParameter("opt_sentMark")) {
			createSentMessage(cp, users, topic, content);
		}
		final JavascriptForward js = new JavascriptForward("$Actions['")
				.append(cp.getComponentName()).append("_win'].close();").append("$alert('")
				.append($m("DefaultMNoticeHandler.0")).append("<br>")
				.append(MessageUtils.toRevString(cp, users, true)).append("', null, 210);");
		return js;
	}

	protected P2PMessage createSentMessage(final ComponentParameter cp, final Set<ID> users,
			final String topic, final String content) {
		final P2PMessage message = new P2PMessage();
		final Date sentDate = new Date();
		message.setCreateDate(sentDate);
		message.setMessageMark(plugin.getMark());
		message.setFromId(cp.getLoginId());
		message.setSentDate(sentDate);
		message.setCategory(PrivateMessagePlugin.SENT_MODULE.getName());
		message.setToUsers(MessageUtils.toRevString(cp, users, false));
		message.setTopic(topic);
		message.setContent(content);
		plugin.getMessageService().insert(message);
		return message;
	}
}
