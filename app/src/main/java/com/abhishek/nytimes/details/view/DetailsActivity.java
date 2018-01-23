package com.abhishek.nytimes.details.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhishek.nytimes.R;
import com.abhishek.nytimes.app.NYTApplication;
import com.abhishek.nytimes.details.di.DaggerDetailsComponent;
import com.abhishek.nytimes.details.preseter.IDetailsPresenter;
import com.abhishek.nytimes.details.di.DetailsComponent;
import com.abhishek.nytimes.home.QueryType;
import com.abhishek.nytimes.model.Credit;
import com.abhishek.nytimes.model.NewsItem;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class DetailsActivity extends AppCompatActivity implements IDetailsPresenter.IDetailsView {

    public static final String ITEM_POSITION = "itemPosition";
    public static final String SEARCH_TYPE = "searchType";

    NewsItem newsItem;
    ImageView mediaLarge;
    TextView headline, summary, author, date, link;

    @Inject
    IDetailsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle(R.string.title_details);

        DetailsComponent component = DaggerDetailsComponent.builder()
                .appComponent(NYTApplication.getComponent())
                .build();
        component.injectActivity(this);
        presenter.setView(this);

        setSupportActionBar(findViewById(R.id.detailsToolbar));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mediaLarge = findViewById(R.id.mediaLarge);
        headline = findViewById(R.id.headline);
        summary = findViewById(R.id.summary);
        author = findViewById(R.id.author);
        date = findViewById(R.id.date);
        link = findViewById(R.id.link);
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
