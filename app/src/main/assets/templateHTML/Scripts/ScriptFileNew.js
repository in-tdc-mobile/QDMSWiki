


var CommonFunc = CommonFunc || {};

var QDMS = QDMS || {};
var rootUrl = '';
var viewMode = '';
var bookmarks = [];
var PublishedDoc = true; // by default is false but for qdms wiki only publushed doc
var contextMenu = false;
var pinnedArray = [];
var UserId = '';
var AppId = '';
var notification = '';
var docPrintMode = "normal";
var tags = [];
var docSkipCount = 0;
var draftSkipCount = 0;
var homeDraftSkipCount = 0;
var homeDocSkipCount = 0;
var articleSkipCount = 0;
var articleDraftSkipCount = 0;
var articleTab = "publised";
var documentTab = "publised";
var notificationInterval = "";
var flagoo = 0;
var _articlesLoderHtmlTiny = "<tr><td><div class=\"tiny-loader\"><div class=\"loadernew loader--style3\" title=\"Loading\"><svg version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" width=\"30px\" height=\"30px\" viewBox=\"0 0 50 50\" style=\"enable-background:new 0 0 50 50;\" xml:space=\"preserve\"><path fill=\"#000\" d=\"M43.935,25.145c0-10.318-8.364-18.683-18.683-18.683c-10.318,0-18.683,8.365-18.683,18.683h4.068c0-8.071,6.543-14.615,14.615-14.615c8.072,0,14.615,6.543,14.615,14.615H43.935z\" transform=\"rotate(300 25 25)\">";
_articlesLoderHtmlTiny = _articlesLoderHtmlTiny + "<animateTransform attributeType=\"xml\" attributeName=\"transform\" type=\"rotate\" from=\"0 25 25\" to=\"360 25 25\" dur=\"0.6s\" repeatCount=\"indefinite\"></animateTransform></path></svg></div></div></td></tr>";
function RenderDocView() {
    // $("#contentWindow").html("");
    // var url = $("#hfdUrlPath").val() + '/document/ViewDoc?id=' + id;

      // remove Temp tr on article toggle
    try{$("#tableDragger").find(".article-table>tbody>tr.temp-tr.hide-article").remove();
    $('.toggle-article').each(function () {
          var ID = $(this).closest("tr.draggable-item").find(".article-table").attr("id");
          $("#" + ID).find('tr:not(.temp-tr)').remove();

          });

    $(".viewdoc").scroll(function () {
                    //    androidAppProxy.logdata("scroll function called")
                         //ArticleData($("#5a3baaa73b76de674c38a711"), false, false, "5a3cdc043b6a9e83988dfd2b");
                         var viewRangeStart = $(this).offset().top;
                         var viewRangeEnd = viewRangeStart + $(this).height();
                         var eleTop = $(this).offset().top;
                         var eleBottom = eleTop + $(this).height();
                         $(this).find(".article-table[data-visible='false']").filter(function () {
                            alert("scroll ArticleData retrivel ");
                             var viewRangeStart1 = $(this).offset().top;
                             var viewRangeEnd1 = viewRangeStart1 + $(this).height();
                             var eleTop = $(this).offset().top;
                             var eleBottom = eleTop + $(this).height();
                             //                                                                                     console.log("inside")
                             //                                                                                     console.log("viewRangeStart")
                             //                                                                                     console.log(viewRangeStart1)
                             //                                                                                     console.log("viewRangeEnd")
                             //                                                                                     console.log(viewRangeEnd1)
                             //                                                                                     console.log("eleTop")
                             //                                                                                     console.log(eleTop)
                             //                                                                                     console.log("eleBottom")
                             //                                                                                     console.log(eleBottom)

                             if ((eleBottom <= viewRangeEnd1) && (eleTop >= viewRangeStart1)) {
                             if ($(this).attr("data-visible") == "false") {
                             $(this).attr("data-visible", true);
                             androidAppProxy.logdata("article data called now");
                             ArticleData($(this));
                             }
                             else{
                             //new else
                            androidAppProxy.logdata("article data not called");
                             }
                             }
                             else{
                             alert("other");
                             androidAppProxy.logdata("article data not called");
                             }
                             });
                         });
                         }catch(e){
                         androidAppProxy.logdata("articledatacatch  "+e.message);
                         }
}


function afterRenderDocView(isArticle = false){
   try{
    $("#tableDragger").find(".article-table>tbody>tr.temp-tr.hide-article").remove();
    if (isArticle == false ){
    try{
    $("#tableDragger tr.draggable-item").children("td").find(".bookmark-settings:first").html(CommonFunc.Bookmark._bookmarkSettings());
            $("#tableWrapper").find(".bookmark-settings").show();
    }
    catch(e){
    androidAppProxy.logdata("newbooklog"+e.message);
    }
    }



    $("#tableWrapper").find('table').removeClass('NewDataArt');
    SetDocMode("view");
    $(".toggle-article").click(function () {
                               ToggleExpand($(this).closest("tr.draggable-item"), $(this).closest("tr.draggable-item").attr("toc-title"));
                               //                               $(this).closest("tr.draggable-item").find(".article-table").attr("data-visible", true);
                               //                               $(this).toggleClass('opentoggle');
                               //                               if ($(this).closest("tr.draggable-item").find(".article-table").hasClass("NewDataArt") == false) {
                               //                               var loaderNew = $('body').find('.loadernew')[0]
                               //                               if (!$(this).closest("tr.draggable-item").find(".article-table").find(".loadernew").length)
                               //                               $(this).closest("tr.draggable-item").find(".article-table").find('.insertMeidum h1.subheaderText').append(loaderNew)
                               //                               var id = $(this).closest("tr.draggable-item").find(".article-table").attr("id")
                               //                               ArticleData($(this).closest("tr.draggable-item").find(".article-table"), false, false, id)
                               //                               }

                               });
    $("#tableWrapper").on("click", '.add-bookmark', function (event) {
                          var bookmarkTitle = "";
                          var bookmarkId = "";
                          text_nodes = "";
                          bookmarkId = $(this).closest(".bookmark-settings").closest("table").closest("tr").attr("id");
                          if (bookmarkId == "" || bookmarkId == undefined) {
                          bookmarkId = $(this).closest("tr").attr("id");
                          }
                          var header = $(this).closest(".bookmark-settings").closest(".draggable-item").find(":header");
                          var image = $(this).closest(".bookmark-settings").closest(".draggable-item").find("img");
                          var plainText = $(this).closest(".bookmark-settings").closest("table.draggable-item").find(">tbody>tr>td").find(">:last-child");
                          if (header.length) {
                          CommonFunc.Bookmark._GetTextNodeOnly(header);
                          bookmarkTitle = text_nodes;
                          if (bookmarkTitle == "") {
                          var th = $(this).closest(".bookmark-settings").closest(".draggable-item").find("th");
                          CommonFunc.Bookmark._GetTextNodeOnly(th);
                          bookmarkTitle = text_nodes;
                          }
                          }
                          if (plainText.length && bookmarkTitle == "") {
                          //CommonFunc.Bookmark._GetTextNodeOnly(plainText);
                          bookmarkTitle = plainText.text().length > 40 ? plainText.text().substr(0, 40) + "..." : plainText.text();
                          }
                          if (image.length && header.length == 0) {
                          bookmarkTitle = $(image).attr("src");
                          }
                          CommonFunc.Bookmark._addBookmark(bookmarkId, bookmarkTitle);
                          });}catch(e){
                          androidAppProxy.logdata("newbooklog"+e.message);
                          }
}
function ArticleData(control) {
    try {
        var Id = control.attr("id");
        $('.tableDetails-wrapper').find("#" + Id).find('tr').remove();
        $('.tableDetails-wrapper').find("#" + Id).find('tbody').html("");
        var obj = {
            "id":Id,
            "data":"",
            "name":""
        };
        androidAppProxy.getArtcileData(Id);
        androidAppProxy.logdata("article loading>>    "+Id);
    }
    catch (e) {
androidAppProxy.logdata("article loading error"+e.message);
         alert("ArticleData retrivel error " );
    }
}
function highlightbookmarks(ids) {
    androidAppProxy.logdata("highlightbookmarksfunction called");
    try {
      $(ids).find(".add-bookmark").addClass("selected");
    }
    catch (e) {
        androidAppProxy.logdata("highlightbookmarks  error"+e.message);
    }


}
function removehighlightedbookmarks(ids) {
    androidAppProxy.logdata("removehighlightedbookmarks called");
    try {
      $(ids).find(".add-bookmark").removeClass("selected");
    }
    catch (e) {
        androidAppProxy.logdata("highlightbookmarks  error"+e.message);
    }


}




//For iOS Render Article Method

function setArticleDataFromViewController(id){
try{
    var data =androidAppProxy.getArticleData(id);
    var curArticleObj = $("#" + id);
    if (data != null) {
        curArticleObj.find(">tbody>tr").remove(); // remove old article data
        // SetArticleBody("1111111", curArticleObj.find(">tbody"));
        try{SetArticleBody(data, curArticleObj.find(">tbody"));}catch(e){}
        //SetArticleBody(result.DocumentData, curArticleObj.find(">tbody"));  //NEED TO UNCOMMENT
        ToggleExpand(curArticleObj.closest("tr.draggable-item"), "article");
        ArticleExpand(curArticleObj.closest("tr.draggable-item"));
        curArticleObj.find(".drg-rmv").html("");
        curArticleObj.find(".drg-rmv-settings").html("");
        curArticleObj.find(".drg-rmv-toc").html("");
        curArticleObj.find(".drg-rmv-settings-toc").html("");
        curArticleObj.find(".drg-rmv").hide();
        curArticleObj.find(".drg-rmv-settings").hide();
        curArticleObj.find(".drg-rmv-toc").hide();
        curArticleObj.find(".drg-rmv-settings-toc").hide();
        curArticleObj.find(".drg-rmv-copy").hide();
        curArticleObj.find(".drg-rmv-settings-copy").hide();
        curArticleObj.find(".drg-rmv-toc-copy").hide();
        curArticleObj.find(".drg-rmv-settings-toc-copy").hide();
        curArticleObj.find(".drg-rmv-addrow-deleterow-toc-copy").hide();
        curArticleObj.find(".drg-rmv-addrow-deleterow-withsubrow").hide();
        curArticleObj.find(".dt-img").hide();
        curArticleObj.find(".templateSettings").hide();
        curArticleObj.find(".ui-resizable-handle").hide();
        curArticleObj.find("#header").hide();
        curArticleObj.find("#footerTemplate").hide();
        curArticleObj.find("*[contenteditable]").attr("contenteditable", false);
        curArticleObj.find('grammarly-btn').remove();



        // Remove Gramamer tab from browser Extension

        // highligh word if user serch keywork with .higlighrange

        if ($("#View_search_field").val() != "" && $("#View_search_field").val() != "undefined" && $("#View_search_field").val() !=undefined) {
            var _searchTerm = $("#View_search_field").val();
            ////Highlight search term inside a specific context
            $(curArticleObj).unmark().mark(_searchTerm);
        }


        setTimeout(function () {
//                                      curArticleObj.find(".two-column .imgresource").resizable();
//                                      curArticleObj.find(".two-column .imgresource").resizable('destroy');
                                      if ($("#pageSize").val() == "A4") {
                                      $("#tableDragger").find(".ul-box").removeClass("Procedure");
                                      $("#tableDragger").find(".ol-box").removeClass("Procedure");
                                      $("#tableDragger").find(".ul-alpha-box").removeClass("Procedure");
                                      $("#tableDragger").find(".ul-hyp-box").removeClass("Procedure");

                                      $("#tableDragger").find(".ul-box").addClass("guidlisce");
                                      $("#tableDragger").find(".ol-box").addClass("guidlisce");
                                      $("#tableDragger").find(".ul-alpha-box").addClass("guidlisce");
                                      $("#tableDragger").find(".ul-hyp-box").addClass("guidlisce");
                                      }
                                      else if ($("#pageSize").val() == "A5") {
                                      $("#tableDragger").find(".ul-box").addClass("Procedure");
                                      $("#tableDragger").find(".ol-box").addClass("Procedure");
                                      $("#tableDragger").find(".ul-alpha-box").addClass("Procedure");
                                      $("#tableDragger").find(".ul-hyp-box").addClass("Procedure");

                                      $("#tableDragger").find(".ul-box").removeClass("guidlisce");
                                      $("#tableDragger").find(".ol-box").removeClass("guidlisce");
                                      $("#tableDragger").find(".ul-alpha-box").removeClass("guidlisce");
                                      $("#tableDragger").find(".ul-hyp-box").removeClass("guidlisce");
                                      }
                   docSetBodyFont();
                   });

    }
    else {
        ToggleExpand(curArticleObj.closest("tr.draggable-item"), curArticleObj.closest("tr.draggable-item").attr("toc-title"));
    }
    }
    catch(e) {
                  alert("ArticleData object error " );
             }

}

// Article data  Manipulate  from ArticleData function

function SetArticleBody(documentData, tbody) {
    try {var toggleIcon = "<span class=\"toggle-article\"><i class=\"fa fa-caret-right\"></i></span>";
    var Rootclonetbody = tbody.clone();
    Rootclonetbody.html(documentData);

    //var tempcontent = tbody.html(documentData).clone();
    if (Rootclonetbody.find(".comment-display").next().length > 0) {
        if (Rootclonetbody.find(".comment-display").next()[0].nodeName == "SCRIPT") {
            Rootclonetbody.find(".comment-display").each(function () {
                                                         $(this).next().html("");
                                                         });
        }
    }
    Rootclonetbody.find(".comment-display").removeAttr("onclick");
    Rootclonetbody.find('.comment-display').addClass("commenttiphide");
    Rootclonetbody.find(".commentbox-Done").hide();
    Rootclonetbody.find(".commentbox-close").hide();
    Rootclonetbody.find(".headerTemplate").hide();
    Rootclonetbody.find(".footerTemplate").hide();
    tbody.html($(Rootclonetbody).html());
    $(Rootclonetbody).html("");
    tbody.closest("tr.draggable-item").find("td:first").css("position", "relative");  // applicable if any css modification
    tbody.closest(".article-table").addClass("NewDataArt");
    tbody.closest(".article-table").attr("data-visible", true);
     tbody.closest(".article-table").find("tr.temp-tr").remove();
    $(tbody).find('grammarly-btn').remove(); // Remove Gramamer tab from browser Extension
    tbody.closest(".article-table").removeClass("islockarticle");
    if (!tbody.closest("tr.draggable-item").find(".toggle-article").hasClass('opentoggle')) {
        tbody.closest("tr.draggable-item").find(".toggle-article").toggleClass('opentoggle');
    }
            androidAppProxy.logdata("set article body");
    }
    catch(e){
            androidAppProxy.logdata("set article body");
    }
    ///new log
}


function ArticleExpand(control) {
    try{var columnType = control.find(".article-table>tbody>tr:first").attr("data-column");
    control.find(".article-table").removeClass("hide-state");
    control.find(".article-table>tbody>tr").removeClass("hide-article");
    if (columnType != "column-SubHeaderBSM-layout" && columnType != "column-ContentHeader-BSM" && columnType != "column-subheader1Layout-layout")
        control.find(".article-table").find("tr.temp-tr").remove();
        if ($("#tableDragger >tr.flowchartDragger").length > 0) // have flow chart control read diagram from json value.
                        {

                            $("#tableDragger >tr.flowchartDragger").each(function () {
                                var TrID = $(this).find(".flow-chart-editor").attr("id");
                                $(this).find(".flow-chart-editor").removeAttr("onfocusout");
                                if (TrID != "") {
                                    // initalize the GoJs Diagram objectas globaly.
                                    _FlowchartViewPanel(TrID);
                                    window['myDiagram_' + TrID].model = go.Model.fromJson(document.getElementById("mySavedModel_" + TrID).value);

                                }


                            });
                           }
        ///new log
        androidAppProxy.logdata("article expand called");}catch(e){
        }
}

function docSetBodyFont()
{
    try{if ($("#DocumentType").val() == "1") {
        $("#tableDragger").find(".textDragger").each(function () {
                                                     $(this).find("table.draggable-item").addClass("guidlisce");
                                                     $(this).find("table.draggable-item").removeClass("Procedure");
                                                     });
        $("#tableDragger").find(".singleCol-text").each(function () {
                                                        $(this).closest("tr").closest("table.draggable-item").addClass("guidlisce");
                                                        $(this).closest("tr").closest("table.draggable-item").removeClass("Procedure");
                                                        });
        $("#tableDragger").find(".section-class-one").each(function () {
                                                           $(this).closest("tr").closest("table.draggable-item").addClass("guidlisce");
                                                           $(this).closest("tr").closest("table.draggable-item").removeClass("Procedure");
                                                           });
        $("#tableDragger").find(".ul-box").each(function () {
                                                $(this).addClass("guidlisce");
                                                $(this).removeClass("Procedure");
                                                });
        $("#tableDragger").find(".ol-box").each(function () {
                                                $(this).addClass("guidlisce");
                                                $(this).removeClass("Procedure");
                                                });
        $("#tableDragger").find(".ul-alpha-box").each(function () {
                                                      $(this).addClass("guidlisce");
                                                      $(this).removeClass("Procedure");
                                                      });
        $("#tableDragger").find(".ul-hyp-box").each(function () {
                                                    $(this).addClass("guidlisce");
                                                    $(this).removeClass("Procedure");
                                                    });
    } else {
        $("#tableDragger").find(".textDragger").each(function () {
                                                     $(this).find("table.draggable-item").addClass("Procedure");
                                                     $(this).find("table.draggable-item").removeClass("guidlisce");
                                                     });
        $("#tableDragger").find(".singleCol-text").each(function () {
                                                        $(this).closest("tr").closest("table.draggable-item").addClass("Procedure");
                                                        $(this).closest("tr").closest("table.draggable-item").removeClass("guidlisce");
                                                        });
        $("#tableDragger").find(".section-class-one").each(function () {
                                                           $(this).closest("tr").closest("table.draggable-item").addClass("Procedure");
                                                           $(this).closest("tr").closest("table.draggable-item").removeClass("guidlisce");
                                                           });
        $("#tableDragger").find(".ul-box").each(function () {
                                                $(this).addClass("Procedure");
                                                $(this).removeClass("guidlisce");
                                                });
        $("#tableDragger").find(".ol-box").each(function () {
                                                $(this).addClass("Procedure");
                                                $(this).removeClass("guidlisce");
                                                });
        $("#tableDragger").find(".ul-alpha-box").each(function () {
                                                      $(this).addClass("Procedure");
                                                      $(this).removeClass("guidlisce");
                                                      });
        $("#tableDragger").find(".ul-hyp-box").each(function () {
                                                    $(this).addClass("Procedure");
                                                    $(this).removeClass("guidlisce");
                                                    });
    }}catch(e){
    }
}
function _FlowchartViewPanel(ID) {

    var GoJs = go.GraphObject.make;  // for conciseness in defining templates
    $(".flow-chart-editor").css("width", "auto");

    var myDiagram_ID = GoJs(go.Diagram, ID, {

        allowDrop: false,  // must be true to accept drops from the Palette
        "draggingTool.dragsLink": false,
        "draggingTool.isGridSnapEnabled": false,
        "linkingTool.isUnconnectedLinkValid": false,
        "relinkingTool.isUnconnectedLinkValid": false,
        "undoManager.isEnabled": false,
        initialContentAlignment: go.Spot.Center,
    });
    myDiagram_ID.position = new go.Point(0, 100);
    // Define a function for creating a "port" that is normally transparent.
    // The "name" is used as the GraphObject.portId, the "spot" is used to control how links connect
    // and where the port is positioned on the node, and the boolean "output" and "input" arguments
    // control whether the user can draw links from or to the port.

    function nodeStyle() {
        return [
            // The Node.location comes from the "loc" property of the node data,
            // converted by the Point.parse static method.
            // If the Node.location is changed, it updates the "loc" property of the node data,
            // converting back using the Point.stringify static method.
            new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
            {
                // the Node.location is at the center of each node
                locationSpot: go.Spot.Center
            }
        ];
    }
    function textStyle() {
        return {
            font: "bold 11pt Helvetica, Arial, sans-serif",
            stroke: "whitesmoke"
        };
    }


    myDiagram_ID.nodeTemplate =
        GoJs(go.Node, "Spot",
            { locationSpot: go.Spot.Center },
            //{ movable: true},
            new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
            { selectable: false },
            { resizable: false, resizeObjectName: "PANEL" },
            { rotatable: false},
            new go.Binding("angle").makeTwoWay(),
            // the main object is a Panel that surrounds a TextBlock with a Shape
            GoJs(go.Panel, "Auto",
                { name: "PANEL" },
                new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                GoJs(go.Shape, "Rectangle",  // default figure
                    {
                        portId: "", // the default port: if no spot on link data, use closest side
                        fromLinkable: false, toLinkable: false, cursor: "pointer",
                        fill: "white",  // default color
                        strokeWidth: 1
                    },
                    new go.Binding("figure"),
                    new go.Binding("fill")),
                GoJs(go.TextBlock,
                    {
                        font: "11pt Calibri",
                        margin: 8,
                        maxSize: new go.Size(160, NaN),
                        wrap: go.TextBlock.WrapFit,
                        editable: false
                    },
                    new go.Binding("text").makeTwoWay())
            )

        );


    myDiagram_ID.linkTemplate =
        GoJs(go.Link,  // the whole link panel
            { selectable: false },
            { relinkableFrom: false, relinkableTo: false, reshapable: false },
            {
                routing: go.Link.AvoidsNodes,
                curve: go.Link.JumpOver,
                corner: 5,
                toShortLength: 4
            },
            new go.Binding("points").makeTwoWay(),
            GoJs(go.Shape,  // the link path shape
                { isPanelMain: true, strokeWidth: 1 }),
            GoJs(go.Shape,  // the arrowhead
                { toArrow: "Standard", stroke: null }),
            GoJs(go.Panel, "Auto",
                new go.Binding("visible", "isSelected").ofObject(),
                GoJs(go.Shape, "RoundedRectangle",  // the link shape
                    { fill: "#F8F8F8", stroke: null }),
                GoJs(go.TextBlock,
                    {
                        textAlign: "center",
                        font: "10pt Calibri",
                        stroke: "#919191",
                        margin: 2,
                        minSize: new go.Size(10, NaN),
                        editable: false
                    },
                    new go.Binding("text").makeTwoWay())
            )
        );

    myDiagram_ID.nodeTemplateMap.add("Comment",
        GoJs(go.Node, "Auto", nodeStyle(),
            { selectable: false },
            { resizable: false, resizeObjectName: "PANEL" },
            { rotatable: false },

            new go.Binding("angle").makeTwoWay(),
            GoJs(go.Shape, "Rectangle",
                { fill: "white", strokeWidth: 0 }),
            GoJs(go.TextBlock, textStyle(),
                {
                    margin: 5,
                    maxSize: new go.Size(200, NaN),
                    wrap: go.TextBlock.WrapFit,
                    textAlign: "center",
                    editable: false,
                    font: "12pt Calibri",
                    stroke: '#454545'
                },
                new go.Binding("text").makeTwoWay())
            // no ports, because no links are allowed to connect with a comment


        ));

    window['myDiagram_' + ID] = myDiagram_ID;
}

function ToggleExpand(control, articleName) {
    try{var columnType = control.find(".article-table>tbody>tr:first").attr("data-column");

    control
    .each(function () {
          control.find('.toggle-article', this)
          .unbind().click(function () {
                          $(this).toggleClass('opentoggle');
                          try {
                          ///new log
                                      console.log("toggle article function");
                          //remove unwanted p tag
                          if ($(this).closest("tr.draggable-item").find(".article-table > tbody").find('tr:first')[0] != null && $(this).closest("tr.draggable-item").find(".article-table > tbody").find('tr:first')[0].previousElementSibling != null) {
                          if ($(this).closest("tr.draggable-item").find(".article-table > tbody").find('tr:first')[0].previousElementSibling.nodeName == "P") {
                          $(this).closest("tr.draggable-item").find(".article-table > tbody").find('tr:first')[0].previousElementSibling.remove();
                          }
                          }
                          }
                          catch (err)
                          {
                           androidAppProxy.logdata("article expand function called"+err.message);
                          console.log(err.message);
                          }

                          if (!$(this).closest("tr.draggable-item").find(".article-table").hasClass("hide-state")) {
                          var text = control.find(".article-table").find(">tbody>tr:first").find(":header:first").text();
                          text = $(this).closest("tr.draggable-item").attr("toc-title") || text;

                          $(this).closest("tr.draggable-item").find(".article-table").addClass("hide-state");
                          $(this).closest("tr.draggable-item").find(".article-table>tbody>tr").addClass("hide-article");

                          if (columnType != "column-SubHeaderBSM-layout" && columnType != "column-ContentHeader-BSM" && columnType != "column-subheader1Layout-layout") {
                          $(this).closest("tr.draggable-item").find(".article-table>tbody>tr:first").before("<tr class=\"temp-tr\"><td><h2>" + text + "</h2></td></tr>");
                          }

                          else {
                          $(this).closest("tr.draggable-item").attr("toc-title", control.find(".article-table>tbody>tr:first").text().trim());
                          $(this).closest("tr.draggable-item").find(".article-table>tbody>tr:first").removeClass("hide-article");
                          }

                          }
                          else {
                          $(this).closest("tr.draggable-item").find(".article-table").removeClass("hide-state");
                          $(this).closest("tr.draggable-item").find(".article-table>tbody>tr").removeClass("hide-article");
                          if (columnType != "column-SubHeaderBSM-layout" && columnType != "column-ContentHeader-BSM" && columnType != "column-subheader1Layout-layout")
                          $(this).closest("tr.draggable-item").find(".article-table").find("tr.temp-tr").remove();
                          }
                          });
                           androidAppProxy.logdata("article expand function called end");
          });}
          catch(e){
          }
}

// Download file from DOcument view

    function downloadFileObject(fileId) {
//    $(".progressing-loader").show();
//    percentageUpdate();
//
//    var formData = new FormData();
//    formData.append('id', fileId || 0);
//    var xhttp;
//    if (window.XMLHttpRequest) { xhttp = new XMLHttpRequest(); }//code for modern browsers}
//    else { xhttp = new ActiveXObject("Microsoft.XMLHTTP"); }// code for IE6, IE5
//
//    xhttp.open("POST", $("#hfdUrlPath").val() + "/File/DownloadShowfile", true);
//    xhttp.onreadystatechange = function () {
//
//        if (xhttp.readyState == 4) {
//            if (xhttp.status == 200) {
//                StopIntervel();
//                var d = $.Deferred();
//                setTimeout(function () {
//                           gauge.update(100);
//                           d.resolve(true);
//                           }, 1000);
//
//                d.promise();
//                d.then(function () {
//                       var fileName = xhttp.getResponseHeader('File-Name');
//                       var blob = new Blob([xhttp.response], { type: "octet/stream" });
//                       var fileName = fileName;
//                       saveAs(blob, fileName);
//                       setTimeout(function () {
//                                  $(".progressing-loader").hide();
//                                  gauge.update(0);
//                                  }, 1000);
//                       });
//            }
//            else {
//                StopIntervel();
//                $(".progressing-loader").hide();
//                var win = window.open($("#hfdUrlPath").val() + "/Error/FileNotFound");
//                setTimeout(function () {
//                           win.close();
//                           gauge.update(0);
//                           }, 1000);
//            }
//        }
//
//    };
//    xhttp.responseType = "arraybuffer";
//    xhttp.send(formData);
}

// goto  Bookmark
    function gotoElement(id){
    try{$(".viewdoc").scrollTop(0);
    $(".viewdoc").animate({
                          scrollTop: $("#" + id).offset().top - 300
                          }, 0);
    if ($("#" + id).find(".article-table[data-visible='false']").length) {
        $("#" + id).find(".article-table[data-visible='false']").attr("data-visible", true);
        // ArticleData($("#" + id).find(".article-table"));
    }
    }
    catch(e){
    androidAppProxy.logdata("gotobokkmarkerror  "+e.message);
    }
}

    function setFooterDataAfterRender(htmlObject, artNum,artdate,artversion){
    try{if (artNum != "" && artNum != undefined)
        htmlObject.find("#footer #docNum").text(artNum);



    if (artversion != "" && artversion != undefined)
        htmlObject.find("#footer #DocV").text(artversion);

    if (artversion != undefined)
        htmlObject.find("#footer .revision").html(artversion);
    if (artdate != "" && artdate != undefined)
        htmlObject.find("#footer #docDate").text(artdate);}
        catch(e){
        }

}

    function SetDocMode(mode) {
    try{viewMode = mode;
    if (mode == "view") {
        $("body").addClass("show-historyTabStrip show-bookmarkTabStrip").removeClass(" expand-doc-history hide-toolbox");
        $(".drg-rmv-settings").remove();
        $(".drg-rmv").remove();
        $(".drg-rmv-settings-toc").remove();
        $(".drg-rmv-toc").remove();
        $(".drg-rmv-settings-copy").hide();
        $(".drg-rmv-copy").hide();
        $(".drg-rmv-settings-toc-copy").hide();
        $(".drg-rmv-addrow-deleterow-toc-copy").hide();
        $(".drg-rmv-addrow-deleterow-withsubrow").hide();
        $(".drg-rmv-toc-copy").hide();
        $(".rotate-img").hide();
        $("#draggableContainer").addClass("view-mode-container");
        $("#tableDragger").find("*[contenteditable=true]").removeAttr("contenteditable");
    }
    else if (mode == "new") {
        $("body").removeClass("show-historyTabStrip show-bookmarkTabStrip hide-toolbox docInfo-panel-close");
    }
    else if (mode == "edit") {
        $("body").addClass("show-historyTabStrip show-bookmarkTabStrip").removeClass("docInfo-panel-close hide-toolbox expand-doc-history");
        $("#draggableContainer").removeClass("view-mode-container");
    }}
    catch(e){
    }

}
    function RenderLinkDocView(fileId) {
        try{androidAppProxy.getDocumentAttachment(fileId);}
        catch(e){
        }
    }
    function RenderLinkArticleView(fileId) {

         try{androidAppProxy.getArticleAttachment(fileId);}
         catch(e){
         }
    }
    function downloadFileObject(fileId) {
           try{androidAppProxy.getFileAttachment(fileId);}catch(e){}
         //window.webkit.messageHandlers.showAttachedItems.postMessage(fileId);
      }
