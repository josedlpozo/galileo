/*
 * The MIT License (MIT)
 *
 * Original Work: Copyright (c) 2015 Danylyk Dmytro
 *
 * Modified Work: Copyright (c) 2015 Rottmann, Jonas
 *
 * Modified Work: Copyright (c) 2018 vicfran
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.josedlpozo.galileo.realm.realmbrowser.models.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.realm.realmbrowser.models.ModelsContract;
import com.josedlpozo.galileo.realm.realmbrowser.models.ModelsPresenter;
import com.josedlpozo.galileo.realm.realmbrowser.models.model.InformationPojo;
import com.josedlpozo.galileo.realm.realmbrowser.models.model.ModelPojo;
import java.io.File;
import java.util.ArrayList;

public class ModelsActivity extends AppCompatActivity implements ModelsContract.View, SearchView.OnQueryTextListener {

    private ModelsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ModelsContract.Presenter presenter;
    private MenuItem sortMenuItem;
    private static final String SEARCH_KEY = "search";
    private static final String FILE_NAME_KEY = "file_name";
    private SearchView searchView;
    private String searchString;

    public static Intent getIntent(@NonNull Context context, @NonNull String fileName) {
        Intent intent = new Intent(context, ModelsActivity.class);
        intent.putExtra(FILE_NAME_KEY, fileName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realm_browser_ac_recycler);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getIntent().getStringExtra(FILE_NAME_KEY));
        }
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.requestForContentUpdate());
        swipeRefreshLayout.setColorSchemeResources(R.color.realm_browser_dark_purple);

        adapter = new ModelsAdapter(new ArrayList<>(), file -> ModelsActivity.this.presenter.onModelSelected(file));
        RecyclerView recyclerView = findViewById(R.id.realm_browser_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (savedInstanceState != null) {
            searchString = savedInstanceState.getString(SEARCH_KEY);
        }

        attachPresenter((ModelsContract.Presenter) getLastCustomNonConfigurationInstance());
        presenter.requestForContentUpdate();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_KEY, searchString);
    }

    @Override public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.realm_browser_menu_modelsactivity, menu);

        this.sortMenuItem = menu.findItem(R.id.realm_browser_action_sort);

        MenuItem searchMenuItem = menu.findItem(R.id.realm_browser_action_filter);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override public boolean onPrepareOptionsMenu(Menu menu) {
        if (searchString != null && !searchString.isEmpty()) {
            ( (SearchView) menu.findItem(R.id.realm_browser_action_filter).getActionView() ).setIconified(false);
            searchView.requestFocus();
            searchView.setQuery(searchString, true);
            searchView.clearFocus();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.realm_browser_action_sort) {
            presenter.onSortModeChanged();
            return true;
        } else if (item.getItemId() == R.id.realm_browser_action_share) {
            presenter.onShareSelected();
            return true;
        } else if (item.getItemId() == R.id.realm_browser_action_info) {
            presenter.onInformationSelected();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override public void updateWithModels(@NonNull ArrayList<ModelPojo> filesList, @ModelsContract.SortMode int sortedMode) {
        adapter.swapList(filesList);
        if (sortedMode == ModelsContract.SortMode.ASC && sortMenuItem != null) {
            this.sortMenuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.realm_browser_ic_sort_ascending_white_24dp));
        } else if (sortedMode == ModelsContract.SortMode.DESC && sortMenuItem != null) {
            this.sortMenuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.realm_browser_ic_sort_descending_white_24dp));
        }
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override public Context getViewContext() {
        return this;
    }

    @Override public void attachPresenter(@Nullable ModelsContract.Presenter presenter) {
        this.presenter = presenter;
        if (this.presenter == null) {
            this.presenter = new ModelsPresenter();
        }
        this.presenter.attachView(this);
    }

    @Override public void presentShareDialog(@NonNull String path) {
        Uri contentUri = FileProvider.getUriForFile(this, String.format(this.getApplicationInfo().packageName + ".com.josedlpozo.galileo.provider"),
                                                                        new File(path));
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType("application/*");
        intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentShareFile.putExtra(Intent.EXTRA_STREAM, contentUri);
        startActivity(Intent.createChooser(intentShareFile, "Share Realm File"));
    }

    @Override public void showInformation(@NonNull InformationPojo informationPojo) {
        Toast.makeText(this,
                       String.format("%s\nSize: %s", informationPojo.getPath(), Formatter.formatShortFileSize(this, informationPojo.getSizeInByte())),
                       Toast.LENGTH_LONG).show();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override public boolean onQueryTextChange(String newText) {
        this.searchString = newText;
        presenter.onFilterChanged(newText);
        return true;
    }
}
