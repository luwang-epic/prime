package filter;

import play.http.DefaultHttpFilters;

import javax.inject.Inject;


/**
 * @author wanglu
 * @date 2018/08/25
 *
 */
public class Filters extends DefaultHttpFilters {

    @Inject
    public Filters(LogRequestFilter logRequestFilter) {
        super(logRequestFilter);
    }

}
