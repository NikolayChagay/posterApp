package android.chagay.poster.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.chagay.poster.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class PostNewActivity extends AppCompatActivity {

    private static final String TAG_FRAGMENT_ADD = "ADD_POST_FRAGMENT";
    private static final Integer CONST_USER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        FragmentManager mFragmentManager;
        mFragmentManager = getSupportFragmentManager();
        PostFragment fragment = (PostFragment) mFragmentManager.findFragmentByTag(TAG_FRAGMENT_ADD);

        if (fragment == null) {
            fragment = PostFragment.newInstance(CONST_USER_ID);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_new, fragment, TAG_FRAGMENT_ADD)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
