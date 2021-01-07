package com.nablarch.example.app.web.metrics;

import io.micrometer.core.instrument.binder.MeterBinder;
import nablarch.core.log.Logger;
import nablarch.core.log.LoggerManager;
import nablarch.core.repository.initialization.Initializable;
import nablarch.integration.micrometer.DefaultMeterBinderListProvider;
import nablarch.integration.micrometer.instrument.binder.MetricsMetaData;
import nablarch.integration.micrometer.instrument.binder.jmx.JmxGaugeMetrics;
import nablarch.integration.micrometer.instrument.binder.jmx.MBeanAttributeCondition;
import nablarch.integration.micrometer.instrument.binder.logging.LogCountMetrics;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomMeterBinderListProvider extends DefaultMeterBinderListProvider implements Initializable {
    private static final Logger LOGGER = LoggerManager.get(CustomMeterBinderListProvider.class);

    private DataSource dataSource;

    @Override
    protected List<MeterBinder> createMeterBinderList() {
        List<MeterBinder> list = new ArrayList<>(super.createMeterBinderList());

        list.add(new LogCountMetrics());

        // Tomcatの現在のスレッド数
        list.add(new JmxGaugeMetrics(
            new MetricsMetaData("thread.count.current", "Current thread count."),
            new MBeanAttributeCondition("Catalina:type=ThreadPool,name=\"http-nio-8080\"", "currentThreadCount")
        ));

        // DBコネクションプールのアクティブ数
        list.add(new JmxGaugeMetrics(
            new MetricsMetaData("db.pool.active", "Active DB pool count."),
            new MBeanAttributeCondition("com.zaxxer.hikari:type=Pool (HikariPool-1)", "ActiveConnections")
        ));

        return list;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void initialize() {
        try (Connection ignored = dataSource.getConnection()) {
            // 初期化時にコネクションを確立することで、MBeanが取れないことによる警告ログの出力を抑制する
        } catch (SQLException e) {
            LOGGER.logWarn("Failed initial connection.", e);
        }
    }
}
