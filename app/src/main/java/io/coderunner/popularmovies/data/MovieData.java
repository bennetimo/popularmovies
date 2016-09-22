package io.coderunner.popularmovies.data;

import java.util.Collection;

public interface MovieData<T> {

    Collection<T> getData();

}
