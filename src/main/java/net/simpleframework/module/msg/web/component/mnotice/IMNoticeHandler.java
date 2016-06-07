package net.simpleframework.module.msg.web.component.mnotice;

import java.util.Set;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.ID;
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
	IDataQuery<PermissionUser> allUsers(ComponentParameter cp);

	String getReceiver(ComponentParameter cp);

	String getTopic(ComponentParameter cp);

	String getContent(ComponentParameter cp);

	/**
	 * 打开页面的地址
	 * 
	 * @param cp
	 * @return
	 */
	LinkElement getOpenUrl(ComponentParameter cp);

	JavascriptForward onSent(ComponentParameter cp, Set<ID> users, String topic, String content);
}