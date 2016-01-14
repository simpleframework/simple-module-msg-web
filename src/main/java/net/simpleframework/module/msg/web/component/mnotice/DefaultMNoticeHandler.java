package net.simpleframework.module.msg.web.component.mnotice;

import java.util.Iterator;

import net.simpleframework.ctx.permission.PermissionUser;
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
	public Iterator<PermissionUser> allUsers(final ComponentParameter cp) {
		// 缺省取当前部门的用户
		return cp.getLdept().users();
	}

	@Override
	public LinkElement getOpenUrl(final ComponentParameter cp) {
		return null;
	}
}
