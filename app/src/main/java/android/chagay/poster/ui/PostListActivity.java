package android.chagay.poster.ui;

import android.chagay.poster.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class PostListActivity extends AppCompatActivity  {

    private static final String TAG  = "LIST_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager mFragmentManager;
        mFragmentManager = getSupportFragmentManager();
        PostListFragment fragment = (PostListFragment) mFragmentManager.findFragmentByTag(TAG);

        if (fragment == null) {
            fragment = new PostListFragment();
        }

        if (fragment.isAdded()) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment, TAG)
                    .commit();
        }
        else {
        mFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment, TAG)
                .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.new_post) {
            Intent intent = new Intent(this, PostNewActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}