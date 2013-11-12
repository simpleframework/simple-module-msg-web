package net.simpleframework.module.msg.web.page.t1;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;
import java.util.Map;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.module.msg.P2PMessage;
import net.simpleframework.module.msg.web.IMessageWebContext;
import net.simpleframework.module.msg.web.plugin.PrivateMessagePlugin;
import net.simpleframework.mvc.PageMapping;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ETextAlign;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@PageMapping(url = "/msg/private/mgr")
public class MgrPrivateMessagePage extends AbstractMgrMessagePage {

	public static final String COL_FROMID = "fromId";

	public static final String COL_MESSAGESTATUS = "messageStatus";

	@Override
	protected PrivateMessagePlugin getMessageMark(final PageParameter pp) {
		return ((IMessageWebContext) context).getPrivateMessagePlugin();
	}

	@Override
	protected void addComponents(final PageParameter pp) {
		super.addComponents(pp);

		final TablePagerBean tablePager = addTablePagerBean(pp, PrivateMessageTbl.class);
		tablePager
				.addColumn(
						TablePagerColumn.col(COL_TOPIC, $m("AbstractMgrMessagePage.0")).setSort(false)
								.setTextAlign(ETextAlign.left))
				.addColumn(
						createUserColumn(pp, COL_FROMID, $m("MyPrivateMessageTPage.0"),
								"AbstractMessageMgrPage_tbl").setWidth(80).setTextAlign(ETextAlign.left))
				.addColumn(
						createUserColumn(pp, COL_USERID, $m("AbstractMyMessageTPage.2"),
								"AbstractMessageMgrPage_tbl").setWidth(80).setTextAlign(ETextAlign.left))
				.addColumn(new TablePagerColumn(COL_MESSAGESTATUS, $m("MgrPrivateMessagePage.0"), 80))
				.addColumn(
						new TablePagerColumn(COL_CREATEDATE, $m("AbstractMgrMessagePage.1"), 120)
								.setPropertyClass(Date.class))
				.addColumn(TablePagerColumn.OPE().setWidth(80));

		// 用户选择
		addUserSelectForTbl(pp, "AbstractMessageMgrPage_tbl");
		addUserSelectForTbl(pp, "AbstractMessageMgrPage_tbl", COL_FROMID);
	}

	public static class PrivateMessageTbl extends MgrMessageTbl {

		@Override
		protected Map<String, Object> getRowData(final ComponentParameter cp, final Object dataObject) {
			final P2PMessage msg = (P2PMessage) dataObject;
			final KVMap kv = (KVMap) super.getRowData(cp, dataObject);
			kv.put(COL_FROMID, toIconUser(cp, msg.getFromId()));
			kv.put(COL_USERID, toIconUser(cp, msg.getUserId()));
			kv.put(COL_MESSAGESTATUS, msg.getMessageStatus());
			return kv;
		}
	}
}
