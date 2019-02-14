package com.abhishek.nytimes.details.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhishek.nytimes.R;
import com.abhishek.nytimes.details.preseter.IDetailsPresenter;
import com.abhishek.nytimes.home.QueryType;
import com.abhishek.nytimes.model.Credit;
import com.abhishek.nytimes.model.NewsItem;
import com.squareup.picasso.Picasso;

import dagger.android.AndroidInjection;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements IDetailsPresenter.IDetailsView {

    public static final String ITEM_POSITION = "itemPosition";
    public static final String SEARCH_TYPE = "searchType";

    NewsItem newsItem;
    @BindView(R.id.mediaLarge) ImageView mediaLarge;
    @BindView(R.id.headline) TextView headline;
    @BindView(R.id.summary) TextView summary;
    @BindView(R.id.author) TextView author;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.linkTitle) TextView linkTitle;
    @BindView(R.id.link) TextView link;

    @Inject
    IDetailsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        presenter.setView(this);

        setSupportActionBar(findViewById(R.id.detailsToolbar));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.title_details);
        int position = getIntent().getIntExtra(ITEM_POSITION, 0);
        QueryType type = (QueryType) getIntent().getSerializableExtra(SEARCH_TYPE);
        presenter.getNews(position, type);
    }

    @Override
    public void showNews(NewsItem item) {
        if (item != null) {
            this.newsItem = item;

            headline.setText(item.getHeadline().getTitle());
            summary.setText(Html.fromHtml(item.getSummary()));
            Credit credit = item.getCredit();
            if (credit != null)
                author.setText(credit.getAuthor());
            date.setText(item.getPublicationDate());
            if(TextUtils.isEmpty(item.getUrl()))
                linkTitle.setVisibility(View.GONE);
            else
                link.setText(item.getUrl());

            if (item.getMediaUri() != null) {
                mediaLarge.setVisibility(View.VISIBLE);
                Picasso.with(this)
                        .load(Uri.parse(item.getMediaUri()))
                        .fit().centerCrop()
                        .into(mediaLarge);
            } else
                mediaLarge.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.shareMenu) {
            shareNews();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void shareNews() {
        if (newsItem != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, newsItem.getHeadline().getTitle());
            intent.putExtra(Intent.EXTRA_TEXT, newsItem.getUrl());

            startActivity(intent);
        }
    }

}
