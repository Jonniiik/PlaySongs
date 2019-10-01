package com.example.player.Model;

import java.util.List;

public class Result {
    public int resultCount;
    public List<RootObject> results;

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public List<RootObject> getResults() {
        return results;
    }

    public void setResults(List<RootObject> results) {
        this.results = results;
    }
}
