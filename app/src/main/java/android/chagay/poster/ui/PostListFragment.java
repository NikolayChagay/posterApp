package android.chagay.poster.ui;

import android.chagay.poster.R;
import android.chagay.poster.data.db.DbManager;
import android.chagay.poster.model.Post;
import android.chagay.poster.network.Broadcast;
import android.chagay.poster.network.NetworkService;
import android.chagay.poster.network.model.NetworkRequest;
import android.chagay.poster.network.model.Request;
import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class PostListFragment extends Fragment {

    private final static String TAG = "FRAGMENT_POST_LIST";

    private Context mContext;
    private RecyclerView mPostRecyclerView;
    private TextView mEmptyPostText;
    private PostAdapter mAdapter;
    private BroadcastReceiver mBroadcastReceiver;
    private ProgressBar mProgressBar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        refresh();

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int task = intent.getIntExtra(Broadcast.PARAM_TASK, 0);
                String result = intent.getStringExtra(Broadcast.PARAM_RESULT);
                int status = intent.getIntExtra(Broadcast.PARAM_STATUS, 0);
                String msg = "";
                String err_msg;
                if (status  == Broadcast.STATUS_START) {
                    msg = "onReceive START : task = " + task + ", result = " + result + ", status = " + status;
                    showProgress(ProgressBar.VISIBLE);
                }

                if (status == Broadcast.STATUS_FINISH) {
                    updateUI();

                    if (task == 1) {
                        msg = "Обновление завершено успешно, данные обновлены";
                    } else { msg = "Запись добавлена. Ответ сервера = " + result; }

                    showProgress(ProgressBar.INVISIBLE);
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }

                if (status == Broadcast.STATUS_ERROR) {
                    showProgress(ProgressBar.INVISIBLE);
                    msg =  "onReceive ERROR: task = " + task + ", result = " + result + ", status = " + status;
                    err_msg = "Ошибка при получении данных : " + result;
                    Toast.makeText(getActivity(), err_msg, Toast.LENGTH_LONG).show();
                }
                Log.d(TAG, msg);
            }
        };

        IntentFilter intentFilter = new IntentFilter(Broadcast.BROADCAST_ACTION);
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
        Log.i(TAG, "Registered receiver : " + Broadcast.BROADCAST_ACTION);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        mPostRecyclerView = view.findViewById(R.id.post_recycler_view);
        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEmptyPostText = view.findViewById(R.id.empty_post_list);
        mProgressBar = view.findViewById(R.id.progress_bar);

        updateUI();

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext.unregisterReceiver(mBroadcastReceiver);
        Log.i(TAG, "UN-registered receiver : " + Broadcast.BROADCAST_ACTION);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.refresh) {
            refresh();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {

        showProgress(ProgressBar.VISIBLE);
        List<Post> posts = DbManager.get(mContext).getPosts();

        mAdapter = new PostAdapter(posts);
        mPostRecyclerView.setAdapter(mAdapter);

        showProgress(ProgressBar.INVISIBLE);
        updateEmptyListText();
    }

    private class PostHolder extends RecyclerView.ViewHolder {
        private TextView mPostTitle;
        private TextView mPostDetail;

        PostHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_post, parent, false));

            mPostTitle = itemView.findViewById(R.id.post_title);
            mPostDetail = itemView.findViewById(R.id.post_detail);
        }

        void bind(Post post) {

            mPostTitle.setText(post.getTitle());
            mPostDetail.setText(post.getBody());
        }
    }

    private class PostAdapter extends RecyclerView.Adapter<PostHolder> {
        private List<Post> mPosts;

        PostAdapter(List<Post> posts) {
            mPosts = posts;
        }

        @NonNull
        @Override
        public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PostHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PostHolder holder, int position) {
            Post post = mPosts.get(position);
            holder.bind(post);
        }

        @Override
        public int getItemCount() {
            return mPosts.size();
        }
    }

    private void refresh() {

        if (!isNetworkAvailableAndConnected()) {
            Toast.makeText(mContext, "Сеть не доступна. Данные могут быть не актуальные", Toast.LENGTH_LONG).show();
            return;
        }

        Request request = new Request(NetworkRequest.POST_LIST);
        NetworkService.fetchPostsList(mContext, request);
    }

    private void updateEmptyListText() {
        mEmptyPostText.setVisibility((mAdapter.mPosts.size() > 0) ? View.GONE : View.VISIBLE);
    }

    private void showProgress(int visible) {
        mProgressBar.setVisibility(visible);
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
}