$(document).ready(function () {
    $(".btnApplytocSupportControlSettings").off();
    $(".btnApplytocSupportControlSettings").click(function (e) {
        var layoutElement = $(e.target.closest(".draggable-item").closest("tr"));
        layoutElement.attr("toc-title", layoutElement.find(".tocSupportControl-toc-title").val());

        if (($(layoutElement.find(".tocSupportControl-toc-check")).prop('checked')) && ($(layoutElement.find(".tocSupportControl-toc-title").val().trim()) != "")) {
            layoutElement.attr("TOC-Title", layoutElement.find(".tocSupportControl-toc-title").val());
            layoutElement.attr("TOC-Level", layoutElement.find(".tocSupportControl-toc-level").val());
            generateTOC();
            _templateSettingClose = "true";
        }
        else if ($(layoutElement.find(".tocSupportControl-toc-check")).prop('checked') == false) {
            layoutElement.removeAttr("TOC-Title");
            layoutElement.removeAttr("TOC-Level");
            generateTOC();
            _templateSettingClose = "true";
        }
        else if (layoutElement.attr("TOC-Level") != "undefined") {
            _templateSettingClose = "false";
            layoutElement.removeAttr("TOC-Level");
            generateTOC();
            if (layoutElement.find(".tocSupportControl-toc-title").val().trim() == "")
                errorHandler("Warning", "TOC Title is not provided. Please provide TOC Title for modifying TOC");
        }
        else {
            _templateSettingClose = "false";
            errorHandler("warning", "TOC Title is not provided. Please provide TOC Title for modifying TOC");
        }

    });
});
