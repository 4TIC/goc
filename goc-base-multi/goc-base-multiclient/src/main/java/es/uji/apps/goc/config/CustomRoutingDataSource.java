package es.uji.apps.goc.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class CustomRoutingDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> holder = new ThreadLocal<String>();

    @Override
    protected Object determineCurrentLookupKey() {
        return holder.get();
    }

    public static void clear() {
        holder.remove();
    }

    public static void setDataSourceKey(String key) {
        holder.set(key);
    }
}
