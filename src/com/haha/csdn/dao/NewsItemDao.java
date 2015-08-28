
package com.haha.csdn.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.haha.splider.Utils.Type;
import com.haha.splider.bean.NewsItem;

public class NewsItemDao {

    private final static String TAG = "NewsItemDao";

    private DBHelper dbHelper;

    public NewsItemDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void add(NewsItem newsItem) {
        Log.i(TAG, "add news newstype " + newsItem.getNewsType());
        String sql = "insert into tb_newsItem (title,link,date,imgLink,content,newstype) values(?,?,?,?,?,?) ;";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(
                sql,
                new Object[] {
                        newsItem.getTitle(), newsItem.getLink(), newsItem.getDate(),
                        newsItem.getImgUrl(),
                        newsItem.getContent(), newsItem.getNewsType().getId()
                });
        db.close();
    }

    public void deleteAll(int newsType){
        String sql = "delete from tb_newsItem where newstype = ?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql, new Object[] {
            newsType
        });
        db.close();
    }

    public void add(List<NewsItem> newsItems){
        for (NewsItem newsItem : newsItems){
            add(newsItem);
        }
    }

    /**
     * 根据newsType和currentPage从数据库中取数据
     * 
     * @param newsType
     * @param currentPage
     * @return
     */
    public List<NewsItem> list(int newsType, int currentPage){

        Log.i(TAG,newsType + "  newsType");
        Log.i(TAG,currentPage + "  currentPage");
        // 0 -9 , 10 - 19 ,
        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        try{
            int offset = 10 * (currentPage - 1);
            String sql = "select title,link,date,imgLink,content,newstype from tb_newsItem where newstype = ? limit ?,? ";
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery(sql, new String[] {
                    newsType + "", offset + "", "" + (offset + 10)
            });

            NewsItem newsItem = null;

            while (c.moveToNext()){
                newsItem = new NewsItem();

                String title = c.getString(0);
                String link = c.getString(1);
                String date = c.getString(2);
                String imgLink = c.getString(3);
                String content = c.getString(4);
                int newstype = c.getInt(5);

                newsItem.setTitle(title);
                newsItem.setLink(link);
                newsItem.setImgUrl(imgLink);
                newsItem.setDate(date);
                newsItem.setNewsType(getType(newstype));
                newsItem.setContent(content);

                newsItems.add(newsItem);

            }
            c.close();
            db.close();
            Log.e(TAG,newsItems.size() + "  newsItems.size()");
        } catch (Exception e){
            e.printStackTrace();
        }
        return newsItems;

    }

    private Type getType(int newsType) {
        switch (newsType) {
            case 1:
                return Type.BUSINESS;
            case 2:
                return Type.MOBILE;
            case 3:
                return Type.DEV;
            case 4:
                return Type.PROGAMMER;
            case 5:
                return Type.CLOUD;
            default:
                break;
        }
        return Type.BUSINESS;
    }

}
