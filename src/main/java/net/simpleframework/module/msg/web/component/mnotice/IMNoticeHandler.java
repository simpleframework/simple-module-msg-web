package net.simpleframework.module.msg.web.component.mnotice;

import java.util.Enumeration;

import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IMNoticeHandler extends IComponentHandler {

	/**
	 * 获取发送人范围
	 * 
	 * @param cp
	 * @return
	 */
	Enumeration<PermissionUser> allUsers(ComponentParameter cp);
}