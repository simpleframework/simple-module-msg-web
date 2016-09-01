package net.simpleframework.module.msg.web;

import static net.simpleframework.common.I18n.$m;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.FileUtils;
import net.simpleframework.common.ID;
import net.simpleframework.ctx.IApplicationContext;
import net.simpleframework.ctx.ModuleFunctions;
import net.simpleframework.module.msg.IMessageContext;
import net.simpleframework.module.msg.MessageContextSettings;
import net.simpleframework.module.msg.impl.MessageContext;
import net.simpleframework.module.msg.web.page.MyNoticeMessageTPage;
import net.simpleframework.module.msg.web.page.t1.MgrNoticeMessagePage;
import net.simpleframework.module.msg.web.plugin.NoticeMessageWebPlugin;
import net.simpleframework.module.msg.web.plugin.PrivateMessagePlugin;
import net.simpleframework.module.msg.web.plugin.SystemMessagePlugin;
import net.simpleframework.mvc.MVCUtils;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.common.element.SupElement;
import net.simpleframework.mvc.ctx.WebModuleFunction;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MessageWebContext extends MessageContext implements IMessageWebContext {

	@Override
	public void onInit(final IApplicationContext application) throws Exception {
		super.onInit(application);

		getPluginRegistry().registPlugin(NoticeMessageWebPlugin.class);
		getPluginRegistry().registPlugin(SystemMessagePlugin.class);
		getPluginRegistry().registPlugin(PrivateMessagePlugin.class);
	}

	@Override
	public PrivateMessagePlugin getPrivateMessagePlugin() {
		return (PrivateMessagePlugin) getPluginRegistry().getPlugin(PRIVATEMESSAGE_MARK);
	}

	@Override
	public SystemMessagePlugin getSystemMessagePlugin() {
		return (SystemMessagePlugin) getPluginRegistry().getPlugin(SYSTEMMESSAGE_MARK);
	}

	@Override
	public ModuleFunctions getFunctions() {
		return ModuleFunctions.of(
				new WebModuleFunction(this, MgrNoticeMessagePage.class)
						.setName(MODULE_NAME + "-MessageMgrPage").setText($m("MessageWebContext.0")),
				new WebModuleFunction(this)
						.setUrl(getUrlsFactory().getUrl(null, MyNoticeMessageTPage.class))
						.setName(MODULE_NAME + "-MyMessagePage").setText($m("MessageContext.0"))
						.setDisabled(true));
	}

	@Override
	public AbstractElement<?> toMyMessageElement(final PageParameter pp) {
		return toMyMessageElement(pp, 20, -3);
	}

	@Override
	public AbstractElement<?> toMyMessageElement(final PageParameter pp, final int left,
			final int top) {
		final WebModuleFunction f = (WebModuleFunction) getFunctionByName(
				MODULE_NAME + "-MyMessagePage");
		final LinkElement link = new LinkElement(f.getText()).setHref(f.getUrl())
				.addStyle("position: relative;");
		final ID loginId = pp.getLoginId();
		final int count = getP2PMessageService().getUnreadMessageCount(loginId)
				+ getSubscribeMessageService().getUnreadMessageCount(loginId);
		if (count > 0) {
			link.addElements(new SupElement(count).setHighlight(true)
					.addStyle("position: absolute; left: " + left + "px; top: " + top + "px;"));
		}
		return link;
	}

	@Override
	public MessageUrlsFactory getUrlsFactory() {
		return singleton(MessageUrlsFactory.class);
	}

	@Override
	public MessageContextSettings getContextSettings() {
		return singleton(_MessageContextSettings.class);
	}

	public static class _MessageContextSettings extends MessageContextSettings {

		static File targetFile = new File(MVCUtils.getRealPath("/WEB-INF/module-msg.xml"));

		public _MessageContextSettings() throws FileNotFoundException {
			super(targetFile);
			if (!targetFile.exists()) {
				try {
					FileUtils.copyFile(
							ClassUtils.getResourceAsStream(IMessageContext.class, "module-msg.xml"),
							targetFile);
				} catch (final IOException e) {
					getLog().warn(e);
				}
			}
		}

		@Override
		protected void save() throws IOException {
			getDocument().saveToFile(targetFile);
		}
	}
}
