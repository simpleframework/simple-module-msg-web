var MNoticeLoaded = {

  userselect : function(selects) {
    var rev = $('sm_receiver');
    var val = "";
    selects.each(function(s) {
      if (s.row) {
        val += s.row.getAttribute("_user") + ";";
      } else if (s.branch) {
        val += s["_user"] + ";";
      }
    });
    $Actions.setValue(rev, val, true);
    return true;
  }
};