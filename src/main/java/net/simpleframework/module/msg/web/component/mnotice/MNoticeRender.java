package net.simpleframework.module.msg.web.component.mnotice;

import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MNoticeRender extends ComponentJavascriptRender {

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("alert(1);");
		return sb.toString();
	}
}
