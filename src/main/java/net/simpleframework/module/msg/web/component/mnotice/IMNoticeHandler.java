package net.simpleframework.module.msg.web.component.mnotice;

import java.util.Iterator;
import java.util.List;

import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.module.msg.IMessageContextAware;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IMNoticeHandler extends IComponentHandler, IMessageContextAware {

	/**
	 * 获取发送人范围
	 * 
	 * @param cp
	 * @return
	 */
	Iterator<PermissionUser> allUsers(ComponentParameter cp);

	/**
	 * 打开页面的地址
	 * 
	 * @param cp
	 * @return
	 */
	LinkElement getOpenUrl(ComponentParameter cp);

	JavascriptForward onSent(ComponentParameter cp, List<PermissionUser> users, String topic,
			String content);
}