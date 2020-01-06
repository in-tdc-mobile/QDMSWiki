var mdown = false;
var msel = [[], []];
var tableSettingsStart = function () {
}

var tableSettingsGetpos = function (o, i) {
    var o = $(o); // get position of current cell               
    msel[0][i] = o.parent().index(); // set row
    msel[1][i] = o.index(); // set column
    return msel;
}

var tableSettingsModsel = function (o) {
    var numsrt = function (a, b) {
        return a - b;
    }
    var r = tableSettingsGetpos(o, 1)[0].slice(0);
    r.sort(numsrt);
    var c = msel[1].slice(0);
    c.sort(numsrt);
    var tbl = $(o).closest(".tbl-Settings");
    $trs = $(tbl).find('tbody tr');
    $('td', $trs).removeClass('hi_td');
    $trs.slice(r[0], r[1] + 1).each(function () {
        $('td', this).slice(c[0] - 1, c[1]).addClass('hi_td');
    });
    $(tbl).find("thead tr th").removeClass('hi_th')
        .slice(c[0], c[1] + 1).addClass('hi_th');
    $(tbl).find("tbody tr th").removeClass('hi_th')
        .slice(r[0], r[1] + 1).addClass('hi_th');
}

var tableSettingsHover = function (ev) {
    if (mdown) tableSettingsModsel(this);
}

var tableSettingsDownUp = function (ev) {
    var tbl = ev.target.closest(".tbl-Settings");
    $(tbl).find('td').removeClass('hi_top hi_bottom hi_left hi_right');
    var allHighlighted = $(tbl).find('.hi_td');
    mdown = (ev.type == 'mousedown') ? 1 : 0;
    tableSettingsGetpos(this, 1 - mdown);
    if (mdown) {
        tableSettingsModsel(this);
    }
    else {
        allHighlighted.each(function (i, item) {
            var index = $(item).index();
            var b = $(tbl).find('td.hi_td:last').addClass("autofill-cover");
            if (!$(item).prev().hasClass('hi_td')) {
                $(item).addClass('hi_left');
            }
            if (!$(item).next().hasClass('hi_td')) {
                $(item).addClass('hi_right');
            }
            if (!$(item).closest('tr').prev().find('td:nth-child(' + (index + 1) + ')').hasClass('hi_td')) {
                $(item).addClass('hi_top');
            }
            if (!$(item).closest('tr').next().find('td:nth-child(' + (index + 1) + ')').hasClass('hi_td')) {
                $(item).addClass('hi_bottom');
            }
        });
    }
}

var $tbl = $(".tbl-Settings"),
    $tblHead = $(".tbl-Settings thead tr");

$(document).ready(function () {
    debugger;
    $(".tbl-Settings td").off();
    $("tbody td", $tbl).on({
        "mousedown": tableSettingsDownUp,
        "mouseup": tableSettingsDownUp,
        "mouseenter": tableSettingsHover,
        "selectstart": tableSettingsStart
    });

    $(".btnApplyTableDimensions").off();
    $(".btnApplyTableDimensions").click(function (e) {
       
        var trCount = 1, tdCount = 0;
        var tbl = $(e.target.closest(".templateSettings")).find(".tbl-Settings");
        var targetTable = $(e.target.closest(".templateSettings").closest("td")).find("#resizeTable");
        var settings = $(e.target.closest(".templateSettings"));
        var RowVal = $(this).parent().find("#SetRowCunt").val();
        if (tbl.find(".hi_td").length > 0) {
            if ($.isNumeric(RowVal) == true && RowVal != "0") {
                trCount = RowVal;
            }
            // trCount = (tbl.find(".hi_td").length / tbl.find(".hi_td:first").closest("tr").find(".hi_td").length);
            tdCount = tbl.find(".hi_td:first").closest("tr").find(".hi_td").length;
        }
        else {
            if ($.isNumeric(RowVal) == true && RowVal != "0") {
                trCount = RowVal;
            }
            tdCount = $("#resizeTable th").length
        }

        if (trCount != 0 && tdCount != 0) {
            var thead = $("<thead/>");
            var trhead = $("<tr/>");
            for (var i = 0; i < tdCount; i++) {
                
                var existdata = targetTable.find("thead> tr ").find("th:eq(" + i + ")>div").html()
                if (existdata != undefined)
                {
                    var th = $("<th class=\"resizeCol\" style='text-align:center;width: 50px'><div contenteditable=\"true\">&nbsp;" + existdata + "</div></th>");
                    //th.append($("<div class='ui-resizable-handle ui-resizable-e' style='z-index: 90;'></div>"));
                    //th.append($("<div class='ui-resizable-handle ui-resizable-e' style='z-index: 90;'></div>"));
                    //th.append($("<div class='ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se' style='z-index: 90;'></div>"));
                    trhead.append(th);
                }
                else
                {
                    var th = $("<th class=\"resizeCol\" style='text-align:center;width: 50px'><div contenteditable=\"true\">&nbsp;</div></th>");
                    //th.append($("<div class='ui-resizable-handle ui-resizable-e' style='z-index: 90;'></div>"));
                    //th.append($("<div class='ui-resizable-handle ui-resizable-e' style='z-index: 90;'></div>"));
                    //th.append($("<div class='ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se' style='z-index: 90;'></div>"));
                    trhead.append(th);
                }
                
            }
            thead.append(trhead);

            var tbody = $("<tbody/>");
            for (var i = 0; i < trCount; i++) {
                var tr = $("<tr/>");
                for (var j = 0; j < tdCount; j++) {
                    var tdData = targetTable.find("tbody> tr:eq(" + (i-1) + ")").find("td:eq(" + j + ")>div").html();
                    if (tdData != undefined)
                    {
                        var td = $("<td style=\"width: 50px;\" contenteditable=\"true\">" + tdData + "</td>");
                        tr.append(td);
                    }
                    else
                    {
                        var td = $("<td style=\"width: 50px;\" contenteditable=\"true\"></td>");
                        tr.append(td);
                    }
                
                   
                }
                tbody.append(tr);
            }

            targetTable.html('');
            targetTable.append(thead).append(tbody);
        }
        fillRowsColumnsTableSettings(targetTable, settings);
        SetTable(targetTable);
        $(".popup-close").trigger('click');
        //$(e.target.closest(".templateSettings")).hide();
    });

    $(".btnApplyTableSettings").off();
    $(".btnApplyTableSettings").click(function (e) {
        var settings = $(e.target.closest(".templateSettings"));
        var targetTable = $(e.target.closest(".templateSettings").closest("td")).find("#resizeTable");
        //Applying Table Settings
        var width = parseInt($(settings).find(".tablewidth").val());
        var widthunit = $(settings).find(".tablewidthunit").val();
        var bordercollapsevalue = parseInt($(settings).find(".tablecollapse").val());
        var border = parseInt($(settings).find(".tableborder").val());
        var cellpadding = parseInt($(settings).find(".tablecellpadding").val());


        //targetTable.removeAttr('style');

        targetTable.css("width", (width + (1 == widthunit ? "%" : "px")));

        if (2 == bordercollapsevalue) {
            targetTable.css("border-collapse", "collapse");
        }
        else {
            targetTable.css("border-collapse", "");
        }

        targetTable.resizable();
        targetTable.find(".resizeCol").resizable();
        targetTable.resizable('destroy');
        targetTable.find(".resizeCol").resizable('destroy');

        targetTable.resizable({
            containment: "#tableDragger",
            stop: function (event, ui) {
                ui.element.closest(".draggable-item").attr("height", ui.helper.outerHeight());
                ui.element.closest(".draggable-item").find("#resizeTable").attr("actual-width", ui.element.closest(".draggable-item").find("#resizeTable").inlineStyle("width"));
            }
        });
        targetTable.find(".resizeCol").resizable({
            containment: "#resizeTable",
            stop: function (event, ui) {
                var totalWidth = 0;
                $(ui.helper).closest("#resizeTable").find(".resizeCol").each(function () {
                    totalWidth = totalWidth + $(this).width();
                });
                if (totalWidth > $("#tableDragger").width())
                    $(ui.helper).closest("#resizeTable").find(".resizeCol").css("width", "50px");

                $(ui.helper).attr("actual-width", $(ui.helper).inlineStyle("width"));
            }
        });

        if (border != "0" && border != "1") {
            targetTable.attr("border", border);
            targetTable.css("border-bottom", 0);
            targetTable.find(">.ui-resizable-s").css("border-bottom", border + "px" + " solid");
            targetTable.find("tr th").css("border", "1px solid black")

            targetTable.find("tr").css("border", "1px solid black");
        }

        if (cellpadding != 0) {
            targetTable.attr("cellpadding", cellpadding);
            targetTable.find('td, th').css("padding", cellpadding);
        }
        $(".popup-close").trigger('click');
        //$(e.target.closest(".templateSettings")).hide();
    });

    $(".btnApplyTableHeaderSettings").off();
    $(".btnApplyTableHeaderSettings").click(function (e) {
        var settings = $(e.target.closest(".templateSettings"));
        var targetTable = $(e.target.closest(".templateSettings").closest("td")).find("#resizeTable");

        //Applying Table Header Settings
        var height = $(settings).find(".header-height").val();
        var border = $(settings).find(".header-border").val();
        var bgcolor = $(settings).find(".header-bgcolor-picker").spectrum("get");
        var fontfamily = $(settings).find("select[name='header-fontfamily']").val();

        var thead = targetTable.find("thead");

        if (bgcolor._a != 0 || bgcolor._b != 0 || bgcolor._r != 0 || bgcolor._g != 0)
            thead.css("background-color", bgcolor ? bgcolor.toRgbString() : "");

        if (border != "0")
            thead.css("border", (border != "" ? (border + "px solid black") : border));

        if (fontfamily != "0")
            var style = "font-family:'" + fontfamily + "'";

        thead.find("div").attr("style", style);

        thead.find("th").css("height", (height != "" ? (height + "px") : height));
    });

    $(".btnApplyRowSettings").off();
    $(".btnApplyRowSettings").click(function (e) {
        var settings = $(e.target.closest(".templateSettings"));
        var targetTable = $(e.target.closest(".templateSettings").closest("td")).find("#resizeTable");
        var border = $(settings).find(".rowborder").val();
        var height = $(settings).find(".rowheight").val();
        //Applying Row Settings
        var selectdRows = $(settings).find(".rowCollection").val();
        selectdRows = $.grep(selectdRows, function (value) {
            return value != 0;
        });
        if (selectdRows.length > 0) {
            var bgcolor = $(settings).find(".row-bgcolor-picker").spectrum("get");
            for (var k = 0, l = selectdRows.length; k < l; k++) {
                var row = parseInt(selectdRows[k]) - 1;

                if (bgcolor != null) {
                    if (bgcolor._a != 0 || bgcolor._b != 0 || bgcolor._r != 0 || bgcolor._g != 0)
                        targetTable.find("tbody tr:eq('" + (row) + "')").css("background-color", bgcolor ? bgcolor.toRgbString() : "");

                }

                targetTable.find("tbody tr:eq('" + (row) + "')").css("font-size", ($(settings).find(".rowfontsize").val() + "px"));

                if (border != "0")
                    targetTable.find("tbody tr:eq('" + (row) + "')").css("border", (border != "" ? (border + "px solid black") : border));

                targetTable.find("tbody tr:eq('" + (row) + "')").css("height", height);
                $('.rowCollection').removeClass('errorinput');
            }
        } else {
            $('.rowCollection').addClass('errorinput');
        }
        //$(e.target.closest(".templateSettings")).hide();
    });

    $(".btnApplyColumnSettings").off();
    $(".btnApplyColumnSettings").click(function (e) {
        var settings = $(e.target.closest(".templateSettings"));
        var targetTable = $(e.target.closest(".templateSettings").closest("td")).find("#resizeTable");

        //Applying Column Settings
        var selectdColumns = $(settings).find(".columnCollection").val();
        selectdColumns = $.grep(selectdColumns, function (value) {
            return value != 0;
        });
        var width = $(settings).find(".columnwidth").val();
        var bgcolor = $(settings).find(".column-bgcolor-picker").spectrum("get");
        if (selectdColumns.length > 0) {
            $('.columnCollection').removeClass('errorinput');
            var trs = targetTable.find("tbody tr");
            for (var k = 0, l = selectdColumns.length; k < l; k++) {
                for (var i = 0, j = trs.length; i < j; i++) {
                    var $tr = $(trs[i]);
                    var col = parseInt(selectdColumns[k]) - 1;

                    if (bgcolor != null) {
                        if (bgcolor._a != 0 || bgcolor._b != 0 || bgcolor._r != 0 || bgcolor._g != 0)
                            $tr.find("td:eq('" + (col) + "')").css("background-color", bgcolor ? bgcolor.toRgbString() : "");
                    }

                    $tr.find("td:eq('" + (col) + "')").css("font-size", ($(settings).find(".columnfontsize").val() + "px"));
                    if (width > 0)
                        $tr.find("td:eq('" + (col) + "')").css("width", (width + "px"));
                }
            }
            var theadTrs = targetTable.find("thead tr");
            for (var k = 0, l = selectdColumns.length; k < l; k++) {
                for (var i = 0, j = theadTrs.length; i < j; i++) {
                    var $tr = $(trs[i]);
                    var col = parseInt(selectdColumns[k]) - 1;
                    if (width > 0)
                        $tr.find("th:eq('" + (col) + "')").css("width", (width + "px"));
                }
            }
        }
        else {
            $('.columnCollection').addClass('errorinput');
        }
        //$(e.target.closest(".templateSettings")).hide();
    });

    $(".btnApplyMergeSettings").off();
    $(".btnApplyMergeSettings").click(function (e) {

        var settings = $(e.target.closest(".templateSettings"));
        var targetTable = $(e.target.closest(".templateSettings").closest("td")).find("#resizeTable");

        //Apply Merging
        var fRow = parseInt($(settings).find(".rowMergeFromCollection").val());
        var tRow = parseInt($(settings).find(".rowMergeToCollection").val());
        var fColumn = parseInt($(settings).find(".columnMergeFromCollection").val());
        var tColumn = parseInt($(settings).find(".columnMergeToCollection").val());
        var twoColSettup = parseInt($(settings).find(".two-col-merge").val());

        if (parseInt($('.fromRow').val()) != 0 && parseInt($('.toRow').val()) != 0 && parseInt($('.fromRow').val()) > parseInt($('.toRow').val())) {
            $('.fromRow').addClass('errorinput');
            return false;
        }
        else { $('.fromRow').removeClass('errorinput'); $('.toRow').removeClass('errorinput'); }


        if (parseInt($('.fromColumn').val()) != 0 && parseInt($('.toColumn').val()) != 0 && parseInt($('.fromColumn').val()) > parseInt($('.toColumn').val())) {
            $('.fromColumn').addClass('errorinput');
            return false;
        }
        else { $('.fromColumn').removeClass('errorinput'); $('.toColumn').removeClass('errorinput'); }



        var flag = 0;

        if (fRow == 0 || tRow == 0 || fColumn == 0 || tColumn == 0) {
            $('.row-merge .tablemergesel').each(function (i, obj) {
                if (this.value == 0) {
                    $(this).addClass('errorinput');
                    flag = 1;


                } else {
                    if (!$(this).is(".errorinput"))
                        $(this).removeClass('errorinput');
                }
            });
        }
        if (flag == 1) {
            return false
        } else {
            $('.row-merge .tablemergesel').removeClass('errorinput');
        }
        var rowspan = (tRow - fRow) + 1;
        var colspan = (tColumn - fColumn) + 1;

        targetTable.find("tbody>tr").each(function (i, n) {

            if (i < tRow && count > 0) {
                if (i == (fRow - 1)) { // remove (toColumn-1) tds from first row

                    for (var k = (fColumn - 1) ; k < (tColumn - 1) ; k++) {
                        $(this).find("td:eq('" + k + "')").addClass("remove-td");
                        targetTable.find("tbody>tr:eq('" + (fRow - 1) + "')").find(">td:eq('" + (tColumn - 1) + "')").find(">div").prepend($(this).find("td:eq('" + k + "')").text());
                    }

                }
                else if (i > (fRow - 1) && i < tRow) { // remove (toColumn) tds from next rows upto toRow
                    for (var j = (fColumn - 1) ; j < tColumn ; j++) {
                        $(this).find("td:eq('" + j + "')").addClass("remove-td");
                        targetTable.find("tbody>tr:eq('" + (fRow - 1) + "')").find(">td:eq('" + (tColumn - 1) + "')").find(">div").append($(this).find("td:eq('" + j + "')").text());
                    }

                }
            }
        });
        targetTable.find("tbody>tr>td.remove-td").remove();
        setTimeout(function () {
            targetTable.find("tbody>tr:eq('" + (fRow - 1) + "')").find(">td:eq('" + (fColumn - 1) + "')").attr("rowspan", rowspan);
            targetTable.find("tbody>tr:eq('" + (fRow - 1) + "')").find(">td:eq('" + (fColumn - 1) + "')").attr("colspan", colspan);

            if (twoColSettup == "1") {
                targetTable.find("tbody>tr:eq('" + (fRow - 1) + "')").find(">td:eq('" + (fColumn - 1) + "')").html('<table class="extra-div"><tbody><tr><td contenteditable="true"></td><td contenteditable="true"></td></tr></tbody></table>');
                targetTable.find("tbody>tr:eq('" + (fRow - 1) + "')").find(">td:eq('" + (fColumn - 1) + "')").removeAttr("contenteditable");
            }

            $(".popup-close").trigger('click');
        }, 100);

        fillRowsColumnsTableSettings(targetTable, settings);
        //$(e.target.closest(".templateSettings")).hide();
    });

    $(".btnApplyCellPaddingSettings").off();
    $(".btnApplyCellPaddingSettings").click(function (e) {
      
        var settings = $(e.target.closest(".templateSettings"));
        var targetTable = $(e.target.closest(".templateSettings").closest("td")).find("#resizeTable");

        //Apply Merging
        var fRow = parseInt($(settings).find(".Cellfrmrow").val());
        var tRow = parseInt($(settings).find(".Celltorow").val());
        var fColumn = parseInt($(settings).find(".Cellfromcol").val());
        var tColumn = parseInt($(settings).find(".Celltocol").val());
        var SideVal = $(settings).find(".cellPaddingsideCollection").val();
        var Pxval = $(this).parent().parent().parent().find("#Cellpaddingsize").val();

        var flag = 0;
        if (fRow == 0 || tRow == 0 || fRow > tRow) {
            $('.Cellfrmrow').addClass('errorinput');
            $('.Celltorow').addClass('errorinput')
            flag = 1;
        }
        else { $('.Cellfrmrow').removeClass('errorinput'); $('.Celltorow').removeClass('errorinput'); }


        if (fColumn == 0 || tColumn == 0 || fColumn > tColumn) {
            $('.Cellfromcol').addClass('errorinput');
            $('.Celltocol').addClass('errorinput')
            flag = 1;
        }
        else { $('.Cellfromcol').removeClass('errorinput'); $('.Celltocol').removeClass('errorinput'); }


        if (flag == 1)
        {
            return false
        }


        for (var j = (fRow - 1) ; j < tRow ; j++) {

            for (var k = (fColumn - 1) ; k < tColumn  ; k++) {

                if (SideVal=="0") {
                    targetTable.find("tbody>tr:eq('" + j + "')").find("td:eq('" + k + "')").css("padding","0px "+ Pxval + "px");
                }
                else if (SideVal=="1") {
                    targetTable.find("tbody>tr:eq('" + j + "')").find("td:eq('" + k + "')").css("padding-left", Pxval + "px");
                }
                else if (SideVal=="2") {
                    targetTable.find("tbody>tr:eq('" + j + "')").find("td:eq('" + k + "')").css("padding-right", Pxval + "px");
                }
                 
            }
        }
        $(".popup-close").trigger('click');
       


    });

    $(".btnApplyColumnMergeSettings").off();
    $(".btnApplyColumnMergeSettings").click(function (e) {

        var settings = $(e.target.closest(".templateSettings"));
        var targetTable = $(e.target.closest(".templateSettings").closest("td")).find("#resizeTable");
        var height = targetTable.find("th").css("height");
        //Apply Merging
        var fRow = 0;//parseInt($(settings).find(".columnMergeFromCollection").val());
        var tRow = 0;//parseInt($(settings).find(".columnMergeToCollection").val());
        var fColumn = parseInt($(settings).find(".fcolmrg").val());
        var tColumn = parseInt($(settings).find(".tcolmrg").val());
        var flag = 0;

        if (fColumn == 0 || tColumn == 0) {
            $('.column-merge .tableclmergesel').each(function (i, obj) {
                if (this.value == 0) {
                    $(this).addClass('errorinput');
                    flag = 1;


                } else {
                    $(this).removeClass('errorinput');
                }
            });
        }
        if (flag == 1) {
            return false
        } else {
            $('.column-merge .tablemergesel').removeClass('errorinput');
        }
        if (parseInt($('.fcolmrg').val()) != 0 && parseInt($('.tcolmrg').val()) != 0 && parseInt($('.fcolmrg').val()) > parseInt($('.tcolmrg').val())) {
            $('.fcolmrg').addClass('errorinput');
            return false;
        }
        else { $('.fcolmrg').removeClass('errorinput'); $('.tcolmrg').removeClass('errorinput'); }


        var rowspan = 1;//(tRow - fRow) + 1;
        var colspan = (tColumn - fColumn) + 1;
        var firstTr = $(targetTable.find("thead tr:eq('" + (0) + "')"));
        var firstTrTds = firstTr.find("th");
        var firstcellindex = 0;
        var $firstcell;
        for (var i = 0; i < firstTrTds.length; i++) {
            var cols = $(firstTrTds[i]).attr("colspan");
            if (cols) {
                colspan = (tColumn - fColumn) + parseInt(cols);

                firstcellindex = firstcellindex + parseInt(cols);
                //$firstcell = $(firstTrTds[i]);
                //break;
            }
            else
                firstcellindex++;

            if (firstcellindex >= fColumn) {

                $firstcell = $(firstTrTds[i]);
                break;
            }
            if ($firstcell)
                break;
        }

        if (rowspan > 1) $firstcell.attr("rowspan", rowspan);
        if (colspan > 1) $firstcell.attr("colspan", colspan);
        for (var k = fRow, l = tRow; k <= l; k++) {
            for (var i = fColumn, j = tColumn; i <= j; i++) {
                if (k == fRow && i == fColumn)
                    continue;
                else {
                    var $cell = $(targetTable.find("thead th:eq('" + (i - 1) + "')"));//.find("td:eq('" + (i - 1) + "')")
                    $cell.addClass("th-merge-remove");
                }
            }
        }
        targetTable.find(".th-merge-remove").remove();
        targetTable.find("thead>tr").each(function () {
            if ($(this).find("th").length == 0)
                $(this).remove();
        });
        targetTable.find("thead>tr>th").each(function () {
            $(this).css("height", height);
        });
        $(".popup-close").trigger('click');
        //fillRowsColumnsTableSettings(targetTable, settings);
        //$(e.target.closest(".templateSettings")).hide();
    });

    $('.fromRow').change(function (e) {
        var settings = $(e.target.closest(".templateSettings"));
        var targetTable = $(e.target.closest(".templateSettings").closest("td")).find("#resizeTable");

        if (parseInt($('.fromRow').val()) != 0 && parseInt($('.toRow').val()) != 0 && parseInt($('.fromRow').val()) > parseInt($('.toRow').val()))
            $('.fromRow').addClass('errorinput');
        else
        { $('.fromRow').removeClass('errorinput'); $('.toRow').removeClass('errorinput'); }
        DynamicfillRowsColumnsTableSettings(targetTable, settings, parseInt($('.fromRow').val()), parseInt($('.toRow').val()));

    });
    $('.toRow').change(function (e) {
        var settings = $(e.target.closest(".templateSettings"));
        var targetTable = $(e.target.closest(".templateSettings").closest("td")).find("#resizeTable");

        if (parseInt($('.fromRow').val()) != 0 && parseInt($('.toRow').val()) != 0 && parseInt($('.fromRow').val()) > parseInt($(this).val()))
            $('.toRow').addClass('errorinput');
        else
        { $('.toRow').removeClass('errorinput'); $('.fromRow').removeClass('errorinput'); }
        DynamicfillRowsColumnsTableSettings(targetTable, settings, parseInt($('.fromRow').val()), parseInt($('.toRow').val()));
    });
    $('.fromColumn').change(function () {
        if (parseInt($('.fromColumn').val()) != 0 && parseInt($('.toColumn').val()) != 0 && parseInt($('.fromColumn').val()) > parseInt($('.toColumn').val()))
            $('.fromColumn').addClass('errorinput');
        else
        {
            $('.fromColumn').removeClass('errorinput'); $('.toColumn').removeClass('errorinput');
            var totalColumn = $(this).closest(".templateSettings").closest(".draggable-item").find("#resizeTable").find("thead tr:first").find("th").length;
            var seletcedColum = parseInt($('.toColumn').val()) - parseInt($('.fromColumn').val());
            if (totalColumn == (seletcedColum + 1))
                $(".two-col-mergeDiv").show();
            else $(".two-col-mergeDiv").hide();
        }

    });
    $('.toColumn').change(function () {
      
        if (parseInt($('.fromColumn').val()) != 0 && parseInt($('.toColumn').val()) != 0 && parseInt($('.fromColumn').val()) > parseInt($('.toColumn').val()))
            $('.toColumn').addClass('errorinput');
        else
        {
            $('.fromColumn').removeClass('errorinput'); $('.toColumn').removeClass('errorinput');
            var totalColumn = $(this).closest(".templateSettings").closest(".draggable-item").find("#resizeTable").find("thead tr:first").find("th").length;
            var seletcedColum = parseInt($('.toColumn').val()) - parseInt($('.fromColumn').val());
            if (totalColumn == (seletcedColum + 1))
                $(".two-col-mergeDiv").show();
            else $(".two-col-mergeDiv").hide();
        }
    });
    $('.fcolmrg').change(function () {
        if (parseInt($('.fcolmrg').val()) != 0 && parseInt($('.tcolmrg').val()) != 0 && parseInt($('.fcolmrg').val()) > parseInt($('.tcolmrg').val()))
            $('.fcolmrg').addClass('errorinput');
        else
        { $('.fcolmrg').removeClass('errorinput'); $('.tcolmrg').removeClass('errorinput'); }

    });
    $('.tcolmrg').change(function () {
        if (parseInt($('.fcolmrg').val()) != 0 && parseInt($('.tcolmrg').val()) != 0 && parseInt($('.fcolmrg').val()) > parseInt($('.tcolmrg').val()))
            $('.tcolmrg').addClass('errorinput');
        else
        { $('.fcolmrg').removeClass('errorinput'); $('.tcolmrg').removeClass('errorinput'); }
    });
});

$(function () {
    $(".tblmaxmin").keyup(function () {
        var max = parseInt($(this).attr('max'));
        var min = parseInt($(this).attr('min'));
        if ($(this).val() > max) {
            $(this).val(max);
        }
        else if ($(this).val() < min) {
            $(this).val(min);
        }
    });
});
$('.columnCollection').change(function () {
    $('.columnCollection').removeClass('errorinput');
});
$('.rowCollection').change(function () {
    $('.rowCollection').removeClass('errorinput');

});
$(document).on('keydown', '[data-toggle=just_number]', function (e) {
    if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
        e.preventDefault();
    }
});
function fillRowsColumnsTableSettings(tbl, templateSettingsDiv) {
    //filling rows in row settings
    var rowCount = tbl.find("tbody tr").length;
    var columnCount = tbl.find("thead tr:first").find("th").length;

    var select = $(templateSettingsDiv).find(".rowCollection");
    select.html('');
    select.append($("<option value='0'>None</option>"));
    for (var i = 0; i < rowCount; i++) {
        var option = document.createElement('option');

        option.value = option.text = (i + 1);
        select.append(option);
    }
    select.val("0");

    //filling columns in row settings
    select = $(templateSettingsDiv).find(".columnCollection");
    select.html('');
    select.append($("<option value='0'>None</option>"));
    for (var i = 0; i < columnCount; i++) {
        var option = document.createElement('option');
        option.value = option.text = (i + 1);
        select.append(option);
    }
    select.val("0");

    //filling columns and rows in merge settings
    select = $(templateSettingsDiv).find(".rowMergeFromCollection");
    select.html($(templateSettingsDiv).find(".rowCollection").html());
    select.val("0");

    select = $(templateSettingsDiv).find(".rowMergeToCollection");
    select.html($(templateSettingsDiv).find(".rowCollection").html());
    select.val("0");

    select = $(templateSettingsDiv).find(".columnMergeFromCollection");
    select.html($(templateSettingsDiv).find(".columnCollection").html());
    select.val("0");

    select = $(templateSettingsDiv).find(".columnMergeToCollection");
    select.html($(templateSettingsDiv).find(".columnCollection").html());
    select.val("0");
}
function DynamicfillRowsColumnsTableSettings(tbl, templateSettingsDiv, fRow, tRow) {
    var columnCount = tbl.find("thead tr:first").find("th").length;
    if (tRow != 0 && fRow != 0) {
        columnCount = tbl.find("tbody tr:eq('" + (fRow - 1) + "')").find(">td").length;
        tbl.find("tbody>tr").each(function (i, n) {
            if (i > (fRow - 1) && i < tRow) {
                if (columnCount < tbl.find("tbody tr:eq('" + i + "')").find(">td").length)
                    columnCount = tbl.find("tbody tr:eq('" + i + "')").find(">td").length;
            }
        });
    }
    //filling columns in row settings
    select = $(templateSettingsDiv).find(".fromColumn");
    select.html('');
    select.append($("<option value='0'>None</option>"));
    for (var i = 0; i < columnCount; i++) {
        var option = document.createElement('option');
        option.value = option.text = (i + 1);
        select.append(option);
    }

    select = $(templateSettingsDiv).find(".toColumn");
    select.html('');
    select.append($("<option value='0'>None</option>"));
    for (var i = 0; i < columnCount; i++) {
        var option = document.createElement('option');
        option.value = option.text = (i + 1);
        select.append(option);
    }
}
function SetTable(tbl) {
    tbl.find("thead>tr>th>div:not([class])").attr("tabindex", "0");
    tbl.find("tbody>tr>td>div").attr("tabindex", "0");

    tbl.resizable();
    tbl.find(".resizeCol").resizable();
    tbl.resizable('destroy');
    tbl.find(".resizeCol").resizable('destroy');

    tbl.resizable({
        containment: "#tableDragger",
        stop: function (event, ui) {
            ui.element.closest(".draggable-item").attr("height", ui.helper.outerHeight());
            ui.element.closest(".draggable-item").find("#resizeTable").attr("actual-width", ui.element.closest(".draggable-item").find("#resizeTable").inlineStyle("width"));
        }
    });
    tbl.find(".resizeCol").resizable({
        containment: "#resizeTable",
        stop: function (event, ui) {
            var totalWidth = 0;
            $(ui.helper).closest("#resizeTable").find(".resizeCol").each(function () {
                totalWidth = totalWidth + $(this).width();
            });
            if (totalWidth > $("#tableDragger").width())
                $(ui.helper).closest("#resizeTable").find(".resizeCol").css("width", "50px");

            $(ui.helper).attr("actual-width", $(ui.helper).inlineStyle("width"));
        }
    });
    tbl.attr("border", (tbl.attr("border") != undefined ? tbl.attr("border") : 1));
    tbl.css("border-bottom", 0);
    tbl.find(">.ui-resizable-s").css("border-bottom", (tbl.attr("border") != undefined && parseInt(tbl.attr("border")) > 1 ? tbl.attr("border") + "px solid" : "0.5px"));
    tbl.find("tr th").css("border", "1px solid black")

    tbl.find("tr").css("border", "1px solid black");
}