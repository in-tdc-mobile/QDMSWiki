var text_nodes = "";
CommonFunc.Bookmark = {
    _bookmarkSettings: function () {
        var IsRole = $("#ISDCRLRole").val();

        if (PublishedDoc) {

            return "<span style=\"right:25px\" class=\"icon-btn add-bookmark\" title=\"bookmark\"><svg viewBox=\"0 0 18 18\" x=\"0px\" y=\"0px\" xml:space=\"preserve\"><path fill=\"#fff\" d=\"m10.201,.758l2.478,5.865 6.344,.545c0.44,0.038 0.619,0.587 0.285,0.876l-4.812,4.169 1.442,6.202c0.1,0.431-0.367,0.77-0.745,0.541l-5.452-3.288-5.452,3.288c-0.379,0.228-0.845-0.111-0.745-0.541l1.442-6.202-4.813-4.17c-0.334-0.289-0.156-0.838 0.285-0.876l6.344-.545 2.478-5.864c0.172-0.408 0.749-0.408 0.921,0z\"/></svg>";
            //if (IsRole == "true") {
               
            //}
            //else {
            //    return "<span style=\"right:25px\" class=\"icon-btn add-bookmark\" title=\"bookmark\"><svg viewBox=\"0 0 18 18\" x=\"0px\" y=\"0px\" xml:space=\"preserve\"><path fill=\"#fff\" d=\"m10.201,.758l2.478,5.865 6.344,.545c0.44,0.038 0.619,0.587 0.285,0.876l-4.812,4.169 1.442,6.202c0.1,0.431-0.367,0.77-0.745,0.541l-5.452-3.288-5.452,3.288c-0.379,0.228-0.845-0.111-0.745-0.541l1.442-6.202-4.813-4.17c-0.334-0.289-0.156-0.838 0.285-0.876l6.344-.545 2.478-5.864c0.172-0.408 0.749-0.408 0.921,0z\"/></svg></span>";
            //}
           // return "<span style=\"right:25px\" class=\"icon-btn add-bookmark\" title=\"bookmark\"><svg viewBox=\"0 0 18 18\" x=\"0px\" y=\"0px\" xml:space=\"preserve\"><path fill=\"#fff\" d=\"m10.201,.758l2.478,5.865 6.344,.545c0.44,0.038 0.619,0.587 0.285,0.876l-4.812,4.169 1.442,6.202c0.1,0.431-0.367,0.77-0.745,0.541l-5.452-3.288-5.452,3.288c-0.379,0.228-0.845-0.111-0.745-0.541l1.442-6.202-4.813-4.17c-0.334-0.289-0.156-0.838 0.285-0.876l6.344-.545 2.478-5.864c0.172-0.408 0.749-0.408 0.921,0z\"/></svg></span>";
        }
        else {
            return "";
        }
    },
    _addBookmark: function (bookmarkId, bookmarkTitle) {
        var bookmarkItem = bookmarkId + "##" + bookmarkTitle
        window.webkit.messageHandlers.addBookmark.postMessage(bookmarkItem);
    //        if (bookmarks.filter(x => x.BookmarkId == bookmarkId).length == 0) {
    //            bookmarks.push({ 'BookmarkId': bookmarkId, 'BookmarkTitle': bookmarkTitle });
    //        }
    //COMMENTED FOR MOBILE
    //        criticalNotify('Bookmarked', '', "Success");
    //        ShowBookmarkList(bookmarks);
    },
    _removeBookmark: function (controlId) {
        ConfirmAlert("Do you sure want to remove bookmark?").then(function (answer) {
            if (answer) {
                var bookmarkId = $(controlId).attr("data-bookmarkId");
                if (bookmarkId != undefined) {
                    bookmarks.splice(bookmarks.findIndex(x => x.BookmarkId == bookmarkId), 1);
                    ShowBookmarkList(bookmarks);
                }
            }
        });
    },
    _GetTextNodeOnly: function (rootElement) {
        $.each(rootElement, function (i, node) {
            if (node) {
                node = node.firstChild;
                while (node != null) {
                    if (node.nodeType == 3 && node.parentNode.getAttribute("id") != "commentHeading" && node.parentNode.getAttribute("id") != "popupHeading") {
                        text_nodes = text_nodes + node.nodeValue;
                    } else if (node.nodeType == 1) {
                        if (node.getAttribute('class') != "comment-box" && node.nodeName != "SCRIPT" && node.getAttribute('class') != "templateSettings") {
                            CommonFunc.Bookmark._recursiveWalk(node);
                        }
                    }
                    node = node.nextSibling;
                }
            }
        });
    },
    _recursiveWalk: function (node) {
        if (node) {
            node = node.firstChild;
            while (node != null) {
                if (node.nodeType == 3 && node.parentNode.getAttribute("id") != "commentHeading" && node.parentNode.getAttribute("id") != "popupHeading") {
                    text_nodes = text_nodes + node.nodeValue;
                } else if (node.nodeType == 1) {
                    if (node.getAttribute('class') != "comment-box" && node.nodeName != "SCRIPT" && node.getAttribute('class') != "templateSettings") {
                        CommonFunc.Bookmark._recursiveWalk(node);
                    }
                }
                node = node.nextSibling;
            }
        }
    }
}
//$("#bookmarkTemplate").click(function () {
//    var documentId = $("#Id").val();
//    $.ajax({
//        type: 'POST',
//        url: $("#hfdUrlPath").val() + '/Document/AddBookmarks',
//        data: {
//            'bookmarks': bookmarks, 'documentId': documentId
//        },
//        processData: true,
//        beforeSend: function () {
//        },
//        success: function (result) {
//            criticalNotify('Bookmarks updated successfully', '', "Success");
//        },
//        error: loadJsonError,
//        complete: function () {
//        }
//    });
//});

