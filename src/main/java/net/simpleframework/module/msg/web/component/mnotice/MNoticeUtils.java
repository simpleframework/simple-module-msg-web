package net.simpleframework.module.msg.web.component.mnotice;

import static net.simpleframework.common.I18n.$m;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.EInputType;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.RowField;
import net.simpleframework.mvc.common.element.TableRow;
import net.simpleframework.mvc.common.element.TableRows;
import net.simpleframework.mvc.common.element.TextButton;
import net.simpleframework.mvc.component.AbstractComponentRender;
import net.simpleframework.mvc.component.AbstractComponentRender.IJavascriptCallback;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class MNoticeUtils {
	public static final String BEAN_ID = "msg_manual_notice_@bid";

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static String toParams(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append(BEAN_ID).append("=").append(cp.hashId());
		return sb.toString();
	}

	public static void doForword(final ComponentParameter cp) throws Exception {
		AbstractComponentRender.doJavascriptForward(cp, new IJavascriptCallback() {
			@Override
			public void doJavascript(final JavascriptForward js) throws Exception {
				js.append("$Actions['").append(cp.getComponentName()).append("_win']('")
						.append(toParams(cp)).append("');");
			}
		});
	}

	public static String toSentbarHTML(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='left'>");
		sb.append(new ButtonElement("查看打开地址"));
		sb.append("</div>");
		sb.append("<div class='right'>");
		sb.append(ButtonElement.closeBtn());
		sb.append("</div>");
		return sb.toString();
	}

	public static TableRows toSentTableRows(final ComponentParameter cp) {
		final TextButton sm_receiver = new TextButton("sm_receiver")
				.setInputType(EInputType.textarea).setEditable(true)
				.setOnclick("$Actions['MNoticeLoaded_userSelect']();").setAutoRows(true).setRows(1)
				.setReadonly(!(Boolean) cp.getBeanProperty("receiverEnable"));
		final InputElement sm_topic = new InputElement("sm_topic").setReadonly(!(Boolean) cp
				.getBeanProperty("topicEnable"));
		final InputElement sm_content = InputElement.textarea("sm_content").setRows(12);

		final TableRow r1 = new TableRow(
				new RowField($m("PrivateMessageSentPage.0"), sm_receiver).setStarMark(true));
		final TableRow r2 = new TableRow(
				new RowField($m("PrivateMessageSentPage.1"), sm_topic).setStarMark(true));
		final TableRow r3 = new TableRow(new RowField("", sm_content));
		return TableRows.of(r1, r2, r3);
	}
}
