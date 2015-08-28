
package com.haha.csdn.fragment;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.haha.csdn.R;
import com.haha.csdn.activity.NewsContentActivity;
import com.haha.csdn.adapter.NewsItemAdapter;
import com.haha.csdn.dao.NewsItemDao;
import com.haha.csdn.utils.AppUtil;
import com.haha.csdn.utils.NetUtil;
import com.haha.splider.Utils.Type;
import com.haha.splider.bean.NewsItem;
import com.haha.splider.manager.NewsItemManager;

@SuppressLint("ValidFragment")
public class MainFragment extends Fragment implements IXListViewRefreshListener, IXListViewLoadMore,OnItemClickListener{
    
    //private final static String TAG = "MainFragment";
    
    private final static int LOAD_REFRESH = 1;
    private final static int LOAD_MORE = 2;
    
    private final static int TIP_ERROR_NO_NETWORK = 1;
    private final static int TIP_ERROR_SERVER = 2;

    /** 默认的类型 */
    private Type newsType = Type.BUSINESS;

    private int curPage = 1;

    private NewsItemManager mNewItemManager;

    private XListView mXListView;

    private NewsItemAdapter mAdapter;

    private List<NewsItem> mDatas = new ArrayList<NewsItem>();
    
    /**是否是第一次进入*/
    private boolean isFirstIn = true;
    
    /**是否连接网络*/
    private boolean isConnNet = false;
    
    /**当前数据是否从网络中获取*/
    private boolean isLoadingDataFromNetWork;
    
    private NewsItemDao mNewsItemDao;

    public MainFragment(Type newsType){
        this.newsType = newsType;
        mNewItemManager = new NewsItemManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.tab_item_fragment_main, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        mNewsItemDao = new NewsItemDao(getActivity());

        mAdapter = new NewsItemAdapter(getActivity(), mDatas);
        mXListView = (XListView) getView().findViewById(R.id.xlistView);
        mXListView.setAdapter(mAdapter);
        mXListView.setPullRefreshEnable(this);
        mXListView.setPullLoadEnable(this);
        mXListView.setRefreshTime(AppUtil.getRefreshTime(getActivity(), newsType.getId()));
        // mXListView.NotRefreshAtBegin();
        
        if (isFirstIn){  
            
            /** 进来时直接刷新 */
            mXListView.startRefresh();
            isFirstIn = false;  
        }else{  
            mXListView.NotRefreshAtBegin();  
        }
        
        mXListView.setOnItemClickListener(this);

    }
    

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsItem newsItem = mDatas.get(position-1);
        Intent intent = new Intent(getActivity(),NewsContentActivity.class);
        intent.putExtra("url",newsItem.getLink());
        startActivity(intent);
    }
    
    @Override
    public void onRefresh() {
        new LoadDatasTask().execute(LOAD_REFRESH);
    }

    @Override
    public void onLoadMore() {
        new LoadDatasTask().execute(LOAD_MORE);
    }

    class LoadDatasTask extends AsyncTask<Integer, Void, Integer>{

        @Override
        protected Integer doInBackground(Integer... params){
            
            switch (params[0]) {
                case LOAD_REFRESH:
                    return refresh();
                case LOAD_MORE:
                    loadMore(); 
                    break;
                default:
                    break;
            }
            
            return -1;
        }

        @Override
        protected void onPostExecute(Integer result){
            
            switch (result) {
                case TIP_ERROR_NO_NETWORK:
                    Toast.makeText(getActivity(), "没有网络连接！",Toast.LENGTH_LONG).show();;  
                    mAdapter.setDatas(mDatas);  
                    mAdapter.notifyDataSetChanged(); 
                    break;
                case TIP_ERROR_SERVER:
                    Toast.makeText(getActivity(), "服务器错误！",Toast.LENGTH_LONG).show();  
                    break;
                default:
                    break;
            }
            
            mXListView.setRefreshTime(AppUtil.getRefreshTime(getActivity(), newsType.getId()));  
            mXListView.stopRefresh();  
            mXListView.stopLoadMore();
        }

    }
    
    /**
     * 下拉刷新数据
     * @return
     */
    public Integer refresh(){
        
        if (NetUtil.checkNet(getActivity())){
            isConnNet = true;
            // 获取最新数据
            try{
                List<NewsItem> newsItems = mNewItemManager.getNewsItem(newsType, curPage);
                mAdapter.setDatas(newsItems);
                
                isLoadingDataFromNetWork = true;
                // 设置刷新时间
                AppUtil.setRefreshTime(getActivity(), newsType.getId());
                // 清除数据库数据
                mNewsItemDao.deleteAll(newsType.getId());
                // 存入数据库
                mNewsItemDao.add(newsItems);

            } catch (Exception e){
                e.printStackTrace();
                isLoadingDataFromNetWork = false;
                return TIP_ERROR_SERVER;
            }
        } else{
            isConnNet = false;
            isLoadingDataFromNetWork = false;
            //从数据库加载
            List<NewsItem> newsItems = mNewsItemDao.list(newsType.getId(), curPage);
            mDatas = newsItems;
            //mAdapter.setDatas(newsItems);
            return TIP_ERROR_NO_NETWORK;
        }

        return -1;

    }

    /**
     * 会根据当前网络情况，判断是从数据库加载还是从网络继续获取
     */
    public void loadMore()
    {
        // 当前数据是从网络获取
        if (isLoadingDataFromNetWork){
            curPage += 1;
            try{
                List<NewsItem> newsItems = mNewItemManager.getNewsItem(newsType, curPage);
                mNewsItemDao.add(newsItems);
                mAdapter.addAll(newsItems);
            } catch (Exception e){
                e.printStackTrace();
            }
        } else{
            //从数据库加载
            curPage += 1;
            List<NewsItem> newsItems = mNewsItemDao.list(newsType.getId(), curPage);
            mAdapter.addAll(newsItems);
        }

    }

}
