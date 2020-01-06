$(document).ready(function () {
	$(".btnApplyTextSettings").off();
	$(".btnApplyTextSettings").click(function (e) {
	    var settings = $(e.target.closest(".templateSettings"));
	    var targetP = $(e.target.closest(".templateSettings").closest("td")).find(".toc-text");
	    var font = settings.find("select[name='text-fontfamily']").val();

		var leftpadding, rightpadding, toppadding, bottompadding, padding;
		toppadding = settings.find(".text-column-padding-top").val();
		rightpadding = settings.find(".text-column-padding-right").val();
		bottompadding = settings.find(".text-column-padding-bottom").val();
		leftpadding = settings.find(".text-column-padding-left").val();
		padding = (toppadding != "" ? (toppadding + "px") : "0px") + " " + (rightpadding != "" ? (rightpadding + "px") : "0px") + " " + (bottompadding != "" ? (bottompadding + "px") : "0px") + " " + (leftpadding != "" ? (leftpadding + "px") : "0px");

		var style = '';
		if (font != "0")
			style = "font-family:'" + font + "';";

		targetP.attr("style", style);
		targetP.css("padding", padding);
		
	});

});