package net.simpleframework.module.msg.web;

import net.simpleframework.module.msg.IMessageContext;
import net.simpleframework.module.msg.web.plugin.PrivateMessagePlugin;
import net.simpleframework.module.msg.web.plugin.SystemMessagePlugin;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.AbstractElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IMessageWebContext extends IMessageContext {

	MessageUrlsFactory getUrlsFactory();

	/**
	 * 定义我的消息html元素
	 * 
	 * @param pp
	 * @return
	 */
	AbstractElement<?> toMyMessageElement(final PageParameter pp);

	PrivateMessagePlugin getPrivateMessagePlugin();

	SystemMessagePlugin getSystemMessagePlugin();
}
