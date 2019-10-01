package com.example.player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.player.Adapter.ListSoundsAdapter;
import com.example.player.Common.Common;
import com.example.player.Model.Result;
import com.example.player.Retrofit.IRetrofir;
import com.example.player.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable;
    IRetrofir iRetrofir;

    SearchView searchViewSearch;
    RecyclerView rvListSounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        getInformationAdapterInto();
    }

    private void initComponents() {
        rvListSounds = (RecyclerView) findViewById(R.id.rvListSounds);
        rvListSounds.setHasFixedSize(true);
        rvListSounds.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        iRetrofir = retrofit.create(IRetrofir.class);
    }

    private void getInformationAdapterInto() {
        compositeDisposable.add(iRetrofir.getMusic(Common.Search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        getAdapter(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void getAdapter(Result result) {
        ListSoundsAdapter adapter = new ListSoundsAdapter(this, result);
        rvListSounds.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setTitle(null);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        searchViewSearch = (SearchView) searchItem.getActionView();
        searchViewSearch.setQueryHint(getString(R.string.search));
        searchViewSearch.setIconifiedByDefault(true);
        searchViewSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 4) {
                    Common.Search = newText.toLowerCase();
                    getInformationAdapterInto();
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
