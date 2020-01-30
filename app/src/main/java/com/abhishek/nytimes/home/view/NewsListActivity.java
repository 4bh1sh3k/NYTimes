package com.abhishek.nytimes.home.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.nytimes.R;
import com.abhishek.nytimes.details.view.DetailsActivity;
import com.abhishek.nytimes.home.QueryType;
import com.abhishek.nytimes.home.presenter.INewsListPresenter;
import com.abhishek.nytimes.model.Credit;
import com.abhishek.nytimes.model.NewsItem;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class NewsListActivity extends AppCompatActivity implements MenuItem.OnActionExpandListener, SearchView.OnQueryTextListener, INewsListPresenter.INewsListView {

    private static final String SEARCH_TYPE_KEY = "searchType";
    private static final int loadNextThreshold = 4;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.homeToolbar)
    Toolbar toolbar;
    @BindView(R.id.rcView)
    RecyclerView rcView;
    @Inject
    INewsListPresenter presenter;
    private Snackbar snackbar;
    private NewsAdapter adapter;
    private QueryType currentQueryType;
    private Snackbar.Callback snackbarCallback;

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        presenter.onCreate();
        presenter.setView(this);

        setSupportActionBar(toolbar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcView.setLayoutManager(layoutManager);
        rcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!presenter.isOnGoing(currentQueryType)) {
                    if (dy > 0) {
                        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                        int totalItems = layoutManager.getItemCount();

                        if (lastVisibleItem + loadNextThreshold >= totalItems)
                            presenter.getNextPage(currentQueryType);
                    }
                }
            }
        });
        adapter = new NewsAdapter();
        rcView.setAdapter(adapter);
        snackbarCallback = new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                snackbar = null;
            }
        };

        if (savedInstanceState != null)
            currentQueryType = (QueryType) savedInstanceState.getSerializable(SEARCH_TYPE_KEY);
        else
            currentQueryType = QueryType.Recent;

        setTitle(R.string.title_home);
        presenter.init(currentQueryType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.searchView);
        searchItem.setOnActionExpandListener(this);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);

        if (currentQueryType == QueryType.Search) {
            searchView.setIconified(false);
            searchItem.expandActionView();
        }

        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        currentQueryType = QueryType.Search;
        presenter.init(currentQueryType);
        setOngoing(presenter.isOnGoing(currentQueryType), currentQueryType);
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        currentQueryType = QueryType.Recent;
        presenter.init(currentQueryType);
        setOngoing(presenter.isOnGoing(currentQueryType), currentQueryType);

        presenter.setQuery(null);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        presenter.setQuery(query);
        presenter.getNextPage(QueryType.Search);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SEARCH_TYPE_KEY, currentQueryType);
    }

    @Override
    public void setOngoing(boolean isOngoing, QueryType type) {
        if (this.currentQueryType == type) {
            progressBar.setVisibility(isOngoing ? View.VISIBLE : View.GONE);
            progressBar.setIndeterminate(isOngoing);
        }
    }

    @Override
    public void notifyNewsChanged(QueryType type) {
        if (this.currentQueryType == type)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void notifyNewsAdded(int startIndex, int count, QueryType type) {
        if (this.currentQueryType == type) {
            adapter.notifyItemRangeInserted(startIndex, count);
        }
    }

    @Override
    public void clearData(QueryType type) {
        if (this.currentQueryType == type) {
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showError(int messageId, QueryType type) {
        if (this.currentQueryType == type) {
            if (snackbar != null)
                snackbar.dismiss();

            snackbar = Snackbar.make(progressBar, messageId, Snackbar.LENGTH_LONG);
            snackbar.addCallback(snackbarCallback);
            snackbar.show();
        }
    }

    class NewsAdapter extends RecyclerView.Adapter<NewsListActivity.NewsHolder> {

        @Override @NonNull
        public NewsListActivity.NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.layout_newsitem_withimage, parent, false);
            return new NewsListActivity.NewsHolder(view);
        }

        @Override
        public void onBindViewHolder(NewsListActivity.NewsHolder holder, int position) {
            holder.bindView(position);
        }

        @Override
        public int getItemCount() {
            return presenter.getNewsCount(currentQueryType);
        }
    }

    class NewsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.media)
        ImageView media;
        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.date)
        TextView date;

        NewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(int position) {
            NewsItem item = presenter.getNewsItem(position, currentQueryType);
            if (item != null) {
                title.setText(item.getHeadline().getTitle());
                Credit credit = item.getCredit();
                if (credit != null)
                    author.setText(credit.getAuthor());
                date.setText(item.getPublicationDate());

                String mediaUri = item.getMediaUri();
                if (mediaUri != null) {
                    media.setVisibility(View.VISIBLE);
                    Picasso.with(NewsListActivity.this)
                            .load(Uri.parse(item.getMediaUri()))
                            .fit()
                            .centerCrop()
                            .into(media);
                } else
                    media.setVisibility(View.GONE);

                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(NewsListActivity.this, DetailsActivity.class);
                    intent.putExtra(DetailsActivity.ITEM_POSITION, position);
                    intent.putExtra(DetailsActivity.SEARCH_TYPE, currentQueryType);

                    startActivity(intent);
                });
            }
        }
    }
}
