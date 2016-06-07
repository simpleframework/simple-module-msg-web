<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.module.msg.web.component.mnotice.MNoticeUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ComponentRenderUtils"%>
<%
	ComponentParameter nCP = MNoticeUtils.get(request, response);
%>
<div class="mnotice_sent">
  <%=ComponentRenderUtils.genParameters(nCP)%>
  <div class="senttb clearfix">
    <%=MNoticeUtils.toSentbarHTML(nCP)%>
  </div>
  <%=MNoticeUtils.toSentTableRows(nCP)%>
  <div class="sm_atten">#(PrivateMessageSentPage.3)</div>
</div>
<script type="text/javascript">
  $ready(function() {
    var receiver = $("sm_receiver");
    if (receiver && receiver.getAttribute('autorows') == 'true') {
      receiver.style.height = '0px';
      receiver.style.height = receiver.scrollHeight + 'px';
    }
  });
</script>