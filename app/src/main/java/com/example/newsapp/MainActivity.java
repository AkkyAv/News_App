package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {
    
    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModal> categoryRVModalArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsRV=findViewById(R.id.idRVNews);
        categoryRV=findViewById(R.id.idRVCategories);
        loadingPB=findViewById(R.id.idPBLoading);
        articlesArrayList=new ArrayList<>();
        categoryRVModalArrayList=new ArrayList<>();
        categoryRVAdapter=new CategoryRVAdapter(categoryRVModalArrayList,this,this::onCategoryClick);
        newsRVAdapter=new NewsRVAdapter(articlesArrayList,this);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        //Layout manager already set for categoryRV
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();

    }
    private void getCategories(){
        categoryRVModalArrayList.add(new CategoryRVModal("All","https://media.istockphoto.com/id/1278583120/photo/colorful-abstract-collage-from-clippings-with-letters-and-numbers-texture-background-torn-and.jpg?s=1024x1024&w=is&k=20&c=EDF56coph1SK6STJhAfFwzjXeQ9mB-TTIVhaZt4LJ7A="));
        categoryRVModalArrayList.add(new CategoryRVModal("Technology","https://www.brookings.edu/wp-content/uploads/2022/01/shutterstock_1145553203_small.jpg"));
        categoryRVModalArrayList.add(new CategoryRVModal("Science","https://discovery.sndimg.com/content/dam/images/discovery/fullset/2022/10/Asteroid%20GettyImages-1273859993.jpg.rend.hgtvcom.686.686.suffix/1666995940234.jpeg"));
        categoryRVModalArrayList.add(new CategoryRVModal("Sports","https://d1whtlypfis84e.cloudfront.net/guides/wp-content/uploads/2020/02/10105218/Sports-1024x622.jpg"));
        categoryRVModalArrayList.add(new CategoryRVModal("General","https://cdn-nyt-prd.nytlicensing.com/media/uploads/ckeditor/2022/03/21/climate-grants-equity-2-12032021-014724.JPG"));
        categoryRVModalArrayList.add(new CategoryRVModal("Business","https://images.pexels.com/photos/534216/pexels-photo-534216.jpeg?cs=srgb&dl=pexels-pixabay-534216.jpg&fm=jpg"));
        categoryRVModalArrayList.add(new CategoryRVModal("Entertainment","https://media.istockphoto.com/id/1191001701/photo/popcorn-and-clapperboard.jpg?s=612x612&w=0&k=20&c=iUkFTVuU8k-UCcZDxczTWs6gkRa0nAMihp2Jf_2ASKM="));
        categoryRVModalArrayList.add(new CategoryRVModal("Health","https://media.istockphoto.com/id/1446229465/photo/red-heart-and-stethoscope-are-on-blue-background.webp?b=1&s=170667a&w=0&k=20&c=1-aE7XV24f8qVr8fGnpvypir8fSxYaM9sHZurKoutj8="));
        categoryRVAdapter.notifyDataSetChanged();
    }
    private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL="https://newsapi.org/v2/top-headlines?country=in&category="+category+"&apiKey=3ebeee34552e474ea1eff851e83cd4d5";
        String url="https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow&sortby=publishedAt&language=en&apikey=3ebeee34552e474ea1eff851e83cd4d5";
        String BASE_URL="https://newsapi.org/"  ;
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI=retrofit.create(RetrofitAPI.class);
        Call<NewsModal> call;
        if(category.equals("All")){
            call= retrofitAPI.getAllNews(url);}
        else{
            call= retrofitAPI.getNewsByCategory(categoryURL);
        }

        call.enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                NewsModal newsModal=response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles=newsModal.getArticles();
                for (int i = 0; i <articles.size(); i++) {
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),articles.get(i).getUrl(),
                            articles.get(i).getContent()));
                }
                newsRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable t) {
                Toast.makeText(MainActivity.this, "fail to get news", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onCategoryClick(int position) {
        String category=categoryRVModalArrayList.get(position).getCategory();
        getNews(category);
    }
}
