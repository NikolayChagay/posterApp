package android.chagay.poster.ui;

import android.chagay.poster.R;
import android.chagay.poster.model.Post;
import android.chagay.poster.network.NetworkService;
import android.chagay.poster.network.model.NetworkRequest;
import android.chagay.poster.network.model.Request;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class PostFragment extends Fragment {

    private static final String ARG_USER_ID = "USER_ID";

    private Context mContext;
    private Integer mUserId;
    private EditText mPostTitle;
    private EditText mPostBody;

    public static PostFragment newInstance(Integer userId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_ID, userId);
        PostFragment fragment = new PostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mUserId = (Integer) getArguments().getSerializable(ARG_USER_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post, container, false);
        mPostTitle = v.findViewById(R.id.input_post_title);
        mPostBody = v.findViewById(R.id.input_post_body);
        mPostTitle.setText("");
        mPostBody.setText("");

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPostTitle.setText("");
        mPostBody.setText("");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.post_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_post) {
            Post newPost = new Post();
            newPost.setUserId(mUserId);
            newPost.setTitle(mPostTitle.getText().toString());
            newPost.setBody(mPostBody.getText().toString());

            Request request = new Request(NetworkRequest.ADD_POST);
            NetworkService.newPost(mContext, request, newPost);

            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}