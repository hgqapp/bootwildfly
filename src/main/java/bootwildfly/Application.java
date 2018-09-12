package bootwildfly;

import bootwildfly.consts.Constants;
import com.mashape.unirest.http.Unirest;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
@EnableScheduling
public class Application extends SpringBootServletInitializer {

    private static final long CONNECTION_TIMEOUT = 10000;
    private static final long SOCKET_TIMEOUT = 60000;
    private static final int MAX_TOTAL = 200;
    private static final int MAX_PER_ROUTE = 20;

    static {
        RequestConfig clientConfig = RequestConfig.custom()
                .setConnectTimeout(((Long) CONNECTION_TIMEOUT).intValue())
                .setSocketTimeout(((Long) SOCKET_TIMEOUT).intValue())
                .setConnectionRequestTimeout(((Long) SOCKET_TIMEOUT).intValue())
                .setRedirectsEnabled(false)
                .build();
        PoolingHttpClientConnectionManager syncConnectionManager = new PoolingHttpClientConnectionManager();
        syncConnectionManager.setMaxTotal(MAX_TOTAL);
        syncConnectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(clientConfig)
                .setConnectionManager(syncConnectionManager)
                .disableCookieManagement()
                .disableAuthCaching()
                .disableConnectionState()
                .disableRedirectHandling()
                .setDefaultHeaders(Arrays.asList(
                        new BasicHeader(Constants.HEADER_USER_AGENT_KEY, Constants.HEADER_USER_AGENT_VALUE),
                        new BasicHeader(Constants.HEADER_REFERER_KEY, Constants.HEADER_REFERER_VALUE)
                ))
                .build();
        Unirest.setHttpClient(client);
    }

    @Bean
    @ConditionalOnMissingBean
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("Executor-");
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
