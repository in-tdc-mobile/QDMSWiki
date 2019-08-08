package com.mariapps.qdmswiki.home.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mariapps.qdmswiki.SessionManager;
import org.json.JSONArray;
import org.json.JSONObject;


public class HomeDao {

    HomeDBHelper homeDBHelper;
    SessionManager sessionManager;

    public HomeDao(Context context) {
        homeDBHelper = new HomeDBHelper(context);
        sessionManager = new SessionManager(context);
    }

    public void insertdocumentCollectionList(JSONObject jObj, Context context) {
        SQLiteDatabase db = homeDBHelper.getWritableDatabase();
        String html = "<html>\n" +
                "\n" +
                "<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html;charset=windows-1252\">\n" +
                "<title>Example of a simple HTML page</title>\n" +
                "<meta name=\"generator\" content=\"Adobe RoboHelp - www.adobe.com\">\n" +
                "<link rel=\"stylesheet\" href=\"default_ns.css\"><script type=\"text/javascript\" language=\"JavaScript\" title=\"WebHelpSplitCss\">\n" +
                "<!--\n" +
                "if (navigator.appName !=\"Netscape\")\n" +
                "{   document.write(\"<link rel='stylesheet' href='default.css'>\");}\n" +
                "//-->\n" +
                "</script>\n" +
                "<style type=\"text/css\">\n" +
                "<!--\n" +
                "img_whs1 { border:none; width:301px; height:295px; float:none; }\n" +
                "p.whs2 { margin-bottom:5pt; }\n" +
                "p.whs3 { margin-bottom:9.5pt; }\n" +
                "-->\n" +
                "</style><script type=\"text/javascript\" language=\"JavaScript\" title=\"WebHelpInlineScript\">\n" +
                "<!--\n" +
                "function reDo() {\n" +
                "  if (innerWidth != origWidth || innerHeight != origHeight)\n" +
                "     location.reload();\n" +
                "}\n" +
                "if ((parseInt(navigator.appVersion) == 4) && (navigator.appName == \"Netscape\")) {\n" +
                "\torigWidth = innerWidth;\n" +
                "\torigHeight = innerHeight;\n" +
                "\tonresize = reDo;\n" +
                "}\n" +
                "onerror = null; \n" +
                "//-->\n" +
                "</script>\n" +
                "<style type=\"text/css\">\n" +
                "<!--\n" +
                "div.WebHelpPopupMenu { position:absolute; left:0px; top:0px; z-index:4; visibility:hidden; }\n" +
                "p.WebHelpNavBar { text-align:left; }\n" +
                "-->\n" +
                "</style><script type=\"text/javascript\" language=\"javascript1.2\" src=\"whmsg.js\"></script>\n" +
                "<script type=\"text/javascript\" language=\"javascript\" src=\"whver.js\"></script>\n" +
                "<script type=\"text/javascript\" language=\"javascript1.2\" src=\"whproxy.js\"></script>\n" +
                "<script type=\"text/javascript\" language=\"javascript1.2\" src=\"whutils.js\"></script>\n" +
                "<script type=\"text/javascript\" language=\"javascript1.2\" src=\"whtopic.js\"></script>\n" +
                "<script type=\"text/javascript\" language=\"javascript1.2\">\n" +
                "<!--\n" +
                "if (window.gbWhTopic)\n" +
                "{\n" +
                "\tif (window.setRelStartPage)\n" +
                "\t{\n" +
                "\taddTocInfo(\"Building your website\\nCreating an EasySiteWizard 6 website\\nExample of a simple HTML page\");\n" +
                "addButton(\"show\",BTN_TEXT,\"Show\",\"\",\"\",\"\",\"\",0,0,\"whd_show0.gif\",\"whd_show2.gif\",\"whd_show1.gif\");\n" +
                "addButton(\"hide\",BTN_TEXT,\"Hide\",\"\",\"\",\"\",\"\",0,0,\"whd_hide0.gif\",\"whd_hide2.gif\",\"whd_hide1.gif\");\n" +
                "addButton(\"synctoc\",BTN_TEXT,\"Show Topic in Contents\",\"\",\"\",\"\",\"\",0,0,\"whd_sync0.gif\",\"whd_sync2.gif\",\"whd_sync1.gif\");\n" +
                "\n" +
                "\t}\n" +
                "\n" +
                "\n" +
                "\tif (window.setRelStartPage)\n" +
                "\t{\n" +
                "\tsetRelStartPage(\"websiteos.html\");\n" +
                "\n" +
                "\t\tautoSync(0);\n" +
                "\t\tsendSyncInfo();\n" +
                "\t\tsendAveInfoOut();\n" +
                "\t}\n" +
                "\n" +
                "}\n" +
                "else\n" +
                "\tif (window.gbIE4)\n" +
                "\t\tdocument.location.reload();\n" +
                "//-->\n" +
                "</script>\n" +
                "</head>\n" +
                "<body><script type=\"text/javascript\" language=\"javascript1.2\">\n" +
                "<!--\n" +
                "if (window.writeIntopicBar)\n" +
                "\twriteIntopicBar(1);\n" +
                "//-->\n" +
                "</script>\n" +
                "<h1>Example of a simple HTML page</h1>\n" +
                "\n" +
                "<p>Hypertext Markup Language (HTML) is the most common language used to \n" +
                " create documents on the World Wide Web. HTML uses hundreds of different \n" +
                " tags to define a layout for web pages. Most tags require an opening &lt;tag&gt; \n" +
                " and a closing &lt;/tag&gt;.</p>\n" +
                "\n" +
                "<p><span style=\"font-weight: bold;\"><B>Example:</B></span> &nbsp;&lt;b&gt;On \n" +
                " a webpage, this sentence would be in bold print.&lt;/b&gt; </p>\n" +
                "\n" +
                "<p>Below is an example of a very simple page: </p>\n" +
                "\n" +
                "<p><img src=\"htmlpage.jpg\" x-maintain-ratio=\"TRUE\" width=\"301px\" height=\"295px\" border=\"0\" class=\"img_whs1\"></p>\n" +
                "\n" +
                "<p>&nbsp;This \n" +
                " is the code used to make the page: </p>\n" +
                "\n" +
                "<p class=\"whs2\">&lt;HTML&gt; </p>\n" +
                "\n" +
                "<p class=\"whs2\">&lt;HEAD&gt; </p>\n" +
                "\n" +
                "<p class=\"whs2\">&lt;TITLE&gt;Your Title Here&lt;/TITLE&gt; \n" +
                " </p>\n" +
                "\n" +
                "<p class=\"whs2\">&lt;/HEAD&gt; </p>\n" +
                "\n" +
                "<p class=\"whs2\">&lt;BODY BGCOLOR=&quot;FFFFFF&quot;&gt; \n" +
                " </p>\n" +
                "\n" +
                "<p class=\"whs2\">&lt;CENTER&gt;&lt;IMG SRC=&quot;clouds.jpg&quot; \n" +
                " ALIGN=&quot;BOTTOM&quot;&gt; &lt;/CENTER&gt; </p>\n" +
                "\n" +
                "<p class=\"whs2\">&lt;HR&gt; </p>\n" +
                "\n" +
                "<p class=\"whs2\">&lt;a href=&quot;http://somegreatsite.com&quot;&gt;Link \n" +
                " Name&lt;/a&gt; </p>\n" +
                "\n" +
                "<p class=\"whs2\">is a link to another nifty site </p>\n" +
                "\n" +
                "<p class=\"whs2\">&lt;H1&gt;This is a Header&lt;/H1&gt; </p>\n" +
                "\n" +
                "<p class=\"whs2\">&lt;H2&gt;This is a Medium Header&lt;/H2&gt; \n" +
                " </p>\n" +
                "\n" +
                "<p class=\"whs2\">Send me mail at &lt;a href=&quot;mailto:support@yourcompany.com&quot;&gt;</p>\n" +
                "\n" +
                "<p class=\"whs2\">support@yourcompany.com&lt;/a&gt;. </p>\n" +
                "\n" +
                "<p class=\"whs2\">&lt;P&gt; This is a new paragraph! </p>\n" +
                "\n" +
                "<p class=\"whs2\">&lt;P&gt; &lt;B&gt;This is a new paragraph!&lt;/B&gt; \n" +
                " </p>\n" +
                "\n" +
                "<p class=\"whs2\">&lt;BR&gt; &lt;B&gt;&lt;I&gt;This is a new \n" +
                " sentence without a paragraph break, in bold italics.&lt;/I&gt;&lt;/B&gt; \n" +
                " </p>\n" +
                "\n" +
                "<p class=\"whs2\">&lt;HR&gt; </p>\n" +
                "\n" +
                "<p class=\"whs2\">&lt;/BODY&gt; </p>\n" +
                "\n" +
                "<p class=\"whs2\">&lt;/HTML&gt; </p>\n" +
                "\n" +
                "<p class=\"whs2\">&nbsp;</p>\n" +
                "\n" +
                "<p class=\"whs2\">&nbsp;</p>\n" +
                "\n" +
                "<p class=\"whs3\">&nbsp;</p>\n" +
                "\n" +
                "<script type=\"text/javascript\" language=\"javascript1.2\">\n" +
                "<!--\n" +
                "if (window.writeIntopicBar)\n" +
                "\twriteIntopicBar(0);\n" +
                "//-->\n" +
                "</script>\n" +
                "<!-- WiredMinds eMetrics tracking with Enterprise Edition V5.4 START -->\n" +
                "<script type='text/javascript' src='https://count.carrierzone.com/app/count_server/count.js'></script>\n" +
                "<script type='text/javascript'><!--\n" +
                "wm_custnum='a57bcd0d5bb4bdac';\n" +
                "wm_page_name='example_of_a_simple_html_page.htm';\n" +
                "wm_group_name='/services/webpages/h/e/help.websiteos.com/public/websiteos';\n" +
                "wm_campaign_key='campaign_id';\n" +
                "wm_track_alt='';\n" +
                "wiredminds.count();\n" +
                "// -->\n" +
                "</script>\n" +
                "<!-- WiredMinds eMetrics tracking with Enterprise Edition V5.4 END -->\n" +
                "</body>\n" +
                "</html>\n";
        try {
            db.execSQL("DELETE FROM "+HomeContract.DocumentCollection.TABLE_NAME);
            if (!jObj.get(HomeContract.DocumentCollection.TABLE_NAME).equals(null)) {
                JSONArray jsonArray = jObj.getJSONArray(HomeContract.DocumentCollection.TABLE_NAME);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    ContentValues values = new ContentValues();

                    values.put(HomeContract.DocumentCollection.COLUMN_NAME_ID, jsonObject1.getString(HomeContract.DocumentCollection.COLUMN_NAME_ID).toString());
                    values.put(HomeContract.DocumentCollection.COLUMN_NAME_APP_ID, jsonObject1.getString(HomeContract.DocumentCollection.COLUMN_NAME_APP_ID).toString());
                    values.put(HomeContract.DocumentCollection.COLUMN_NAME_DOCUMENT_NAME, jsonObject1.getString(HomeContract.DocumentCollection.COLUMN_NAME_DOCUMENT_NAME).toString());
                    values.put(HomeContract.DocumentCollection.COLUMN_NAME_DOCUMENT_DATA, jsonObject1.getString(HomeContract.DocumentCollection.COLUMN_NAME_DOCUMENT_DATA).toString().trim());

                    db.insert(
                            HomeContract.DocumentCollection.TABLE_NAME,
                            HomeContract.DocumentCollection.COLUMN_NAME_NULLABLE,
                            values);
                }
            }
        } catch (Exception e) {
            // JSON errord
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public String fetchDocumentData() {
        SQLiteDatabase db = homeDBHelper.getReadableDatabase();

        String documentData = null;

        String query = "SELECT " + HomeContract.DocumentCollection.COLUMN_NAME_DOCUMENT_DATA + " FROM " + HomeContract.DocumentCollection.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            documentData = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return documentData;
    }

}
