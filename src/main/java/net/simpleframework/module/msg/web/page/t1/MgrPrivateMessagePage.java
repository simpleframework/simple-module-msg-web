package net.simpleframework.module.msg.web.page.t1;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.module.msg.P2PMessage;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.module.msg.web.plugin.PrivateMessagePlugin;
import net.simpleframework.mvc.PageMapping;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.template.TemplateUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@PageMapping(url = "/msg/private/mgr")
public class MgrPrivateMessagePage extends AbstractMgrMessagePage {

	@Override
	protected PrivateMessagePlugin getMessageMark(final PageParameter pp) {
		return ((IMessageWebContext) messageContext).getPrivateMessagePlugin();
	}

	@Override
	protected void onForward(final PageParameter pp) throws Exception {
		super.onForward(pp);

		final TablePagerBean tablePager = addTablePagerBean(pp, PrivateMessageTbl.class);
		tablePager.addColumn(TC_TOPIC())
				.addColumn(createUserColumn(pp, "fromId", $m("MyPrivateMessageTPage.0")).setWidth(80))
				.addColumn(createUserColumn(pp, "userId", $m("AbstractMyMessageTPage.2")).setWidth(80))
				.addColumn(TC_CATEGORY())
				.addColumn(TC_CREATEDATE().setColumnText($m("AbstractMgrMessagePage.1")))
				.addColumn(TablePagerColumn.OPE(70));

		// 用户选择
		addUserSelectForTbl(pp, "AbstractMessageMgrPage_tbl", "userId");
		addUserSelectForTbl(pp, "AbstractMessageMgrPage_tbl", "fromId");
	}

	public static class PrivateMessageTbl extends MgrMessageTbl {

		@Override
		protected Map<String, Object> getRowData(final ComponentParameter cp,
				final Object dataObject) {
			final P2PMessage msg = (P2PMessage) dataObject;
			final KVMap kv = (KVMap) super.getRowData(cp, dataObject);
			kv.put("fromId", TemplateUtils.toIconUser(cp, msg.getFromId()));
			kv.put("userId", TemplateUtils.toIconUser(cp, msg.getUserId()));
			kv.put("category", msg.getCategory());
			return kv;
		}
	}
}
