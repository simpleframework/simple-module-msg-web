package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.LinkedHashSet;
import java.util.Set;

import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.logger.LogFactory;
import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.mvc.PageParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class MessageUtils {

	public static String toRevString(final PageParameter pp, final String users,
			final boolean showText) {
		return toRevString(pp, toRevSet(pp, StringUtils.split(users, ";")), showText);
	}

	public static String toRevString(final PageParameter pp, final Set<ID> users,
			final boolean showText) {
		final StringBuilder sb = new StringBuilder();
		int i = 0;
		for (final ID userId : users) {
			if (i > 0) {
				sb.append(";");
			}
			final PermissionUser user = pp.getUser(userId);
			if (user.exists()) {
				if (showText) {
					sb.append(user.getText()).append(" (").append(user.getName()).append(")");
				} else {
					sb.append(user.getName());
				}
				i++;
			}
		}
		return sb.toString();
	}

	public static Set<ID> toRevSet(final PageParameter pp, final String[] rlist) {
		final Set<ID> users = new LinkedHashSet<>();
		for (String rev : rlist) {
			rev = rev.trim();
			if (!StringUtils.hasText(rev)) {
				continue;
			}
			final int ps = rev.indexOf("(");
			final int pe = rev.indexOf(")");
			if (ps > -1 && pe > ps) {
				rev = rev.substring(ps + 1, pe);
			}
			final PermissionUser user = pp.getUser(rev.trim());
			if (user.exists()) {
				users.add(user.getId());
			} else {
				LogFactory.getLogger(MessageUtils.class).warn($m("PrivateMessageSentPage.4", rev));
				// throw ComponentHandlerException.of($m("PrivateMessageSentPage.4",
				// rev));
			}
		}
		return users;
	}
}
