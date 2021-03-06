package net.simpleframework.module.msg.web.page;

import static net.simpleframework.common.I18n.$m;

import net.simpleframework.common.web.html.HtmlUtils;
import net.simpleframework.module.msg.IMessageContextAware;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.BlockElement;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.Checkbox;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;
import net.simpleframework.mvc.template.lets.FormTableRowTemplatePage;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractMessagePage extends FormTableRowTemplatePage
		implements IMessageContextAware {

	@Override
	public boolean isButtonsOnTop(final PageParameter pp) {
		return true;
	}

	@Override
	public String getLabelWidth(final PageParameter pp) {
		return "75px";
	}

	public static abstract class AbstractSentMessagePage extends AbstractMessagePage {

		protected void addSmileyDictionary(final PageParameter pp) {
			pp.addComponentBean("AbstractSentMessagePage_smiley", DictionaryBean.class)
					.setBindingId("sm_content").addSmiley(pp);
		}

		protected String getContent(final PageParameter pp) {
			String c = pp.getParameter("sm_content");
			if (pp.getBoolParameter("sm_autolink")) {
				c = HtmlUtils.autoLink(c);
			}
			return c;
		}

		@Override
		protected ButtonElement SAVE_BTN() {
			return super.SAVE_BTN().setText($m("AbstractSentMessagePage.2"));
		}

		protected final BlockElement sm_content_bar = new BlockElement()
				.setClassName("sm_content_bar").addElements(
						new LinkElement($m("AbstractSentMessagePage.0"))
								.setOnclick("$Actions['AbstractSentMessagePage_smiley']();"),
						new BlockElement().addStyle("float: right")
								.addElements(new Checkbox("sm_autolink", $m("AbstractSentMessagePage.1"))
										.setChecked(true)),
						BlockElement.CLEAR);
	}
}
